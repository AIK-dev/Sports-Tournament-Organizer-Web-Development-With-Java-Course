import React, { useState, useEffect } from 'react';
import { fetchParticipation } from '../api/participationApi';
import { fetchTeam } from '../api/teamsApi';
import { fetchPlayer } from '../api/playersApi';
import './MatchDetails.css';

const MatchDetails = ({ match, allTournaments }) => {
    const [detailedMatchWithNames, setDetailedMatchWithNames] = useState(null);
    const [tournamentForMatch, setTournamentForMatch] = useState(null);
    const [loadingDetails, setLoadingDetails] = useState(false);

    useEffect(() => {
        const loadDetails = async () => {
            if (!match) {
                setDetailedMatchWithNames(null);
                setTournamentForMatch(null);
                return;
            }

            setLoadingDetails(true);
            try {
                const participant1 = await fetchParticipation(match.participant1Id);
                const participant2 = await fetchParticipation(match.participant2Id);

                let firstCompetitor = 'N/A';
                if (participant1.data.playerId) {
                    const player = await fetchPlayer(participant1.data.playerId);
                    firstCompetitor = `${player.data.firstName} ${player.data.secondName}`;
                } else if (participant1.data.teamId) {
                    const team = await fetchTeam(participant1.data.teamId);
                    firstCompetitor = team.data.name;
                }

                let secondCompetitor = 'N/A';
                if (participant2.data.playerId) {
                    const player = await fetchPlayer(participant2.data.playerId);
                    secondCompetitor = `${player.data.firstName} ${player.data.secondName}`;
                } else if (participant1.data.teamId) {
                    const team = await fetchTeam(participant2.data.teamId);
                    secondCompetitor = team.data.name;
                }

                const relatedTournament = allTournaments.find(t => t.id === match.tournamentId);

                setDetailedMatchWithNames({ ...match, firstCompetitor, secondCompetitor });
                setTournamentForMatch(relatedTournament);

            } catch (err) {
                console.error("Failed to load detailed match participants:", err);
                setDetailedMatchWithNames(null);
                setTournamentForMatch(null);
            } finally {
                setLoadingDetails(false);
            }
        };

        loadDetails();
    }, [match, allTournaments]);

    if (loadingDetails) {
        return (
            <div className="match-details-placeholder">
                <p>Loading match details...</p>
            </div>
        );
    }

    if (!detailedMatchWithNames || !tournamentForMatch) {
        return (
            <div className="match-details-placeholder">
                <p>Select a match to see details</p>
            </div>
        );
    }

    const matchDate = new Date(detailedMatchWithNames.scheduledStart);

    return (
        <div className="match-details-card">
            <div className="match-details-header">
                <p>{tournamentForMatch.name}</p>
                <h3>Match Details</h3>
            </div>

            <div className="competitor-display">
                <p className="name">{detailedMatchWithNames.firstCompetitor}</p>
                <p className="vs">VS</p>
                <p className="name">{detailedMatchWithNames.secondCompetitor}</p>
            </div>

            <div className="match-meta-info">
                <div className="match-meta-row">
                    <span className="label">Status:</span>
                    <span className="value status-value">{detailedMatchWithNames.status}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Date:</span>
                    <span className="value">{matchDate.toLocaleDateString()}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Time:</span>
                    <span className="value">{matchDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Tournament:</span>
                    <span className="value">{tournamentForMatch.name}</span>
                </div>
            </div>
        </div>
    );
};

export default MatchDetails;