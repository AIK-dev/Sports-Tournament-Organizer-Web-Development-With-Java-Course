// src/pages/AssignTeam.jsx
import { useEffect, useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router';
import { fetchTeams }  from '../api/teamsApi';
import { addToTeam }   from '../api/playersApi';
import { getAccessToken, logout } from '../api/authApi';

export default function AssignTeam() {
    /* ---------- routing ---------- */
    const { id } = useParams();           // player id
    const nav    = useNavigate();
    const token  = getAccessToken();

    /* ---------- data ---------- */
    const [teams, setTeams] = useState([]);
    const [q,     setQ]     = useState('');
    const [err,   setErr]   = useState(null);

    /* ---------- fetch ---------- */
    useEffect(() => {
        fetchTeams()
            .then(r => setTeams(r.data))
            .catch(e => setErr(e.message));
    }, []);

    /* ---------- filtered view ---------- */
    const view = useMemo(() => {
        return teams.filter(t =>
            t.name.toLowerCase().includes(q.toLowerCase())
        );
    }, [teams, q]);

    if (err) return <p className="pad err">Error: {err}</p>;

    /* ---------- UI ---------- */
    return (
        <>
            {/* ---------- Top-bar ---------- */}
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
                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <h2 style={{ marginBottom: '1rem' }}>Choose team</h2>

                            {/* Search */}
                            <input
                                className="players-search"
                                style={{ maxWidth: 320 }}
                                placeholder="Search…"
                                value={q}
                                onChange={e => setQ(e.target.value)}
                            />

                            {/* List */}
                            {view.length ? (
                                <ul className="teamSelectList">
                                    {view.map(t => (
                                        <li key={t.id} className="teamSelectItem">
                                            <span>{t.name}</span>
                                            <button
                                                className="primaryBtn"
                                                onClick={async () => {
                                                    await addToTeam(id, t.id);
                                                    nav(`/players/${id}`);
                                                }}
                                            >
                                                Select
                                            </button>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p className="pad">No teams found.</p>
                            )}
                        </section>
                    </div>
                </main>
            </div>
        </>
    );
}