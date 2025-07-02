import {
    createBrowserRouter,
    RouterProvider,
    redirect,
} from 'react-router';

import Login          from './pages/Login';
import Register       from './pages/Register';
import Dashboard      from './pages/Dashboard';
import Players        from './pages/Players';
import PlayerDetail from './pages/PlayerDetail';
import Teams          from './pages/Teams';
import NotFound       from './pages/NotFound';
import RequireAdmin   from './components/RequireAdmin';
import PlayerForm     from './pages/PlayerForm';
import AssignTeam from './pages/AssignTeam.jsx'

import DashboardLayout from './components/DashboardLayout';
import ProtectedRoute  from './layouts/ProtectedRoutes';
import { getAccessToken } from './api/authApi';

const redirectIfAuth = () => { if (getAccessToken()) throw redirect('/'); };

const router = createBrowserRouter([
    { path:'/login',    element:<Login />,    loader:redirectIfAuth },
    { path:'/register', element:<Register />, loader:redirectIfAuth },

    {
        element:<ProtectedRoute />,
        children:[
            {
                element:<DashboardLayout />,
                children:[
                    { index:true,  element:<Dashboard /> },
                    { path:'players', element:<Players /> },
                    { path:'players/:id', element:<PlayerDetail /> },
                    { path:'teams',   element:<Teams   /> },
                    {
                        element: <RequireAdmin />,
                        children: [
                            { path:'players/:id/add-team', element:<AssignTeam/> },
                            { path: 'players/new',        element: <PlayerForm mode="create" /> },
                            { path: 'players/:id/edit',   element: <PlayerForm mode="edit"   /> }
                        ]
                    }
                ],
            },
        ],
    },

    { path:'*', element:<NotFound /> },
]);

export default function App(){ return <RouterProvider router={router} />; }
