import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router';

import {
    fetchTeam,
    deleteTeam,
    changeTeamOwner,          // <-- добавена API-функция
} from '../api/teamsApi';

import { fetchUsers } from '../api/userApi';

import {
    getAccessToken,
    logout,
    getCurrentUser,
    hasRole,
    isAdmin,
} from '../api/authApi';

export default function TeamDetail() {
    const { id } = useParams();
    const nav    = useNavigate();
    const token  = getAccessToken();

    const [team,     setTeam]     = useState(null);
    const [users,    setUsers]    = useState([]);
    const [newOwner, setNewOwner] = useState('');
    const [loading,  setLoading]  = useState(true);
    const [err,      setErr]      = useState(null);

    useEffect(() => {
        Promise.all([
            fetchTeam(id).then(r => setTeam(r.data)),
            isAdmin() && fetchUsers().then(r =>
                           // показвай само потребители с роля ORGANIZER
                               setUsers(r.data.filter(u => u.role === 'ORGANIZER'))
                       ),
        ])
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) return <p className="pad">Loading…</p>;
    if (err)     return <p className="pad err">Error: {err}</p>;
    if (!team)   return <p className="pad">No data.</p>;

    const me       = getCurrentUser();
    const myId     = me?.userId;
    const iAmOwner = team.ownerId === myId;

    const canEdit  = isAdmin() || (hasRole('ORGANIZER') && iAmOwner);
    const canAdmin = isAdmin();

    return (
        <>
            <header className="home-topbar">
                {!token ? (
                    <button className="topBtn" onClick={() => nav('/login')}>Log&nbsp;in</button>
                ) : (
                    <>
                        <button className="topBtn" onClick={() => { logout(); nav('/'); }}>Logout</button>
                        <button className="topBtn navBtn" onClick={() => nav('/players')}>Players</button>
                        <button className="topBtn navBtn" onClick={() => nav('/teams')}>Teams</button>
                        <button className="topBtn navBtn" onClick={() => nav('/users')}>Users</button>
                    </>
                )}
            </header>

            <div className="home-page-layout">
                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <div className="pad wrapper">
                                <h2>{team.name}</h2>
                                <p>{team.sport} · {team.ageGroup} · {team.type}</p>
                                <p><b>Owner:</b> {team.ownerUsername || '—'}</p>

                                {canAdmin && (
                                    <OwnerBox
                                        users={users}
                                        value={newOwner}
                                        onChange={setNewOwner}
                                        onSave={async () => {
                                            await changeTeamOwner(team.id, newOwner);
                                            location.reload();
                                        }}
                                    />
                                )}

                                <h4 style={{ marginTop: '1.25rem' }}>Players:</h4>
                                {team.players?.length ? (
                                    <div className="players-grid" style={{ marginTop: '0.75rem' }}>
                                        {team.players.map(p => (
                                            <Link key={p.id} to={`/players/${p.id}`} className="player-card">
                                                <h3>{p.firstName} {p.secondName}</h3>
                                                <p>{p.age} y · {p.gender}</p>
                                                <p>{p.level}</p>
                                            </Link>
                                        ))}
                                    </div>
                                ) : (
                                    <p>No players in this team yet.</p>
                                )}

                                {canEdit && (
                                    <div className="inline" style={{ marginTop: 24 }}>
                                        <button
                                            className="primaryBtn"
                                            onClick={() => nav(`/teams/${team.id}/edit`)}
                                        >
                                            Edit
                                        </button>
                                        {canAdmin && (
                                            <button
                                                className="danger"
                                                onClick={async () => {
                                                    if (confirm('Delete this team?')) {
                                                        await deleteTeam(team.id);
                                                        nav('/teams');
                                                    }
                                                }}
                                            >
                                                Delete
                                            </button>
                                        )}
                                    </div>
                                )}
                            </div>
                        </section>
                    </div>
                </main>
            </div>
        </>
    );
}

/* ---------- helpers ---------- */
function OwnerBox({ users, value, onChange, onSave }) {
    return (
        <div className="inline" style={{ marginTop: 16 }}>
            <select value={value} onChange={e => onChange(e.target.value)}>
                <option value="">Change owner…</option>
                {users.map(u => (
                    <option key={u.id} value={u.id}>{u.username}</option>
                ))}
            </select>
            <button
                className="primaryBtn"
                disabled={!value}
                onClick={onSave}
            >
                Save
            </button>
        </div>
    );
}
