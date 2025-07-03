import api from './axios';

export const fetchVenues      = ()      => api.get('/v1/venues');
export const fetchVenue       = id      => api.get(`/v1/venues/${id}`);
export const createVenue      = body    => api.post('/v1/venues', body);
export const updateVenue      = (id,b)  => api.put(`/v1/venues/${id}`, b);
export const deleteVenue      = id      => api.delete(`/v1/venues/${id}`);

/* sport management helpers (ADMIN only) */
export const addVenueSport    = (id,s)  => api.post   (`/v1/venues/${id}/sports/${s}`);
export const removeVenueSport = (id,s)  => api.delete(`/v1/venues/${id}/sports/${s}`);
