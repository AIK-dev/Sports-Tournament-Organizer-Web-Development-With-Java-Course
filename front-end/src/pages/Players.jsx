import { useState, useEffect, useMemo } from 'react';
import { Link } from 'react-router';
import { fetchPlayers } from '../api/playersApi';
import './Players.css';
import { isAdmin } from '../api/authApi';
import { useNavigate } from 'react-router';
export default function Players() {
    const nav = useNavigate();
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [err, setErr]         = useState(null);
    const [search, setSearch]       = useState('');
    const [sport, setSport]         = useState('all');
    const [level, setLevel]         = useState('all');
    const [gender, setGender]       = useState('all');
    const [ageFrom, setAgeFrom]     = useState('');
    const [ageTo,   setAgeTo]       = useState('');
    const [sortBy,  setSortBy]      = useState('firstName');

    useEffect(() => {
        fetchPlayers()
            .then(r => setPlayers(r.data))
            .catch(e => setErr(e.message))
            .finally(() => setLoading(false));
    }, []);

    const sports = useMemo(
        () => Array.from(new Set(players.map(p => p.sport))).sort(),
        [players],
    );
    const levels = useMemo(
        () => Array.from(new Set(players.map(p => p.level))).sort(),
        [players],
    );

    const view = useMemo(() => {
        let out = [...players];

        if (search)
            out = out.filter(p =>
                (`${p.firstName} ${p.secondName} ${p.associatedTeam}`)
                    .toLowerCase()
                    .includes(search.toLowerCase())
            );

        if (sport  !== 'all') out = out.filter(p => p.sport  === sport);
        if (level  !== 'all') out = out.filter(p => p.level  === level);
        if (gender !== 'all') out = out.filter(p => p.gender === gender);
        if (ageFrom) out = out.filter(p => p.age >= Number(ageFrom));
        if (ageTo)   out = out.filter(p => p.age <= Number(ageTo));

        out.sort((a, b) => {
            if (sortBy === 'age') return a.age - b.age;
            if (sortBy === 'sport') return a.sport.localeCompare(b.sport);
            return a.firstName.localeCompare(b.firstName);
        });

        return out;
    }, [players, search, sport, level, gender, ageFrom, ageTo, sortBy]);

    if (loading) return <p style={{padding:24}}>Loading…</p>;
    if (err)     return <p style={{padding:24,color:'red'}}>Error: {err}</p>;

    return (
        <div className="playersPage">

            <div className="toolbar">
                <input
                    placeholder="Search…"
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                />

                <select value={sport} onChange={e=>setSport(e.target.value)}>
                    <option value="all">All sports</option>
                    {sports.map(s => <option key={s}>{s}</option>)}
                </select>

                <select value={level} onChange={e=>setLevel(e.target.value)}>
                    <option value="all">All levels</option>
                    {levels.map(l => <option key={l}>{l}</option>)}
                </select>

                <select value={gender} onChange={e=>setGender(e.target.value)}>
                    <option value="all">Any gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>

                <input
                    type="number" min="0" placeholder="Age from"
                    value={ageFrom} onChange={e=>setAgeFrom(e.target.value)}
                />
                <input
                    type="number" min="0" placeholder="Age to"
                    value={ageTo} onChange={e=>setAgeTo(e.target.value)}
                />

                <select value={sortBy} onChange={e=>setSortBy(e.target.value)}>
                    <option value="firstName">Sort: Name</option>
                    <option value="age">Age</option>
                    <option value="sport">Sport</option>
                </select>
            </div>

            {view.length
                ? (
                    <div className="gridWrap">
                        {view.map(p => (
                            <Link key={p.id} to={`/players/${p.id}`} className="card linkCard">
                                <h3>{p.firstName} {p.secondName}</h3>
                                <p>{p.age} y · {p.gender}</p>
                                <p>{p.level} · {p.sport}</p>
                                <small>{p.associatedTeam}</small>
                            </Link>
                        ))}
                    </div>
                )
                : <p style={{padding:24}}>Nothing matches your criteria.</p>}
            {isAdmin() && (
                <button
                className="primaryBtn"
                style={{marginLeft:'auto'}}
                onClick={() => nav('/players/new')}
                >
                + Add player
                </button>
                )}
        </div>
    );
}
