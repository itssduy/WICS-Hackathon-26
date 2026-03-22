import { Outlet, useLocation } from 'react-router';
import Navbar from './components/Navbar';

export default function AppShell() {
  const location = useLocation();

  // Pages where the navbar should be hidden (auth pages handle their own layout)
  const hideNavbar = ['/login', '/signup', '/onboarding'].includes(location.pathname);

  return (
    <>
      {!hideNavbar && <Navbar />}
      <main>
        <Outlet />
      </main>
    </>
  );
}
