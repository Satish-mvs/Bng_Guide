import React from 'react';
import { useLocation } from '../context/LocationContext';
import { translations, PRESET_LOCATIONS } from '../i18n/translations';
import { Language } from '../types';
import {
  Globe,
  Compass,
  Train,
  Bus,
  ShieldCheck,
  Info,
  Check,
  ChevronRight,
  Sparkles,
  Smartphone
} from 'lucide-react';

export const ProfileScreen: React.FC = () => {
  const {
    language,
    setLanguage,
    setSimulatedLocation,
    requestLocation,
    currentArea,
    activePresetId
  } = useLocation();

  const t = translations[language];

  const languages: { code: Language; label: string; native: string }[] = [
    { code: 'en', label: 'English', native: 'English' },
    { code: 'kn', label: 'Kannada', native: 'ಕನ್ನಡ' },
    { code: 'te', label: 'Telugu', native: 'తెలుగు' },
    { code: 'hi', label: 'Hindi', native: 'हिन्दी' }
  ];

  return (
    <div className="screen-scroll-container" style={{ paddingBottom: '100px' }}>
      {/* Header Banner */}
      <div style={{ padding: '24px 18px 16px', background: 'var(--bg-surface)', borderBottom: '1px solid var(--border-subtle)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <div style={{
            width: '44px',
            height: '44px',
            borderRadius: '50%',
            background: 'var(--primary-gradient)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: '#ffffff',
            boxShadow: '0 4px 12px rgba(216, 58, 72, 0.4)'
          }}>
            <Sparkles size={22} />
          </div>
          <div>
            <h1 style={{ fontSize: '18px', fontWeight: '800', color: '#f8fafc' }}>
              {t.appTitle}
            </h1>
            <div style={{ fontSize: '12px', color: '#94a3b8' }}>
              {t.currentLocation}: <strong style={{ color: '#f8fafc' }}>{currentArea}</strong>
            </div>
          </div>
        </div>
      </div>

      <div style={{ padding: '16px' }}>
        {/* Section 1: Multilingual Language Settings */}
        <div style={{ marginBottom: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '10px' }}>
            <Globe size={16} color="#38bdf8" />
            <h3 style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
              {t.language}
            </h3>
          </div>

          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(2, 1fr)',
            gap: '8px'
          }}>
            {languages.map((lang) => {
              const isSelected = language === lang.code;
              return (
                <button
                  key={lang.code}
                  onClick={() => setLanguage(lang.code)}
                  style={{
                    background: isSelected ? 'rgba(216, 58, 72, 0.2)' : 'var(--bg-card)',
                    border: `1px solid ${isSelected ? '#ef4444' : 'var(--border-subtle)'}`,
                    borderRadius: '12px',
                    padding: '12px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    cursor: 'pointer',
                    color: isSelected ? '#fca5a5' : '#f8fafc'
                  }}
                >
                  <div style={{ textAlign: 'left' }}>
                    <div style={{ fontSize: '14px', fontWeight: '700' }}>{lang.native}</div>
                    <div style={{ fontSize: '11px', color: '#94a3b8' }}>{lang.label}</div>
                  </div>
                  {isSelected && <Check size={16} color="#ef4444" />}
                </button>
              );
            })}
          </div>
        </div>

        {/* Section 2: Location Simulation & GPS Selector */}
        <div style={{ marginBottom: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '10px' }}>
            <Compass size={16} color="#fbbf24" />
            <h3 style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
              {t.selectLocationPreset}
            </h3>
          </div>

          {/* Real GPS Button */}
          <button
            onClick={() => requestLocation()}
            style={{
              width: '100%',
              padding: '12px 14px',
              background: activePresetId === null ? 'rgba(56, 189, 248, 0.15)' : 'var(--bg-card)',
              border: `1px solid ${activePresetId === null ? '#38bdf8' : 'var(--border-subtle)'}`,
              borderRadius: '12px',
              color: activePresetId === null ? '#38bdf8' : '#f8fafc',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              cursor: 'pointer',
              marginBottom: '8px'
            }}
          >
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
              <Compass size={18} />
              <div style={{ textAlign: 'left' }}>
                <div style={{ fontSize: '13px', fontWeight: '700' }}>Live Device GPS</div>
                <div style={{ fontSize: '11px', color: '#94a3b8' }}>Real-time location detection</div>
              </div>
            </div>
            {activePresetId === null && <Check size={16} />}
          </button>

          {/* Iconic Bengaluru Presets */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
            {PRESET_LOCATIONS.map((preset) => {
              const isSelected = activePresetId === preset.id;
              const label = language === 'kn' ? preset.labelKn :
                            language === 'te' ? preset.labelTe :
                            language === 'hi' ? preset.labelHi : preset.name;
              return (
                <button
                  key={preset.id}
                  onClick={() => setSimulatedLocation(preset.lat, preset.lng, preset.name)}
                  style={{
                    background: isSelected ? 'rgba(216, 58, 72, 0.2)' : 'var(--bg-card)',
                    border: `1px solid ${isSelected ? '#ef4444' : 'var(--border-subtle)'}`,
                    borderRadius: '12px',
                    padding: '10px 14px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    cursor: 'pointer',
                    color: isSelected ? '#fca5a5' : '#f8fafc'
                  }}
                >
                  <div style={{ textAlign: 'left' }}>
                    <div style={{ fontSize: '13px', fontWeight: '700' }}>{label}</div>
                    <div style={{ fontSize: '11px', color: '#94a3b8' }}>{preset.description}</div>
                  </div>
                  {isSelected && <Check size={16} color="#ef4444" />}
                </button>
              );
            })}
          </div>
        </div>

        {/* Section 3: First-time Visitor Tips */}
        <div style={{ marginBottom: '22px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '10px' }}>
            <Info size={16} color="#10b981" />
            <h3 style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
              {t.visitorTipsTitle}
            </h3>
          </div>

          <div style={{ background: 'var(--bg-card)', border: '1px solid var(--border-subtle)', borderRadius: '16px', padding: '14px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <div style={{ display: 'flex', gap: '10px' }}>
              <Train size={18} color="#8b5cf6" style={{ flexShrink: 0, marginTop: '2px' }} />
              <div>
                <div style={{ fontSize: '13px', fontWeight: '700', color: '#f8fafc' }}>Namma Metro</div>
                <div style={{ fontSize: '12px', color: '#94a3b8', lineHeight: '1.4' }}>{t.tipMetro}</div>
              </div>
            </div>

            <div style={{ height: '1px', background: 'var(--border-subtle)' }} />

            <div style={{ display: 'flex', gap: '10px' }}>
              <Bus size={18} color="#3b82f6" style={{ flexShrink: 0, marginTop: '2px' }} />
              <div>
                <div style={{ fontSize: '13px', fontWeight: '700', color: '#f8fafc' }}>BMTC Buses</div>
                <div style={{ fontSize: '12px', color: '#94a3b8', lineHeight: '1.4' }}>{t.tipBmtc}</div>
              </div>
            </div>

            <div style={{ height: '1px', background: 'var(--border-subtle)' }} />

            <div style={{ display: 'flex', gap: '10px' }}>
              <span style={{ fontSize: '18px', flexShrink: 0 }}>🛺</span>
              <div>
                <div style={{ fontSize: '13px', fontWeight: '700', color: '#f8fafc' }}>Auto Rickshaws</div>
                <div style={{ fontSize: '12px', color: '#94a3b8', lineHeight: '1.4' }}>{t.tipAuto}</div>
              </div>
            </div>
          </div>
        </div>

        {/* Section 4: Privacy & Stack Note */}
        <div style={{ background: 'rgba(15, 23, 42, 0.6)', border: '1px solid var(--border-subtle)', borderRadius: '14px', padding: '12px', textAlign: 'center' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '6px', color: '#10b981', fontSize: '12px', fontWeight: '700', marginBottom: '4px' }}>
            <ShieldCheck size={15} />
            <span>Secure & Privacy Protected</span>
          </div>
          <div style={{ fontSize: '11px', color: '#64748b' }}>
            {t.privacyNote}
          </div>
        </div>
      </div>
    </div>
  );
};
