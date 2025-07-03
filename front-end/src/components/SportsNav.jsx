import React from 'react';
import './SportsNav.css';

const SportsNav = ({ sports, selectedSportId, onSelectSport }) => {
    return (
        <header className="sports-nav-header">
            <nav className="container"> {}
                <div className="sports-nav-wrapper">
                    {sports.map(sport => (
                        <button
                            key={sport.id}
                            onClick={() => onSelectSport(sport.id)}
                            className={`sports-nav-button ${
                                selectedSportId === sport.id ? 'active' : ''
                            }`}
                        >
                            {sport.name}
                        </button>
                    ))}
                </div>
            </nav>
        </header>
    );
};

export default SportsNav;