/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useMemo, useState } from 'react';

const AuthContext = createContext(null);

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const TOKEN_STORAGE_KEY = 'ireflect_token';

function decodeTokenUser(token) {
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return {
      userId: payload.sub || payload.userId,
      username: payload.username,
      plan: payload.plan || 'FREE',
      role: payload.role || 'USER',
    };
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const initialToken = localStorage.getItem(TOKEN_STORAGE_KEY);
  const initialDecodedUser = decodeTokenUser(initialToken);
  if (initialToken && !initialDecodedUser) {
    localStorage.removeItem(TOKEN_STORAGE_KEY);
  }

  const [token, setToken] = useState(initialDecodedUser ? initialToken : null);
  const [authUser, setAuthUser] = useState(initialDecodedUser);
  const loading = false;

  const user = useMemo(() => {
    if (!token) return null;
    const decoded = decodeTokenUser(token);
    if (!decoded) return authUser;
    return { ...decoded, ...authUser };
  }, [token, authUser]);

  const persistAuthState = (nextToken, userOverride = null) => {
    if (!nextToken) {
      localStorage.removeItem(TOKEN_STORAGE_KEY);
      setToken(null);
      setAuthUser(null);
      return;
    }

    const decoded = decodeTokenUser(nextToken);
    localStorage.setItem(TOKEN_STORAGE_KEY, nextToken);
    setToken(nextToken);
    setAuthUser(decoded ? { ...decoded, ...userOverride } : userOverride);
  };

  const login = async (usernameOrEmail, password) => {
    let res;
    try {
      res = await fetch(`${API_BASE}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usernameOrEmail, password }),
      });
    } catch {
      throw new Error('Cannot reach backend. Ensure backend (:8080) and MongoDB (:27017) are running.');
    }
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Login failed');
    }
    const data = await res.json();
    persistAuthState(data.token, {
      userId: data.userId,
      username: data.username,
      plan: data.plan,
      subscriptionStatus: data.subscriptionStatus,
    });
    return data;
  };

  const signup = async (username, password, confirmPassword) => {
    let res;
    try {
      res = await fetch(`${API_BASE}/auth/signup`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, confirmPassword }),
      });
    } catch {
      throw new Error('Cannot reach backend. Ensure backend (:8080) and MongoDB (:27017) are running.');
    }
    if (!res.ok) {
      const err = await res.json().catch(() => ({}));
      throw new Error(err.message || 'Signup failed');
    }
    const data = await res.json();
    persistAuthState(data.token, {
      userId: data.userId,
      username: data.username,
      plan: data.plan,
      subscriptionStatus: data.subscriptionStatus,
    });
    return data;
  };

  const logout = () => {
    persistAuthState(null);
  };

  const isAuthenticated = !!user;
  const isAdmin = user?.role === 'ADMIN';

  return (
    <AuthContext.Provider value={{
      user, token, loading,
      isAuthenticated, isAdmin,
      login, signup, logout,
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
