import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useForm } from 'react-hook-form';
import {getAccessToken, login} from '../api/authApi';
import '../Auth.css';
import {jwtDecode} from "jwt-decode";

export default function Login() {
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm();

    const onSubmit = async data => {
        try {
            await login(data.username, data.password);
            navigate('/');
            const raw = getAccessToken();          // взимаме го от authApi
            console.log('TOKEN PAYLOAD:', jwtDecode(raw));
        } catch {
            setError('Invalid username or password.');
        }
    };

    return (
        <div className="loginPage">
            <main>
                <section>
                    <h1>Log in</h1>

                    <form onSubmit={handleSubmit(onSubmit)}>
                        <div>
                            <label>Username: </label>
                            <input
                                {...register('username', { required: 'Username is required' })}
                            />
                            {errors.username && (
                                <p className="validationError">{errors.username.message}</p>
                            )}
                        </div>

                        <div>
                            <label>Password: </label>
                            <input
                                type="password"
                                {...register('password', { required: 'Password is required' })}
                            />
                            {errors.password && (
                                <p className="validationError">{errors.password.message}</p>
                            )}
                        </div>

                        <button type="submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Logging in…' : 'Log in'}
                        </button>

                        {error && <p className="authError">{error}</p>}
                    </form>

                    <p>
                        Don’t have an account?{' '}
                        <a
                            href="/register"
                            onClick={e => {
                                e.preventDefault();
                                navigate('/register');
                            }}
                        >
                            Register
                        </a>
                    </p>
                </section>
            </main>
        </div>
    );
}
