/* src/api/tournamentsApi.js */
import api from './axios';

export const fetchTournaments      = ()      => api.get('/v1/tournaments');
export const fetchTournament       = id      => api.get(`/v1/tournaments/${id}`);
export const createTournament      = body    => api.post('/v1/tournaments', body);
export const updateTournament      = (id, b) => api.put(`/v1/tournaments/${id}`, b);
export const deleteTournament      = id      => api.delete(`/v1/tournaments/${id}`);
