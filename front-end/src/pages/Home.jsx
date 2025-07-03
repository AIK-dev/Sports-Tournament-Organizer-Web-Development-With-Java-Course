import { useState, useEffect, useMemo } from 'react';
import { useNavigate }  from 'react-router';

import TournamentList from '../components/TournamentList';
import MatchDetails   from '../components/MatchDetails';

import { fetchMatches }     from '../api/matchesApi';
import { fetchTournaments } from '../api/tournamentsApi';
import { getAccessToken, logout } from '../api/authApi';

import './Home.css';

const SPORTS = [
    'basketball','baseball','volleyball','football','tennis','table_tennis',
    'handball','golf','ice_hockey','wrestling','archery','cycling','swimming',
    'skiing','running','marathon','pole_vault','weightlifting','powerlifting',
    'surfing','chess','lacrosse','squash','rugby','american_football',
    'boxing','biathlon',
];
const TODAY = new Date().toISOString().slice(0,10);     // YYYY-MM-DD

export default function Home() {
    /* ---------- state ---------- */
    const [tournaments, setTournaments] = useState([]);
    const [matches,     setMatches]     = useState([]);

    const [selSport, setSelSport] = useState(SPORTS[0]);
    const [selMatch, setSelMatch] = useState(null);

    const [err,  setErr ] = useState(null);
    const [load, setLoad] = useState(true);

    const nav   = useNavigate();
    const token = getAccessToken();

    useEffect(() => {
        Promise.all([fetchTournaments(), fetchMatches()])
            .then(([tRes, mRes]) => {
                setTournaments(tRes.data);
                setMatches(mRes.data);
            })
            .catch(e => setErr(e.message))
            .finally(() => setLoad(false));
    }, []);

    const visibleTournaments = useMemo(
        () => tournaments.filter(t => t.sport?.toLowerCase() === selSport),
        [tournaments, selSport]
    );

    const todayMatches = useMemo(
        () => matches.filter(m => m.matchDate.startsWith(TODAY)),
        [matches]
    );

    const matchObj   = matches.find(m => m.id === selMatch) || null;
    const matchTourn = matchObj
        ? tournaments.find(t => t.id === matchObj.tournamentId)
        : null;

    if (load) return <p className="pad">Loading…</p>;
    if (err)  return <p className="pad err">Error: {err}</p>;

    return (
        <>
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

                        {/* навигация към защитените раздели */}
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

            <div className="home-page-layout">
                <header className="sports-bar">
                    {SPORTS.map(name => (
                        <button
                            key={name}
                            onClick={() => { setSelSport(name); setSelMatch(null); }}
                            className={name === selSport ? 'sport-btn active' : 'sport-btn'}
                        >
                            {name.replace('_',' ').toUpperCase()}
                        </button>
                    ))}
                </header>

                {/* съдържание */}
                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <TournamentList
                                tournaments={visibleTournaments}
                                matches={todayMatches}
                                today={TODAY}
                                selectedMatchId={selMatch}
                                onSelectMatch={setSelMatch}
                            />
                        </section>

                        <aside className="right-column-sidebar">
                            <MatchDetails match={matchObj} tournament={matchTourn}/>
                        </aside>
                    </div>
                </main>
            </div>
        </>
    );
}
