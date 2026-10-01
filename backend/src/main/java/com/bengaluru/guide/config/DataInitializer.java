package com.bengaluru.guide.config;

import com.bengaluru.guide.entity.*;
import com.bengaluru.guide.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final MetroStationRepository metroStationRepository;
    private final BmtcRouteRepository bmtcRouteRepository;

    public DataInitializer(PlaceRepository placeRepository,
                           CategoryRepository categoryRepository,
                           MetroStationRepository metroStationRepository,
                           BmtcRouteRepository bmtcRouteRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.metroStationRepository = metroStationRepository;
        this.bmtcRouteRepository = bmtcRouteRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            initCategories();
        }
        if (placeRepository.count() == 0) {
            initPlaces();
        }
        if (metroStationRepository.count() == 0) {
            initMetroStations();
        }
        if (bmtcRouteRepository.count() == 0) {
            initBmtcRoutes();
        }
        log.info("Bengaluru Guide Database Initialized successfully with {} categories, {} places, {} metro stations, and {} bus routes.",
                categoryRepository.count(), placeRepository.count(), metroStationRepository.count(), bmtcRouteRepository.count());
    }

    private void initCategories() {
        categoryRepository.saveAll(List.of(
                new Category("FAMOUS_PLACES", "Famous Places", "ಪ್ರಸಿದ್ಧ ಸ್ಥಳಗಳು", "ప్రసిద్ధ ప్రదేశాలు", "प्रसिद्ध स्थान", "🏛", 1, "Iconic landmarks and heritage sites"),
                new Category("TEMPLES", "Temples", "ದೇವಾಲಯಗಳು", "దేవాలయాలు", "मंदिर", "🛕", 2, "Sacred and historic temples"),
                new Category("SHOPPING_MALLS", "Shopping Malls", "ಶಾಪಿಂಗ್ ಮಾಲ್‌ಗಳು", "షాపింగ్ మాల్స్", "शॉपिंग मॉल", "🛍", 3, "Modern shopping centers & brands"),
                new Category("RESTAURANTS", "Restaurants", "ಉಪಹಾರ ಗೃಹಗಳು", "రెస్టారెంట్లు", "रेस्तरां", "🍴", 4, "South Indian, Andhra & global dining"),
                new Category("CAFES", "Cafes", "ಕೆಫೆಗಳು", "కేఫ్‌లు", "कैफे", "☕", 5, "Filter coffee hubs & artisan cafes"),
                new Category("HOTELS", "Hotels", "ಹೋಟೆಲ್‌ಗಳು", "హోటళ్ళు", "होटल", "🏨", 6, "Comfortable stays & luxury resorts"),
                new Category("PARKS", "Parks & Gardens", "ಉದ್ಯಾನವನಗಳು", "పార్కులు & తోటలు", "पार्क और उद्यान", "🌳", 7, "Lush green botanical gardens & walking parks"),
                new Category("TOURIST_ATTRACTIONS", "Tourist Attractions", "ಪ್ರವಾಸಿ ತಾಣಗಳು", "పర్యాటక ఆకర్షణలు", "पर्यटक आकर्षण", "🏞", 8, "Sightseeing and amusement parks"),
                new Category("MOVIE_THEATRES", "Movie Theatres", "ಚಿತ್ರಮಂದಿರಗಳು", "సినిమా థియేటర్లు", "सिनेमाघर", "🎬", 9, "Multiplexes and single-screen cinemas"),
                new Category("HOSPITALS", "Hospitals", "ಆಸ್ಪತ್ರೆಗಳು", "ఆసుపత్రులు", "अस्पताल", "🏥", 10, "Emergency and multi-speciality medical care"),
                new Category("PHARMACIES", "Pharmacies", "ಔಷಧಾಲಯಗಳು", "ఫార్మసీలు", "दवाइयाँ", "💊", 11, "24/7 medical & chemist stores"),
                new Category("METRO_STATIONS", "Metro Stations", "ಮೆಟ್ರೋ ನಿಲ್ದಾಣಗಳು", "మెట్రో స్టేషన్లు", "मेट्रो स्टेशन", "🚇", 12, "Namma Metro stations & lines"),
                new Category("BUS_STOPS", "Bus Stops", "ಬಸ್ ನಿಲ್ದಾಣಗಳು", "బస్ స్టాప్‌లు", "बस स्टॉप", "🚌", 13, "BMTC bus stations and boarding points"),
                new Category("RAILWAY_STATIONS", "Railway Stations", "ರೈಲ್ವೆ ನಿಲ್ದಾಣಗಳು", "రైల్వే స్టేషన్లు", "रेलवे स्टेशन", "🚉", 14, "Indian Railways junctions"),
                new Category("BANKS", "Banks", "ಬ್ಯಾಂಕ್‌ಗಳು", "బ్యాంకులు", "बैंक", "🏦", 15, "Nationalized and private banks"),
                new Category("ATMS", "ATMs", "ಎಟಿಎಂಗಳು", "ఏటీఎంలు", "एटीएम", "🏧", 16, "Cash withdrawal counters"),
                new Category("MARKETS", "Markets", "ಮಾರುಕಟ್ಟೆಗಳು", "మార్కెట్లు", "बाज़ार", "🛒", 17, "Bustling local markets & street bazaars")
        ));
    }

    private void initPlaces() {
        placeRepository.saveAll(List.of(
                // 1. Famous / Heritage / Tourist
                new Place(
                        "Bengaluru Palace", "ಬೆಂಗಳೂರು ಅರಮನೆ", "బెంగళూరు ప్యాలెస్", "बेंगलुरु पैलेस",
                        "FAMOUS_PLACES", "Palace & Heritage", 12.9988, 77.5921,
                        "Vasanth Nagar, Bengaluru, Karnataka 560052", "Vasanth Nagar",
                        "Built in 1878 by Chamarajendra Wadiyar X in Tudor Revival style with fortified towers and woodcarvings.",
                        4.4, 52000, "10:00 AM - 5:30 PM", "+91 80 2235 5666", "https://karnatakatourism.org",
                        "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?w=800&q=80",
                        true, "palace,heritage,architecture,tudor,royal", "Tudor architecture, royal artifacts & sprawling grounds"
                ),
                new Place(
                        "Vidhana Soudha", "ವಿಧಾನ ಸೌಧ", "విధాన సౌధ", "विधान सौध",
                        "FAMOUS_PLACES", "Government Landmark", 12.9797, 77.5908,
                        "Dr Ambedkar Rd, Sampangi Rama Nagara, Bengaluru 560001", "Central Bengaluru",
                        "The seat of Karnataka state legislature, an iconic Neo-Dravidian architectural marvel built in 1956.",
                        4.7, 48000, "Illuminated evenings on Sundays & holidays", "+91 80 2225 0001", "https://kla.kar.nic.in",
                        "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?w=800&q=80",
                        true, "monument,architecture,government,heritage,neo-dravidian", "Magnificent Neo-Dravidian architecture & evening lighting"
                ),
                new Place(
                        "Cubbon Park", "ಕಬ್ಬನ್ ಪಾರ್ಕ್", "కబ్బన్ పార్క్", "कब्बन पार्क",
                        "PARKS", "Botanical Park", 12.9763, 77.5929,
                        "Kasturba Road, Sampangi Rama Nagara, Bengaluru 560001", "Central Bengaluru",
                        "Over 300 acres of green haven in the heart of Bengaluru, home to lush bamboo groves, state library, and museum.",
                        4.6, 68000, "6:00 AM - 7:00 PM (Mondays Closed)", "+91 80 2286 4125", "https://horticulture.kar.nic.in",
                        "https://images.unsplash.com/photo-1628178873041-39a5840d21e8?w=800&q=80",
                        true, "park,greenery,walk,nature,relaxation,garden", "Vast lung space, century-old trees and peaceful walkways"
                ),
                new Place(
                        "Lalbagh Botanical Garden", "ಲಾಲ್‌ಬಾಗ್ ಸಸ್ಯಶಾಸ್ತ್ರೀಯ ಉದ್ಯಾನ", "లాల్‌బాగ్ బొటానికల్ గార్డెన్", "लालबाग बॉटनिकल गार्डन",
                        "PARKS", "Botanical Garden", 12.9507, 77.5848,
                        "Mavalli, Bengaluru, Karnataka 560004", "Basavanagudi",
                        "Commissioned by Hyder Ali in 1760, famous for its grand Glass House modeled after London's Crystal Palace.",
                        4.5, 75000, "6:00 AM - 7:00 PM", "+91 80 2657 0181", "https://horticulture.kar.nic.in",
                        "https://images.unsplash.com/photo-1590073844006-33379778ae09?w=800&q=80",
                        true, "garden,glass house,flowers,lake,nature,hyder ali", "Iconic Glass House, annual flower shows & rare botanical species"
                ),
                new Place(
                        "ISKCON Temple Bengaluru", "ಇಸ್ಕಾನ್ ದೇವಸ್ಥಾನ", "ఇస్కాన్ టెంపుల్", "इस्कॉन मंदिर",
                        "TEMPLES", "Hindu Temple", 13.0098, 77.5511,
                        "Hare Krishna Hill, Chord Rd, Rajajinagar, Bengaluru 560010", "Rajajinagar",
                        "One of the largest ISKCON temples in the world, perched atop Hare Krishna Hill with breathtaking neo-classical architecture.",
                        4.7, 89000, "4:15 AM - 5:00 AM, 7:15 AM - 1:00 PM, 4:15 PM - 8:30 PM", "+91 80 2347 1956", "https://www.iskconbangalore.org",
                        "https://images.unsplash.com/photo-1609766857041-ed402ea8069a?w=800&q=80",
                        true, "temple,krishna,spiritual,architecture,rajajinagar", "Majestic hilltop temple, delicious prasadam and peaceful chanting"
                ),
                new Place(
                        "Tipu Sultan's Summer Palace", "ಟಿಪ್ಪು ಸುಲ್ತಾನ್ ಬೇಸಿಗೆ ಅರಮನೆ", "టిప్పు సుల్తాన్ వేసవి ప్యాలెస్", "टीपू सुल्तान का समर पैलेस",
                        "FAMOUS_PLACES", "Historical Palace", 12.9593, 77.5738,
                        "Tippu Sultan Palace Rd, Chamrajpet, Bengaluru 560018", "Chamrajpet",
                        "Built entirely of teakwood with carved pillars, arches and balconies. Known as the 'Abode of Peace' (Rashk-e-Jannat).",
                        4.2, 28000, "8:30 AM - 5:30 PM", "+91 80 2670 6836", "https://asi.nic.in",
                        "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=800&q=80",
                        true, "history,teakwood,palace,tipu sultan,chamrajpet", "Exquisite teakwood architecture and historical museum"
                ),
                new Place(
                        "Bangalore Fort", "ಬೆಂಗಳೂರು ಕೋಟೆ", "బెంగళూరు కోట", "बैंगलोर फोर्ट",
                        "FAMOUS_PLACES", "Historical Fort", 12.9629, 77.5756,
                        "Krishna Rajendra Rd, New Tharagupet, Bengaluru 560002", "KR Market",
                        "Originally built as a mud fort in 1537 by Kempe Gowda I, later reinforced in stone by Hyder Ali.",
                        4.1, 14000, "8:30 AM - 5:30 PM", "+91 80 2670 6836", "https://asi.nic.in",
                        "https://images.unsplash.com/photo-1599831104321-729227c94fa2?w=800&q=80",
                        true, "fort,kempe gowda,stone,heritage,history", "Ancient Delhi Gate, bastions and Kempegowda legacy"
                ),
                new Place(
                        "Bull Temple (Dodda Basavana Gudi)", "ದೊಡ್ಡ ಬಸವನ ಗುಡಿ", "బుల్ టెంపుల్", "बुल मंदिर",
                        "TEMPLES", "Sacred Temple", 12.9424, 77.5681,
                        "Bull Temple Rd, Basavanagudi, Bengaluru 560004", "Basavanagudi",
                        "Famous 16th century temple dedicated to Nandi, carved out of a single monolithic granite boulder.",
                        4.6, 24000, "6:00 AM - 8:00 PM", "+91 80 2667 8777", "https://karnatakatourism.org",
                        "https://images.unsplash.com/photo-1544717305-2782549b5136?w=800&q=80",
                        true, "temple,nandi,monolith,basavanagudi,kadalekai parse", "Colossal monolithic Nandi bull statue and Kadalekai Parishe"
                ),
                new Place(
                        "Visvesvaraya Industrial & Technological Museum", "ವಿಶ್ವೇಶ್ವರಯ್ಯ ವಸ್ತುಸಂಗ್ರಹಾಲಯ", "విశ్వేశ్వరయ్య మ్యూజియం", "विश्वेश्वरैया संग्रहालय",
                        "TOURIST_ATTRACTIONS", "Science Museum", 12.9752, 77.5963,
                        "Kasturba Rd, near Cubbon Park, Bengaluru 560001", "Central Bengaluru",
                        "Interactive science and tech museum featuring aerospace, engine mechanics, dinosaur enclave and fun science models.",
                        4.5, 41000, "9:30 AM - 6:00 PM", "+91 80 2286 6414", "https://vismuseum.gov.in",
                        "https://images.unsplash.com/photo-1565793298595-6a879b1d9492?w=800&q=80",
                        true, "science,museum,aerospace,children,technology", "Hands-on science experiments, full-scale Wright Brothers replica"
                ),
                new Place(
                        "Jawaharlal Nehru Planetarium", "ಜವಾಹರಲಾಲ್ ನೆಹರು ತಾರಾಲಯ", "జవహర్‌లాల్ నెహ్రూ ప్లానెటోరియం", "जवाहरलाल नेहरू तारामंडल",
                        "TOURIST_ATTRACTIONS", "Planetarium", 12.9849, 77.5896,
                        "Sri T, Sankey Rd, High Grounds, Bengaluru 560001", "High Grounds",
                        "Sky theater with high-resolution full-dome digital projections, science park and astronomy shows.",
                        4.4, 22000, "10:00 AM - 5:30 PM (Mondays Closed)", "+91 80 2237 9725", "https://taralaya.org",
                        "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&q=80",
                        true, "planetarium,space,astronomy,shows,stars", "Immersive space dome shows and outdoor science park"
                ),
                new Place(
                        "Commercial Street", "ಕಮರ್ಷಿಯಲ್ ಸ್ಟ್ರೀಟ್", "కమర్షియల్ స్ట్రీట్", "कमर्शियल स्ट्रीट",
                        "MARKETS", "Street Shopping", 12.9822, 77.6083,
                        "Tasker Town, Shivaji Nagar, Bengaluru 560001", "Shivajinagar",
                        "Bengaluru's legendary vibrant shopping street for clothes, jewelry, footwear, traditional silks, and street food.",
                        4.4, 38000, "10:30 AM - 9:30 PM", "+91 80 2558 0000", "https://bengalurucity.gov.in",
                        "https://images.unsplash.com/photo-1555529669-e69e7aa0ba9a?w=800&q=80",
                        true, "shopping,clothes,silks,bargain,street,food", "Famous street shopping hub, bridal wear and endless bargains"
                ),
                new Place(
                        "Brigade Road & MG Road", "ಬ್ರಿಗೇಡ್ ರಸ್ತೆ ಮತ್ತು ಎಂ.ಜಿ ರಸ್ತೆ", "బ్రిగేడ్ రోడ్ & ఎం.జి రోడ్", "ब्रिगेड रोड और एमजी रोड",
                        "SHOPPING_MALLS", "High Street & Nightlife", 12.9741, 77.6074,
                        "Shanthala Nagar, Ashok Nagar, Bengaluru 560001", "MG Road",
                        "The buzzing commercial epicenter of Bengaluru with retail showrooms, pubs, bookstores and festive illuminations.",
                        4.6, 62000, "10:00 AM - 11:30 PM", "+91 80 2558 7777", "https://bengalurutourism.in",
                        "https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=800&q=80",
                        true, "shopping,nightlife,pubs,metro,mg road,brigade road", "High street brands, vibrant nightlife and iconic boulevard walk"
                ),
                new Place(
                        "UB City Mall", "ಯುಬಿ ಸಿಟಿ ಮಾಲ್", "యుబి సిటీ మాల్", "यूबी सिटी मॉल",
                        "SHOPPING_MALLS", "Luxury Mall", 12.9716, 77.5960,
                        "24, Vittal Mallya Rd, KG Halli, D' Souza Layout, Bengaluru 560001", "Lavelle Road",
                        "India's pioneering luxury mall housing ultra-high-end fashion houses, fine-dining restaurants, and rooftop lounges.",
                        4.6, 45000, "11:00 AM - 11:00 PM", "+91 80 2264 5000", "https://ubcitybangalore.in",
                        "https://images.unsplash.com/photo-1567449303183-ae0d6ed1498e?w=800&q=80",
                        true, "luxury,shopping,fine dining,rooftop,brands", "Ultra-luxury international brands, amphitheatre & skyline dining"
                ),
                new Place(
                        "Orion Mall", "ಓರಿಯನ್ ಮಾಲ್", "ఓరియన్ మాల్", "ओरियन मॉल",
                        "SHOPPING_MALLS", "Shopping Complex", 13.0112, 77.5550,
                        "Brigade Gateway, 26/1 Dr Rajkumar Rd, Malleshwaram, Bengaluru 560055", "Malleshwaram",
                        "A sprawling mall overlooking an artificial lake with PVR IMAX, international retail outlets, and lakeside dining promenade.",
                        4.6, 54000, "10:00 AM - 10:00 PM", "+91 80 6728 0000", "https://orionmalls.com",
                        "https://images.unsplash.com/photo-1519567241046-7f570eee3ce6?w=800&q=80",
                        true, "mall,lake,imax,shopping,food court", "Lakeside promenade, PVR IMAX and luxury shopping"
                ),

                // 2. Iconic Restaurants & Cafes
                new Place(
                        "Vidyarthi Bhavan", "ವಿದ್ಯಾರ್ಥಿ ಭವನ", "విద్యార్థి భవన్", "विद्यार्थी भवन",
                        "RESTAURANTS", "South Indian Heritage", 12.9452, 77.5694,
                        "32, Gandhi Bazaar Main Rd, Basavanagudi, Bengaluru 560004", "Basavanagudi",
                        "Legendary vegetarian restaurant founded in 1943, world famous for its thick, crispy golden Masale Dose.",
                        4.5, 49000, "6:30 AM - 11:30 AM, 2:00 PM - 8:00 PM (Fridays Closed)", "+91 80 2667 7588", "http://vidyarthibhavan.in",
                        "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800&q=80",
                        true, "dosa,masala dosa,coffee,heritage,breakfast,gandhi bazaar", "Iconic crispy Masale Dose with potato palya & filter coffee"
                ),
                new Place(
                        "MTR (Mavalli Tiffin Room)", "ಎಂಟಿಆರ್ (ಮಾವಳ್ಳಿ ಟಿಫಿನ್ ರೂಮ್)", "ఎంటిఆర్ టిఫిన్ రూమ్", "एमटीआर टिफिन रूम",
                        "RESTAURANTS", "Traditional Karnataka Cuisine", 12.9554, 77.5855,
                        "14, Lalbagh Fort Rd, Doddamavalli, Sudhama Nagar, Bengaluru 560004", "Lalbagh",
                        "Established in 1924, pioneer of Rava Idli and authentic Karnataka Silver Thali meals.",
                        4.5, 36000, "6:30 AM - 11:00 AM, 12:30 PM - 8:30 PM (Mondays Closed)", "+91 80 2222 0022", "https://mavallitiffinroom.com",
                        "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&q=80",
                        true, "rava idli,meals,heritage,coffee,traditional", "Inventor of Rava Idli, authentic ghee roast dosa & filter coffee"
                ),
                new Place(
                        "CTR (Shri Sagar)", "ಸಿಟಿಆರ್ (ಶ್ರೀ ಸಾಗರ್)", "సిటిఆర్ (శ్రీ సాగర్)", "सीटीआर (श्री सागर)",
                        "CAFES", "Breakfast & Dosa", 12.9984, 77.5711,
                        "7th Cross Rd, Malleshwaram, Bengaluru 560003", "Malleshwaram",
                        "Centuries-old breakfast hub revered for its Benne Masala Dosa (butter dosa), crispy on the outside, fluffy inside.",
                        4.6, 32000, "7:00 AM - 12:30 PM, 4:00 PM - 9:00 PM", "+91 80 2331 7531", "https://bengalurucity.gov.in",
                        "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&q=80",
                        true, "benne dosa,malleshwaram,breakfast,coffee,butter dosa", "Legendary Benne Masale Dose & Mangalore Bajji"
                ),
                new Place(
                        "Nagarjuna Andhra Restaurant", "ನಾಗಾರ್ಜುನ ಆಂಧ್ರ ರೆಸ್ಟೋರೆಂಟ್", "నాగార్జున ఆంధ్రా రెస్టారెంట్", "नागार्जुन आंध्रा रेस्तरां",
                        "RESTAURANTS", "Andhra Meals & Biryani", 12.9728, 77.6067,
                        "44/1, Residency Rd, near Galaxy Theatre, Bengaluru 560025", "Residency Road",
                        "Beloved hotspot for authentic Andhra meals served on banana leaves with spicy chilli chicken and gunpowder ghee rice.",
                        4.4, 27000, "12:00 PM - 3:45 PM, 7:00 PM - 11:00 PM", "+91 80 2559 2266", "https://nagarjunarestaurants.com",
                        true, "andhra meals,biryani,spicy,chilli chicken,pappu", "Spicy Andhra banana leaf meals, Sholay Kebab & Biryani"
                ),
                new Place(
                        "Brahmin's Coffee Bar", "ಬ್ರಾಹ್ಮಣರ ಕಾಫಿ ಬಾರ್", "బ్రాహ్మిన్స్ కాఫీ బార్", "ब्राह्मण कॉफी बार",
                        "CAFES", "Traditional Filter Coffee", 12.9540, 77.5692,
                        "Ranga Rao Rd, near Shankar Math, Shankarpuram, Bengaluru 560004", "Shankarapuram",
                        "Iconic standing-only South Indian cafe renowned for super soft Idlis, crispy Vadas, and strong hot filter coffee.",
                        4.6, 29000, "6:00 AM - 12:00 PM, 3:00 PM - 7:00 PM (Sundays Closed)", "+91 98450 30214", "https://bengalurufood.in",
                        true, "filter coffee,idli,vada,kesari bath,standing cafe", "Steaming soft Idlis, coconut chutney, and piping filter coffee"
                ),
                new Place(
                        "Truffles", "ಟ್ರಫಲ್ಸ್", "ట్రఫుల్స్", "ट्रफल्स",
                        "CAFES", "Gourmet Burgers & Shakes", 12.9718, 77.6019,
                        "22, St Marks Rd, Shanthala Nagar, Ashok Nagar, Bengaluru 560001", "St. Marks Road",
                        "Youth favorite cafe known for mouth-watering loaded burgers, pasta, steaks, and decadent Ferrero Rocher shakes.",
                        4.5, 38000, "11:00 AM - 11:00 PM", "+91 80 4112 1100", "https://truffles.co.in",
                        false, "burgers,shakes,cafe,youth,pasta,dessert", "All-American Burgers, Peri-Peri Fries & Thick Milkshakes"
                ),

                // 3. Transit Hubs (Bus, Metro, Train)
                new Place(
                        "Kempegowda Bus Station (Majestic KBS)", "ಕೆಂಪೇಗೌಡ ಬಸ್ ನಿಲ್ದಾಣ (ಮೆಜೆಸ್ಟಿಕ್)", "కెంపేగౌడ బస్ స్టేషన్ (మెజెస్టిక్)", "केम्पेगौड़ा बस स्टेशन (मैजेस्टिक)",
                        "BUS_STOPS", "Central Bus Terminal", 12.9767, 77.5713,
                        "Tank Bund Rd, Gubbi Thotadappa Rd, Majestic, Bengaluru 560009", "Majestic",
                        "The heartbeat of Bengaluru public transit with intra-city BMTC buses, KSRTC inter-state buses and metro access.",
                        4.2, 75000, "Open 24/7", "1800-425-1663", "https://mybmtc.karnataka.gov.in",
                        "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?w=800&q=80",
                        true, "bus station,majestic,bmtc,ksrtc,transit hub,central", "Connecting all parts of Karnataka and intercity transit"
                ),
                new Place(
                        "KSR Bengaluru City Railway Station", "ಕ್ರಾಂತಿವೀರ ಸಂಗೊಳ್ಳಿ ರಾಯಣ್ಣ ರೈಲ್ವೆ ನಿಲ್ದಾಣ", "క్రాంతివీర సంగొల్లి రాయన్న రైల్వే స్టేషన్", "क्रांतिवीर संगोली रायन्ना रेलवे स्टेशन",
                        "RAILWAY_STATIONS", "Main Railway Junction", 12.9781, 77.5696,
                        "Kempegowda, Sevashrama, Bengaluru, Karnataka 560023", "Majestic",
                        "Main railway terminus of Bengaluru connecting major cities across India with direct skywalk to Metro and KBS.",
                        4.3, 62000, "Open 24/7", "139", "https://indianrail.gov.in",
                        "https://images.unsplash.com/photo-1515165562839-978bbcf18277?w=800&q=80",
                        true, "railway station,train,ksr,sangolli rayanna,majestic,express", "Major railway junction with 10 platforms and amenities"
                ),
                new Place(
                        "Nadaprabhu Kempegowda Metro Station (Majestic)", "ನಾಡಪ್ರಭು ಕೆಂಪೇಗೌಡ ಮೆಟ್ರೋ ನಿಲ್ದಾಣ (ಮೆಜೆಸ್ಟಿಕ್)", "నాడప్రభు కెంపేగౌడ మెట్రో స్టేషన్", "नादप्रभु केम्पेगौड़ा मेट्रो स्टेशन",
                        "METRO_STATIONS", "Metro Interchange", 12.9757, 77.5728,
                        "Subhash Nagar, Majestic, Bengaluru, Karnataka 560009", "Majestic",
                        "Central underground interchange station connecting Purple Line (East-West) and Green Line (North-South).",
                        4.7, 51000, "5:00 AM - 11:30 PM", "+91 80 2519 1234", "https://bmrc.co.in",
                        "https://images.unsplash.com/photo-1558441719-7d12f171221b?w=800&q=80",
                        true, "metro,namma metro,interchange,purple line,green line", "Massive 2-level interchange for Purple and Green metro lines"
                ),
                new Place(
                        "Cubbon Park Metro Station", "ಕಬ್ಬನ್ ಪಾರ್ಕ್ ಮೆಟ್ರೋ ನಿಲ್ದಾಣ", "కబ్బన్ పార్క్ మెట్రో స్టేషన్", "कब्बन पार्क मेट्रो स्टेशन",
                        "METRO_STATIONS", "Purple Line Metro", 12.9793, 77.5996,
                        "Kasturba Rd, Shivaji Nagar, Bengaluru 560001", "Shivajinagar",
                        "Underground station on Purple Line with direct access to High Court, Vidhana Soudha, and Cubbon Park.",
                        4.6, 18000, "5:00 AM - 11:30 PM", "+91 80 2519 1234", "https://bmrc.co.in",
                        false, "metro,purple line,cubbon park,court,high court", "Underground station beside Cubbon Park and High Court"
                ),
                new Place(
                        "MG Road Metro Station", "ಎಂ.ಜಿ ರಸ್ತೆ ಮೆಟ್ರೋ ನಿಲ್ದಾಣ", "ఎం.జి రోడ్ మెట్రో స్టేషన్", "एमजी रोड मेट्रो स्टेशन",
                        "METRO_STATIONS", "Purple Line Metro", 12.9756, 77.6068,
                        "Mahatma Gandhi Rd, Bengaluru 560001", "MG Road",
                        "Elevated station on Purple Line opening directly onto Boulevard walkway and Brigade Road.",
                        4.6, 29000, "5:00 AM - 11:30 PM", "+91 80 2519 1234", "https://bmrc.co.in",
                        false, "metro,purple line,mg road,brigade road,elevated", "Direct pedestrian access to MG Road Boulevard and Brigade Road"
                ),

                // 4. Hospitals & Emergency
                new Place(
                        "Victoria Hospital", "ವಿಕ್ಟೋರಿಯಾ ಆಸ್ಪತ್ರೆ", "విక్టోరియా ఆసుపత్రి", "विक्टोरिया अस्पताल",
                        "HOSPITALS", "Government Medical College Hospital", 12.9620, 77.5744,
                        "Fort Rd, near City Market, Bengaluru 560002", "KR Market",
                        "One of the oldest and largest tertiary government teaching hospitals in India with 24/7 trauma care.",
                        4.0, 12000, "Emergency 24/7", "+91 80 2670 1150", "https://bmc-bangalore.kar.nic.in",
                        false, "hospital,emergency,trauma,government,kr market", "24/7 Emergency trauma care, ICU and medical facilities"
                ),
                new Place(
                        "Bowring and Lady Curzon Hospital", "ಬೌರಿಂಗ್ ಮತ್ತು ಲೇಡಿ ಕರ್ಜನ್ ಆಸ್ಪತ್ರೆ", "బౌరింగ్ మరియు లేడీ కర్జాన్ ఆసుపత్రి", "बॉवरिंग और लेडी कर्जन अस्पताल",
                        "HOSPITALS", "Multi-Speciality Hospital", 12.9837, 77.6033,
                        "Lady Curzon Rd, Shivaji Nagar, Bengaluru 560001", "Shivajinagar",
                        "Major government teaching hospital with comprehensive medical departments and round-the-clock casualty.",
                        4.1, 8500, "Emergency 24/7", "+91 80 2559 1325", "https://bengalurucity.gov.in",
                        false, "hospital,medical,shivajinagar,emergency,chemist", "24/7 Casualty & emergency services in central Bengaluru"
                ),
                new Place(
                        "Apollo Pharmacy Majestic", "ಅಪೊಲೊ ಫಾರ್ಮಸಿ ಮೆಜೆಸ್ಟಿಕ್", "అపోలో ఫార్మసీ మెజెస్టిక్", "अपोलो फार्मेसी मैजेस्टिक",
                        "PHARMACIES", "24x7 Chemist", 12.9772, 77.5721,
                        "Opposite Majestic Bus Stand, KG Road, Bengaluru 560009", "Majestic",
                        "24/7 pharmacy providing all emergency prescription medicines, first-aid, healthcare supplies and baby care.",
                        4.5, 4200, "Open 24/7", "+91 80 2235 4411", "https://apollopharmacy.in",
                        false, "pharmacy,chemist,medicines,24/7,majestic", "24-hour availability of genuine medicines and first-aid kits"
                ),
                new Place(
                        "MedPlus Pharmacy Gandhinagar", "ಮೆಡ್‌ಪ್ಲಸ್ ಫಾರ್ಮಸಿ ಗಾಂಧಿನಗರ", "మెడ్‌ప్లస్ ఫార్మసీ గాంధీనగర్", "मेडप्लस फार्मेसी गांधीनगर",
                        "PHARMACIES", "Chemist & Health", 12.9785, 77.5772,
                        "5th Main, Gandhinagar, near Majestic, Bengaluru 560009", "Gandhinagar",
                        "Well-stocked pharmacy offering generic & branded medicines, surgical accessories, and health supplements.",
                        4.4, 2100, "8:00 AM - 11:00 PM", "+91 80 2220 5544", "https://medplusmart.com",
                        false, "pharmacy,chemist,gandhinagar,majestic,health", "Wide range of healthcare products with discount on medicines"
                ),

                // 5. Markets, Banks & ATMs
                new Place(
                        "KR Market (City Market)", "ಕೃಷ್ಣ ರಾಜೇಂದ್ರ ಮಾರುಕಟ್ಟೆ", "కృష్ణ రాజేంద్ర మార్కెట్", "केआर मार्केट (सिटी मार्केट)",
                        "MARKETS", "Wholesale & Flower Market", 12.9647, 77.5765,
                        "Near Mysore Road Flyover, KR Market, Bengaluru 560002", "KR Market",
                        "One of Asia's largest and most sensory flower markets, bustling with exotic blooms, fresh spices, and fruits at dawn.",
                        4.3, 21000, "4:00 AM - 9:00 PM", "+91 80 2222 1111", "https://bengalurucity.gov.in",
                        "https://images.unsplash.com/photo-1513836279014-a89f7a76ae86?w=800&q=80",
                        true, "flowers,market,wholesale,spices,morning,kr market", "Spectacular dawn flower market, vibrant colors and wholesale goods"
                ),
                new Place(
                        "Gandhi Bazaar", "ಗಾಂಧಿ ಬಜಾರ್", "గాంధీ బజార్", "गांधी बाज़ार",
                        "MARKETS", "Traditional Street Bazaar", 12.9464, 77.5699,
                        "Main Road, Basavanagudi, Bengaluru 560004", "Basavanagudi",
                        "Traditional South Bengaluru street bazaar lined with flower vendors, temple goods, silk shops, and heritage eateries.",
                        4.5, 19000, "9:00 AM - 9:30 PM", "+91 80 2667 0000", "https://bengalurucity.gov.in",
                        false, "market,bazaar,flowers,fruits,traditional,basavanagudi", "Traditional shopping experience with fragrant jasmine and street food"
                ),
                new Place(
                        "SBI Main Branch Majestic", "ಎಸ್.ಬಿ.ಐ ಮುಖ್ಯ ಶಾಖೆ", "ఎస్‌బిఐ మెయిన్ బ్రాంచ్", "एसबीआई मुख्य शाखा मैजेस्टिक",
                        "BANKS", "Nationalized Bank", 12.9761, 77.5732,
                        "KG Road, Majestic, Bengaluru 560009", "Majestic",
                        "Full service State Bank of India branch with foreign exchange, traveler assistance, locker and digital banking.",
                        4.1, 3800, "10:00 AM - 4:00 PM (2nd & 4th Sat closed)", "1800-11-2211", "https://sbi.co.in",
                        false, "bank,sbi,majestic,forex,cash,services", "Foreign exchange, passbook update & customer banking services"
                ),
                new Place(
                        "HDFC Bank 24/7 ATM Majestic", "ಎಚ್‌ಡಿಎಫ್‌ಸಿ ಬ್ಯಾಂಕ್ ಎಟಿಎಂ", "హెచ్‌డిఎఫ్‌సి బ్యాంక్ ఏటీఎం", "एचडीएफसी बैंक एटीएम",
                        "ATMS", "24/7 Cash ATM", 12.9755, 77.5781,
                        "Gubbi Thotadappa Road, Majestic, Bengaluru 560009", "Majestic",
                        "24/7 operational ATM and Cash Deposit Machine supporting domestic & international cards (Visa, MasterCard, Rupay).",
                        4.4, 1500, "Open 24/7", "1800-202-6161", "https://hdfcbank.com",
                        false, "atm,cash,hdfc,majestic,24/7,deposit", "Reliable 24-hour cash withdrawal and PIN services"
                ),
                new Place(
                        "Canara Bank ATM Majestic Station", "ಕೆನರಾ ಬ್ಯಾಂಕ್ ಎಟಿಎಂ", "కెనరా బ్యాంక్ ఏటీఎం", "केनरा बैंक एटीएम",
                        "ATMS", "24/7 Cash ATM", 12.9769, 77.5710,
                        "Platform Entry Concourse, Majestic Bus Station, Bengaluru 560009", "Majestic",
                        "Quick cash withdrawal counter inside Majestic bus station concourse.",
                        4.3, 980, "Open 24/7", "1800-425-0018", "https://canarabank.com",
                        false, "atm,canara bank,bus station,majestic,cash", "Convenient cash counter for passengers at bus terminal"
                ),

                // 6. Hotels & Stays
                new Place(
                        "The Lalit Ashok Bengaluru", "ದಿ ಲಲಿತ್ ಅಶೋಕ್", "ది లలిత్ అశోక్", "द ललित अशोक",
                        "HOTELS", "5-Star Luxury Hotel", 12.9934, 77.5819,
                        "Kumara Krupa High Grounds, Bengaluru 560001", "High Grounds",
                        "Luxury 5-star hotel set amidst 10 acres of serene greenery overlooking Bangalore Golf Club.",
                        4.5, 16000, "Open 24/7", "+91 80 6817 7777", "https://thelalit.com",
                        "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&q=80",
                        true, "hotel,luxury,stay,resort,5-star,golf", "Sprawling landscaped gardens, luxurious suites and fine dining"
                ),
                new Place(
                        "Hotel Grand Pavilion Majestic", "ಹೋಟೆಲ್ ಗ್ರ್ಯಾಂಡ್ ಪೆವಿಲಿಯನ್", "హోటల్ గ్రాండ్ పెవిలియన్", "होटल ग्रैंड पैवेलियन",
                        "HOTELS", "Budget & Business Stay", 12.9788, 77.5752,
                        "Gandhinagar, near Majestic Bus Stand, Bengaluru 560009", "Gandhinagar",
                        "Convenient hotel for travelers arriving at Majestic bus station and City railway station with clean rooms & travel desk.",
                        4.2, 5400, "Open 24/7", "+91 80 4124 8899", "https://bengalurucity.gov.in",
                        false, "hotel,budget,majestic,travelers,rooms,stay", "Clean budget rooms and 2-minute walking distance from Majestic hub"
                ),

                // 7. Movie Theatres & Entertainment
                new Place(
                        "Urvashi Cinema", "ಉರ್ವಶಿ ಚಿತ್ರಮಂದಿರ", "ఊర్వశి సినిమా", "उर्वशी सिनेमा",
                        "MOVIE_THEATRES", "Single Screen 4K Cinema", 12.9598, 77.5879,
                        "58, Siddaiah Rd, Sudhama Nagar, Bengaluru 560027", "Lalbagh Road",
                        "Iconic single-screen theatre famous for cutting-edge 4K laser projection, Dolby Atmos sound and electric fan atmosphere.",
                        4.6, 21000, "10:30 AM - 11:30 PM", "+91 80 2222 3840", "https://in.bookmyshow.com",
                        false, "cinema,theatre,movie,dolby atmos,4k,urvashi", "Massive screen, thunderous Dolby Atmos and premier movie releases"
                ),
                new Place(
                        "PVR INOX Orion Mall", "ಪಿವಿಆರ್ ಐನಾಕ್ಸ್ ಓರಿಯನ್ ಮಾಲ್", "పివిఆర్ ఐనాక్స్ ఓరియన్ మాల్", "पीवीआर आईनॉक्स ओरियन मॉल",
                        "MOVIE_THEATRES", "Multiplex & IMAX", 13.0114, 77.5552,
                        "3rd Floor, Orion Mall, Dr Rajkumar Rd, Rajajinagar, Bengaluru 560055", "Malleshwaram",
                        "11-screen multiplex featuring IMAX, 4DX, and Gold Class dining screens.",
                        4.6, 31000, "9:00 AM - 12:30 AM", "+91 88009 00009", "https://pvrcinemas.com",
                        false, "cinema,imax,pvr,multiplex,movies,4dx", "State of the art IMAX screen, recliners and gourmet concessions"
                ),

                // 8. Day Trips & Extended Attractions
                new Place(
                        "Bannerghatta Biological Park", "ಬನ್ನೇರುಘಟ್ಟ ಜೈವಿಕ ಉದ್ಯಾನವನ", "బన్నేరుఘట్ట నేషనల్ పార్క్", "बन्नेरघट्टा जैविक उद्यान",
                        "TOURIST_ATTRACTIONS", "Safari & Zoo", 12.8009, 77.5777,
                        "Bannerghatta Biological Park, Bannerghatta Rd, Bengaluru 560083", "Bannerghatta",
                        "Vast national park featuring Tiger & Lion safaris, Grand Butterfly Park, zoo, and rescue sanctuary.",
                        4.3, 58000, "9:30 AM - 5:00 PM (Tuesdays Closed)", "+91 80 2977 6466", "https://bannerghattabiologicalpark.org",
                        "https://images.unsplash.com/photo-1534567153574-2b12153a87f0?w=800&q=80",
                        true, "safari,zoo,animals,lions,tigers,nature,butterfly park", "Exciting Tiger safari, India's first Butterfly Park & zoo"
                ),
                new Place(
                        "Nandi Hills", "ನಂದಿ ಬೆಟ್ಟ", "నంది హిల్స్", "नंदी हिल्स",
                        "TOURIST_ATTRACTIONS", "Hill Station & Sunrise", 13.3702, 77.6835,
                        "Chikkaballapur District, near Bengaluru, Karnataka 562101", "Outskirts",
                        "Ancient hill fortress famous for mesmerizing sea-of-clouds sunrises, Tipu's Drop and Bhoga Nandeeshwara Temple.",
                        4.5, 94000, "6:00 AM - 6:00 PM", "+91 80 2235 2828", "https://karnatakatourism.org",
                        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&q=80",
                        true, "sunrise,hills,clouds,viewpoint,trekking,nandi hills", "Breathtaking sunrise viewpoints over blanket of clouds"
                ),
                new Place(
                        "Wonderla Amusement Park", "ವಂಡರ್‌ಲಾ ಮನೋರಂಜನಾ ಪಾರ್ಕ್", "వండర్‌లా ఎమ్యూజ్‌మెంట్ పార్క్", "वंडरलॉ अम्यूजमेंट पार्क",
                        "TOURIST_ATTRACTIONS", "Water & Thrill Park", 12.8344, 77.4010,
                        "28th km, Mysore Road, Bengaluru 562109", "Mysore Road",
                        "India's top rated theme park with over 60 thrilling high-adrenaline land rides and heated wave pools.",
                        4.6, 68000, "11:00 AM - 6:00 PM", "+91 80 3723 0300", "https://wonderla.com",
                        "https://images.unsplash.com/photo-1513889961551-628c1e5e2ee9?w=800&q=80",
                        true, "wonderla,amusement,rides,water park,roller coaster", "Over 60 world-class thrill rides, wave pools and family fun"
                )
        ));
    }

    private void initMetroStations() {
        // Namma Metro Stations (Purple & Green Lines)
        metroStationRepository.saveAll(List.of(
                // Purple Line (Select Core Stations)
                new MetroStation("Kengeri", "ಕೆಂಗೇರಿ", "PURPLE", 12.9090, 77.4850, false, null, 1),
                new MetroStation("Mysuru Road", "ಮೈಸೂರು ರಸ್ತೆ", "PURPLE", 12.9463, 77.5300, false, null, 2),
                new MetroStation("Vijayanagar", "ವಿಜಯನಗರ", "PURPLE", 12.9696, 77.5375, false, null, 3),
                new MetroStation("City Railway Station", "ಕ್ರಾಂತಿವೀರ ಸಂಗೊಳ್ಳಿ ರಾಯಣ್ಣ ನಿಲ್ದಾಣ", "PURPLE", 12.9778, 77.5690, false, null, 4),
                new MetroStation("Nadaprabhu Kempegowda Majestic", "ನಾಡಪ್ರಭು ಕೆಂಪೇಗೌಡ ಮೆಜೆಸ್ಟಿಕ್", "PURPLE", 12.9757, 77.5728, true, "PURPLE,GREEN", 5),
                new MetroStation("Sir M. Visveshwaraya Central College", "ಸರ್ ಎಂ. ವಿಶ್ವೇಶ್ವರಯ್ಯ ಸೆಂಟ್ರಲ್ ಕಾಲೇಜು", "PURPLE", 12.9740, 77.5855, false, null, 6),
                new MetroStation("Dr. B.R. Ambedkar Vidhana Soudha", "ಡಾ. ಬಿ.ಆರ್. ಅಂಬೇಡ್ಕರ್ ವಿಧಾನ ಸೌಧ", "PURPLE", 12.9797, 77.5908, false, null, 7),
                new MetroStation("Cubbon Park", "ಕಬ್ಬನ್ ಪಾರ್ಕ್", "PURPLE", 12.9793, 77.5996, false, null, 8),
                new MetroStation("Mahatma Gandhi Road", "ಮಹಾತ್ಮ ಗಾಂಧಿ ರಸ್ತೆ", "PURPLE", 12.9756, 77.6068, false, null, 9),
                new MetroStation("Trinity", "ಟ್ರಿನಿಟಿ", "PURPLE", 12.9729, 77.6169, false, null, 10),
                new MetroStation("Indiranagar", "ಇಂದಿರಾನಗರ", "PURPLE", 12.9784, 77.6385, false, null, 11),
                new MetroStation("Swami Vivekananda Road", "ಸ್ವಾಮಿ ವಿವೇಕಾನಂದ ರಸ್ತೆ", "PURPLE", 12.9859, 77.6449, false, null, 12),
                new MetroStation("Baiyappanahalli", "ಬೈಯಪ್ಪನಹಳ್ಳಿ", "PURPLE", 12.9908, 77.6525, false, null, 13),
                new MetroStation("KR Puram", "ಕೆ.ಆರ್. ಪುರಂ", "PURPLE", 12.9996, 77.6775, false, null, 14),
                new MetroStation("Whitefield (Kadugodi)", "ವೈಟ್‌ಫೀಲ್ಡ್ (ಕಾಡುಗೋಡಿ)", "PURPLE", 12.9959, 77.7610, false, null, 15),

                // Green Line (Select Core Stations)
                new MetroStation("Nagasandra", "ನಾಗಸಂದ್ರ", "GREEN", 13.0478, 77.5002, false, null, 1),
                new MetroStation("Yeshwanthpur", "ಯಶವಂತಪುರ", "GREEN", 13.0232, 77.5501, false, null, 2),
                new MetroStation("Sandal Soap Factory", "ಸ್ಯಾಂಡಲ್ ಸೋಪ್ ಫ್ಯಾಕ್ಟರಿ", "GREEN", 13.0146, 77.5539, false, null, 3),
                new MetroStation("Mahalakshmi (near ISKCON)", "ಮಹಾಲಕ್ಷ್ಮಿ", "GREEN", 13.0078, 77.5501, false, null, 4),
                new MetroStation("Rajajinagar", "ರಾಜಾಜಿನಗರ", "GREEN", 12.9989, 77.5558, false, null, 5),
                new MetroStation("Krantivira Malleshwaram", "ಸಂಪಿಗೆ ರಸ್ತೆ ಮಲ್ಲೇಶ್ವರಂ", "GREEN", 12.9912, 77.5710, false, null, 6),
                new MetroStation("Nadaprabhu Kempegowda Majestic (Green)", "ನಾಡಪ್ರಭು ಕೆಂಪೇಗೌಡ ಮೆಜೆಸ್ಟಿಕ್", "GREEN", 12.9757, 77.5728, true, "PURPLE,GREEN", 7),
                new MetroStation("Chickpete", "ಚಿಕ್ಕಪೇಟೆ", "GREEN", 12.9667, 77.5750, false, null, 8),
                new MetroStation("Krishna Rajendra Market", "ಕೃಷ್ಣ ರಾಜೇಂದ್ರ ಮಾರುಕಟ್ಟೆ", "GREEN", 12.9605, 77.5744, false, null, 9),
                new MetroStation("National College Basavanagudi", "ನ್ಯಾಷನಲ್ ಕಾಲೇಜು ಬಸವನಗುಡಿ", "GREEN", 12.9502, 77.5725, false, null, 10),
                new MetroStation("Lalbagh (West Gate)", "ಲಾಲ್‌ಬಾಗ್ ಪಶ್ಚಿಮ ಗೇಟ್", "GREEN", 12.9460, 77.5800, false, null, 11),
                new MetroStation("South End Circle", "ಸೌತ್ ಎಂಡ್ ಸರ್ಕಲ್", "GREEN", 12.9380, 77.5801, false, null, 12),
                new MetroStation("Jayanagar", "ಜಯನಗರ", "GREEN", 12.9298, 77.5828, false, null, 13),
                new MetroStation("Banashankari", "ಬನಶಂಕರಿ", "GREEN", 12.9155, 77.5736, false, null, 14),
                new MetroStation("Silk Institute", "ರೇಷ್ಮೆ ಸಂಸ್ಥೆ", "GREEN", 12.8624, 77.5255, false, null, 15)
        ));
    }

    private void initBmtcRoutes() {
        bmtcRouteRepository.saveAll(List.of(
                new BmtcRoute("G-4", "Majestic to Brigade Road / MG Road", "Majestic (KBS)", "Brigade Road", "Central College, Vidhana Soudha, Cubbon Park, MG Road", 10, 15.0, 25.0),
                new BmtcRoute("252", "Majestic to Bangalore Palace & Vasanth Nagar", "Majestic (KBS)", "Vasanth Nagar (Palace Gate)", "Anand Rao Circle, Shivananda Circle, High Grounds, Palace Guttahalli", 15, 15.0, 25.0),
                new BmtcRoute("335E", "Majestic to Whitefield & ITPL", "Majestic (KBS)", "Kadugodi / ITPL", "Corporation, Domlur, HAL Airport, Marathahalli, Kundalahalli, ITPL", 10, 20.0, 45.0),
                new BmtcRoute("365", "Majestic to Bannerghatta National Park", "Majestic (KBS)", "Bannerghatta Safari Park", "Town Hall, Lalbagh, Dairy Circle, Jayadeva, Arekere, Bannerghatta", 15, 20.0, 40.0),
                new BmtcRoute("250", "Majestic to ISKCON Temple & Rajajinagar", "Majestic (KBS)", "Rajajinagar 1st Block (ISKCON)", "Okalipuram, Malleshwaram 8th Cross, Yeshwantpur, Mahalakshmi Layout", 10, 15.0, 25.0),
                new BmtcRoute("KIA-9", "Majestic to Kempegowda International Airport (Vayu Vajra)", "Majestic (KBS)", "Bengaluru Airport (BLR)", "Hebbal, Yelahanka, Trumpet Flyover, Airport Terminal", 20, 240.0, 270.0),
                new BmtcRoute("201", "Banashankari to Domlur via Koramangala", "Banashankari", "Domlur", "Jayanagar, BTM Layout, Koramangala Sony World, Domlur", 12, 15.0, 30.0),
                new BmtcRoute("500D", "Hebbal to Silk Board (Outer Ring Road)", "Hebbal", "Central Silk Board", "Manyata Tech Park, Tin Factory, Marathahalli, Bellandur, HSR Layout, Silk Board", 8, 20.0, 40.0)
        ));
    }
}
