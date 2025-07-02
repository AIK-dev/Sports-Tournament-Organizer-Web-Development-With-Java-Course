/* src/pages/PlayerDetail.jsx */
import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router';
import {
    fetchPlayer, deletePlayer,
    changeOwner, removeFromTeam
} from '../api/playersApi';
import { fetchUsers } from '../api/userApi';
import { isAdmin }   from '../api/authApi';
import './PlayerDetail.css';

export default function PlayerDetail() {
    const { id } = useParams();
    const nav    = useNavigate();

    const [player, setPlayer] = useState(null);
    const [users,  setUsers]  = useState([]);
    const [newOwner, setNewOwner] = useState('');
    const [err, setErr]   = useState(null);
    const [loading, setLoad] = useState(true);

    /* ── fetch ─────────────────────────────── */
    useEffect(() => {
        Promise.all([
            fetchPlayer(id).then(r => setPlayer(r.data)),
            isAdmin()      && fetchUsers().then(r => setUsers(r.data))
        ])
            .catch(e => setErr(e.message))
            .finally(() => setLoad(false));
    }, [id]);

    if (loading) return <p className="pad">Loading…</p>;
    if (err)     return <p className="pad err">Error: {err}</p>;
    if (!player) return <p className="pad">No data.</p>;

    /* ⇢ играч НЕ е в отбор <=> няма associatedTeam или == 'SINGLE PLAYER' */
    const hasTeam = !!player.associatedTeam && player.associatedTeam !== 'SINGLE PLAYER';

    return (
        <div className="pad wrapper">
            <h2>{player.firstName} {player.secondName}</h2>

            <p><b>Age:</b> {player.age}</p>
            <p><b>Gender:</b> {player.gender}</p>
            <p><b>Sport / Level:</b> {player.sport} • {player.level}</p>
            <p><b>Team:</b> {hasTeam ? player.associatedTeam : '—'}</p>
            <p><b>Owner:</b> {player.ownerUsername || '—'}</p>

            {isAdmin() && (
                <>
                    {/* ── change owner ── */}
                    <OwnerBox
                        users={users}
                        value={newOwner}
                        onChange={setNewOwner}
                        onSave={async () => {
                            await changeOwner(player.id, newOwner);
                            location.reload();
                        }}
                    />

                    {/* ── Add OR Remove team ── */}
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

                    {/* ── Edit / Delete ── */}
                    <AdminCrudButtons
                        id={player.id}
                        onEdit={() => nav(`/players/${player.id}/edit`)}
                        onDeleteSuccess={() => nav('/players')}
                    />
                </>
            )}
        </div>
    );
}

/* ───────── helpers ───────── */

function OwnerBox({ users, value, onChange, onSave }) {
    return (
        <div className="inline">
            <select value={value} onChange={e => onChange(e.target.value)}>
                <option value="">Change owner…</option>
                {users.map(u => (
                    <option key={u.id} value={u.id}>{u.username}</option>
                ))}
            </select>
            <button className="primaryBtn" disabled={!value} onClick={onSave}>
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

function AdminCrudButtons({ id, onEdit, onDeleteSuccess }) {
    return (
        <div className="inline" style={{ marginTop: 24 }}>
            <button className="primaryBtn" onClick={onEdit}>Edit</button>
            <button
                className="danger"
                onClick={async () => {
                    if (confirm('Delete this player?')) {
                        await deletePlayer(id);
                        onDeleteSuccess();
                    }
                }}
            >
                Delete
            </button>
        </div>
    );
}
