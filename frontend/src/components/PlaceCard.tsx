import React from 'react';
import { Place } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { Heart, Star, MapPin, Navigation, Info, Clock, Sparkles } from 'lucide-react';

interface Props {
  place: Place;
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

export const PlaceCard: React.FC<Props> = ({
  place,
  onViewDetails,
  onHowToReach
}) => {
  const { language, toggleSavePlace, isPlaceSaved } = useLocation();
  const t = translations[language];
  const saved = isPlaceSaved(place.id);

  const getLocalizedName = () => {
    if (language === 'kn' && place.kannadaName) return place.kannadaName;
    if (language === 'te' && place.teluguName) return place.teluguName;
    if (language === 'hi' && place.hindiName) return place.hindiName;
    return place.name;
  };

  const dm = place.distanceMatrix;

  return (
    <div className="place-card">
      {/* Image Header with Tags */}
      <div className="place-image-wrapper">
        <img
          src={place.imageUrl || 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80'}
          alt={place.name}
          className="place-image"
          loading="lazy"
        />
        <div className="place-image-overlay" />

        {/* Badges top left */}
        <div className="place-badge-top">
          {place.isFamous && (
            <span className="famous-tag">
              <Sparkles size={11} />
              Famous
            </span>
          )}
          <span className="category-tag">
            {place.subCategory || place.categoryLabel || 'Attraction'}
          </span>
        </div>

        {/* Bookmark Button top right */}
        <button
          className={`bookmark-btn ${saved ? 'saved' : ''}`}
          onClick={(e) => {
            e.stopPropagation();
            toggleSavePlace(place);
          }}
          title={saved ? t.saved : t.savePlace}
        >
          <Heart size={16} fill={saved ? '#ef4444' : 'none'} color={saved ? '#ef4444' : '#ffffff'} />
        </button>
      </div>

      {/* Card Content Body */}
      <div className="place-card-body">
        {/* Title & Rating */}
        <div className="place-title-row">
          <h3 className="place-title">{getLocalizedName()}</h3>
          {place.rating && (
            <div className="place-rating-badge">
              <Star size={12} fill="#fbbf24" color="#fbbf24" />
              <span>{place.rating.toFixed(1)}</span>
            </div>
          )}
        </div>

        {/* Distance & Area */}
        <div className="place-address">
          <MapPin size={12} color="#ef4444" />
          <span>
            {dm ? <strong style={{ color: '#f8fafc' }}>{dm.formattedDistance}</strong> : 'Nearby'}
            {place.area ? ` • ${place.area}` : ''}
          </span>
        </div>

        {/* Exact Multi-modal Travel Times Grid */}
        {dm && (
          <div className="transport-times-grid">
            <div className="transport-time-item">
              <span className="transport-icon">🚶</span>
              <span className="transport-mode-label">{t.walk}</span>
              <span className="transport-time-val">{dm.walkingMinutes}m</span>
            </div>

            <div className="transport-time-item">
              <span className="transport-icon">🚗</span>
              <span className="transport-mode-label">{t.car}</span>
              <span className="transport-time-val">{dm.drivingMinutes}m</span>
            </div>

            <div className="transport-time-item">
              <span className="transport-icon">🚌</span>
              <span className="transport-mode-label">{t.bus}</span>
              <span className="transport-time-val">{dm.transitMinutes}m</span>
            </div>

            <div className="transport-time-item">
              <span className="transport-icon">🚇</span>
              <span className="transport-mode-label">{t.metro}</span>
              <span className="transport-time-val">{dm.metroMinutes}m</span>
            </div>
          </div>
        )}

        {/* Action Buttons */}
        <div className="card-actions">
          <button
            className="btn-secondary"
            onClick={() => onViewDetails(place)}
          >
            <Info size={14} />
            <span>{t.viewDetails}</span>
          </button>

          <button
            className="btn-primary"
            onClick={() => onHowToReach(place)}
          >
            <Navigation size={14} />
            <span>{t.howToReach}</span>
          </button>
        </div>
      </div>
    </div>
  );
};
