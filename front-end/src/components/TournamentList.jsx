/* src/components/TournamentList.jsx */
import React from 'react';
import './TournamentList.css';

export default function TournamentList({
                                           tournaments,
                                           matches,
                                           today,
                                           selectedMatchId,
                                           onSelectMatch,
                                       }) {
    const list = tournaments
        .map(t => ({
            ...t,
            todaysMatches: matches.filter(
                m => m.tournamentId === t.id && m.matchDate.startsWith(today)
            ),
        }))
        .filter(t => t.todaysMatches.length);

    if (!list.length) {
        return (
            <div className="no-matches-message">
                <p>No matches scheduled for today in this sport.</p>
            </div>
        );
    }

    return (
        <div className="tournament-list-section">
            {list.map(t => (
                <div key={t.id} className="tournament-card">
                    <h2 className="tournament-title">{t.name}</h2>

                    <div className="match-list">
                        {t.todaysMatches.map(m => {
                            const time = new Date(m.matchDate).toLocaleTimeString([], {
                                hour: '2-digit',
                                minute: '2-digit',
                            });
                            const active = m.id === selectedMatchId;

                            return (
                                <div
                                    key={m.id}
                                    onClick={() => onSelectMatch(m.id)}
                                    className={`match-item ${active ? 'active' : ''}`}
                                >
                                    <div className="match-info">
                                        <span className="match-time">{time}</span>
                                        <span className="competitor-name">{m.firstCompetitor}</span>
                                        <span className="vs-separator">vs</span>
                                        <span className="competitor-name">{m.secondCompetitor}</span>
                                    </div>
                                    <span className="match-status">{m.status.toUpperCase()}</span>
                                </div>
                            );
                        })}
                    </div>
                </div>
            ))}
        </div>
    );
}
