// src/pages/PlayerDetail.jsx
import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import {
    fetchPlayer,
    deletePlayer,
    changeOwner,
    removeFromTeam,
} from '../api/playersApi';
import { fetchUsers } from '../api/userApi';
import {
    isAdmin,
    hasRole,
    getCurrentUser,
    getAccessToken,
    logout,
} from '../api/authApi';

export default function PlayerDetail() {
    /* ---------- routing ---------- */
    const { id } = useParams();
    const nav    = useNavigate();
    const token  = getAccessToken();

    /* ---------- data ---------- */
    const [player,   setPlayer]   = useState(null);
    const [users,    setUsers]    = useState([]);
    const [newOwner, setNew]      = useState('');
    const [err,      setErr]      = useState(null);
    const [loading,  setLoading]  = useState(true);

    /* ---------- fetch ---------- */
    useEffect(() => {
        Promise.all([
            fetchPlayer(id).then(r => setPlayer(r.data)),
            isAdmin() && fetchUsers().then(r =>
                // показвай само потребители с роля ORGANIZER
                setUsers(r.data.filter(u => u.role === 'ORGANIZER'))
            ),
        ])
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, [id]);

    /* ---------- early states ---------- */
    if (loading) return <p className="pad">Loading…</p>;
    if (err)     return <p className="pad err">Error: {err}</p>;
    if (!player) return <p className="pad">No data.</p>;

    /* ---------- who am I ---------- */
    const me       = getCurrentUser();
    const myId     = me?.userId;

    const iAmOwner = player.ownerId === myId;
    const iAmThis  = player.userId  === myId;   // participant–self

    const canEdit  =
        isAdmin()
        || (hasRole('ORGANIZER')   && iAmOwner)
        || (hasRole('PARTICIPANT') && iAmThis);

    const canAdmin =
        isAdmin()
        || (hasRole('ORGANIZER') && iAmOwner);

    const hasTeam  =
        player.teamId != null
        || (player.associatedTeam && player.associatedTeam !== 'SINGLE PLAYER');

    /* ---------- UI ---------- */
    return (
        <>
            {/* ---------- Top-bar (same as in <Players>) ---------- */}
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
                {/* ---------- Main content ---------- */}
                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <div className="pad wrapper">
                                <h2>{player.firstName} {player.secondName}</h2>

                                <p><b>Age:</b> {player.age}</p>
                                <p><b>Gender:</b> {player.gender}</p>
                                <p><b>Sport / Level:</b> {player.sport} • {player.level}</p>
                                <p><b>Team:</b> {hasTeam ? player.associatedTeam : '—'}</p>
                                <p><b>Owner:</b> {player.ownerUsername || '—'}</p>

                                {/* --- only ADMIN + organizer-owner --- */}
                                {canAdmin && (
                                    <>
                                        <OwnerBox
                                            users={users}
                                            value={newOwner}
                                            onChange={setNew}
                                            onSave={async () => {
                                                await changeOwner(player.id, newOwner);
                                                location.reload();
                                            }}
                                        />

                                        {hasTeam ? (
                                            <RemoveBtn onRemove={async () => {
                                                await removeFromTeam(player.id);
                                                location.reload();
                                            }} />
                                        ) : (
                                            <button
                                                className="primaryBtn"
                                                onClick={() => nav(`/players/${player.id}/add-team`)}
                                            >
                                                + Add&nbsp;to&nbsp;team
                                            </button>
                                        )}
                                    </>
                                )}

                                {/* --- Edit / Delete --- */}
                                {canEdit && (
                                    <div className="inline" style={{ marginTop: 24 }}>
                                        <button
                                            className="primaryBtn"
                                            onClick={() => nav(`/players/${player.id}/edit`)}
                                        >
                                            Edit
                                        </button>

                                        {canAdmin && (
                                            <button
                                                className="danger"
                                                onClick={async () => {
                                                    if (confirm('Delete this player?')) {
                                                        await deletePlayer(player.id);
                                                        nav('/players');
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
        <div className="inline">
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

function RemoveBtn({ onRemove }) {
    return (
        <button
            className="danger"
            onClick={() => { if (confirm('Remove from team?')) onRemove(); }}
        >
            Remove from team
        </button>
    );
}