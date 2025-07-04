import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';

import {
    fetchPlayer,
    deletePlayer,
    changeOwner,
    removeFromTeam,
} from '../api/playersApi';
import { fetchUsers, fetchUser } from '../api/userApi';

import {
    isAdmin,
    hasRole,
    getCurrentUser,
    getAccessToken,
    logout,
} from '../api/authApi';

export default function PlayerDetail() {
    const { id }  = useParams();
    const nav     = useNavigate();
    const token   = getAccessToken();

    const [player,   setPlayer]   = useState(null);
    const [users,    setUsers]    = useState([]);
    const [owner,    setOwner]    = useState(null);
    const [newOwner, setNew]      = useState('');
    const [err,      setErr]      = useState(null);
    const [loading,  setLoading]  = useState(true);

    useEffect(() => {
        (async () => {
            try {
                const { data: p } = await fetchPlayer(id);
                setPlayer(p);

                if (p.ownerId) {
                    const { data: o } = await fetchUser(p.ownerId);
                    setOwner(o);
                }

                if (isAdmin()) {
                    const { data: all } = await fetchUsers();
                    setUsers(all.filter(u => u.role === 'ORGANIZER'));
                }
            } catch (e) {
                setErr(e.message);
            } finally {
                setLoading(false);
            }
        })();
    }, [id]);

    if (loading) return <p className="pad">Loading…</p>;
    if (err)     return <p className="pad err">Error: {err}</p>;
    if (!player) return <p className="pad">No data.</p>;

    const me       = getCurrentUser();
    const myId     = me?.userId;

    const iAmOwner = player.ownerId === myId;
    const iAmThis  = player.userId  === myId;

    const canEdit =
        isAdmin()
        || (hasRole('ORGANIZER')   && iAmOwner)
        || (hasRole('PARTICIPANT') && iAmThis);

    const canAdmin =
        isAdmin()

    const hasTeam =
        player.teamId != null
        || (player.associatedTeam && player.associatedTeam !== 'SINGLE PLAYER');

    return (
        <>
            {/* top-bar */}
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

            {/* page */}
            <div className="home-page-layout">
                <main className="main-content-area">
                    <div className="content-grid">
                        <section className="center-column">
                            <div className="pad wrapper">
                                <h2>{player.firstName} {player.secondName}</h2>

                                <p><b>Age:</b> {player.age}</p>
                                <p><b>Gender:</b> {player.gender}</p>
                                <p><b>Sport / Level:</b> {player.sport} • {player.level}</p>
                                <p><b>Team:</b> {hasTeam ? player.associatedTeam : '—'}</p>
                                <p>
                                    <b>Owner:</b>{' '}
                                    {owner
                                        ? owner.username
                                        : player.ownerId || '—'}    {}
                                </p>

                                {canAdmin && (
                                    <>
                                        <OwnerBox
                                            users={users}
                                            value={newOwner}
                                            onChange={setNew}
                                            onSave={async () => {
                                                await changeOwner(player.id, newOwner);
                                                location.reload();          // бърз refresh
                                            }}
                                        />

                                        {hasTeam ? (
                                            <RemoveBtn onRemove={async () => {
                                                await removeFromTeam(player.id);
                                                location.reload();
                                            }}/>
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
