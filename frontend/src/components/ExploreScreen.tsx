import React, { useState, useEffect } from 'react';
import { Place, Category } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { PlaceCard } from './PlaceCard';
import { fetchFamousPlaces, fetchNearbyPlaces } from '../services/api';
import { Compass, Sparkles, Filter, Search } from 'lucide-react';

interface Props {
  categories: Category[];
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

export const ExploreScreen: React.FC<Props> = ({
  categories,
  onViewDetails,
  onHowToReach
}) => {
  const { currentCoords, language } = useLocation();
  const [places, setPlaces] = useState<Place[]>([]);
  const [selectedFilter, setSelectedFilter] = useState<string>('ALL');
  const [loading, setLoading] = useState<boolean>(true);
  const t = translations[language];

  const userLat = currentCoords?.lat || 12.9767;
  const userLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    loadExplorePlaces();
  }, [userLat, userLng, selectedFilter]);

  const loadExplorePlaces = async () => {
    setLoading(true);
    try {
      if (selectedFilter === 'ALL') {
        const famous = await fetchFamousPlaces(userLat, userLng, 50.0);
        setPlaces(famous);
      } else {
        const filtered = await fetchNearbyPlaces(userLat, userLng, 50.0, selectedFilter);
        setPlaces(filtered);
      }
    } catch (err) {
      console.error('Failed to load explore places:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="screen-scroll-container">
      {/* Banner */}
      <div style={{
        padding: '20px 18px',
        background: 'linear-gradient(135deg, rgba(139, 0, 0, 0.4) 0%, rgba(30, 41, 59, 0.9) 100%)',
        borderBottom: '1px solid var(--border-subtle)',
        marginBottom: '14px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
          <Sparkles size={20} color="#fbbf24" />
          <h1 style={{ fontSize: '20px', fontWeight: '800', color: '#f8fafc' }}>
            {t.exploreTitle}
          </h1>
        </div>
        <p style={{ fontSize: '13px', color: '#94a3b8' }}>
          {t.exploreSubtitle}
        </p>
      </div>

      {/* Filter Chips */}
      <div style={{ display: 'flex', overflowX: 'auto', gap: '8px', padding: '0 16px 12px' }}>
        <button
          className={`category-chip ${selectedFilter === 'ALL' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('ALL')}
        >
          <span>⭐</span>
          <span>All Iconic Gems</span>
        </button>
        <button
          className={`category-chip ${selectedFilter === 'FAMOUS_PLACES' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('FAMOUS_PLACES')}
        >
          <span>🏛</span>
          <span>Palaces & Forts</span>
        </button>
        <button
          className={`category-chip ${selectedFilter === 'PARKS' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('PARKS')}
        >
          <span>🌳</span>
          <span>Gardens</span>
        </button>
        <button
          className={`category-chip ${selectedFilter === 'TEMPLES' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('TEMPLES')}
        >
          <span>🛕</span>
          <span>Historic Temples</span>
        </button>
        <button
          className={`category-chip ${selectedFilter === 'SHOPPING_MALLS' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('SHOPPING_MALLS')}
        >
          <span>🛍</span>
          <span>Shopping & High Streets</span>
        </button>
        <button
          className={`category-chip ${selectedFilter === 'TOURIST_ATTRACTIONS' ? 'active' : ''}`}
          onClick={() => setSelectedFilter('TOURIST_ATTRACTIONS')}
        >
          <span>🏞</span>
          <span>Day Trips & Safari</span>
        </button>
      </div>

      {/* Places List */}
      {loading ? (
        <div style={{ padding: '40px', textAlign: 'center', color: '#94a3b8' }}>
          <div className="pulse-dot" style={{ margin: '0 auto 12px' }} />
          <div>Curating Bengaluru highlights...</div>
        </div>
      ) : (
        <div>
          {places.map((place) => (
            <PlaceCard
              key={`explore-${place.id}`}
              place={place}
              onViewDetails={onViewDetails}
              onHowToReach={onHowToReach}
            />
          ))}
        </div>
      )}
    </div>
  );
};
