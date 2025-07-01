import React from 'react';

const SportsNav = ({ sports, selectedSportId, onSelectSport }) => {
    return (
        <header className="bg-gray-800 shadow-md sticky top-0 z-20">
            <nav className="container mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center space-x-2 sm:space-x-4 py-3 overflow-x-auto">
                    {sports.map(sport => (
                        <button
                            key={sport.id}
                            onClick={() => onSelectSport(sport.id)}
                            className={`px-4 py-2 text-sm sm:text-base font-semibold rounded-md transition-colors duration-200 whitespace-nowrap ${
                                selectedSportId === sport.id
                                    ? 'bg-indigo-600 text-white'
                                    : 'bg-gray-700 hover:bg-gray-600'
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
