import React, { useState, useEffect } from 'react';
import { fetchParticipation } from '../api/participationApi';
import { fetchTeam } from '../api/teamsApi';
import { fetchPlayer } from '../api/playersApi';
import './TournamentList.css';

export default function TournamentList({
                                           tournaments,
                                           selectedMatchId,
                                           onSelectMatch,
                                       }) {
    const [enrichedMatches, setEnrichedMatches] = useState({});

    useEffect(() => {
        const fetchParticipationDetails = async (participantId) => {
            try {
                const participantResponse = await fetchParticipation(participantId);
                if (participantResponse.data.playerId) {
                    const playerResponse = await fetchPlayer(participantResponse.data.playerId);
                    const fullName = `${playerResponse.data.firstName} ${playerResponse.data.secondName}`;
                    return fullName;
                } else if (participantResponse.data.teamId) {
                    const teamResponse = await fetchTeam(participantResponse.data.teamId);
                    return teamResponse.data.name;
                }
                return 'N/A';
            } catch (error) {
                console.error(`Failed to fetch details for participant ${participantId}:`, error);
                return 'N/A';
            }
        };

        const enrichAllMatches = async () => {
            const newEnrichedMatches = {};
            const matchPromises = [];

            tournaments.forEach(tournament => {
                tournament.todaysMatches.forEach(match => {
                    if (!enrichedMatches[match.id]) {
                        matchPromises.push((async () => {
                            const firstCompetitorName = await fetchParticipationDetails(match.participant1Id);
                            const secondCompetitorName = await fetchParticipationDetails(match.participant2Id);
                            newEnrichedMatches[match.id] = {
                                ...match,
                                firstCompetitor: firstCompetitorName,
                                secondCompetitor: secondCompetitorName,
                            };
                        })());
                    } else {
                        newEnrichedMatches[match.id] = enrichedMatches[match.id];
                    }
                });
            });

            await Promise.all(matchPromises);
            setEnrichedMatches(prev => ({ ...prev, ...newEnrichedMatches }));
        };

        if (tournaments.length > 0) {
            enrichAllMatches();
        }
    }, [tournaments]);

    if (!tournaments.length) {
        return (
            <div className="no-matches-message">
                <p>No matches scheduled for today in this sport.</p>
            </div>
        );
    }

    return (
        <div className="tournament-list-section">
            {tournaments.map(t => (
                <div key={t.id} className="tournament-card">
                    <h2 className="tournament-title">{t.name}</h2>

                    <div className="match-list">
                        {t.todaysMatches && t.todaysMatches.length > 0 ? (
                            t.todaysMatches.map(m => {
                                const displayMatch = enrichedMatches[m.id] || m;

                                const time = displayMatch.scheduledStart ?
                                    new Date(displayMatch.scheduledStart).toLocaleTimeString([], {
                                        hour: '2-digit',
                                        minute: '2-digit',
                                    }) : 'N/A';
                                const active = displayMatch.id === selectedMatchId;

                                return (
                                    <div
                                        key={displayMatch.id}
                                        onClick={() => onSelectMatch(displayMatch.id)}
                                        className={`match-item ${active ? 'active' : ''}`}
                                    >
                                        <div className="match-info">
                                            <span className="match-time">{time}</span>
                                            <span className="competitor-name">{displayMatch.firstCompetitor || `ID: ${displayMatch.participant1Id || 'N/A'}`}</span>
                                            <span className="vs-separator">vs</span>
                                            <span className="competitor-name">{displayMatch.secondCompetitor || `ID: ${displayMatch.participant2Id || 'N/A'}`}</span>
                                        </div>
                                        <span className="match-status">{displayMatch.status?.toUpperCase() || ''}</span>
                                    </div>
                                );
                            })
                        ) : (
                            <p className="no-matches-text-card">No matches today for this tournament.</p>
                        )}
                    </div>
                </div>
            ))}
        </div>
    );
}