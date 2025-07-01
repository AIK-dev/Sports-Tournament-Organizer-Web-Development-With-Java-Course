import React, { useState, useEffect } from 'react';
import SportsNav from '../components/SportsNav';
import TournamentList from '../components/TournamentList';
import MatchDetails from '../components/MatchDetails';
import { SPORTS_DATA, TOURNAMENTS_DATA, MATCHES_DATA } from '../data/mockData';

export default function Home() {
    const [selectedSportId, setSelectedSportId] = useState(null);
    const [selectedMatchId, setSelectedMatchId] = useState(null);

    useEffect(() => {
        if (SPORTS_DATA.length > 0) {
            setSelectedSportId(SPORTS_DATA[0].id);
        }
    }, []);

    // Handler for selecting a new sport
    const handleSelectSport = (sportId) => {
        setSelectedSportId(sportId);
        setSelectedMatchId(null);
    };

    // Derived state: filter data based on selections
    const visibleTournaments = TOURNAMENTS_DATA.filter(t => t.sportId === selectedSportId);
    const selectedMatch = MATCHES_DATA.find(m => m.id === selectedMatchId);
    const selectedMatchTournament = selectedMatch ? TOURNAMENTS_DATA.find(t => t.id === selectedMatch.tournamentId) : null;

    return (
        <div className="bg-gray-900 text-white font-sans min-h-screen flex flex-col">
            <SportsNav
                sports={SPORTS_DATA}
                selectedSportId={selectedSportId}
                onSelectSport={handleSelectSport}
            />

            <main className="container mx-auto p-4 sm:p-6 lg:p-8 flex-grow">
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Center Column */}
                    <div className="lg:col-span-2">
                        {selectedSportId ? (
                            <TournamentList
                                tournaments={visibleTournaments}
                                matches={MATCHES_DATA}
                                selectedMatchId={selectedMatchId}
                                onSelectMatch={setSelectedMatchId}
                            />
                        ) : (
                            <div className="text-center py-20">
                                <p className="text-gray-400">Select a sport to see today's matches.</p>
                            </div>
                        )}
                    </div>

                    {/* Right Column */}
                    <aside className="lg:col-span-1 lg:sticky top-24 self-start">
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