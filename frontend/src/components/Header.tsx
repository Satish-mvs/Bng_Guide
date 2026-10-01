import React, { useState } from 'react';
import { useLocation } from '../context/LocationContext';
import { translations, PRESET_LOCATIONS } from '../i18n/translations';
import { MapPin, RefreshCw, Globe, ChevronDown, Compass } from 'lucide-react';
import { Language } from '../types';

export const Header: React.FC = () => {
  const {
    currentArea,
    subArea,
    refreshLocation,
    isLoading,
    language,
    setLanguage,
    setSimulatedLocation,
    requestLocation,
    activePresetId
  } = useLocation();

  const [showLocationPicker, setShowLocationPicker] = useState(false);
  const [showLangPicker, setShowLangPicker] = useState(false);
  const t = translations[language];

  return (
    <header className="glass-header">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        {/* Current Location Badge */}
        <div style={{ position: 'relative' }}>
          <button
            onClick={() => setShowLocationPicker(!showLocationPicker)}
            style={{
              background: 'rgba(216, 58, 72, 0.12)',
              border: '1px solid rgba(216, 58, 72, 0.35)',
              padding: '6px 12px',
              borderRadius: '9999px',
              color: '#fca5a5',
              fontSize: '13px',
              fontWeight: '600',
              cursor: 'pointer',
              display: 'flex',
              alignItems: 'center',
              gap: '6px',
              maxWidth: '220px'
            }}
          >
            <span className="pulse-dot" />
            <MapPin size={14} color="#ef4444" />
            <span style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', color: '#f8fafc' }}>
              {currentArea}
            </span>
            <ChevronDown size={12} color="#fca5a5" />
          </button>

          {/* Location Presets Dropdown */}
          {showLocationPicker && (
            <div style={{
              position: 'absolute',
              top: '40px',
              left: 0,
              width: '280px',
              background: '#1e293b',
              border: '1px solid rgba(255, 255, 255, 0.12)',
              borderRadius: '14px',
              padding: '8px',
              boxShadow: '0 12px 32px rgba(0,0,0,0.6)',
              zIndex: 100
            }}>
              <div style={{ fontSize: '11px', fontWeight: '700', color: '#94a3b8', padding: '6px 8px', textTransform: 'uppercase' }}>
                {t.selectLocationPreset}
              </div>

              {/* Real GPS Option */}
              <button
                onClick={() => {
                  requestLocation();
                  setShowLocationPicker(false);
                }}
                style={{
                  width: '100%',
                  padding: '8px 10px',
                  background: activePresetId === null ? 'rgba(56, 189, 248, 0.15)' : 'transparent',
                  border: 'none',
                  borderRadius: '8px',
                  color: activePresetId === null ? '#38bdf8' : '#f8fafc',
                  fontSize: '12px',
                  fontWeight: '600',
                  textAlign: 'left',
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '8px',
                  marginBottom: '4px'
                }}
              >
                <Compass size={14} />
                Live Device GPS
              </button>

              <div style={{ height: '1px', background: 'rgba(255,255,255,0.08)', margin: '4px 0' }} />

              {PRESET_LOCATIONS.map((preset) => {
                const isSelected = activePresetId === preset.id;
                const label = language === 'kn' ? preset.labelKn :
                              language === 'te' ? preset.labelTe :
                              language === 'hi' ? preset.labelHi : preset.name;
                return (
                  <button
                    key={preset.id}
                    onClick={() => {
                      setSimulatedLocation(preset.lat, preset.lng, preset.name);
                      setShowLocationPicker(false);
                    }}
                    style={{
                      width: '100%',
                      padding: '8px 10px',
                      background: isSelected ? 'rgba(239, 68, 68, 0.2)' : 'transparent',
                      border: 'none',
                      borderRadius: '8px',
                      color: isSelected ? '#fca5a5' : '#e2e8f0',
                      fontSize: '12px',
                      fontWeight: isSelected ? '700' : '500',
                      textAlign: 'left',
                      cursor: 'pointer',
                      display: 'flex',
                      flexDirection: 'column',
                      gap: '2px'
                    }}
                  >
                    <span>{label}</span>
                    <span style={{ fontSize: '10px', color: '#94a3b8' }}>{preset.description}</span>
                  </button>
                );
              })}
            </div>
          )}
        </div>

        {/* Right Tools: Refresh GPS & Language */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          {/* Refresh Location Button */}
          <button
            onClick={refreshLocation}
            title={t.refreshLocation}
            style={{
              width: '34px',
              height: '34px',
              borderRadius: '50%',
              background: 'rgba(255,255,255,0.06)',
              border: '1px solid rgba(255,255,255,0.1)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#f8fafc',
              cursor: 'pointer'
            }}
          >
            <RefreshCw size={15} style={{ animation: isLoading ? 'spin 1s linear infinite' : 'none' }} />
          </button>

          {/* Language Switcher */}
          <div style={{ position: 'relative' }}>
            <button
              onClick={() => setShowLangPicker(!showLangPicker)}
              style={{
                height: '34px',
                padding: '0 10px',
                borderRadius: '9999px',
                background: 'rgba(255,255,255,0.06)',
                border: '1px solid rgba(255,255,255,0.1)',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
                color: '#f8fafc',
                fontSize: '12px',
                fontWeight: '600',
                cursor: 'pointer'
              }}
            >
              <Globe size={14} color="#94a3b8" />
              <span style={{ textTransform: 'uppercase' }}>{language}</span>
            </button>

            {showLangPicker && (
              <div style={{
                position: 'absolute',
                top: '40px',
                right: 0,
                width: '150px',
                background: '#1e293b',
                border: '1px solid rgba(255, 255, 255, 0.12)',
                borderRadius: '12px',
                padding: '6px',
                boxShadow: '0 12px 32px rgba(0,0,0,0.6)',
                zIndex: 100
              }}>
                {(['en', 'kn', 'te', 'hi'] as Language[]).map((lang) => {
                  const names = {
                    en: 'English',
                    kn: 'ಕನ್ನಡ',
                    te: 'తెలుగు',
                    hi: 'हिन्दी'
                  };
                  return (
                    <button
                      key={lang}
                      onClick={() => {
                        setLanguage(lang);
                        setShowLangPicker(false);
                      }}
                      style={{
                        width: '100%',
                        padding: '8px 10px',
                        background: language === lang ? 'rgba(239, 68, 68, 0.2)' : 'transparent',
                        border: 'none',
                        borderRadius: '6px',
                        color: language === lang ? '#fca5a5' : '#f8fafc',
                        fontSize: '12px',
                        fontWeight: '600',
                        textAlign: 'left',
                        cursor: 'pointer'
                      }}
                    >
                      {names[lang]}
                    </button>
                  );
                })}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Sub Area Bar */}
      {subArea && (
        <div style={{ fontSize: '11px', color: '#94a3b8', marginTop: '4px', paddingLeft: '4px' }}>
          {t.youAreNear} <strong style={{ color: '#e2e8f0' }}>{subArea}</strong>
        </div>
      )}
    </header>
  );
};
