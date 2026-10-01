export type Language = 'en' | 'kn' | 'te' | 'hi';

export interface DistanceMatrix {
  distanceKm: number;
  walkingMinutes: number;
  drivingMinutes: number;
  transitMinutes: number;
  metroMinutes: number;
  autoMinutes: number;
  cyclingMinutes: number;
  formattedDistance: string;
}

export interface Place {
  id: number;
  name: string;
  kannadaName?: string;
  teluguName?: string;
  hindiName?: string;
  category: string;
  categoryLabel?: string;
  subCategory?: string;
  latitude: number;
  longitude: number;
  address?: string;
  area?: string;
  description?: string;
  rating?: number;
  reviewCount?: number;
  openingHours?: string;
  phone?: string;
  website?: string;
  imageUrl?: string;
  isFamous?: boolean;
  tags?: string;
  popularFor?: string;
  distanceMatrix?: DistanceMatrix;
  isSaved?: boolean;
}

export interface Category {
  id: number;
  key: string;
  name: string;
  kannadaName?: string;
  teluguName?: string;
  hindiName?: string;
  icon: string;
  displayOrder: number;
  description?: string;
  placeCount: number;
}

export interface RouteStep {
  stepNumber: number;
  mode: 'WALK' | 'BUS' | 'METRO' | 'CAR' | 'AUTO' | 'BICYCLE';
  instruction: string;
  instructionKn?: string;
  instructionTe?: string;
  instructionHi?: string;
  distanceKm: number;
  durationMinutes: number;
  icon: string;
  lineOrRouteName?: string;
  departureStop?: string;
  arrivalStop?: string;
  numStops?: number;
}

export interface TransportOption {
  mode: 'WALK' | 'BUS' | 'METRO' | 'CAR' | 'AUTO' | 'BICYCLE';
  title: string;
  icon: string;
  distanceKm: number;
  durationMinutes: number;
  formattedDuration: string;
  formattedDistance: string;
  estimatedFare: string;
  summary: string;
  isAvailable: boolean;
  unavailabilityReason?: string;
  steps: RouteStep[];
  pathCoordinates: [number, number][];
}

export interface RouteComparison {
  originName: string;
  originLat: number;
  originLng: number;
  destinationName: string;
  destinationLat: number;
  destinationLng: number;
  options: TransportOption[];
}

export interface ReverseGeocodeResult {
  areaName: string;
  subArea?: string;
  formattedAddress: string;
  latitude: number;
  longitude: number;
  landmark?: string;
  city: string;
  state: string;
  isInsideBengaluru: boolean;
}

export interface SavedPlace {
  id: number;
  placeId: number;
  placeName: string;
  category?: string;
  address?: string;
  latitude?: number;
  longitude?: number;
  imageUrl?: string;
  notes?: string;
  savedAt?: string;
}

export interface PresetLocation {
  id: string;
  name: string;
  labelKn: string;
  labelTe: string;
  labelHi: string;
  lat: number;
  lng: number;
  description: string;
}
