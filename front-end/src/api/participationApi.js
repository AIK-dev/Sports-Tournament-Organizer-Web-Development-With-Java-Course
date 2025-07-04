import api from './axios';

export const fetchParticipations      = ()      => api.get('/v1/participations');
export const fetchParticipation        = id      => api.get(`/v1/participations/${id}`);
export const createParticipation       = body    => api.post('/v1/participations', body);
export const updateParticipation       = (id, b) => api.put(`/v1/participations/${id}`, b);
export const deleteParticipation       = id      => api.delete(`/v1/participations/${id}`);
