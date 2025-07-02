import { Navigate, Outlet } from 'react-router';
import { isAdmin } from '../api/authApi';

export default function RequireAdmin() {
    return isAdmin()
        ? <Outlet />
        : <Navigate to="/" replace />;
}
