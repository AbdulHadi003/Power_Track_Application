import axios from 'axios';

// Base URLs
const AUTH_URL = process.env.NEXT_PUBLIC_AUTH_URL || 'http://localhost:8080';
const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

// ========== AUTH CLIENT (for /auth endpoints) ==========
const authClient = axios.create({
  baseURL: AUTH_URL,
  withCredentials: true, // CRITICAL: Send cookies with requests
  headers: {
    'Content-Type': 'application/json',
  },
});

// ========== API CLIENT (for /api endpoints) ==========
const apiClient = axios.create({
  baseURL: API_URL,
  withCredentials: true, // CRITICAL: Send cookies with requests
  headers: {
    'Content-Type': 'application/json',
  },
});

// ========== INTERCEPTORS ==========
const attachInterceptors = (client) => {
  // Request Interceptor
  client.interceptors.request.use(
    (config) => {
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  // Response Interceptor
  client.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        // Unauthorized - redirect to login
        if (typeof window !== 'undefined') {
          window.location.href = '/auth/login';
        }
      }
      return Promise.reject(error);
    }
  );
};

// Attach interceptors to both clients
attachInterceptors(authClient);
attachInterceptors(apiClient);

// ========== AUTH API (uses authClient) ==========
export const authAPI = {
  register: (data) => authClient.post('/auth/register', data),
  login: (data) => authClient.post('/auth/login', data),
  logout: () => authClient.post('/auth/logout'),
  getCurrentUser: () => authClient.get('/auth/profile'),
  changePassword: (data) => authClient.put('/auth/change-password', data),
};

// ========== USER API (uses apiClient) ==========
export const userAPI = {
  getProfile: () => apiClient.get('/users/me'),
  updateProfile: (data) => apiClient.put('/users/me', data),
  getAllUsers: () => apiClient.get('/users'), // Admin only
  createUser: (data) => apiClient.post('/users', data), // Admin only
  deleteUser: (id) => apiClient.delete(`/users/${id}`), // Admin only
};

// ========== FEEDER API (Public) ==========
export const feederAPI = {
  getAllFeeders: () => apiClient.get('/feeders'),
  getFeederById: (id) => apiClient.get(`/feeders/${id}`),
};

// ========== TARIFF API ==========
export const tariffAPI = {
  getActiveTariffs: () => apiClient.get('/tariffs/active'),
  getCurrentTariff: () => apiClient.get('/tariffs/current'),
  getAllTariffs: () => apiClient.get('/tariffs'), // Admin only
  createTariff: (data) => apiClient.post('/tariffs', data), // Admin only
  updateTariff: (id, data) => apiClient.put(`/tariffs/${id}`, data), // Admin only
  deleteTariff: (id) => apiClient.delete(`/tariffs/${id}`), // Admin only
};

// ========== METER API ==========
export const meterAPI = {
  getMyMeters: () => apiClient.get('/meters/my-meters'),
  getMeterById: (id) => apiClient.get(`/meters/${id}`),
  getAllMeters: () => apiClient.get('/meters'), // Admin only
};

// ========== METER REQUEST API ==========
export const meterRequestAPI = {
  createRequest: (data) => apiClient.post('/meter-requests', data),
  getMyRequests: () => apiClient.get('/meter-requests/my-requests'),
  getPendingRequests: () => apiClient.get('/meter-requests/pending'), // Admin only
  approveRequest: (id, data) => apiClient.put(`/meter-requests/${id}/approve`, data), // Admin only
  rejectRequest: (id, data) => apiClient.put(`/meter-requests/${id}/reject`, data), // Admin only
};

// ========== METER READING API ==========
export const meterReadingAPI = {
  submitReading: (data) => apiClient.post('/meter-readings', data), // Field Staff
  getReadingsByMeter: (meterId) => apiClient.get(`/meter-readings/meter/${meterId}`),
  verifyReading: (id) => apiClient.put(`/meter-readings/${id}/verify`), // Admin only
};

// ========== BILL API ==========
export const billAPI = {
  getMyBills: () => apiClient.get('/bills/my-bills'),
  getCurrentBill: () => apiClient.get('/bills/current'),
  getBillById: (id) => apiClient.get(`/bills/${id}`),
  generateBill: (data) => apiClient.post('/bills/generate', data), // Admin only
  getAllBills: () => apiClient.get('/bills'), // Admin only
  getUnpaidBills: () => apiClient.get('/bills/unpaid'), // Admin only
};

// ========== PAYMENT API ==========
export const paymentAPI = {
  payBill: (data) => apiClient.post('/payments/pay', data),
  getMyPayments: () => apiClient.get('/payments/my-payments'),
  getPaymentById: (id) => apiClient.get(`/payments/${id}`),
  getAllPayments: () => apiClient.get('/payments'), // Admin only
};

// ========== INSTALLMENT API ==========
export const installmentAPI = {
  requestInstallment: (data) => apiClient.post('/installments/request', data),
  getMyPlans: () => apiClient.get('/installments/my-plans'),
  getPendingRequests: () => apiClient.get('/installments/pending'), // Admin only
  approveRequest: (id, data) => apiClient.put(`/installments/${id}/approve`, data), // Admin only
  rejectRequest: (id, data) => apiClient.put(`/installments/${id}/reject`, data), // Admin only
  payInstallment: (id, data) => apiClient.post(`/installments/${id}/pay`, data),
};

// ========== COMPLAINT API ==========
export const complaintAPI = {
  createComplaint: (data) => apiClient.post('/complaints', data),
  getMyComplaints: () => apiClient.get('/complaints/my-complaints'),
  getComplaintByToken: (token) => apiClient.get(`/complaints/token/${token}`),
  getAllComplaints: () => apiClient.get('/complaints'), // Support/Admin
  updateComplaint: (id, data) => apiClient.put(`/complaints/${id}`, data), // Support/Admin
};

// ========== CHAT API ==========
export const chatAPI = {
  startConversation: (data) => apiClient.post('/chat/conversations', data),
  getMyConversations: () => apiClient.get('/chat/conversations/my'),
  getActiveConversations: () => apiClient.get('/chat/conversations/active'), // Support/Admin
  sendMessage: (data) => apiClient.post('/chat/messages', data),
  getMessages: (conversationId) => apiClient.get(`/chat/messages/${conversationId}`),
};

// ========== LOAD SHEDDING API ==========
export const loadSheddingAPI = {
  getMySchedule: () => apiClient.get('/load-shedding/my-schedule'),
  getTodaySchedule: () => apiClient.get('/load-shedding/today'),
  getWeekSchedule: () => apiClient.get('/load-shedding/week'),
  createSchedule: (data) => apiClient.post('/load-shedding', data), // Admin only
  updateSchedule: (id, data) => apiClient.put(`/load-shedding/${id}`, data), // Admin only
  deleteSchedule: (id) => apiClient.delete(`/load-shedding/${id}`), // Admin only
};

// ========== NOTIFICATION API ==========
export const notificationAPI = {
  getMyNotifications: () => apiClient.get('/notifications/my-notifications'),
  markAsRead: (id) => apiClient.put(`/notifications/${id}/read`),
  sendBroadcast: (data) => apiClient.post('/notifications', data), // Admin only
};

// ========== DASHBOARD API ==========
export const dashboardAPI = {
  getCustomerDashboard: () => apiClient.get('/dashboard/customer'),
  getAdminDashboard: () => apiClient.get('/dashboard/admin'),
  getSupportDashboard: () => apiClient.get('/dashboard/support'),
  getFieldStaffDashboard: () => apiClient.get('/dashboard/field'),
  getConsumptionAnalytics: (meterId) => apiClient.get(`/dashboard/consumption/${meterId}`),
};

// Export both clients for custom requests if needed
export { authClient, apiClient };
export default apiClient;