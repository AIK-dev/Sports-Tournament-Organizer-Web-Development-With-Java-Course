// src/pages/Register.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useForm } from 'react-hook-form';
import { register as apiRegister } from '../api/authApi';
import '../Auth.css';                               // вече стиловете са тук

export default function Register() {
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm({
        defaultValues: { email: '', username: '', password: '' },
    });

    const onSubmit = async data => {
        try {
            await apiRegister(data);
            navigate('/login');                          // към login след успех
        } catch {
            setError('Registration failed. Please try again.');
        }
    };

    return (
        <div className="registerPage">
            <main>
                <section>
                    <h1>Register</h1>

                    <form onSubmit={handleSubmit(onSubmit)}>
                        {/* ---------------- EMAIL ---------------- */}
                        <div>
                            <label>Email: </label>
                            <input
                                type="email"
                                {...register('email', {
                                    required: 'Email is required',
                                    pattern: {
                                        value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                        message: 'Invalid email address',
                                    },
                                })}
                            />
                            {errors.email && (
                                <p className="validationError">{errors.email.message}</p>
                            )}
                        </div>

                        {/* -------------- USERNAME --------------- */}
                        <div>
                            <label>Username: </label>
                            <input
                                type="text"
                                {...register('username', {
                                    required: 'Username is required',
                                    minLength: { value: 3, message: 'Min 3 characters' },
                                    maxLength: { value: 20, message: 'Max 20 characters' },
                                })}
                            />
                            {errors.username && (
                                <p className="validationError">{errors.username.message}</p>
                            )}
                        </div>

                        {/* -------------- PASSWORD --------------- */}
                        <div>
                            <label>Password: </label>
                            <input
                                type="password"
                                {...register('password', {
                                    required: 'Password is required',
                                    minLength: { value: 6, message: 'Min 6 characters' },
                                })}
                            />
                            {errors.password && (
                                <p className="validationError">{errors.password.message}</p>
                            )}
                        </div>

                        <button type="submit" disabled={isSubmitting}>
                            {isSubmitting ? 'Registering…' : 'Register'}
                        </button>

                        {error && <p className="authError">{error}</p>}
                    </form>

                    <p>
                        Already have an account?{' '}
                        <a
                            href="/login"
                            onClick={e => {
                                e.preventDefault();
                                navigate('/login');
                            }}
                        >
                            Log in
                        </a>
                    </p>
                </section>
            </main>
        </div>
    );
}
