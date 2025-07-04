import api from './axios';

export const fetchMatches      = ()      => api.get('/v1/matches');
export const fetchTournamentMatches = id => api.get(`v1/matches/tournaments/${id}`)
export const fetchMatch        = id      => api.get(`/v1/matches/${id}`);
export const createMatch       = body    => api.post('/v1/matches', body);
export const updateMatch       = (id, b) => api.put(`/v1/matches/${id}`, b);
export const deleteMatch       = id      => api.delete(`/v1/matches/${id}`);
