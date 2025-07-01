import React from 'react';

const MatchDetails = ({ match, tournament }) => {
    if (!match || !tournament) {
        return (
            <div className="bg-gray-800 rounded-lg p-6 h-full flex items-center justify-center">
                <p className="text-gray-500">Click on a match to see details</p>
            </div>
        );
    }

    const matchDate = new Date(match.matchDate);

    return (
        <div className="bg-gray-800 rounded-lg shadow-lg p-6 space-y-6">
            <div>
                <p className="text-sm text-indigo-400 font-semibold">{tournament.name}</p>
                <h3 className="text-2xl font-bold mt-1">Match Details</h3>
            </div>

            <div className="text-center space-y-2 py-4">
                <p className="text-xl font-semibold">{match.firstCompetitor}</p>
                <p className="text-gray-400 font-bold">VS</p>
                <p className="text-xl font-semibold">{match.secondCompetitor}</p>
            </div>

            <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                    <span className="text-gray-400">Status:</span>
                    <span className="font-semibold bg-blue-900/50 text-blue-300 px-2 py-1 rounded">{match.status}</span>
                </div>
                <div className="flex justify-between">
                    <span className="text-gray-400">Date:</span>
                    <span className="font-semibold">{matchDate.toLocaleDateString()}</span>
                </div>
                <div className="flex justify-between">
                    <span className="text-gray-400">Time:</span>
                    <span className="font-semibold">{matchDate.toLocaleTimeString()}</span>
                </div>
                <div className="flex justify-between">
                    <span className="text-gray-400">Tournament:</span>
                    <span className="font-semibold">{tournament.name}</span>
                </div>
            </div>
        </div>
    );
};

export default MatchDetails;
