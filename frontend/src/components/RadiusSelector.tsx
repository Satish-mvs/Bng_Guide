import React from 'react';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { Sliders } from 'lucide-react';

const RADIUS_OPTIONS = [
  { value: 0.5, label: '500 m' },
  { value: 1.0, label: '1 km' },
  { value: 2.0, label: '2 km' },
  { value: 5.0, label: '5 km' },
  { value: 10.0, label: '10 km' },
  { value: 25.0, label: '25 km' }
];

export const RadiusSelector: React.FC = () => {
  const { selectedRadius, setSelectedRadius, language } = useLocation();
  const t = translations[language];

  return (
    <div className="radius-bar">
      <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
        <Sliders size={13} color="#94a3b8" />
        <span className="radius-label">{t.radius}</span>
      </div>

      <div className="radius-pills">
        {RADIUS_OPTIONS.map((opt) => (
          <button
            key={opt.value}
            className={`radius-pill ${selectedRadius === opt.value ? 'active' : ''}`}
            onClick={() => setSelectedRadius(opt.value)}
          >
            {opt.label}
          </button>
        ))}
      </div>
    </div>
  );
};
