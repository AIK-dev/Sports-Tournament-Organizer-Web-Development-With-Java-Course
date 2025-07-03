import api from './axios';

export const fetchTeams   = ()     => api.get('/v1/teams');
export const fetchTeam    = id     => api.get(`/v1/teams/${id}`);
export const createTeam   = body   => api.post('/v1/teams', body);
export const updateTeam   = (id,b) => api.put(`/v1/teams/${id}`, b);
export const deleteTeam   = id     => api.delete(`/v1/teams/${id}`);
export const changeTeamOwner = (teamId, userId) =>
    api.patch(`/teams/${teamId}/owner/${userId}`);