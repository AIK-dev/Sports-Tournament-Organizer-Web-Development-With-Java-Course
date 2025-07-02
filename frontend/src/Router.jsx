import { BrowserRouter as Router, Routes, Route } from 'react-router';
import Home from './pages/Home';
import Profile from './pages/Profile';

export default function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </Router>
  );
}

