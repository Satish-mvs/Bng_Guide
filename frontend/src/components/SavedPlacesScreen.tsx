import React, { useState, useEffect } from 'react';
import { Place } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { fetchPlaceById } from '../services/api';
import { Heart, Trash2, Navigation, Info, Sparkles, MapPin } from 'lucide-react';

interface Props {
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

export const SavedPlacesScreen: React.FC<Props> = ({
  onViewDetails,
  onHowToReach
}) => {
  const { savedPlaces, toggleSavePlace, currentCoords, language } = useLocation();
  const [placesData, setPlacesData] = useState<Place[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const t = translations[language];

  const userLat = currentCoords?.lat || 12.9767;
  const userLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    loadSavedDetails();
  }, [savedPlaces, userLat, userLng]);

  const loadSavedDetails = async () => {
    setLoading(true);
    try {
      const promises = savedPlaces.map(s => fetchPlaceById(s.placeId, userLat, userLng).catch(() => null));
      const results = await Promise.all(promises);
      setPlacesData(results.filter((p): p is Place => p !== null));
    } catch (err) {
      console.error('Error fetching saved places:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="screen-scroll-container">
      {/* Header Banner */}
      <div style={{ padding: '20px 18px 14px', background: 'var(--bg-surface)', borderBottom: '1px solid var(--border-subtle)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Heart size={20} color="#ef4444" fill="#ef4444" />
          <h1 style={{ fontSize: '20px', fontWeight: '800', color: '#f8fafc' }}>
            {t.bookmarks}
          </h1>
        </div>
        <p style={{ fontSize: '13px', color: '#94a3b8', marginTop: '4px' }}>
          Places you bookmarked for easy offline access and quick directions.
        </p>
      </div>

      {loading ? (
        <div style={{ padding: '40px', textAlign: 'center', color: '#94a3b8' }}>
          <div className="pulse-dot" style={{ margin: '0 auto 10px' }} />
          <div>Loading saved places...</div>
        </div>
      ) : placesData.length === 0 ? (
        <div style={{
          background: 'rgba(30, 41, 59, 0.4)',
          border: '1px solid var(--border-subtle)',
          borderRadius: '16px',
          padding: '36px 20px',
          margin: '24px 16px',
          textAlign: 'center'
        }}>
          <Heart size={36} color="#64748b" style={{ margin: '0 auto 12px' }} />
          <h3 style={{ fontSize: '16px', fontWeight: '700', color: '#f8fafc' }}>
            No Saved Places Yet
          </h3>
          <p style={{ fontSize: '13px', color: '#94a3b8', marginTop: '6px' }}>
            Tap the heart icon on any famous spot or restaurant in Bengaluru to save it here!
          </p>
        </div>
      ) : (
        <div style={{ padding: '14px 0' }}>
          {placesData.map((place) => (
            <div
              key={place.id}
              style={{
                background: 'var(--bg-card)',
                border: '1px solid var(--border-subtle)',
                borderRadius: '16px',
                padding: '14px',
                margin: '0 16px 12px',
                display: 'flex',
                flexDirection: 'column',
                gap: '10px'
              }}
            >
              <div style={{ display: 'flex', gap: '12px' }}>
                <img
                  src={place.imageUrl || 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80'}
                  alt={place.name}
                  style={{ width: '68px', height: '68px', borderRadius: '12px', objectFit: 'cover' }}
                />
                <div style={{ flex: 1 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                    <h3 style={{ fontSize: '15px', fontWeight: '700', color: '#f8fafc', lineHeight: '1.25' }}>
                      {place.name}
                    </h3>
                    <button
                      onClick={() => toggleSavePlace(place)}
                      title="Remove bookmark"
                      style={{ background: 'none', border: 'none', color: '#f87171', cursor: 'pointer', padding: '2px' }}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>

                  <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '2px' }}>
                    {place.subCategory || place.categoryLabel}
                  </div>

                  <div style={{ fontSize: '12px', color: '#38bdf8', fontWeight: '600', display: 'flex', alignItems: 'center', gap: '4px', marginTop: '4px' }}>
                    <MapPin size={12} />
                    <span>{place.distanceMatrix ? place.distanceMatrix.formattedDistance : 'Nearby'}</span>
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div style={{ display: 'flex', gap: '8px' }}>
                <button
                  className="btn-secondary"
                  onClick={() => onViewDetails(place)}
                  style={{ height: '34px', fontSize: '12px' }}
                >
                  <Info size={13} />
                  <span>{t.viewDetails}</span>
                </button>

                <button
                  className="btn-primary"
                  onClick={() => onHowToReach(place)}
                  style={{ height: '34px', fontSize: '12px' }}
                >
                  <Navigation size={13} />
                  <span>{t.howToReach}</span>
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
