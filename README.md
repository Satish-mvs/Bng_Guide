# 🏙️ Bengaluru Guide — Mobile Application

A production-grade, full-stack mobile navigation and places discovery application built for first-time visitors and residents in **Bengaluru (Namma Bengaluru)**.

---

## 🌟 Core Value Proposition

> **"Discover → Distance → Transport Options → Route → Destination"**

When a visitor arrives in Bengaluru (e.g. from Andhra Pradesh arriving at **Majestic / Kempegowda Bus Station**), they often do not know local roads, bus routes, metro lines, or nearby attractions.

**Bengaluru Guide** instantly detects their location, reverse geocodes the area, displays nearby and famous attractions with authentic distances and travel times, and provides a multi-modal **"How to Reach"** breakdown across:
1. 🚶 **Walking** (Pedestrian walkways, estimated walking minutes)
2. 🚌 **BMTC Bus** (Nearest bus stop, route numbers e.g. G-4, 252, 335E, 365, boarding & deboarding points, fare estimation)
3. 🚇 **Namma Metro** (Purple Line, Green Line, station interchanges at *Nadaprabhu Kempegowda Station Majestic*, stop count, walking connections, fare)
4. 🚗 **Car / Taxi** (Arterial roads, estimated cab fares, real-time driving minutes)
5. 🛺 **Auto Rickshaw** (Bengaluru Government meter fare formula: ₹30 first 2 km + ₹15/km)
6. 🚲 **Cycling** (Eco-friendly routes)

---

## 📱 Tech Stack & Architecture

### **Frontend**
- **Framework**: React 18+ with TypeScript & Vite
- **UI Design System**: Vanilla CSS Design System with CSS Custom Properties, Glassmorphism, Responsive Mobile Bezel & Full-screen toggle
- **Maps**: Interactive Leaflet Map with OpenStreetMap CartoDB Tiles, Category-specific SVG markers, and user GPS beacon
- **Icons**: Lucide React
- **Multilingual Localization**: Native support for **English**, **Kannada (ಕನ್ನಡ)**, **Telugu (తెలుగు)**, and **Hindi (हिन्दी)**
- **State & Geolocation**: React Context API + HTML5 Geolocation API + Instant Bengaluru Hub Simulation

### **Backend**
- **Runtime**: Java 17+ (Eclipse Adoptium OpenJDK 17)
- **Framework**: Spring Boot 3.3.4 (REST Controllers, Spring Data JPA, Hibernate)
- **Database**: Embedded H2 (in-memory & file-persisted default) with zero-configuration startup, fully compatible with PostgreSQL
- **Routing Engine**: Haversine distance calculator, realistic Bangalore road routing coefficients, Namma Metro transit graph BFS with interchange logic, BMTC route matcher, OpenStreetMap Nominatim reverse geocoding gateway

---

## 🚀 REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/places/nearby?lat={lat}&lng={lng}&radius={km}&category={cat}` | Discover places within radius sorted by proximity |
| `GET` | `/api/famous-places?lat={lat}&lng={lng}&radius={km}` | Curated famous Bengaluru landmarks with travel times |
| `GET` | `/api/places/search?q={query}&lat={lat}&lng={lng}` | Natural query search (e.g., "temples near me", "restaurants") |
| `GET` | `/api/places/{id}?lat={lat}&lng={lng}` | Full place details with image, hours, contact, rating |
| `GET` | `/api/categories` | 17 categories with real-time POI counts |
| `GET` | `/api/transport-options?fromLat={lat}&fromLng={lng}&toLat={lat}&toLng={lng}` | Multi-modal route comparison matrix & step-by-step directions |
| `GET` | `/api/location/reverse?lat={lat}&lng={lng}` | Reverse geocode coordinates to Bengaluru area name & address |
| `GET` | `/api/metro/stations` | All Namma Metro stations across Purple & Green lines |
| `GET` | `/api/bmtc/routes` | BMTC bus routes, stops, and schedules |
| `GET` | `/api/saved-places` | Retrieve bookmarked places |
| `POST` | `/api/saved-places` | Bookmark a place |
| `DELETE` | `/api/saved-places/{id}` | Remove a bookmark |

---

## 🏛 Supported Categories

1. 🏛 **Famous Places** (Bengaluru Palace, Vidhana Soudha, Tipu Sultan's Palace, Bangalore Fort...)
2. 🛕 **Temples** (ISKCON Temple Rajajinagar, Bull Temple / Dodda Basavana Gudi, Banashankari...)
3. 🛍 **Shopping Malls** (UB City, Orion Mall, Phoenix Marketcity, Commercial Street, Brigade Road...)
4. 🍴 **Restaurants** (Vidyarthi Bhavan, MTR Mavalli Tiffin Room, Nagarjuna Andhra Meals...)
5. ☕ **Cafes** (CTR Shri Sagar, Brahmin's Coffee Bar, Truffles...)
6. 🏨 **Hotels** (The Lalit Ashok, Hotel Grand Pavilion...)
7. 🌳 **Parks & Gardens** (Cubbon Park, Lalbagh Botanical Garden...)
8. 🏞 **Tourist Attractions** (Visvesvaraya Museum, Planetarium, Bannerghatta Park, Nandi Hills, Wonderla...)
9. 🎬 **Movie Theatres** (Urvashi Cinema, PVR IMAX Orion...)
10. 🏥 **Hospitals** (Victoria Hospital, Bowring & Lady Curzon, Manipal Hospital...)
11. 💊 **Pharmacies** (Apollo Pharmacy 24/7, MedPlus...)
12. 🚇 **Metro Stations** (Nadaprabhu Kempegowda Majestic, MG Road, Cubbon Park, Indiranagar...)
13. 🚌 **Bus Stops** (Majestic KBS, Shivajinagar, Domlur...)
14. 🚉 **Railway Stations** (KSR Bengaluru City, Yesvantpur...)
15. 🏦 **Banks** (SBI Main Branch Majestic...)
16. 🏧 **ATMs** (HDFC 24/7 ATM, Canara Bank ATM...)
17. 🛒 **Markets** (KR Market Flower Bazaar, Gandhi Bazaar, Malleshwaram 8th Cross...)

---

## 🏃 Running Locally

### 1. Start Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```
Backend will start on `http://localhost:8080`.
H2 Console is accessible at `http://localhost:8080/h2-console`.

### 2. Start Frontend (React + Vite)
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173` in your mobile or desktop browser!

---

## 🌐 Multilingual Support
Switch between **English**, **ಕನ್ನಡ (Kannada)**, **తెలుగు (Telugu)**, and **हिन्दी (Hindi)** anytime using the top-right language switcher or from the **Profile** tab.

---

## 🔒 Privacy & Safety
Location data is processed directly in the user's browser session for routing calculations. GPS coordinates are never stored permanently on the server without explicit bookmarking.
