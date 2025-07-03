import React from 'react';
import './MatchDetails.css';

const MatchDetails = ({ match, tournament }) => {
    if (!match || !tournament) {
        return (
            <div className="match-details-placeholder">
                <p>Click on a match to see details</p>
            </div>
        );
    }

    const matchDate = new Date(match.matchDate);

    return (
        <div className="match-details-card">
            <div className="match-details-header">
                <p>{tournament.name}</p>
                <h3>Match Details</h3>
            </div>

            <div className="competitor-display">
                <p className="name">{match.firstCompetitor}</p>
                <p className="vs">VS</p>
                <p className="name">{match.secondCompetitor}</p>
            </div>

            <div className="match-meta-info">
                <div className="match-meta-row">
                    <span className="label">Status:</span>
                    <span className="value status-value">{match.status}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Date:</span>
                    <span className="value">{matchDate.toLocaleDateString()}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Time:</span>
                    <span className="value">{matchDate.toLocaleTimeString()}</span>
                </div>
                <div className="match-meta-row">
                    <span className="label">Tournament:</span>
                    <span className="value">{tournament.name}</span>
                </div>
            </div>
        </div>
    );
};

export default MatchDetails;