import { useEffect, useState, useMemo } from 'react';
import { useNavigate } from 'react-router';

import {
    fetchUsers,
    fetchUser,
    changeUserRole,
    attachPlayer,
} from '../api/userApi';

import { fetchPlayers } from '../api/playersApi';
import { getAccessToken, logout, isAdmin } from '../api/authApi';

import '../pages/Users.css';          // (тъмният стил е по-долу)

export default function Users() {
    /* ---------- routing / auth ---------- */
    const nav   = useNavigate();
    const token = getAccessToken();

    /* ---------- data ---------- */
    const [users,   setUsers]   = useState([]);
    const [players, setPlayers] = useState([]);
    const [search,  setSearch]  = useState('');
    const [err,     setErr]     = useState(null);

    /* ---------- initial load ---------- */
    useEffect(() => {
        Promise.all([
            fetchUsers().then(r => setUsers(r.data)),
            fetchPlayers().then(r => setPlayers(r.data)),
        ]).catch(e => setErr(e.message));
    }, []);

    /* ---------- helper ---------- */
    const refreshUser = async id => {
        const { data } = await fetchUser(id);
        setUsers(u => u.map(x => (x.id === id ? data : x)));
    };

    /* ---------- filtered list ---------- */
    const view = useMemo(
        () =>
            users.filter(u =>
                `${u.username} ${u.email}`
                    .toLowerCase()
                    .includes(search.toLowerCase())
            ),
        [users, search]
    );

    /* ---------- early states ---------- */
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
                            <h2 style={{ marginTop: 0 }}>Users</h2>

                            <input
                                className="players-search"            /* същият тъмен input */
                                style={{ maxWidth: 320 }}
                                placeholder="Search by username or e-mail…"
                                value={search}
                                onChange={e => setSearch(e.target.value)}
                            />

                            <table className="usersTable">
                                <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th style={{ width: 260 }}>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                {view.map(u => (
                                    <UserRow
                                        key={u.id}
                                        user={u}
                                        allPlayers={players}
                                        onRoleChanged={() => refreshUser(u.id)}
                                        onPlayerAttached={() => refreshUser(u.id)}
                                    />
                                ))}
                                </tbody>
                            </table>
                        </section>
                    </div>
                </main>
            </div>
        </>
    );
}

/* ---------- single row ---------- */
function UserRow({ user, allPlayers, onRoleChanged, onPlayerAttached }) {
    const [editRole,   setEditRole]   = useState(false);
    const [newRole,    setNewRole]    = useState(user.role);
    const [attachOpen, setAttachOpen] = useState(false);
    const [picked,     setPicked]     = useState('');

    const freePlayers = allPlayers.filter(p => !p.userId);

    const changeRole = async role => {
        if (!confirm(`Change ${user.username} → ${role}?`)) return;
        try {
            await changeUserRole(user.id, role);
            await onRoleChanged();
            setEditRole(false);
        } catch (e) {
            alert(e.response?.data?.message ?? 'Role change failed');
        }
    };

    const attach = async () => {
        try {
            await attachPlayer(user.id, picked);
            await onPlayerAttached();
            setAttachOpen(false);
            setPicked('');
        } catch (e) {
            alert(e.response?.data?.message ?? 'Attach failed');
        }
    };

    return (
        <tr>
            <td>{user.username}</td>
            <td>{user.email}</td>
            <td>
                <span className={`badge ${user.role.toLowerCase()}`}>{user.role}</span>
            </td>

            <td className="actionsCell">
                {user.role === 'PARTICIPANT' && (
                    <button
                        className="actionBtn danger"
                        onClick={() => changeRole('VIEWER')}
                    >
                        Remove participant
                    </button>
                )}

                {['VIEWER', 'ORGANIZER'].includes(user.role) && (
                    editRole ? (
                        <>
                            <select
                                value={newRole}
                                onChange={e => setNewRole(e.target.value)}
                            >
                                {['VIEWER', 'ORGANIZER'].map(r => (
                                    <option key={r}>{r}</option>
                                ))}
                            </select>
                            <button
                                className="primaryBtn small"
                                disabled={newRole === user.role}
                                onClick={() => changeRole(newRole)}
                            >
                                Save
                            </button>
                            <button className="xBtn" onClick={() => setEditRole(false)}>
                                ×
                            </button>
                        </>
                    ) : (
                        <button
                            className="actionBtn"
                            onClick={() => { setEditRole(true); setNewRole(user.role); }}
                        >
                            Change role
                        </button>
                    )
                )}

                {user.role !== 'PARTICIPANT' && !user.playerId && (
                    attachOpen ? (
                        <>
                            <select
                                value={picked}
                                onChange={e => setPicked(e.target.value)}
                            >
                                <option value="">pick player…</option>
                                {freePlayers.map(p => (
                                    <option key={p.id} value={p.id}>
                                        {p.firstName} {p.secondName} · {p.sport}
                                    </option>
                                ))}
                            </select>
                            <button
                                className="primaryBtn small"
                                disabled={!picked}
                                onClick={attach}
                            >
                                Attach&nbsp;&amp;&nbsp;save
                            </button>
                            <button className="xBtn" onClick={() => setAttachOpen(false)}>
                                ×
                            </button>
                        </>
                    ) : (
                        <button
                            className="actionBtn"
                            onClick={() => setAttachOpen(true)}
                        >
                            Make participant
                        </button>
                    )
                )}
            </td>
        </tr>
    );
}
