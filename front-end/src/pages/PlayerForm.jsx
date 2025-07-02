import { useEffect } from 'react';
import { useForm }  from 'react-hook-form';
import { useNavigate, useParams } from 'react-router';
import { createPlayer, updatePlayer, fetchPlayer } from '../api/playersApi';
import '../forms.css';
const SPORTS = [
    'basketball','baseball','volleyball','football','tennis','table_tennis',
    'handball','golf','ice_hockey','wrestling','archery','cycling','swimming',
    'skiing','running','marathon','pole_vault','weightlifting','powerlifting',
    'surfing','chess','lacrosse','squash','rugby','american_football',
    'boxing','biathlon'
];

const LEVELS = ['U12','U14','U16','U18','U20','AMATEUR','PROFESSIONAL'];

export default function PlayerForm({ mode }) {
    const isEdit = mode === 'edit';
    const { id }  = useParams();
    const nav     = useNavigate();

    const { register, handleSubmit, reset } = useForm({
        defaultValues:{
            firstName:'', secondName:'', age:'', gender:'MALE',
            sport:'', level:''
        }
    });

    useEffect(() => {
        if (isEdit) {
            fetchPlayer(id).then(r => reset(r.data));
        }
    }, [isEdit, id, reset]);

    const onSubmit = async data => {
        if (isEdit) await updatePlayer(id, data);
        else        await createPlayer(data);

        nav('/players');
    };

    return (
        <div className="centerPage">
            <form className="playerForm" onSubmit={handleSubmit(onSubmit)}>
                <h2>{isEdit ? 'Edit player' : 'New player'}</h2>
                <div className="formRow">
                    <input
                        className="textInput"
                        placeholder="First name"
                        {...register('firstName',{ required:true })}
                    />
                    <input
                        className="textInput"
                        placeholder="Second name"
                        {...register('secondName',{ required:true })}
                    />
                </div>
                <div className="formRow">
                    <input
                        className="textInput"
                        type="number"
                        min="0"
                        placeholder="Age"
                        {...register('age',{ valueAsNumber:true, required:true })}
                    />

                    <select className="selectInput" {...register('gender')}>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                    </select>
                    <input
                        className="textInput"
                        placeholder="Sport"
                        list="sports"
                        {...register('sport',{ required:true })}
                    />
                    <datalist id="sports">
                        {SPORTS.map(s => <option key={s} value={s} />)}
                    </datalist>
                </div>
                <div className="formRow">
                    <input
                        className="textInput"
                        placeholder="Level / Age Group"
                        list="levels"
                        {...register('level',{ required:true })}
                    />
                    <datalist id="levels">
                        {LEVELS.map(l => <option key={l} value={l} />)}
                    </datalist>
                </div>

                <button className="primaryBtn" type="submit">Save</button>
            </form>
        </div>
    );
}
