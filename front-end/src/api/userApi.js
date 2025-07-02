import api from './axios';
export const fetchUsers   = ()           => api.get('/users');
export const fetchUser    = id           => api.get(`/users/${id}`);
export const createUser   = body         => api.post('/users', body);
export const updateUser   = (id, body)   => api.put(`/users/${id}`, body);
export const deleteUser   = id           => api.delete(`/users/${id}`);
export const changeUserRole = (id, role) =>
    api.patch(`/users/${id}/role`, { role });
export const attachPlayer = (userId, playerId) =>
    api.post(`/users/${userId}/players/${playerId}`);
