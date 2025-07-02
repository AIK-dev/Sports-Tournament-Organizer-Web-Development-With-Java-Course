// --- MOCK DATA ---
export const SPORTS_DATA = [
    { id: 1, name: 'Football' },
    { id: 2, name: 'Tennis' },
    { id: 3, name: 'Basketball' },
    { id: 4, name: 'Volleyball' }
];

export const TOURNAMENTS_DATA = [
    { id: 101, name: 'Champions League', sportId: 1, startDate: '2025-06-15', endDate: '2025-07-15' },
    { id: 102, name: 'Premier League', sportId: 1, startDate: '2025-05-01', endDate: '2025-08-30' },
    { id: 201, name: 'Wimbledon', sportId: 2, startDate: '2025-06-23', endDate: '2025-07-10' },
    { id: 301, name: 'NBA Finals', sportId: 3, startDate: '2025-06-01', endDate: '2025-06-20' },
    { id: 401, name: 'Nations League', sportId: 4, startDate: '2025-06-28', endDate: '2025-07-20' },
];

// Helper to get today's date in YYYY-MM-DD format for filtering
export const getTodayDateString = () => {
    const demoDate = new Date('2025-07-01T12:00:00Z');
    const year = demoDate.getFullYear();
    const month = String(demoDate.getMonth() + 1).padStart(2, '0');
    const day = String(demoDate.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

export const TODAY_STRING = getTodayDateString();

export const MATCHES_DATA = [
    // Football - Champions League (Today)
    { id: 1001, tournamentId: 101, matchDate: '2025-07-01T19:00:00Z', firstCompetitor: 'Real Madrid', secondCompetitor: 'FC Barcelona', status: 'Scheduled' },
    { id: 1002, tournamentId: 101, matchDate: '2025-07-01T21:00:00Z', firstCompetitor: 'Bayern Munich', secondCompetitor: 'Liverpool', status: 'Scheduled' },
    { id: 1003, tournamentId: 101, matchDate: '2025-07-02T19:00:00Z', firstCompetitor: 'Man City', secondCompetitor: 'PSG', status: 'Scheduled' },
    // Football - Premier League (Not today)
    { id: 1004, tournamentId: 102, matchDate: '2025-07-05T14:00:00Z', firstCompetitor: 'Arsenal', secondCompetitor: 'Chelsea', status: 'Scheduled' },
    // Tennis - Wimbledon (Today)
    { id: 2001, tournamentId: 201, matchDate: '2025-07-01T12:00:00Z', firstCompetitor: 'N. Djokovic', secondCompetitor: 'C. Alcaraz', status: 'Scheduled' },
    { id: 2002, tournamentId: 201, matchDate: '2025-07-01T15:00:00Z', firstCompetitor: 'I. Swiatek', secondCompetitor: 'A. Sabalenka', status: 'Scheduled' },
    { id: 2003, tournamentId: 201, matchDate: '2025-06-30T12:00:00Z', firstCompetitor: 'R. Nadal', secondCompetitor: 'D. Medvedev', status: 'Finished' },
    // Basketball - NBA Finals (Finished)
    { id: 3001, tournamentId: 301, matchDate: '2025-06-18T01:00:00Z', firstCompetitor: 'Boston Celtics', secondCompetitor: 'Dallas Mavericks', status: 'Finished' },
];