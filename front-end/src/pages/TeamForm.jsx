import { useEffect } from 'react';
import { useForm }  from 'react-hook-form';
import { useNavigate, useParams } from 'react-router';

import {
    createTeam,
    updateTeam,
    fetchTeam,
} from '../api/teamsApi';

import { getAccessToken, logout } from '../api/authApi';
import '../forms.css';

const SPORTS = [
    'basketball','baseball','volleyball','football','tennis','table_tennis',
    'handball','golf','ice_hockey','wrestling','archery','cycling','swimming',
    'skiing','running','marathon','pole_vault','weightlifting','powerlifting',
    'surfing','chess','lacrosse','squash','rugby','american_football',
    'boxing','biathlon'
];

const AGE_GROUPS = ['U12','U14','U16','U18','U20','AMATEUR','PROFESSIONAL'];
const TEAM_TYPES = ['MALE', 'FEMALE', 'MIXED'];

export default function TeamForm({ mode }) {
    const isEdit = mode === 'edit';
    const { id }  = useParams();
    const nav     = useNavigate();
    const token   = getAccessToken();

    const { register, handleSubmit, reset } = useForm({
        defaultValues: {
            name: '', sport: '', ageGroup: '', teamType: 'MIXED',
        }
    });

    useEffect(() => {
        if (isEdit) {
            fetchTeam(id).then(r => reset(r.data));
        }
    }, [isEdit, id, reset]);

    const onSubmit = async data => {
        if (isEdit) await updateTeam(id, data);
        else        await createTeam(data);

        nav('/teams');
    };

    return (
        <>
            <header className="home-topbar">
                {!token ? (
                    <button className="topBtn" onClick={() => nav('/login')}>Log&nbsp;in</button>
                ) : (
                    <>
                        <button className="topBtn" onClick={() => {nav('/');}}>Home</button>
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
                            <form className="playerForm" onSubmit={handleSubmit(onSubmit)}>
                                <h2>{isEdit ? 'Edit team' : 'New team'}</h2>

                                <div className="formRow">
                                    <input
                                        className="textInput"
                                        placeholder="Team name"
                                        {...register('name', { required: true })}
                                    />
                                    <input
                                        className="textInput"
                                        placeholder="Sport"
                                        list="sports"
                                        {...register('sport', { required: true })}
                                    />
                                    <datalist id="sports">
                                        {SPORTS.map(s => <option key={s} value={s} />)}
                                    </datalist>
                                </div>

                                <div className="formRow">
                                    <input
                                        className="textInput"
                                        placeholder="Age group"
                                        list="age-groups"
                                        {...register('ageGroup', { required: true })}
                                    />
                                    <datalist id="age-groups">
                                        {AGE_GROUPS.map(g => <option key={g} value={g} />)}
                                    </datalist>

                                    <select className="selectInput" {...register('teamType')}>
                                        {TEAM_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                                    </select>
                                </div>

                                <button className="primaryBtn" type="submit">Save</button>
                            </form>
                        </section>
                    </div>
                </main>
            </div>
        </>
    );
}
