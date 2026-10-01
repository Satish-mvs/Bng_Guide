import React from 'react';
import { Place } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import {
  X,
  MapPin,
  Star,
  Clock,
  Phone,
  Globe,
  Heart,
  Share2,
  Navigation,
  Sparkles,
  Info,
  CheckCircle
} from 'lucide-react';

interface Props {
  place: Place;
  onClose: () => void;
  onHowToReach: (place: Place) => void;
}

export const PlaceDetailsModal: React.FC<Props> = ({
  place,
  onClose,
  onHowToReach
}) => {
  const { language, toggleSavePlace, isPlaceSaved } = useLocation();
  const t = translations[language];
  const saved = isPlaceSaved(place.id);
  const [copied, setCopied] = React.useState(false);

  const getLocalizedName = () => {
    if (language === 'kn' && place.kannadaName) return place.kannadaName;
    if (language === 'te' && place.teluguName) return place.teluguName;
    if (language === 'hi' && place.hindiName) return place.hindiName;
    return place.name;
  };

  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: place.name,
          text: `Check out ${place.name} on Bengaluru Guide!`,
          url: window.location.href
        });
      } catch {
        // User cancelled share
      }
    } else {
      navigator.clipboard.writeText(`${place.name} - ${place.address || 'Bengaluru'}`);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  const dm = place.distanceMatrix;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-sheet" onClick={(e) => e.stopPropagation()}>
        <div className="sheet-handle" />

        {/* Hero Image */}
        <div style={{ position: 'relative', height: '220px', width: '100%', background: '#1e293b' }}>
          <img
            src={place.imageUrl || 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80'}
            alt={place.name}
            style={{ width: '100%', height: '100%', objectFit: 'cover' }}
          />
          <div style={{ position: 'absolute', inset: 0, background: 'linear-gradient(180deg, rgba(15,23,42,0.3) 0%, rgba(15,23,42,0.95) 100%)' }} />

          {/* Close button */}
          <button
            onClick={onClose}
            style={{
              position: 'absolute',
              top: '12px',
              right: '12px',
              width: '34px',
              height: '34px',
              borderRadius: '50%',
              background: 'rgba(15, 23, 42, 0.7)',
              backdropFilter: 'blur(6px)',
              border: '1px solid rgba(255,255,255,0.15)',
              color: '#ffffff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              cursor: 'pointer'
            }}
          >
            <X size={18} />
          </button>

          {/* Category & Famous Pill */}
          <div style={{ position: 'absolute', bottom: '12px', left: '16px', display: 'flex', gap: '6px' }}>
            {place.isFamous && (
              <span className="famous-tag">
                <Sparkles size={11} />
                Famous Bengaluru Landmark
              </span>
            )}
            <span className="category-tag">
              {place.subCategory || place.categoryLabel || 'Attraction'}
            </span>
          </div>
        </div>

        {/* Content Body */}
        <div style={{ padding: '16px 18px 28px' }}>
          {/* Title and Rating */}
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: '10px', marginBottom: '8px' }}>
            <div>
              <h2 style={{ fontSize: '20px', fontWeight: '800', color: '#f8fafc', lineHeight: '1.25' }}>
                {getLocalizedName()}
              </h2>
              {language !== 'en' && place.name && (
                <div style={{ fontSize: '13px', color: '#94a3b8', marginTop: '2px' }}>
                  {place.name}
                </div>
              )}
            </div>

            {place.rating && (
              <div className="place-rating-badge" style={{ fontSize: '13px', padding: '4px 8px' }}>
                <Star size={14} fill="#fbbf24" color="#fbbf24" />
                <span>{place.rating.toFixed(1)}</span>
                <span style={{ fontSize: '10px', color: '#94a3b8' }}>({place.reviewCount || 100})</span>
              </div>
            )}
          </div>

          {/* Distance & Address */}
          <div style={{ background: 'rgba(30, 41, 59, 0.6)', border: '1px solid var(--border-subtle)', borderRadius: '14px', padding: '12px', margin: '12px 0' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
              <MapPin size={16} color="#ef4444" />
              <div style={{ fontSize: '13px', color: '#f8fafc', fontWeight: '600' }}>
                {dm ? dm.formattedDistance + ' away from you' : 'In Bengaluru'}
              </div>
            </div>
            <div style={{ fontSize: '12px', color: '#94a3b8', paddingLeft: '24px', lineHeight: '1.4' }}>
              {place.address || 'Bengaluru, Karnataka'}
            </div>
          </div>

          {/* Travel Times Grid */}
          {dm && (
            <div style={{ marginBottom: '14px' }}>
              <div style={{ fontSize: '11px', fontWeight: '700', color: '#94a3b8', textTransform: 'uppercase', marginBottom: '6px', letterSpacing: '0.5px' }}>
                {t.approxTime} from your location
              </div>
              <div className="transport-times-grid">
                <div className="transport-time-item">
                  <span className="transport-icon">🚶</span>
                  <span className="transport-mode-label">{t.walk}</span>
                  <span className="transport-time-val">{dm.walkingMinutes} min</span>
                </div>
                <div className="transport-time-item">
                  <span className="transport-icon">🚗</span>
                  <span className="transport-mode-label">{t.car}</span>
                  <span className="transport-time-val">{dm.drivingMinutes} min</span>
                </div>
                <div className="transport-time-item">
                  <span className="transport-icon">🚌</span>
                  <span className="transport-mode-label">{t.bus}</span>
                  <span className="transport-time-val">{dm.transitMinutes} min</span>
                </div>
                <div className="transport-time-item">
                  <span className="transport-icon">🚇</span>
                  <span className="transport-mode-label">{t.metro}</span>
                  <span className="transport-time-val">{dm.metroMinutes} min</span>
                </div>
              </div>
            </div>
          )}

          {/* Opening Hours & Contact */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', marginBottom: '14px' }}>
            {place.openingHours && (
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '12px', color: '#e2e8f0' }}>
                <Clock size={15} color="#38bdf8" />
                <span>{t.openingHours}: <strong>{place.openingHours}</strong></span>
              </div>
            )}
            {place.popularFor && (
              <div style={{ display: 'flex', alignItems: 'flex-start', gap: '8px', fontSize: '12px', color: '#fbbf24' }}>
                <Sparkles size={15} style={{ flexShrink: 0, marginTop: '2px' }} />
                <span>{t.popularFor}: {place.popularFor}</span>
              </div>
            )}
          </div>

          {/* Description */}
          {place.description && (
            <div style={{ fontSize: '13px', color: '#cbd5e1', lineHeight: '1.6', marginBottom: '16px' }}>
              {place.description}
            </div>
          )}

          {/* Action Row 1: Primary "How to Reach" Button */}
          <button
            onClick={() => {
              onClose();
              onHowToReach(place);
            }}
            style={{
              width: '100%',
              height: '48px',
              background: 'linear-gradient(135deg, #a71d2a 0%, #ef4444 100%)',
              border: 'none',
              borderRadius: '14px',
              color: '#ffffff',
              fontSize: '15px',
              fontWeight: '700',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '8px',
              cursor: 'pointer',
              boxShadow: '0 6px 18px rgba(239, 68, 68, 0.4)',
              marginBottom: '10px'
            }}
          >
            <Navigation size={18} />
            {t.howToReach}
          </button>

          {/* Action Row 2: Secondary Tools: Save, Share, Call, Website */}
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '8px' }}>
            <button
              onClick={() => toggleSavePlace(place)}
              style={{
                height: '42px',
                background: saved ? 'rgba(239, 68, 68, 0.2)' : 'var(--bg-surface)',
                border: `1px solid ${saved ? '#ef4444' : 'var(--border-subtle)'}`,
                borderRadius: '12px',
                color: saved ? '#fca5a5' : '#f8fafc',
                fontSize: '11px',
                fontWeight: '600',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '2px',
                cursor: 'pointer'
              }}
            >
              <Heart size={14} fill={saved ? '#ef4444' : 'none'} color={saved ? '#ef4444' : '#ffffff'} />
              <span>{saved ? t.saved : t.savePlace}</span>
            </button>

            <button
              onClick={handleShare}
              style={{
                height: '42px',
                background: 'var(--bg-surface)',
                border: '1px solid var(--border-subtle)',
                borderRadius: '12px',
                color: '#f8fafc',
                fontSize: '11px',
                fontWeight: '600',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                gap: '2px',
                cursor: 'pointer'
              }}
            >
              {copied ? <CheckCircle size={14} color="#10b981" /> : <Share2 size={14} />}
              <span>{copied ? 'Copied' : t.share}</span>
            </button>

            {place.phone ? (
              <a
                href={`tel:${place.phone}`}
                style={{
                  height: '42px',
                  background: 'var(--bg-surface)',
                  border: '1px solid var(--border-subtle)',
                  borderRadius: '12px',
                  color: '#f8fafc',
                  fontSize: '11px',
                  fontWeight: '600',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                  gap: '2px',
                  textDecoration: 'none',
                  cursor: 'pointer'
                }}
              >
                <Phone size={14} color="#10b981" />
                <span>{t.call}</span>
              </a>
            ) : (
              <div
                style={{
                  height: '42px',
                  background: 'rgba(255,255,255,0.03)',
                  border: '1px solid var(--border-subtle)',
                  borderRadius: '12px',
                  color: 'var(--text-muted)',
                  fontSize: '11px',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                  gap: '2px',
                  opacity: 0.5
                }}
              >
                <Phone size={14} />
                <span>{t.call}</span>
              </div>
            )}

            {place.website ? (
              <a
                href={place.website}
                target="_blank"
                rel="noreferrer"
                style={{
                  height: '42px',
                  background: 'var(--bg-surface)',
                  border: '1px solid var(--border-subtle)',
                  borderRadius: '12px',
                  color: '#f8fafc',
                  fontSize: '11px',
                  fontWeight: '600',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                  gap: '2px',
                  textDecoration: 'none',
                  cursor: 'pointer'
                }}
              >
                <Globe size={14} color="#38bdf8" />
                <span>{t.website}</span>
              </a>
            ) : (
              <div
                style={{
                  height: '42px',
                  background: 'rgba(255,255,255,0.03)',
                  border: '1px solid var(--border-subtle)',
                  borderRadius: '12px',
                  color: 'var(--text-muted)',
                  fontSize: '11px',
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                  gap: '2px',
                  opacity: 0.5
                }}
              >
                <Globe size={14} />
                <span>{t.website}</span>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
