import React, { useState, useEffect } from 'react';
import { Place, Category } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { CategoryChips } from './CategoryChips';
import { RadiusSelector } from './RadiusSelector';
import { PlaceCard } from './PlaceCard';
import { fetchNearbyPlaces, fetchFamousPlaces, searchPlaces } from '../services/api';
import { Search, X, Sparkles, MapPin, Compass, AlertCircle } from 'lucide-react';

interface Props {
  categories: Category[];
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

export const HomeScreen: React.FC<Props> = ({
  categories,
  onViewDetails,
  onHowToReach
}) => {
  const { currentCoords, selectedRadius, language } = useLocation();
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [nearbyPlaces, setNearbyPlaces] = useState<Place[]>([]);
  const [famousPlaces, setFamousPlaces] = useState<Place[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const t = translations[language];

  const userLat = currentCoords?.lat || 12.9767;
  const userLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    loadPlaces();
  }, [userLat, userLng, selectedRadius, selectedCategory, searchQuery]);

  const loadPlaces = async () => {
    setLoading(true);
    try {
      if (searchQuery.trim().length > 0) {
        const results = await searchPlaces(searchQuery, userLat, userLng);
        setNearbyPlaces(results);
        setFamousPlaces(results.filter(r => r.isFamous));
      } else {
        const [nearby, famous] = await Promise.all([
          fetchNearbyPlaces(userLat, userLng, selectedRadius, selectedCategory),
          fetchFamousPlaces(userLat, userLng, selectedRadius, selectedCategory)
        ]);
        setNearbyPlaces(nearby);
        setFamousPlaces(famous);
      }
    } catch (err) {
      console.error('Failed to load places:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="screen-scroll-container">
      {/* Search Bar with Natural Query Support */}
      <div className="search-container">
        <Search className="search-icon-left" size={18} />
        <input
          type="text"
          className="search-input"
          placeholder={t.searchPlaceholder}
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        {searchQuery && (
          <button className="search-clear-btn" onClick={() => setSearchQuery('')}>
            <X size={16} />
          </button>
        )}
      </div>

      {/* Horizontally Scrollable Categories */}
      <CategoryChips
        categories={categories}
        selectedCategory={selectedCategory}
        onSelectCategory={setSelectedCategory}
      />

      {/* Radius Selector */}
      <RadiusSelector />

      {/* Content Feed */}
      {loading ? (
        <div style={{ padding: '40px 20px', textAlign: 'center', color: '#94a3b8' }}>
          <div className="pulse-dot" style={{ margin: '0 auto 12px' }} />
          <div>Finding authentic places near your location...</div>
        </div>
      ) : (
        <div style={{ paddingTop: '10px' }}>
          {/* Section 1: Famous Places Near You */}
          {famousPlaces.length > 0 && (
            <div style={{ marginBottom: '18px' }}>
              <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                padding: '0 18px 10px'
              }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <Sparkles size={16} color="#fbbf24" />
                  <h2 style={{ fontSize: '15px', fontWeight: '800', color: '#f8fafc', letterSpacing: '-0.2px' }}>
                    {t.famousPlacesNearYou}
                  </h2>
                </div>
                <span style={{ fontSize: '11px', color: '#fbbf24', fontWeight: '700' }}>
                  {famousPlaces.length} Iconic
                </span>
              </div>

              {famousPlaces.map((place) => (
                <PlaceCard
                  key={`famous-${place.id}`}
                  place={place}
                  onViewDetails={onViewDetails}
                  onHowToReach={onHowToReach}
                />
              ))}
            </div>
          )}

          {/* Section 2: All Places Near You */}
          <div>
            <div style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              padding: '4px 18px 10px'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                <MapPin size={16} color="#ef4444" />
                <h2 style={{ fontSize: '15px', fontWeight: '800', color: '#f8fafc', letterSpacing: '-0.2px' }}>
                  {t.placesNearYou}
                </h2>
              </div>
              <span style={{ fontSize: '11px', color: '#94a3b8', fontWeight: '600' }}>
                Within {selectedRadius} km ({nearbyPlaces.length})
              </span>
            </div>

            {nearbyPlaces.length === 0 ? (
              <div style={{
                background: 'rgba(30, 41, 59, 0.5)',
                border: '1px solid var(--border-subtle)',
                borderRadius: '16px',
                padding: '28px 20px',
                margin: '10px 16px',
                textAlign: 'center'
              }}>
                <AlertCircle size={28} color="#94a3b8" style={{ margin: '0 auto 8px' }} />
                <div style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc' }}>
                  {t.noPlacesFound}
                </div>
                <div style={{ fontSize: '12px', color: '#94a3b8', marginTop: '4px' }}>
                  Try expanding the radius to 10 km or 25 km above.
                </div>
              </div>
            ) : (
              nearbyPlaces.map((place) => (
                <PlaceCard
                  key={`nearby-${place.id}`}
                  place={place}
                  onViewDetails={onViewDetails}
                  onHowToReach={onHowToReach}
                />
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
};
