/*  src/App.jsx  */
import {
    createBrowserRouter,
    RouterProvider,
    redirect,
}                         from 'react-router';
import Home               from './pages/Home';
import Login              from './pages/Login';
import Register           from './pages/Register';

import Players            from './pages/Players';
import PlayerDetail       from './pages/PlayerDetail';
import PlayerForm         from './pages/PlayerForm';
import AssignTeam         from './pages/AssignTeam';

import Teams              from './pages/Teams';
import TeamDetail from './pages/TeamDetail.jsx';

import Users              from './pages/Users';

import NotFound           from './pages/NotFound';

import ProtectedRoute     from './layouts/ProtectedRoutes';   // само проверява токена
import RequireAdmin       from './components/RequireAdmin';   // за ADMIN / ORG-owner

import { getAccessToken } from './api/authApi';
import TeamForm from "./pages/TeamForm.jsx";

const redirectIfAuth = () => { if (getAccessToken()) throw redirect('/'); };

const router = createBrowserRouter([
    /* ───── Публични страници ───── */
    { path: '/',          element: <Home /> },                      // landing
    { path: '/login',     element: <Login />,    loader: redirectIfAuth },
    { path: '/register',  element: <Register />, loader: redirectIfAuth },

    {
        element: <ProtectedRoute />,
        children: [
            { path: '/players',          element: <Players /> },
            { path: '/players/:id',      element: <PlayerDetail /> },
            { path: '/players/:id/edit', element: <PlayerForm mode="edit" /> },

            { path: 'teams',      element: <Teams /> },
            { path: 'teams/:id',  element: <TeamDetail /> },

            {
                element: <RequireAdmin />,
                children: [
                    { path: '/players/new',            element: <PlayerForm mode="create" /> },
                    { path: '/players/:id/add-team',   element: <AssignTeam /> },
                    { path: '/users',                  element: <Users /> },
                    { path: 'teams/new',         element: <TeamForm mode="create" /> },
                    { path: 'teams/:id/edit',    element: <TeamForm mode="edit"   /> },
                ],
            },
        ],
    },

    /* ───── 404 ───── */
    { path: '*', element: <NotFound /> },
]);

export default function App() {
    return <RouterProvider router={router} />;
}
