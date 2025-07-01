import { useEffect, useState, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router';
import { fetchTeams }  from '../api/teamsApi';
import { addToTeam }   from '../api/playersApi';
import './AssignTeam.css';

export default function AssignTeam(){
    const { id } = useParams();           // id на играча
    const nav    = useNavigate();

    const [teams,setTeams] = useState([]);
    const [q, setQ] = useState('');
    const [err,setErr]=useState(null);

    useEffect(()=>{
        fetchTeams()
            .then(r=>setTeams(r.data))
            .catch(e=>setErr(e.message));
    },[]);

    const view = useMemo(()=>{
        return teams.filter(t =>
            t.name.toLowerCase().includes(q.toLowerCase())
        );
    },[teams,q]);

    if(err) return <p className="pad err">Error: {err}</p>;

    return (
        <div className="pad teamSelect">
            <h2>Choose team</h2>
            <input
                placeholder="Search…"
                value={q}
                onChange={e=>setQ(e.target.value)}
            />

            <ul>
                {view.map(t=>(
                    <li key={t.id}>
                        {t.name}
                        <button onClick={async ()=>{
                            await addToTeam(id,t.id);
                            nav(`/players/${id}`);     // обратно към детайла
                        }}>Select</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
