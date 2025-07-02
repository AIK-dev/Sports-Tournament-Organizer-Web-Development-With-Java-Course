import React from 'react';
import { TODAY_STRING } from '../data/mockData';
import './TournamentList.css'; 

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
            <div className="no-matches-message">
                <p>No matches scheduled for today in this sport.</p>
            </div>
        );
    }

    return (
        <div className="tournament-list-section">
            {tournamentsWithTodaysMatches.map(tournament => (
                <div key={tournament.id} className="tournament-card">
                    <h2 className="tournament-title">{tournament.name}</h2>
                    <div className="match-list">
                        {tournament.todaysMatches.map(match => {
                            const matchTime = new Date(match.matchDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                            const isActive = match.id === selectedMatchId;
                            return (
                                <div
                                    key={match.id}
                                    onClick={() => onSelectMatch(match.id)}
                                    className={`match-item ${isActive ? 'active' : ''}`}
                                >
                                    <div className="match-info">
                                        <span className="match-time">{matchTime}</span>
                                        <span className="competitor-name">{match.firstCompetitor}</span>
                                        <span className="vs-separator">vs</span>
                                        <span className="competitor-name">{match.secondCompetitor}</span>
                                    </div>
                                    <span className="match-status">{match.status.toUpperCase()}</span>
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