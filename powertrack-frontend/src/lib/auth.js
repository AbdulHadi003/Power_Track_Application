import { authAPI, userAPI } from './api';

export const login = async (email, password) => {
  try {
    const response = await authAPI.login({ email, password });
    return response.data;
  } catch (error) {
    throw error.response?.data?.message || 'Login failed';
  }
};

export const register = async (userData) => {
  try {
    const response = await authAPI.register(userData);
    return response.data;
  } catch (error) {
    throw error.response?.data?.message || 'Registration failed';
  }
};

export const logout = async () => {
  try {
    await authAPI.logout();
    window.location.href = '/auth/login';
  } catch (error) {
    console.error('Logout failed:', error);
  }
};

export const getCurrentUser = async () => {
  try {
    const response = await authAPI.getCurrentUser();
    return response.data;
  } catch (error) {
    return null;
  }
};

export const getProfile = async () => {
  try {
    const response = await userAPI.getProfile();
    return response.data;
  } catch (error) {
    return null;
  }
};
