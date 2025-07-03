/* src/api/authApi.js */
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const API_ROOT = 'http://localhost:8080/api';

let accessToken   = null;
let currentUser   = null;            // { userId, username }
let currentRoles  = [];              // ['ADMIN', 'PARTICIPANT', …]

export const setAccessToken = token => {
    accessToken = token;

    if (!token) {
        currentUser  = null;
        currentRoles = [];
        return;
    }

    const { sub: username, userId, roles = [] } = jwtDecode(token);
    currentUser  = { username, userId };
    currentRoles = Array.isArray(roles) ? roles : [roles];
};

export const getAccessToken  = ()   => accessToken;
export const clearAccessToken = ()  => (accessToken = null);

export const getCurrentUser = () => currentUser;
export const isAdmin        = () => currentRoles.includes('ADMIN');
export const hasRole        = role => currentRoles.includes(role);

export const getUID = () => currentUser?.userId;

/* ---------- auth endpoints ---------- */
export const login = async (username, password) => {
    const { data } = await axios.post(`${API_ROOT}/v1/auth/login`, { username, password }, { withCredentials:true });
    setAccessToken(data.accessToken);
};

export const register = userData =>
    axios.post(`${API_ROOT}/v1/auth/register`, userData, { withCredentials:true });

export const refreshToken = async () => {
    const { data } = await axios.post(`${API_ROOT}/v1/auth/refresh`, {}, { withCredentials:true });
    setAccessToken(data.accessToken);
};

export const logout = async () => {
    await axios.post(`${API_ROOT}/v1/auth/logout`, {}, { withCredentials:true });
    clearAccessToken();
};