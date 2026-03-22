import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router';
import { AuthProvider } from './context/AuthContext';
import './index.css';

import AppShell from './AppShell';
import Landing from './pages/Landing';
import Login from './pages/login';
import Signup from './pages/signup';
import Onboarding from './pages/Onboarding';
import ReflectDashboard from './pages/ReflectDashboard';
import ReflectSession from './pages/ReflectSession';
import Calendar from './pages/Calendar';
import History from './pages/History';
import Profile from './pages/Profile';
import Billing from './pages/Billing';
import Privacy from './pages/Privacy';
import Terms from './pages/Terms';
import Safety from './pages/Safety';
import AdminCategories from './pages/AdminCategories';
import AdminPrompts from './pages/AdminPrompts';
import ProtectedRoute from './components/ProtectedRoute';

const router = createBrowserRouter([
  {
    element: <AppShell />,
    children: [
      // Public routes
      { path: '/', element: <Landing /> },
      { path: '/login', element: <Login /> },
      { path: '/signup', element: <Signup /> },
      { path: '/privacy', element: <Privacy /> },
      { path: '/terms', element: <Terms /> },
      { path: '/safety', element: <Safety /> },

      // Authenticated routes
      {
        path: '/onboarding',
        element: <ProtectedRoute><Onboarding /></ProtectedRoute>,
      },
      {
        path: '/reflect',
        element: <ProtectedRoute><ReflectDashboard /></ProtectedRoute>,
      },
      {
        path: '/reflect/:categoryName',
        element: <ProtectedRoute><ReflectSession /></ProtectedRoute>,
      },
      {
        path: '/calendar',
        element: <ProtectedRoute><Calendar /></ProtectedRoute>,
      },
      {
        path: '/history',
        element: <ProtectedRoute><History /></ProtectedRoute>,
      },
      {
        path: '/profile',
        element: <ProtectedRoute><Profile /></ProtectedRoute>,
      },
      {
        path: '/billing',
        element: <ProtectedRoute><Billing /></ProtectedRoute>,
      },

      // Admin routes
      {
        path: '/admin/categories',
        element: <ProtectedRoute requireAdmin><AdminCategories /></ProtectedRoute>,
      },
      {
        path: '/admin/prompts',
        element: <ProtectedRoute requireAdmin><AdminPrompts /></ProtectedRoute>,
      },
    ],
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  </StrictMode>,
);
