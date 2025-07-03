/*  src/pages/Players.jsx  */
import { useState, useEffect, useMemo } from 'react';
import { Link, useNavigate } from 'react-router';

import { fetchPlayers } from '../api/playersApi';
import { getAccessToken, logout } from '../api/authApi';
import { isAdmin } from '../api/authApi';

const SPORTS = [
    'basketball', 'baseball', 'volleyball', 'football', 'tennis', 'table_tennis',
    'handball', 'golf', 'ice_hockey', 'wrestling', 'archery', 'cycling', 'swimming',
    'skiing', 'running', 'marathon', 'pole_vault', 'weightlifting', 'powerlifting',
    'surfing', 'chess', 'lacrosse', 'squash', 'rugby', 'american_football',
    'boxing', 'biathlon',
];

export default function Players() {
    const nav = useNavigate();
    const token = getAccessToken();

    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);
    const [selSport, setSelSport] = useState(SPORTS[0]);

    const [search, setSearch] = useState('');
    const [level, setLevel] = useState('all');
    const [gender, setGender] = useState('all');
    const [ageFrom, setAgeFrom] = useState('');
    const [ageTo, setAgeTo] = useState('');
    const [sortBy, setSortBy] = useState('firstName');

    useEffect(() => {
        fetchPlayers()
            .then(r => setPlayers(r.data))
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, []);

    const sports = useMemo(
        () => Array.from(new Set(players.map(p => p.sport))).sort(),
        [players]
    );
    const levels = useMemo(
        () => Array.from(new Set(players.map(p => p.level))).sort(),
        [players]
    );

    const view = useMemo(() => {
        let out = [...players];

        const q = search.toLowerCase();
        if (q)
            out = out.filter(p =>
                (`${p.firstName} ${p.secondName} ${p.associatedTeam}`)
                    .toLowerCase()
                    .includes(q)
            );
        if (selSport !== 'all') out = out.filter(p => p.sport === selSport);
        if (level !== 'all') out = out.filter(p => p.level === level);
        if (gender !== 'all') out = out.filter(p => p.gender === gender);
        if (ageFrom) out = out.filter(p => p.age >= +ageFrom);
        if (ageTo) out = out.filter(p => p.age <= +ageTo);

        out.sort((a, b) => {
            if (sortBy === 'age') return a.age - b.age;
            if (sortBy === 'sport') return a.sport.localeCompare(b.sport);
            return a.firstName.localeCompare(b.firstName);
        });

        return out;
    }, [players, search, selSport, level, gender, ageFrom, ageTo, sortBy]);

    if (loading) return <p className="pad">Loading…</p>;
    if (err) return <p className="pad err">Error: {err}</p>;

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
                    <button
                        key="all"
                        onClick={() => setSelSport('all')}
                        className={selSport === 'all' ? 'sport-btn active' : 'sport-btn'}
                    >
                        ALL SPORTS
                    </button>
                    {SPORTS.map(name => (
                        <button
                            key={name}
                            onClick={() => setSelSport(name)}
                            className={name === selSport ? 'sport-btn active' : 'sport-btn'}
                        >
                            {name.replace('_', ' ').toUpperCase()}
                        </button>
                    ))}
                </header>

                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <div className="players-toolbar">
                                <input
                                    className="players-search"
                                    placeholder="Search players..."
                                    value={search}
                                    onChange={e => setSearch(e.target.value)}
                                />

                                <select
                                    className="players-select"
                                    value={level}
                                    onChange={e => setLevel(e.target.value)}
                                >
                                    <option value="all">All levels</option>
                                    {levels.map(l => <option key={l}>{l}</option>)}
                                </select>

                                <select
                                    className="players-select"
                                    value={gender}
                                    onChange={e => setGender(e.target.value)}
                                >
                                    <option value="all">Any gender</option>
                                    <option value="MALE">Male</option>
                                    <option value="FEMALE">Female</option>
                                </select>

                                <input
                                    className="players-input"
                                    type="number"
                                    min="0"
                                    placeholder="Age from"
                                    value={ageFrom}
                                    onChange={e => setAgeFrom(e.target.value)}
                                />
                                <input
                                    className="players-input"
                                    type="number"
                                    min="0"
                                    placeholder="Age to"
                                    value={ageTo}
                                    onChange={e => setAgeTo(e.target.value)}
                                />

                                <select
                                    className="players-select"
                                    value={sortBy}
                                    onChange={e => setSortBy(e.target.value)}
                                >
                                    <option value="firstName">Sort: Name</option>
                                    <option value="age">Age</option>
                                    <option value="sport">Sport</option>
                                </select>

                                {isAdmin() && (
                                    <button
                                        className="topBtn"
                                        style={{ marginLeft: 'auto' }}
                                        onClick={() => nav('/players/new')}
                                    >
                                        + Add player
                                    </button>
                                )}
                            </div>

                            {view.length ? (
                                <div className="players-grid">
                                    {view.map(p => (
                                        <Link
                                            key={p.id}
                                            to={`/players/${p.id}`}
                                            className="player-card"
                                        >
                                            <h3>{p.firstName} {p.secondName}</h3>
                                            <p>{p.age} y · {p.gender}</p>
                                            <p>{p.level} · {p.sport}</p>
                                            <small>{p.associatedTeam}</small>
                                        </Link>
                                    ))}
                                </div>
                            ) : (
                                <p className="pad">Nothing matches your criteria.</p>
                            )}
                        </section>
                    </div>
                </main>
            </div>
        </>
    );
}