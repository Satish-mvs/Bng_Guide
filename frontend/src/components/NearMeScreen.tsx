import React, { useState, useEffect } from 'react';
import { Place, Category } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { PlaceCard } from './PlaceCard';
import { fetchNearbyPlaces } from '../services/api';
import {
  Compass,
  Utensils,
  CreditCard,
  Train,
  Landmark,
  HeartPulse,
  ShoppingBag,
  Bus,
  Coffee,
  Building2
} from 'lucide-react';

interface Props {
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

interface ServiceCategory {
  key: string;
  name: string;
  icon: React.ReactNode;
  emoji: string;
}

const ESSENTIAL_CATEGORIES: ServiceCategory[] = [
  { key: 'RESTAURANTS', name: 'Food & Meals', icon: <Utensils size={18} color="#f59e0b" />, emoji: '🍴' },
  { key: 'CAFES', name: 'Coffee & Cafes', icon: <Coffee size={18} color="#d97706" />, emoji: '☕' },
  { key: 'ATMS', name: 'ATMs & Cash', icon: <CreditCard size={18} color="#10b981" />, emoji: '🏧' },
  { key: 'METRO_STATIONS', name: 'Namma Metro', icon: <Train size={18} color="#8b5cf6" />, emoji: '🚇' },
  { key: 'BUS_STOPS', name: 'Bus Stops', icon: <Bus size={18} color="#3b82f6" />, emoji: '🚌' },
  { key: 'TEMPLES', name: 'Temples', icon: <Landmark size={18} color="#f59e0b" />, emoji: '🛕' },
  { key: 'HOSPITALS', name: 'Hospitals', icon: <HeartPulse size={18} color="#ef4444" />, emoji: '🏥' },
  { key: 'PHARMACIES', name: 'Pharmacies', icon: <HeartPulse size={18} color="#06b6d4" />, emoji: '💊' },
  { key: 'MARKETS', name: 'Markets & Bazaars', icon: <ShoppingBag size={18} color="#ec4899" />, emoji: '🛒' },
  { key: 'HOTELS', name: 'Hotels & Stay', icon: <Building2 size={18} color="#6366f1" />, emoji: '🏨' }
];

export const NearMeScreen: React.FC<Props> = ({
  onViewDetails,
  onHowToReach
}) => {
  const { currentCoords, currentArea, language } = useLocation();
  const [activeCategory, setActiveCategory] = useState<string>('RESTAURANTS');
  const [places, setPlaces] = useState<Place[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [categoryDistances, setCategoryDistances] = useState<Record<string, string>>({});
  const t = translations[language];

  const userLat = currentCoords?.lat || 12.9767;
  const userLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    loadCategoryPlaces();
  }, [userLat, userLng, activeCategory]);

  useEffect(() => {
    loadCategoryOverview();
  }, [userLat, userLng]);

  const loadCategoryPlaces = async () => {
    setLoading(true);
    try {
      const data = await fetchNearbyPlaces(userLat, userLng, 15.0, activeCategory);
      setPlaces(data);
    } catch (err) {
      console.error('Failed to load places for category:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadCategoryOverview = async () => {
    try {
      const all = await fetchNearbyPlaces(userLat, userLng, 15.0);
      const distances: Record<string, string> = {};

      ESSENTIAL_CATEGORIES.forEach((cat) => {
        const matching = all.filter(p => p.category === cat.key);
        if (matching.length > 0 && matching[0].distanceMatrix) {
          distances[cat.key] = matching[0].distanceMatrix.formattedDistance;
        } else {
          distances[cat.key] = 'Nearby';
        }
      });
      setCategoryDistances(distances);
    } catch (err) {
      console.error('Failed to calculate category distances:', err);
    }
  };

  return (
    <div className="screen-scroll-container">
      {/* Header Banner */}
      <div style={{ padding: '20px 18px 14px', background: 'var(--bg-surface)', borderBottom: '1px solid var(--border-subtle)' }}>
        <h1 style={{ fontSize: '20px', fontWeight: '800', color: '#f8fafc', marginBottom: '4px' }}>
          {t.nearMeTitle}
        </h1>
        <p style={{ fontSize: '13px', color: '#94a3b8' }}>
          {t.youAreNear} <strong style={{ color: '#f8fafc' }}>{currentArea}</strong>
        </p>
      </div>

      {/* Grid of Essential Services with Live Proximity */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(2, 1fr)',
        gap: '10px',
        padding: '16px'
      }}>
        {ESSENTIAL_CATEGORIES.map((cat) => {
          const isActive = activeCategory === cat.key;
          const dist = categoryDistances[cat.key] || 'Checking...';
          return (
            <button
              key={cat.key}
              onClick={() => setActiveCategory(cat.key)}
              style={{
                background: isActive ? 'rgba(216, 58, 72, 0.15)' : 'var(--bg-card)',
                border: `1px solid ${isActive ? '#ef4444' : 'var(--border-subtle)'}`,
                borderRadius: '16px',
                padding: '14px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                cursor: 'pointer',
                textAlign: 'left',
                transition: 'all 0.2s ease'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <span style={{ fontSize: '22px' }}>{cat.emoji}</span>
                <div>
                  <div style={{ fontSize: '13px', fontWeight: '700', color: isActive ? '#fca5a5' : '#f8fafc' }}>
                    {cat.name}
                  </div>
                  <div style={{ fontSize: '11px', color: '#38bdf8', fontWeight: '600' }}>
                    {dist}
                  </div>
                </div>
              </div>
            </button>
          );
        })}
      </div>

      {/* Results Header */}
      <div style={{ padding: '6px 18px 10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h3 style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc' }}>
          {ESSENTIAL_CATEGORIES.find(c => c.key === activeCategory)?.name} Near {currentArea}
        </h3>
        <span style={{ fontSize: '12px', color: '#94a3b8' }}>
          {places.length} found
        </span>
      </div>

      {/* Places Feed */}
      {loading ? (
        <div style={{ padding: '30px', textAlign: 'center', color: '#94a3b8' }}>
          <div className="pulse-dot" style={{ margin: '0 auto 10px' }} />
          <div>Locating nearby services...</div>
        </div>
      ) : (
        <div>
          {places.map((place) => (
            <PlaceCard
              key={`nearme-${place.id}`}
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
