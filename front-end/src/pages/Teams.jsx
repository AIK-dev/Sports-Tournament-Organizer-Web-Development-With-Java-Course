// src/pages/Teams.jsx
import { useState, useEffect, useMemo } from 'react';
import { Link, useNavigate } from 'react-router';

import { fetchTeams } from '../api/teamsApi';
import { getAccessToken, logout, isAdmin } from '../api/authApi';

const SPORTS = [
    'basketball', 'baseball', 'volleyball', 'football', 'tennis', 'table_tennis',
    'handball', 'golf', 'ice_hockey', 'wrestling', 'archery', 'cycling', 'swimming',
    'skiing', 'running', 'marathon', 'pole_vault', 'weightlifting', 'powerlifting',
    'surfing', 'chess', 'lacrosse', 'squash', 'rugby', 'american_football',
    'boxing', 'biathlon',
];

export default function Teams() {
    const nav   = useNavigate();
    const token = getAccessToken();

    const [teams,   setTeams]   = useState([]);
    const [loading, setLoading] = useState(true);
    const [err,     setErr]     = useState(null);
    const [selSport, setSelSport] = useState('all');

    const [search,    setSearch]    = useState('');
    const [ageGroup,  setAgeGroup]  = useState('all');
    const [teamType,  setTeamType]  = useState('all');
    const [sortBy,    setSortBy]    = useState('name');

    useEffect(() => {
        fetchTeams()
            .then(r => setTeams(r.data))
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, []);

    const ageGroups = useMemo(
        () => Array.from(new Set(teams.map(t => t.ageGroup))).sort(),
        [teams]
    );
    const types = useMemo(
        () => Array.from(new Set(teams.map(t => t.type))).sort(),
        [teams]
    );

    const view = useMemo(() => {
        let out = [...teams];

        const q = search.toLowerCase();
        if (q)
            out = out.filter(t => t.name.toLowerCase().includes(q));

        if (selSport !== 'all') out = out.filter(t => t.sport === selSport);
        if (ageGroup !== 'all') out = out.filter(t => t.ageGroup === ageGroup);
        if (teamType !== 'all') out = out.filter(t => t.type === teamType);

        out.sort((a, b) => {
            if (sortBy === 'sport') return a.sport.localeCompare(b.sport);
            if (sortBy === 'ageGroup') return a.ageGroup.localeCompare(b.ageGroup);
            return a.name.localeCompare(b.name);
        });

        return out;
    }, [teams, search, selSport, ageGroup, teamType, sortBy]);

    if (loading) return <p className="pad">Loading…</p>;
    if (err)     return <p className="pad err">Error: {err}</p>;

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
                            {/* toolbar */}
                            <div className="players-toolbar">
                                <input
                                    className="players-search"
                                    placeholder="Search teams..."
                                    value={search}
                                    onChange={e => setSearch(e.target.value)}
                                />

                                <select
                                    className="players-select"
                                    value={ageGroup}
                                    onChange={e => setAgeGroup(e.target.value)}
                                >
                                    <option value="all">Any age group</option>
                                    {ageGroups.map(g => <option key={g}>{g}</option>)}
                                </select>

                                <select
                                    className="players-select"
                                    value={teamType}
                                    onChange={e => setTeamType(e.target.value)}
                                >
                                    <option value="all">Any type</option>
                                    {types.map(t => <option key={t}>{t}</option>)}
                                </select>

                                <select
                                    className="players-select"
                                    value={sortBy}
                                    onChange={e => setSortBy(e.target.value)}
                                >
                                    <option value="name">Sort: Name</option>
                                    <option value="sport">Sport</option>
                                    <option value="ageGroup">Age group</option>
                                </select>

                                {isAdmin() && (
                                    <button
                                        className="topBtn"
                                        style={{ marginLeft: 'auto' }}
                                        onClick={() => nav('/teams/new')}
                                    >
                                        + Add team
                                    </button>
                                )}
                            </div>

                            {view.length ? (
                                <div className="teams-grid">
                                    {view.map(t => (
                                        <Link
                                            key={t.id}
                                            to={`/teams/${t.id}`}
                                            className="team-card"
                                        >
                                            <h3>{t.name}</h3>
                                            <p>{t.sport} · {t.ageGroup} · {t.type}</p>
                                            <small>{t.players?.length ?? 0} players</small>
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