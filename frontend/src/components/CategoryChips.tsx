import React from 'react';
import { Category } from '../types';
import { useLocation } from '../context/LocationContext';
import { translations } from '../i18n/translations';

interface Props {
  categories: Category[];
  selectedCategory: string;
  onSelectCategory: (categoryKey: string) => void;
}

export const CategoryChips: React.FC<Props> = ({
  categories,
  selectedCategory,
  onSelectCategory
}) => {
  const { language } = useLocation();
  const t = translations[language];

  const getLocalizedName = (cat: Category) => {
    if (language === 'kn' && cat.kannadaName) return cat.kannadaName;
    if (language === 'te' && cat.teluguName) return cat.teluguName;
    if (language === 'hi' && cat.hindiName) return cat.hindiName;
    return cat.name;
  };

  return (
    <div className="category-scroll-container">
      {/* "All" Category Pill */}
      <button
        className={`category-chip ${selectedCategory === 'ALL' ? 'active' : ''}`}
        onClick={() => onSelectCategory('ALL')}
      >
        <span>🌟</span>
        <span>{t.allCategories}</span>
      </button>

      {/* Dynamic Category Pills */}
      {categories.map((cat) => {
        const isSelected = selectedCategory === cat.key;
        return (
          <button
            key={cat.id || cat.key}
            className={`category-chip ${isSelected ? 'active' : ''}`}
            onClick={() => onSelectCategory(cat.key)}
          >
            <span>{cat.icon}</span>
            <span>{getLocalizedName(cat)}</span>
            {cat.placeCount > 0 && (
              <span style={{
                fontSize: '10px',
                background: isSelected ? 'rgba(255,255,255,0.25)' : 'rgba(255,255,255,0.08)',
                padding: '1px 6px',
                borderRadius: '10px',
                marginLeft: '2px'
              }}>
                {cat.placeCount}
              </span>
            )}
          </button>
        );
      })}
    </div>
  );
};
