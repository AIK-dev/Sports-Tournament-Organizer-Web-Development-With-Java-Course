import React, { useState, useEffect, useMemo } from 'react';
import {fetchTournament, fetchTournaments} from '../api/tournamentsApi';
import { fetchTournamentMatches } from '../api/matchesApi';
import TournamentList from '../components/TournamentList';
import MatchDetails from '../components/MatchDetails';
import { getTodayDate } from '../utils/dateUtils';
import { getAccessToken, logout } from '../api/authApi';
import { useNavigate }  from 'react-router';

import './Home.css';

export default function Home() {
    const [allTournaments, setAllTournaments] = useState([]);
    const [matchesByTournament, setMatchesByTournament] = useState({});
    const [selectedSport, setSelectedSport] = useState('all');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedMatchId, setSelectedMatchId] = useState(null);
    const [selectedMatchData, setSelectedMatchData] = useState(null);
    const nav   = useNavigate();
    const token = getAccessToken();

    const SPORTS = ['all',
        'basketball','baseball','volleyball','football','tennis','table_tennis',
        'handball','golf','ice_hockey','wrestling','archery','cycling','swimming',
        'skiing','running','marathon','pole_vault','weightlifting','powerlifting',
        'surfing','chess','lacrosse','squash','rugby','american_football',
        'boxing','biathlon',
    ];

    const todayDate = getTodayDate();

    useEffect(() => {
        const loadTournaments = async () => {
            try {
                setLoading(true);
                const tournamentsResponse = await fetchTournaments(selectedSport === 'all' ? null : selectedSport);
                setAllTournaments(tournamentsResponse.data);
                setError(null);
            } catch (err) {
                console.error("Failed to fetch tournaments:", err);
                setError("Failed to load tournaments. Please try again later.");
            } finally {
                setLoading(false);
            }
        };
        loadTournaments();
    }, [selectedSport]);

    useEffect(() => {
        const loadMatchesForTournaments = async () => {
            if (!allTournaments.length) {
                setMatchesByTournament({});
                return;
            }

            const newMatchesByTournament = {};
            const fetchPromises = allTournaments.map(async (tournament) => {
                try {
                    const matchesResponse = await fetchTournamentMatches(tournament.id);
                    const todaysAndSportMatches = matchesResponse.data.filter(
                        match => {
                            const isToday = match.scheduledStart?.startsWith(todayDate);
                            const isSelectedSport = selectedSport === 'all' || (tournament.sport && tournament.sport.toLowerCase() === selectedSport.toLowerCase());

                            // START Debug Logs for Match Filtering
                            console.group(`Match Filter Check: ${match.id} (Tournament: ${tournament.name})`);
                            console.log(`Match Sport (from backend): '${match.sport}'`);
                            console.log(`Selected Sport (frontend filter): '${selectedSport}'`);
                            console.log(`Match ScheduledStart: '${match.scheduledStart}'`);
                            console.log(`Today's Date (from getTodayDate): '${todayDate}'`);
                            console.log(`Condition 1: isToday (${match.scheduledStart} startsWith ${todayDate}) = ${isToday}`);
                            console.log(`Condition 2: isSelectedSport (${selectedSport} === 'all' || ('${match.sport}' && '${match.sport}'.toLowerCase() === '${selectedSport}'.toLowerCase())) = ${isSelectedSport}`);
                            console.log(`Overall Result for match ${match.id}: ${isToday && isSelectedSport}`);
                            console.groupEnd();
                            // END Debug Logs for Match Filtering

                            return isToday && isSelectedSport;
                        }
                    );
                    newMatchesByTournament[tournament.id] = todaysAndSportMatches;
                } catch (err) {
                    console.warn(`Failed to fetch matches for tournament ${tournament.id}:`, err);
                    newMatchesByTournament[tournament.id] = [];
                }
            });

            await Promise.all(fetchPromises);
            setMatchesByTournament(newMatchesByTournament);
        };

        loadMatchesForTournaments();
    }, [allTournaments, todayDate, selectedSport, loading]);

    useEffect(() => {
        if (selectedMatchId) {
            let foundMatch = null;
            for (const tournamentId in matchesByTournament) {
                foundMatch = matchesByTournament[tournamentId].find(m => m.id === selectedMatchId);
                if (foundMatch) break;
            }
            setSelectedMatchData(foundMatch);
        } else {
            setSelectedMatchData(null);
        }
    }, [selectedMatchId, matchesByTournament]);

    const tournamentsForList = useMemo(() => {
        return allTournaments
            .map(tournament => ({
                ...tournament,
                todaysMatches: matchesByTournament[tournament.id] || [],
            }))
            .filter(tournament => tournament.todaysMatches.length > 0);
    }, [allTournaments, matchesByTournament]);

    if (loading) {
        return <div className="home-page-layout">Loading...</div>;
    }

    if (error) {
        return <div className="home-page-layout err">{error}</div>;
    }

    return (

        <div className="home-page-layout">
                <header className="home-topbar">
                    {!token ? (
                        <button className="topBtn" onClick={() => nav('/login')}>
                            Log&nbsp;in
                        </button>
                    ) : (
                        <>
                            <button className="topBtn" onClick={() => { logout(); nav('/'); }}>
                                Logout
                            </button>

                            <button className="topBtn navBtn" onClick={() => nav('/players')}>
                                Players
                            </button>
                            <button className="topBtn navBtn" onClick={() => nav('/teams')}>
                                Teams
                            </button>
                            <button className="topBtn navBtn" onClick={() => nav('/users')}>
                                Users
                            </button>
                        </>
                    )}
                </header>

            <div className="sports-bar">
                <header className="sports-bar">
                    {SPORTS.map(name => (
                        <button
                            key={name}
                            onClick={() => {
                                setSelectedSport(name);
                                setSelectedMatchData(null);
                            }}
                            className={name === selectedSport ? 'sport-btn active' : 'sport-btn'}
                        >
                            {name.replace('_', ' ').toUpperCase()}
                        </button>
                    ))}
                </header>
            </div>

            <div className="content-grid">
                <main className="main-content-area center-column">
                    {tournamentsForList.length ? (
                        <TournamentList
                            tournaments={tournamentsForList}
                            selectedMatchId={selectedMatchId}
                            onSelectMatch={setSelectedMatchId}
                        />
                    ) : (
                        <div className="no-matches-message">
                            <p>No matches scheduled for today in {selectedSport === 'all' ? 'any sport' : selectedSport.replace('_', ' ')}.</p>
                        </div>
                    )}
                </main>

                <aside className="match-details-sidebar right-column-sidebar">
                    <MatchDetails
                        match={selectedMatchData}
                        allTournaments={allTournaments}
                    />
                </aside>
            </div>
        </div>
    );
}