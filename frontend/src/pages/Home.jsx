import React, { useState, useEffect } from 'react';
import SportsNav from '../components/SportsNav';
import TournamentList from '../components/TournamentList';
import MatchDetails from '../components/MatchDetails';
import { SPORTS_DATA, TOURNAMENTS_DATA, MATCHES_DATA } from '../data/mockData';
import './Home.css';

export default function Home() {
    // State for selected sport and match
    const [selectedSportId, setSelectedSportId] = useState(null);
    const [selectedMatchId, setSelectedMatchId] = useState(null);

    // Set the first sport as default on initial load
    useEffect(() => {
        if (SPORTS_DATA.length > 0) {
            setSelectedSportId(SPORTS_DATA[0].id);
        }
    }, []);

    // Handler for selecting a new sport
    const handleSelectSport = (sportId) => {
        setSelectedSportId(sportId);
        setSelectedMatchId(null); // Reset match selection when sport changes
    };

    // Derived state: filter data based on selections
    const visibleTournaments = TOURNAMENTS_DATA.filter(t => t.sportId === selectedSportId);
    const selectedMatch = MATCHES_DATA.find(m => m.id === selectedMatchId);
    const selectedMatchTournament = selectedMatch ? TOURNAMENTS_DATA.find(t => t.id === selectedMatch.tournamentId) : null;

    return (
        <div className="home-page-layout">
            <SportsNav
                sports={SPORTS_DATA}
                selectedSportId={selectedSportId}
                onSelectSport={handleSelectSport}
            />

            <main className="main-content-area">
                <div className="content-grid">

                    {/* Center Column */}
                    <div className="center-column">
                        {selectedSportId ? (
                            <TournamentList
                                tournaments={visibleTournaments}
                                matches={MATCHES_DATA}
                                selectedMatchId={selectedMatchId}
                                onSelectMatch={setSelectedMatchId}
                            />
                        ) : (
                            <div className="select-sport-message">
                                <p>Select a sport to see today's matches.</p>
                            </div>
                        )}
                    </div>

                    {/* Right Column */}
                    <aside className="right-column-sidebar">
                        <MatchDetails
                            match={selectedMatch}
                            tournament={selectedMatchTournament}
                        />
                    </aside>
                </div>
            </main>
        </div>
    );
}