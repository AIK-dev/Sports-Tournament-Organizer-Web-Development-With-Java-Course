import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const API_ROOT = 'http://localhost:8080/api';
let accessToken = null;
let currentUser = null;
let currentRoles = [];

export const setAccessToken = (token) => {
    accessToken = token;
    if (accessToken) {
        const parsed =     jwtDecode(accessToken);
        const { sub: username, userId, roles = [] } = parsed;
        currentUser  = { username, userId };
        currentRoles = Array.isArray(roles) ? roles : [roles];   // safety
    } else {
        currentUser = null;
        currentRoles = [];

    }
};
export const getAccessToken = () => {
    return accessToken;
};
export const getCurrentUser = () => currentUser;
export const isAdmin         = () => currentRoles.includes('ADMIN');
export const clearAccessToken = () => {
    accessToken = null;
};

export function getUID(){
    return currentUser.userId;
}

export const login = async (username, password) => {
    const response = await axios.post(`${API_ROOT}/v1/auth/login`, { username, password }, {
        withCredentials: true
    });
    setAccessToken(response.data.accessToken);
    return response;
};

export const register = async (userData) => {
    const response = await axios.post(`${API_ROOT}/v1/auth/register`, userData, {
        withCredentials: true
    });
    return response;
};

export const refreshToken = async () => {
    const response = await axios.post(`${API_ROOT}/v1/auth/refresh`, {}, {
        withCredentials: true
    });
    setAccessToken(response.data.accessToken);
    return response;
};

export const logout = async () => {
    await axios.post(`${API_ROOT}/v1/auth/logout`, {}, {
        withCredentials: true
    });
    clearAccessToken();
};