import React from 'react';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';
import { Home, Compass, Map, Heart, User } from 'lucide-react';

export type TabType = 'home' | 'explore' | 'map' | 'saved' | 'profile';

interface Props {
  activeTab: TabType;
  onSelectTab: (tab: TabType) => void;
}

export const BottomNavigation: React.FC<Props> = ({ activeTab, onSelectTab }) => {
  const { language, savedPlaces } = useLocation();
  const t = translations[language];

  return (
    <nav className="bottom-nav">
      <button
        className={`nav-item ${activeTab === 'home' ? 'active' : ''}`}
        onClick={() => onSelectTab('home')}
      >
        <Home size={20} />
        <span>{t.home}</span>
      </button>

      <button
        className={`nav-item ${activeTab === 'explore' ? 'active' : ''}`}
        onClick={() => onSelectTab('explore')}
      >
        <Compass size={20} />
        <span>{t.explore}</span>
      </button>

      <button
        className={`nav-item ${activeTab === 'map' ? 'active' : ''}`}
        onClick={() => onSelectTab('map')}
      >
        <Map size={20} />
        <span>{t.map}</span>
      </button>

      <button
        className={`nav-item ${activeTab === 'saved' ? 'active' : ''}`}
        onClick={() => onSelectTab('saved')}
        style={{ position: 'relative' }}
      >
        <Heart size={20} />
        <span>{t.bookmarks}</span>
        {savedPlaces.length > 0 && (
          <span style={{
            position: 'absolute',
            top: '4px',
            right: '16px',
            width: '16px',
            height: '16px',
            background: '#ef4444',
            color: '#ffffff',
            borderRadius: '50%',
            fontSize: '9px',
            fontWeight: '800',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}>
            {savedPlaces.length}
          </span>
        )}
      </button>

      <button
        className={`nav-item ${activeTab === 'profile' ? 'active' : ''}`}
        onClick={() => onSelectTab('profile')}
      >
        <User size={20} />
        <span>{t.profile}</span>
      </button>
    </nav>
  );
};
