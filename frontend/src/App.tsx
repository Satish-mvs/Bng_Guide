import React, { useState, useEffect } from 'react';
import { LocationProvider, useLocation } from './context/LocationContext';
import { LocationPermissionScreen } from './components/LocationPermissionScreen';
import { Header } from './components/Header';
import { HomeScreen } from './components/HomeScreen';
import { ExploreScreen } from './components/ExploreScreen';
import { NearMeScreen } from './components/NearMeScreen';
import { MapScreen } from './components/MapScreen';
import { SavedPlacesScreen } from './components/SavedPlacesScreen';
import { ProfileScreen } from './components/ProfileScreen';
import { BottomNavigation, TabType } from './components/BottomNavigation';
import { HowToReachModal } from './components/HowToReachModal';
import { PlaceDetailsModal } from './components/PlaceDetailsModal';
import { Category, Place } from './types';
import { fetchCategories, fetchNearbyPlaces } from './services/api';
import { Wifi, Battery, Smartphone, Maximize2, Minimize2 } from 'lucide-react';

const MainApp: React.FC = () => {
  const { permissionState, currentCoords, setSimulatedLocation } = useLocation();
  const [activeTab, setActiveTab] = useState<TabType>('home');
  const [categories, setCategories] = useState<Category[]>([]);
  const [allPlaces, setAllPlaces] = useState<Place[]>([]);
  const [selectedPlaceForDetails, setSelectedPlaceForDetails] = useState<Place | null>(null);
  const [selectedPlaceForReach, setSelectedPlaceForReach] = useState<Place | null>(null);
  const [manualMode, setManualMode] = useState<boolean>(false);
  const [isFullWindow, setIsFullWindow] = useState<boolean>(false);
  const [currentTime, setCurrentTime] = useState<string>('');

  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      setCurrentTime(now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }));
    };
    updateTime();
    const interval = setInterval(updateTime, 60000);
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    loadInitialData();
  }, [currentCoords]);

  const loadInitialData = async () => {
    try {
      const cats = await fetchCategories();
      setCategories(cats);

      const lat = currentCoords?.lat || 12.9767;
      const lng = currentCoords?.lng || 77.5713;
      const places = await fetchNearbyPlaces(lat, lng, 30.0);
      setAllPlaces(places);
    } catch (err) {
      console.error('Error loading initial app data:', err);
    }
  };

  const handleManualSelect = () => {
    setManualMode(true);
    setSimulatedLocation(12.9767, 77.5713, 'Majestic');
  };

  const showPermissionScreen = permissionState === 'prompt' && !manualMode;

  return (
    <div className="app-viewport">
      {/* Desktop Frame Toggle */}
      <div style={{ position: 'fixed', top: '12px', right: '16px', zIndex: 1000, display: 'flex', gap: '8px' }}>
        <button
          onClick={() => setIsFullWindow(!isFullWindow)}
          style={{
            background: 'rgba(30, 41, 59, 0.8)',
            border: '1px solid rgba(255,255,255,0.15)',
            color: '#f8fafc',
            padding: '6px 12px',
            borderRadius: '20px',
            fontSize: '12px',
            fontWeight: '600',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '6px',
            backdropFilter: 'blur(8px)'
          }}
        >
          {isFullWindow ? <Minimize2 size={13} /> : <Maximize2 size={13} />}
          <span>{isFullWindow ? 'Phone Frame' : 'Full Screen'}</span>
        </button>
      </div>

      <div
        className="mobile-device"
        style={{
          maxWidth: isFullWindow ? '100%' : '440px',
          height: isFullWindow ? '100vh' : '92vh',
          maxHeight: isFullWindow ? '100vh' : '940px',
          borderRadius: isFullWindow ? '0' : undefined,
          border: isFullWindow ? 'none' : undefined
        }}
      >
        {/* Status Bar */}
        <div className="mobile-status-bar">
          <span className="status-time">{currentTime || '12:45'}</span>
          <div className="status-icons">
            <Wifi size={14} />
            <span style={{ fontSize: '11px', fontWeight: '700' }}>5G</span>
            <Battery size={15} />
          </div>
        </div>

        {/* Screen Content Switcher */}
        {showPermissionScreen ? (
          <LocationPermissionScreen onManualSelect={handleManualSelect} />
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', height: 'calc(100% - 40px)', position: 'relative' }}>
            <Header />

            <main style={{ flex: 1, position: 'relative', overflow: 'hidden' }}>
              {activeTab === 'home' && (
                <HomeScreen
                  categories={categories}
                  onViewDetails={setSelectedPlaceForDetails}
                  onHowToReach={setSelectedPlaceForReach}
                />
              )}

              {activeTab === 'explore' && (
                <ExploreScreen
                  categories={categories}
                  onViewDetails={setSelectedPlaceForDetails}
                  onHowToReach={setSelectedPlaceForReach}
                />
              )}

              {activeTab === 'map' && (
                <MapScreen
                  places={allPlaces}
                  onViewDetails={setSelectedPlaceForDetails}
                  onHowToReach={setSelectedPlaceForReach}
                />
              )}

              {activeTab === 'saved' && (
                <SavedPlacesScreen
                  onViewDetails={setSelectedPlaceForDetails}
                  onHowToReach={setSelectedPlaceForReach}
                />
              )}

              {activeTab === 'profile' && (
                <ProfileScreen />
              )}
            </main>

            {/* Bottom Navigation Bar */}
            <BottomNavigation activeTab={activeTab} onSelectTab={setActiveTab} />
          </div>
        )}

        {/* Modals */}
        {selectedPlaceForDetails && (
          <PlaceDetailsModal
            place={selectedPlaceForDetails}
            onClose={() => setSelectedPlaceForDetails(null)}
            onHowToReach={(p) => {
              setSelectedPlaceForDetails(null);
              setSelectedPlaceForReach(p);
            }}
          />
        )}

        {selectedPlaceForReach && (
          <HowToReachModal
            place={selectedPlaceForReach}
            onClose={() => setSelectedPlaceForReach(null)}
          />
        )}
      </div>
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <LocationProvider>
      <MainApp />
    </LocationProvider>
  );
};

export default App;
