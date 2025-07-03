import { NavLink, useNavigate } from 'react-router';
import { logout, isAdmin } from '../api/authApi';

import './Header.css';

const link = ({ isActive }) => 'navItem' + (isActive ? ' active' : '');

export default function Header() {
    const nav = useNavigate();
    return (
        <header className="appHeader">
            <NavLink to="/players" className={link}>Players</NavLink>
            <NavLink to="/teams"   className={link}>Teams</NavLink>
            {isAdmin() && (
                <NavLink to="/users" className={link}>Users</NavLink>
            )}
            <button
                className="logoutBtn"
                onClick={async () => { await logout(); nav('/login'); }}
            >
                Logout
            </button>
        </header>
    );
}
