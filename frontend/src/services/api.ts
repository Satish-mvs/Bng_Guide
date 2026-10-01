import { Category, Place, RouteComparison, ReverseGeocodeResult, SavedPlace } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export async function fetchCategories(): Promise<Category[]> {
  try {
    const res = await fetch(`${API_BASE_URL}/categories`);
    if (!res.ok) throw new Error('Failed to fetch categories');
    return await res.json();
  } catch (err) {
    console.warn('API Error, using fallback:', err);
    return [];
  }
}

export async function fetchNearbyPlaces(
  lat?: number,
  lng?: number,
  radius: number = 5.0,
  category?: string
): Promise<Place[]> {
  const params = new URLSearchParams();
  if (lat !== undefined) params.append('lat', lat.toString());
  if (lng !== undefined) params.append('lng', lng.toString());
  params.append('radius', radius.toString());
  if (category && category !== 'ALL') params.append('category', category);

  const res = await fetch(`${API_BASE_URL}/places/nearby?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to fetch nearby places');
  return await res.json();
}

export async function fetchFamousPlaces(
  lat?: number,
  lng?: number,
  radius?: number,
  category?: string
): Promise<Place[]> {
  const params = new URLSearchParams();
  if (lat !== undefined) params.append('lat', lat.toString());
  if (lng !== undefined) params.append('lng', lng.toString());
  if (radius !== undefined) params.append('radius', radius.toString());
  if (category && category !== 'ALL') params.append('category', category);

  const res = await fetch(`${API_BASE_URL}/famous-places?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to fetch famous places');
  return await res.json();
}

export async function searchPlaces(
  q: string,
  lat?: number,
  lng?: number
): Promise<Place[]> {
  const params = new URLSearchParams();
  params.append('q', q);
  if (lat !== undefined) params.append('lat', lat.toString());
  if (lng !== undefined) params.append('lng', lng.toString());

  const res = await fetch(`${API_BASE_URL}/places/search?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to search places');
  return await res.json();
}

export async function fetchPlaceById(
  id: number,
  lat?: number,
  lng?: number
): Promise<Place> {
  const params = new URLSearchParams();
  if (lat !== undefined) params.append('lat', lat.toString());
  if (lng !== undefined) params.append('lng', lng.toString());

  const res = await fetch(`${API_BASE_URL}/places/${id}?${params.toString()}`);
  if (!res.ok) throw new Error(`Failed to fetch place ${id}`);
  return await res.json();
}

export async function fetchTransportOptions(
  fromLat: number,
  fromLng: number,
  toLat: number,
  toLng: number,
  destName?: string
): Promise<RouteComparison> {
  const params = new URLSearchParams();
  params.append('fromLat', fromLat.toString());
  params.append('fromLng', fromLng.toString());
  params.append('toLat', toLat.toString());
  params.append('toLng', toLng.toString());
  if (destName) params.append('destName', destName);

  const res = await fetch(`${API_BASE_URL}/transport-options?${params.toString()}`);
  if (!res.ok) throw new Error('Failed to calculate transport options');
  return await res.json();
}

export async function reverseGeocode(lat: number, lng: number): Promise<ReverseGeocodeResult> {
  const res = await fetch(`${API_BASE_URL}/location/reverse?lat=${lat}&lng=${lng}`);
  if (!res.ok) throw new Error('Failed to reverse geocode');
  return await res.json();
}

export async function fetchSavedPlaces(): Promise<SavedPlace[]> {
  const res = await fetch(`${API_BASE_URL}/saved-places`);
  if (!res.ok) throw new Error('Failed to fetch saved places');
  return await res.json();
}

export async function savePlace(placeId: number, notes?: string): Promise<SavedPlace> {
  const res = await fetch(`${API_BASE_URL}/saved-places`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ placeId, notes: notes || 'Bookmarked' })
  });
  if (!res.ok) throw new Error('Failed to save place');
  return await res.json();
}

export async function deleteSavedPlace(id: number): Promise<void> {
  const res = await fetch(`${API_BASE_URL}/saved-places/${id}`, {
    method: 'DELETE'
  });
  if (!res.ok) throw new Error('Failed to delete saved place');
}
