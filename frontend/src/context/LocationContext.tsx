import React, { createContext, useContext, useState, useEffect } from 'react';
import { Language, ReverseGeocodeResult, SavedPlace, Place } from '../types';
import { reverseGeocode, fetchSavedPlaces, savePlace, deleteSavedPlace } from '../services/api';
import { PRESET_LOCATIONS } from '../i18n/translations';

export type PermissionState = 'prompt' | 'granted' | 'denied' | 'gps_off' | 'error' | 'manual';

interface LocationContextType {
  permissionState: PermissionState;
  currentCoords: { lat: number; lng: number } | null;
  currentArea: string;
  formattedAddress: string;
  subArea: string;
  reverseGeocodeData: ReverseGeocodeResult | null;
  isLoading: boolean;
  error: string | null;
  selectedRadius: number;
  setSelectedRadius: (radius: number) => void;
  language: Language;
  setLanguage: (lang: Language) => void;
  savedPlaces: SavedPlace[];
  requestLocation: () => Promise<void>;
  setSimulatedLocation: (lat: number, lng: number, name: string) => Promise<void>;
  refreshLocation: () => Promise<void>;
  toggleSavePlace: (place: Place) => Promise<void>;
  isPlaceSaved: (placeId: number) => boolean;
  activePresetId: string | null;
}

const LocationContext = createContext<LocationContextType | undefined>(undefined);

export const LocationProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [permissionState, setPermissionState] = useState<PermissionState>('prompt');
  const [currentCoords, setCurrentCoords] = useState<{ lat: number; lng: number } | null>(null);
  const [currentArea, setCurrentArea] = useState<string>('Detecting Location...');
  const [formattedAddress, setFormattedAddress] = useState<string>('');
  const [subArea, setSubArea] = useState<string>('');
  const [reverseGeocodeData, setReverseGeocodeData] = useState<ReverseGeocodeResult | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [selectedRadius, setSelectedRadius] = useState<number>(5.0);
  const [language, setLanguageState] = useState<Language>('en');
  const [savedPlaces, setSavedPlaces] = useState<SavedPlace[]>([]);
  const [activePresetId, setActivePresetId] = useState<string | null>(null);

  // Load language and saved places from storage on mount
  useEffect(() => {
    const savedLang = localStorage.getItem('bng_guide_lang') as Language;
    if (savedLang && ['en', 'kn', 'te', 'hi'].includes(savedLang)) {
      setLanguageState(savedLang);
    }
    loadBookmarks();
  }, []);

  const setLanguage = (lang: Language) => {
    setLanguageState(lang);
    localStorage.setItem('bng_guide_lang', lang);
  };

  const loadBookmarks = async () => {
    try {
      const list = await fetchSavedPlaces();
      setSavedPlaces(list);
    } catch {
      // Local storage fallback
      const local = localStorage.getItem('bng_saved_places');
      if (local) {
        setSavedPlaces(JSON.parse(local));
      }
    }
  };

  const updateAreaDetails = async (lat: number, lng: number, fallbackArea?: string) => {
    setIsLoading(true);
    try {
      const geo = await reverseGeocode(lat, lng);
      setReverseGeocodeData(geo);
      setCurrentArea(geo.areaName || fallbackArea || 'Majestic');
      setFormattedAddress(geo.formattedAddress || 'Majestic, Bengaluru, Karnataka');
      setSubArea(geo.subArea || 'Bengaluru');
      setError(null);
    } catch {
      setCurrentArea(fallbackArea || 'Majestic');
      setFormattedAddress(`${fallbackArea || 'Majestic'}, Bengaluru, Karnataka`);
      setSubArea('Bengaluru');
    } finally {
      setIsLoading(false);
    }
  };

  const requestLocation = async () => {
    setIsLoading(true);
    setError(null);

    if (!navigator.geolocation) {
      setPermissionState('error');
      setError('Geolocation is not supported by your browser/device.');
      setIsLoading(false);
      return;
    }

    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        const lat = pos.coords.latitude;
        const lng = pos.coords.longitude;
        setCurrentCoords({ lat, lng });
        setPermissionState('granted');
        setActivePresetId(null);
        await updateAreaDetails(lat, lng);
      },
      async (err) => {
        console.warn('Geolocation error:', err);
        if (err.code === err.PERMISSION_DENIED) {
          setPermissionState('denied');
          setError('Location permission was denied. Please allow access or pick a simulated Bengaluru location.');
        } else if (err.code === err.POSITION_UNAVAILABLE) {
          setPermissionState('gps_off');
          setError('GPS location unavailable. Please check device location settings.');
        } else {
          setPermissionState('error');
          setError('Location request timed out. You can choose a location manually.');
        }
        setIsLoading(false);
      },
      { enableHighAccuracy: true, timeout: 10000, maximumAge: 60000 }
    );
  };

  const setSimulatedLocation = async (lat: number, lng: number, name: string) => {
    setIsLoading(true);
    setCurrentCoords({ lat, lng });
    setPermissionState('granted');
    const matchedPreset = PRESET_LOCATIONS.find(p => p.lat === lat && p.lng === lng);
    setActivePresetId(matchedPreset ? matchedPreset.id : null);
    await updateAreaDetails(lat, lng, name);
    setIsLoading(false);
  };

  const refreshLocation = async () => {
    if (currentCoords) {
      await updateAreaDetails(currentCoords.lat, currentCoords.lng);
    } else {
      await requestLocation();
    }
  };

  const toggleSavePlace = async (place: Place) => {
    const existing = savedPlaces.find(s => s.placeId === place.id);
    if (existing) {
      try {
        await deleteSavedPlace(existing.id);
        const updated = savedPlaces.filter(s => s.id !== existing.id);
        setSavedPlaces(updated);
        localStorage.setItem('bng_saved_places', JSON.stringify(updated));
      } catch {
        const updated = savedPlaces.filter(s => s.placeId !== place.id);
        setSavedPlaces(updated);
        localStorage.setItem('bng_saved_places', JSON.stringify(updated));
      }
    } else {
      try {
        const saved = await savePlace(place.id, 'Bookmarked in Bengaluru Guide');
        const updated = [saved, ...savedPlaces];
        setSavedPlaces(updated);
        localStorage.setItem('bng_saved_places', JSON.stringify(updated));
      } catch {
        const fakeSaved: SavedPlace = {
          id: Date.now(),
          placeId: place.id,
          placeName: place.name,
          category: place.category,
          address: place.address,
          latitude: place.latitude,
          longitude: place.longitude,
          imageUrl: place.imageUrl,
          notes: 'Bookmarked',
          savedAt: new Date().toISOString()
        };
        const updated = [fakeSaved, ...savedPlaces];
        setSavedPlaces(updated);
        localStorage.setItem('bng_saved_places', JSON.stringify(updated));
      }
    }
  };

  const isPlaceSaved = (placeId: number) => {
    return savedPlaces.some(s => s.placeId === placeId);
  };

  return (
    <LocationContext.Provider
      value={{
        permissionState,
        currentCoords,
        currentArea,
        formattedAddress,
        subArea,
        reverseGeocodeData,
        isLoading,
        error,
        selectedRadius,
        setSelectedRadius,
        language,
        setLanguage,
        savedPlaces,
        requestLocation,
        setSimulatedLocation,
        refreshLocation,
        toggleSavePlace,
        isPlaceSaved,
        activePresetId
      }}
    >
      {children}
    </LocationContext.Provider>
  );
};

export const useLocation = () => {
  const context = useContext(LocationContext);
  if (!context) {
    throw new Error('useLocation must be used within a LocationProvider');
  }
  return context;
};
