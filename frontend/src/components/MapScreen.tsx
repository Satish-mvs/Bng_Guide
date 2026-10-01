import React, { useEffect, useRef, useState } from 'react';
import { Place } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Navigation, Compass, Layers, Info, Star, Heart, X } from 'lucide-react';

interface Props {
  places: Place[];
  onViewDetails: (place: Place) => void;
  onHowToReach: (place: Place) => void;
}

export const MapScreen: React.FC<Props> = ({
  places,
  onViewDetails,
  onHowToReach
}) => {
  const { currentCoords, currentArea, language, toggleSavePlace, isPlaceSaved } = useLocation();
  const mapContainerRef = useRef<HTMLDivElement>(null);
  const mapInstanceRef = useRef<L.Map | null>(null);
  const markersLayerRef = useRef<L.LayerGroup | null>(null);
  const [selectedPlace, setSelectedPlace] = useState<Place | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const t = translations[language];

  const userLat = currentCoords?.lat || 12.9767;
  const userLng = currentCoords?.lng || 77.5713;

  useEffect(() => {
    if (!mapContainerRef.current) return;

    if (!mapInstanceRef.current) {
      // Initialize Leaflet Map
      const map = L.map(mapContainerRef.current, {
        center: [userLat, userLng],
        zoom: 14,
        zoomControl: false
      });

      // Add OpenStreetMap tile layer (Clean CartoDB Dark/Voyager style)
      L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
        attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
        maxZoom: 19
      }).addTo(map);

      // Add Zoom control bottom right
      L.control.zoom({ position: 'topright' }).addTo(map);

      markersLayerRef.current = L.layerGroup().addTo(map);
      mapInstanceRef.current = map;
    } else {
      mapInstanceRef.current.setView([userLat, userLng], mapInstanceRef.current.getZoom());
    }

    renderMarkers();
  }, [userLat, userLng]);

  useEffect(() => {
    renderMarkers();
  }, [places, selectedCategory]);

  const renderMarkers = () => {
    if (!mapInstanceRef.current || !markersLayerRef.current) return;
    markersLayerRef.current.clearLayers();

    // 1. User Location Marker with pulsing blue beacon
    const userIcon = L.divIcon({
      className: 'custom-user-marker',
      html: `
        <div style="position: relative; width: 24px; height: 24px;">
          <div style="position: absolute; inset: 0; background: rgba(56, 189, 248, 0.4); border-radius: 50%; animation: pulse-ring 2s infinite;"></div>
          <div style="position: absolute; inset: 4px; background: #0284c7; border: 2px solid #ffffff; border-radius: 50%; box-shadow: 0 0 10px rgba(0,0,0,0.5);"></div>
        </div>
      `,
      iconSize: [24, 24],
      iconAnchor: [12, 12]
    });

    const userMarker = L.marker([userLat, userLng], { icon: userIcon });
    userMarker.bindTooltip(`📍 You are near: ${currentArea}`, { permanent: false, direction: 'top' });
    markersLayerRef.current.addLayer(userMarker);

    // 2. Place Markers
    const filteredPlaces = selectedCategory === 'ALL'
      ? places
      : places.filter(p => p.category === selectedCategory);

    filteredPlaces.forEach((place) => {
      const isFamous = place.isFamous;
      const markerColor = isFamous ? '#f59e0b' : '#ef4444';
      const iconEmoji = getCategoryEmoji(place.category);

      const placeIcon = L.divIcon({
        className: 'custom-place-marker',
        html: `
          <div style="
            background: ${markerColor};
            width: 32px;
            height: 32px;
            border-radius: 50% 50% 50% 0;
            transform: rotate(-45deg);
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px solid #ffffff;
            box-shadow: 0 4px 10px rgba(0,0,0,0.4);
            cursor: pointer;
          ">
            <span style="transform: rotate(45deg); font-size: 14px;">${iconEmoji}</span>
          </div>
        `,
        iconSize: [32, 32],
        iconAnchor: [16, 32]
      });

      const marker = L.marker([place.latitude, place.longitude], { icon: placeIcon });
      marker.on('click', () => {
        setSelectedPlace(place);
        if (mapInstanceRef.current) {
          mapInstanceRef.current.panTo([place.latitude, place.longitude]);
        }
      });
      markersLayerRef.current?.addLayer(marker);
    });
  };

  const getCategoryEmoji = (category: string) => {
    switch (category) {
      case 'FAMOUS_PLACES': return '🏛';
      case 'TEMPLES': return '🛕';
      case 'SHOPPING_MALLS': return '🛍';
      case 'RESTAURANTS': return '🍴';
      case 'CAFES': return '☕';
      case 'PARKS': return '🌳';
      case 'HOSPITALS': return '🏥';
      case 'METRO_STATIONS': return '🚇';
      case 'BUS_STOPS': return '🚌';
      case 'MARKETS': return '🛒';
      default: return '📍';
    }
  };

  const recenterToUser = () => {
    if (mapInstanceRef.current) {
      mapInstanceRef.current.flyTo([userLat, userLng], 15, { duration: 1 });
    }
  };

  return (
    <div style={{ position: 'relative', width: '100%', height: 'calc(100vh - 110px)', overflow: 'hidden' }}>
      {/* Map Element */}
      <div ref={mapContainerRef} style={{ width: '100%', height: '100%' }} />

      {/* Floating Recenter Button */}
      <button
        onClick={recenterToUser}
        title="Recenter to my location"
        style={{
          position: 'absolute',
          bottom: selectedPlace ? '180px' : '20px',
          right: '16px',
          zIndex: 400,
          width: '44px',
          height: '44px',
          borderRadius: '50%',
          background: 'rgba(15, 23, 42, 0.9)',
          backdropFilter: 'blur(8px)',
          border: '1px solid rgba(255,255,255,0.15)',
          color: '#38bdf8',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          boxShadow: '0 4px 16px rgba(0,0,0,0.5)',
          cursor: 'pointer',
          transition: 'all 0.2s ease'
        }}
      >
        <Compass size={22} />
      </button>

      {/* Selected Marker Bottom Preview Sheet */}
      {selectedPlace && (
        <div style={{
          position: 'absolute',
          bottom: '12px',
          left: '12px',
          right: '12px',
          zIndex: 500,
          background: 'rgba(15, 23, 42, 0.95)',
          backdropFilter: 'blur(12px)',
          border: '1px solid rgba(255, 255, 255, 0.15)',
          borderRadius: '16px',
          padding: '12px 14px',
          boxShadow: '0 8px 32px rgba(0,0,0,0.6)',
          animation: 'slide-up 0.25s ease'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '6px' }}>
            <div style={{ display: 'flex', gap: '10px' }}>
              <img
                src={selectedPlace.imageUrl || 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80'}
                alt={selectedPlace.name}
                style={{ width: '54px', height: '54px', borderRadius: '10px', objectFit: 'cover' }}
              />
              <div>
                <h4 style={{ fontSize: '14px', fontWeight: '700', color: '#f8fafc', lineHeight: '1.2' }}>
                  {selectedPlace.name}
                </h4>
                <div style={{ fontSize: '11px', color: '#94a3b8', marginTop: '2px' }}>
                  {selectedPlace.subCategory || selectedPlace.categoryLabel} • {selectedPlace.distanceMatrix ? selectedPlace.distanceMatrix.formattedDistance : 'Nearby'}
                </div>
                {selectedPlace.rating && (
                  <div style={{ display: 'flex', alignItems: 'center', gap: '3px', color: '#fbbf24', fontSize: '11px', marginTop: '2px' }}>
                    <Star size={11} fill="#fbbf24" color="#fbbf24" />
                    <span>{selectedPlace.rating.toFixed(1)}</span>
                  </div>
                )}
              </div>
            </div>

            <button
              onClick={() => setSelectedPlace(null)}
              style={{ background: 'none', border: 'none', color: '#94a3b8', cursor: 'pointer', padding: '2px' }}
            >
              <X size={16} />
            </button>
          </div>

          {/* Action Row */}
          <div style={{ display: 'flex', gap: '8px', marginTop: '8px' }}>
            <button
              onClick={() => onViewDetails(selectedPlace)}
              className="btn-secondary"
              style={{ height: '34px', fontSize: '12px' }}
            >
              <Info size={13} />
              <span>{t.viewDetails}</span>
            </button>

            <button
              onClick={() => onHowToReach(selectedPlace)}
              className="btn-primary"
              style={{ height: '34px', fontSize: '12px' }}
            >
              <Navigation size={13} />
              <span>{t.howToReach}</span>
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
