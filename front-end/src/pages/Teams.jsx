import { useEffect, useState } from 'react';
import { fetchTeams } from '../api/teamsApi';

export default function Teams() {
    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);

    useEffect(() => {
        fetchTeams()
            .then(r => setTeams(r.data))
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p style={{padding:24}}>Loading…</p>;
    if (err)     return <p style={{padding:24,color:'red'}}>Error: {err}</p>;

    return (
        <div style={{padding:24}}>
            {teams.map(t => (
                <div key={t.id} className="card" style={{marginBottom:16}}>
                    <h3>{t.name}</h3>
                    <p>{t.sport} · {t.ageGroup} · {t.type}</p>
                    <b>Players:</b>
                    <ul>
                        {t.players.map(p => (
                            <li key={p.id}>{p.firstName} {p.secondName}</li>
                        ))}
                    </ul>
                </div>
            ))}
        </div>
    );
}
