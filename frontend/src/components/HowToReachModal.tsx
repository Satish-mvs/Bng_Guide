import React, { useState, useEffect } from 'react';
import { Place, RouteComparison, TransportOption } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { fetchTransportOptions } from '../services/api';
import {
  X,
  Navigation,
  ArrowRight,
  ExternalLink,
  Footprints,
  Bus,
  Train,
  Car,
  Bike,
  Clock,
  CircleDollarSign,
  AlertCircle
} from 'lucide-react';

interface Props {
  place: Place;
  onClose: () => void;
}

export const HowToReachModal: React.FC<Props> = ({ place, onClose }) => {
  const { currentCoords, currentArea, language } = useLocation();
  const [comparison, setComparison] = useState<RouteComparison | null>(null);
  const [selectedMode, setSelectedMode] = useState<string>('METRO');
  const [loading, setLoading] = useState<boolean>(true);
  const t = translations[language];

  const fromLat = currentCoords?.lat || 12.9767;
  const fromLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    loadRouteOptions();
  }, [place, fromLat, fromLng]);

  const loadRouteOptions = async () => {
    setLoading(true);
    try {
      const data = await fetchTransportOptions(fromLat, fromLng, place.latitude, place.longitude, place.name);
      setComparison(data);
      if (data.options.length > 0) {
        // Default to Metro if available, otherwise Bus or Car
        const metroOpt = data.options.find(o => o.mode === 'METRO' && o.isAvailable);
        const busOpt = data.options.find(o => o.mode === 'BUS');
        setSelectedMode(metroOpt ? 'METRO' : (busOpt ? 'BUS' : data.options[0].mode));
      }
    } catch (err) {
      console.error('Error fetching routes:', err);
    } finally {
      setLoading(false);
    }
  };

  const getModeIcon = (mode: string) => {
    switch (mode) {
      case 'WALK': return <Footprints size={18} />;
      case 'BUS': return <Bus size={18} />;
      case 'METRO': return <Train size={18} />;
      case 'CAR': return <Car size={18} />;
      case 'AUTO': return <span style={{ fontSize: '16px' }}>🛺</span>;
      case 'BICYCLE': return <Bike size={18} />;
      default: return <Navigation size={18} />;
    }
  };

  const currentOption = comparison?.options.find(o => o.mode === selectedMode) || comparison?.options[0];

  const openInExternalMaps = () => {
    const url = `https://www.google.com/maps/dir/?api=1&origin=${fromLat},${fromLng}&destination=${place.latitude},${place.longitude}&travelmode=${selectedMode.toLowerCase() === 'metro' || selectedMode.toLowerCase() === 'bus' ? 'transit' : selectedMode.toLowerCase() === 'walk' ? 'walking' : 'driving'}`;
    window.open(url, '_blank');
  };

  const getStepInstruction = (step: any) => {
    if (language === 'kn' && step.instructionKn) return step.instructionKn;
    if (language === 'te' && step.instructionTe) return step.instructionTe;
    if (language === 'hi' && step.instructionHi) return step.instructionHi;
    return step.instruction;
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-sheet" onClick={(e) => e.stopPropagation()}>
        <div className="sheet-handle" />

        {/* Modal Header */}
        <div style={{ padding: '12px 18px', borderBottom: '1px solid var(--border-subtle)', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <div>
            <div style={{ fontSize: '12px', color: '#94a3b8', fontWeight: '600', textTransform: 'uppercase' }}>
              {t.howToReach}
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginTop: '2px' }}>
              <span style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc' }}>
                {comparison?.originName || currentArea || 'Majestic'}
              </span>
              <ArrowRight size={14} color="#ef4444" />
              <span style={{ fontSize: '14px', fontWeight: '700', color: '#f87171' }}>
                {place.name}
              </span>
            </div>
          </div>

          <button
            onClick={onClose}
            style={{
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              background: 'rgba(255,255,255,0.08)',
              border: 'none',
              color: '#f8fafc',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
          >
            <X size={18} />
          </button>
        </div>

        {loading ? (
          <div style={{ padding: '40px', textAlign: 'center', color: '#94a3b8' }}>
            <div className="pulse-dot" style={{ margin: '0 auto 12px', width: '14px', height: '14px' }} />
            <div>Calculating real multi-modal routes...</div>
          </div>
        ) : (
          <div>
            {/* Horizontal Mode Selection Tabs */}
            <div className="mode-tabs-container">
              {comparison?.options.map((opt) => {
                const isActive = selectedMode === opt.mode;
                return (
                  <button
                    key={opt.mode}
                    className={`mode-tab ${isActive ? 'active' : ''}`}
                    onClick={() => setSelectedMode(opt.mode)}
                  >
                    <div>{getModeIcon(opt.mode)}</div>
                    <div style={{ fontSize: '11px', fontWeight: '600' }}>{opt.title.replace(/[^a-zA-Z /]/g, '').trim()}</div>
                    <div style={{ fontSize: '10px', color: isActive ? '#f8fafc' : '#94a3b8' }}>
                      {opt.durationMinutes}m
                    </div>
                  </button>
                );
              })}
            </div>

            {/* Selected Mode Summary Card */}
            {currentOption && (
              <div style={{ padding: '16px 18px 0' }}>
                <div style={{
                  background: 'rgba(15, 23, 42, 0.7)',
                  border: '1px solid rgba(255, 255, 255, 0.1)',
                  borderRadius: '16px',
                  padding: '14px'
                }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <span style={{ fontSize: '20px' }}>{currentOption.icon}</span>
                      <div>
                        <h4 style={{ fontSize: '15px', fontWeight: '700', color: '#f8fafc' }}>
                          {currentOption.title}
                        </h4>
                        <div style={{ fontSize: '11px', color: '#94a3b8' }}>
                          {currentOption.summary}
                        </div>
                      </div>
                    </div>

                    <div style={{ textAlign: 'right' }}>
                      <div style={{ fontSize: '17px', fontWeight: '800', color: '#38bdf8' }}>
                        {currentOption.formattedDuration}
                      </div>
                      <div style={{ fontSize: '11px', color: '#94a3b8' }}>
                        {currentOption.formattedDistance}
                      </div>
                    </div>
                  </div>

                  {/* Fare & Highlights */}
                  <div style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    padding: '8px 12px',
                    background: 'rgba(255, 255, 255, 0.04)',
                    borderRadius: '10px',
                    fontSize: '12px'
                  }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '5px', color: '#fbbf24' }}>
                      <CircleDollarSign size={14} />
                      <span>Estimated: <strong>{currentOption.estimatedFare}</strong></span>
                    </div>

                    <div style={{ display: 'flex', alignItems: 'center', gap: '4px', color: '#10b981' }}>
                      <Clock size={13} />
                      <span>{selectedMode === 'METRO' ? 'Avoids Traffic' : 'Direct Route'}</span>
                    </div>
                  </div>
                </div>

                {/* Step-by-Step Timeline Section */}
                <div style={{ marginTop: '16px' }}>
                  <h4 style={{ fontSize: '13px', fontWeight: '700', textTransform: 'uppercase', color: '#94a3b8', marginBottom: '8px', letterSpacing: '0.5px' }}>
                    {t.stepByStepJourney}
                  </h4>

                  <div className="timeline">
                    {currentOption.steps.map((step, idx) => (
                      <div key={idx} className="timeline-step">
                        <div className="timeline-icon">
                          {step.icon || '📍'}
                        </div>
                        <div className="timeline-content">
                          <div className="timeline-instruction">
                            {getStepInstruction(step)}
                          </div>
                          <div className="timeline-meta">
                            <span>⏱ ~{step.durationMinutes} min</span>
                            {step.distanceKm > 0 && <span>📏 {step.distanceKm} km</span>}
                            {step.lineOrRouteName && (
                              <span style={{ color: '#38bdf8', fontWeight: '600' }}>
                                🏷 {step.lineOrRouteName}
                              </span>
                            )}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Compare All Modes Table (Rule 9 Requirement) */}
                <div style={{ marginTop: '16px', background: 'rgba(30, 41, 59, 0.5)', borderRadius: '12px', padding: '12px', border: '1px solid var(--border-subtle)' }}>
                  <div style={{ fontSize: '12px', fontWeight: '700', color: '#f8fafc', marginBottom: '8px' }}>
                    {t.transitComparison}
                  </div>
                  <table style={{ width: '100%', fontSize: '12px', borderCollapse: 'collapse' }}>
                    <thead>
                      <tr style={{ color: '#94a3b8', borderBottom: '1px solid var(--border-subtle)', textAlign: 'left' }}>
                        <th style={{ padding: '4px 6px' }}>Mode</th>
                        <th style={{ padding: '4px 6px', textAlign: 'right' }}>Distance</th>
                        <th style={{ padding: '4px 6px', textAlign: 'right' }}>Approx. Time</th>
                      </tr>
                    </thead>
                    <tbody>
                      {comparison?.options.map((opt) => (
                        <tr
                          key={opt.mode}
                          onClick={() => setSelectedMode(opt.mode)}
                          style={{
                            cursor: 'pointer',
                            background: selectedMode === opt.mode ? 'rgba(216, 58, 72, 0.15)' : 'transparent',
                            color: selectedMode === opt.mode ? '#fca5a5' : '#e2e8f0',
                            borderBottom: '1px solid rgba(255,255,255,0.04)'
                          }}
                        >
                          <td style={{ padding: '6px' }}>{opt.title}</td>
                          <td style={{ padding: '6px', textAlign: 'right' }}>{opt.formattedDistance}</td>
                          <td style={{ padding: '6px', textAlign: 'right', fontWeight: '700' }}>{opt.formattedDuration}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>

                {/* External Navigation Button */}
                <div style={{ padding: '16px 0 24px' }}>
                  <button
                    onClick={openInExternalMaps}
                    style={{
                      width: '100%',
                      height: '46px',
                      background: 'linear-gradient(135deg, #2563eb 0%, #38bdf8 100%)',
                      border: 'none',
                      borderRadius: '12px',
                      color: '#ffffff',
                      fontSize: '14px',
                      fontWeight: '700',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      gap: '8px',
                      cursor: 'pointer',
                      boxShadow: '0 4px 14px rgba(37, 99, 235, 0.4)'
                    }}
                  >
                    <ExternalLink size={16} />
                    {t.openGoogleMaps}
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
