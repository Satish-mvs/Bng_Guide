import React, { useState } from 'react';
import { useLocation } from '../context/LocationContext';
import { translations, PRESET_LOCATIONS } from '../i18n/translations';
import { MapPin, Navigation, Compass, AlertTriangle, Globe, Sparkles, ChevronRight } from 'lucide-react';
import { Language } from '../types';

interface Props {
  onManualSelect: () => void;
}

export const LocationPermissionScreen: React.FC<Props> = ({ onManualSelect }) => {
  const {
    permissionState,
    requestLocation,
    setSimulatedLocation,
    isLoading,
    error,
    language,
    setLanguage
  } = useLocation();

  const [showPresets, setShowPresets] = useState(false);
  const t = translations[language];

  return (
    <div style={{
      minHeight: '100%',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-between',
      padding: '24px 20px 32px',
      background: 'radial-gradient(circle at 50% 20%, #2e1065 0%, #0f172a 60%, #090d16 100%)',
      color: '#f8fafc'
    }}>
      {/* Language Selector Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
          <Sparkles size={18} color="#f59e0b" />
          <span style={{ fontSize: '12px', fontWeight: '700', letterSpacing: '1px', color: '#fbbf24', textTransform: 'uppercase' }}>
            Namma Bengaluru
          </span>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '4px', background: 'rgba(255,255,255,0.08)', padding: '4px 8px', borderRadius: '20px' }}>
          <Globe size={14} color="#94a3b8" />
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value as Language)}
            style={{
              background: 'transparent',
              border: 'none',
              color: '#f8fafc',
              fontSize: '12px',
              fontWeight: '600',
              outline: 'none',
              cursor: 'pointer'
            }}
          >
            <option value="en" style={{ background: '#1e293b' }}>English</option>
            <option value="kn" style={{ background: '#1e293b' }}>ಕನ್ನಡ (Kannada)</option>
            <option value="te" style={{ background: '#1e293b' }}>తెలుగు (Telugu)</option>
            <option value="hi" style={{ background: '#1e293b' }}>हिन्दी (Hindi)</option>
          </select>
        </div>
      </div>

      {/* Main Illustration & Hero */}
      <div style={{ textAlign: 'center', margin: '28px 0 20px' }}>
        <div style={{
          width: '88px',
          height: '88px',
          borderRadius: '50%',
          background: 'linear-gradient(135deg, rgba(239, 68, 68, 0.2) 0%, rgba(245, 158, 11, 0.2) 100%)',
          border: '2px solid rgba(239, 68, 68, 0.4)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          margin: '0 auto 20px',
          boxShadow: '0 0 28px rgba(239, 68, 68, 0.3)'
        }}>
          <MapPin size={44} color="#ef4444" />
        </div>

        <h1 style={{ fontSize: '24px', fontWeight: '800', marginBottom: '8px', letterSpacing: '-0.5px' }}>
          {t.welcomeTitle}
        </h1>
        <p style={{ fontSize: '14px', color: '#94a3b8', lineHeight: '1.5', maxWidth: '320px', margin: '0 auto' }}>
          {t.appSubtitle}
        </p>
      </div>

      {/* Status Alert if Denied / Error */}
      {error && (
        <div style={{
          background: 'rgba(239, 68, 68, 0.15)',
          border: '1px solid rgba(239, 68, 68, 0.4)',
          borderRadius: '14px',
          padding: '12px 14px',
          marginBottom: '16px',
          display: 'flex',
          alignItems: 'flex-start',
          gap: '10px'
        }}>
          <AlertTriangle size={20} color="#ef4444" style={{ flexShrink: 0, marginTop: '2px' }} />
          <div>
            <div style={{ fontSize: '13px', fontWeight: '700', color: '#fca5a5' }}>
              {permissionState === 'denied' ? t.permissionDeniedTitle :
               permissionState === 'gps_off' ? t.gpsDisabledTitle : 'Location Status'}
            </div>
            <div style={{ fontSize: '12px', color: '#f87171', marginTop: '2px' }}>
              {error}
            </div>
          </div>
        </div>
      )}

      {/* Action Card */}
      <div style={{
        background: 'rgba(30, 41, 59, 0.7)',
        backdropFilter: 'blur(12px)',
        border: '1px solid rgba(255, 255, 255, 0.1)',
        borderRadius: '20px',
        padding: '18px',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '4px' }}>
          <Navigation size={20} color="#38bdf8" />
          <div>
            <h3 style={{ fontSize: '15px', fontWeight: '700' }}>{t.enableLocationTitle}</h3>
            <p style={{ fontSize: '12px', color: '#94a3b8' }}>{t.enableLocationDesc}</p>
          </div>
        </div>

        {/* Allow Location Button */}
        <button
          onClick={requestLocation}
          disabled={isLoading}
          style={{
            width: '100%',
            height: '48px',
            background: 'linear-gradient(135deg, #a71d2a 0%, #ef4444 100%)',
            border: 'none',
            borderRadius: '12px',
            color: '#ffffff',
            fontSize: '15px',
            fontWeight: '700',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '8px',
            cursor: 'pointer',
            boxShadow: '0 4px 16px rgba(239, 68, 68, 0.4)',
            transition: 'all 0.2s'
          }}
        >
          <Navigation size={18} />
          {isLoading ? 'Detecting GPS...' : t.allowLocationBtn}
        </button>

        {/* Majestic Quick Start Button (Core Scenario) */}
        <button
          onClick={() => setSimulatedLocation(12.9767, 77.5713, 'Majestic')}
          style={{
            width: '100%',
            height: '44px',
            background: 'rgba(245, 158, 11, 0.15)',
            border: '1px solid rgba(245, 158, 11, 0.4)',
            borderRadius: '12px',
            color: '#fbbf24',
            fontSize: '13px',
            fontWeight: '600',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '8px',
            cursor: 'pointer'
          }}
        >
          <Compass size={16} />
          {t.tryMajesticBtn}
        </button>

        {/* Toggle Simulated Landmark Presets */}
        <button
          onClick={() => setShowPresets(!showPresets)}
          style={{
            background: 'none',
            border: 'none',
            color: '#94a3b8',
            fontSize: '12px',
            fontWeight: '600',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '4px',
            cursor: 'pointer',
            padding: '6px'
          }}
        >
          {t.selectLocationPreset}
          <ChevronRight size={14} style={{ transform: showPresets ? 'rotate(90deg)' : 'none', transition: 'transform 0.2s' }} />
        </button>

        {showPresets && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', maxHeight: '160px', overflowY: 'auto' }}>
            {PRESET_LOCATIONS.map((preset) => (
              <button
                key={preset.id}
                onClick={() => setSimulatedLocation(preset.lat, preset.lng, preset.name)}
                style={{
                  background: 'rgba(15, 23, 42, 0.8)',
                  border: '1px solid rgba(255, 255, 255, 0.08)',
                  borderRadius: '8px',
                  padding: '8px 10px',
                  textAlign: 'left',
                  color: '#f8fafc',
                  fontSize: '12px',
                  cursor: 'pointer',
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center'
                }}
              >
                <div>
                  <div style={{ fontWeight: '600' }}>
                    {language === 'kn' ? preset.labelKn :
                     language === 'te' ? preset.labelTe :
                     language === 'hi' ? preset.labelHi : preset.name}
                  </div>
                  <div style={{ fontSize: '10px', color: '#94a3b8' }}>{preset.description}</div>
                </div>
                <ChevronRight size={14} color="#64748b" />
              </button>
            ))}
          </div>
        )}

        {/* Manual Search Button */}
        <button
          onClick={onManualSelect}
          style={{
            background: 'none',
            border: 'none',
            color: '#38bdf8',
            fontSize: '13px',
            fontWeight: '600',
            cursor: 'pointer',
            padding: '4px',
            textDecoration: 'underline'
          }}
        >
          {t.searchManuallyBtn}
        </button>
      </div>

      {/* Privacy Notice */}
      <div style={{ textAlign: 'center', fontSize: '11px', color: '#64748b', marginTop: '16px' }}>
        {t.privacyNote}
      </div>
    </div>
  );
};
