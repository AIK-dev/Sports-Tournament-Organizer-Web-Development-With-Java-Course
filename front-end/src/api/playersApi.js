import api from './axios';

export const fetchPlayers     = ()        => api.get('/v1/players');
export const fetchPlayer      = id        => api.get(`/v1/players/${id}`);
export const createPlayer     = body      => api.post('/v1/players', body);
export const updatePlayer     = (id,b)    => api.put(`/v1/players/${id}`, b);
export const deletePlayer     = id        => api.delete(`/v1/players/${id}`);
export const rosterByTeam     = teamId    => api.get(`/v1/players/team/${teamId}`);
export const changeOwner  = (playerId, userId) => api.patch(`/v1/players/${playerId}/owner/${userId}`);
export const addToTeam    = (playerId, teamId) => api.post (`/v1/players/${playerId}/team/${teamId}`);
export const removeFromTeam = playerId        => api.delete(`/v1/players/${playerId}/team`);