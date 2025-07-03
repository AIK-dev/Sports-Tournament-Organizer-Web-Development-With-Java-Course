import axios from 'axios';
import {
    getAccessToken,
    refreshToken,
    clearAccessToken,
} from './authApi';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true,
});

api.interceptors.request.use(cfg => {
    const t = getAccessToken();
    if (t) {
        cfg.headers.Authorization = `Bearer ${t}`;
        cfg.headers['Content-Type'] = 'application/json';
    }
    return cfg;
});

api.interceptors.response.use(
    r => r,
    async err => {
        const orig = err.config;
        if (err.response?.status === 401 && !orig._retry) {
            orig._retry = true;
            try {
                await refreshToken();
                return api(orig);
            } catch {
                clearAccessToken();
                window.location.href = '/login';
            }
        }
        return Promise.reject(err);
    },
);

export default api;
