import { Outlet, Navigate } from 'react-router';
import { getAccessToken }   from '../api/authApi';

export default function ProtectedRoute() {
    return getAccessToken()
        ? <Outlet />
        : <Navigate to="/login" replace />;
}
