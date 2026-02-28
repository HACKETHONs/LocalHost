import { useState } from 'react';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';

function App() {
  const [user, setUser] = useState(null);

  // If user exists, show Dashboard. Otherwise, show Login.
  return (
    <>
      {!user ? (
        <Login onLogin={(userData) => setUser(userData)} />
      ) : (
        <Dashboard user={user} />
      )}
    </>
  );
}

export default App;