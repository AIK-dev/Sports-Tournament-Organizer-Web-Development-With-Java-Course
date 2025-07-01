import React from 'react';
import { TODAY_STRING } from '../data/mockData'; // Import TODAY_STRING

const TournamentList = ({ tournaments, matches, selectedMatchId, onSelectMatch }) => {
    const tournamentsWithTodaysMatches = tournaments
        .map(tournament => ({
            ...tournament,
            todaysMatches: matches.filter(m =>
                m.tournamentId === tournament.id && m.matchDate.startsWith(TODAY_STRING)
            ),
        }))
        .filter(tournament => tournament.todaysMatches.length > 0);

    if (tournamentsWithTodaysMatches.length === 0) {
        return (
            <div className="text-center py-20">
                <p className="text-gray-400">No matches scheduled for today in this sport.</p>
            </div>
        );
    }

    return (
        <div className="space-y-8">
            {tournamentsWithTodaysMatches.map(tournament => (
                <div key={tournament.id} className="bg-gray-800 rounded-lg shadow-lg overflow-hidden">
                    <h2 className="text-xl font-bold p-4 bg-gray-900">{tournament.name}</h2>
                    <div className="divide-y divide-gray-700">
                        {tournament.todaysMatches.map(match => {
                            const matchTime = new Date(match.matchDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                            const isActive = match.id === selectedMatchId;
                            return (
                                <div
                                    key={match.id}
                                    onClick={() => onSelectMatch(match.id)}
                                    className={`flex justify-between items-center p-4 cursor-pointer transition-colors duration-200 ${
                                        isActive ? 'bg-indigo-600' : 'hover:bg-gray-700'
                                    }`}
                                >
                                    <div className="flex items-center space-x-4">
                                        <span className="font-mono text-sm text-gray-400">{matchTime}</span>
                                        <span className="font-semibold">{match.firstCompetitor}</span>
                                        <span className="text-gray-500">vs</span>
                                        <span className="font-semibold">{match.secondCompetitor}</span>
                                    </div>
                                    <span className="text-xs font-bold text-indigo-400">{match.status.toUpperCase()}</span>
                                </div>
                            );
                        })}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default TournamentList;
