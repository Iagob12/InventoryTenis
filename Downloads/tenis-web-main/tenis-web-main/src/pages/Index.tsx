
import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from '../components/auth/Login';
import Register from '../components/auth/Register';
import Dashboard from '../components/dashboard/Dashboard';
import TennisManagement from '../components/tennis/TennisManagement';
import TennisDetails from '../components/tennis/TennisDetails';
import AddTennis from '../components/tennis/AddTennis';
import EditTennis from '../components/tennis/EditTennis';
import Inventory from '../components/inventory/Inventory';
import Movements from '../components/movements/Movements';
import Navbar from '../components/layout/Navbar';

const Index = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    setIsAuthenticated(!!token);
    setLoading(false);
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-lg">Carregando...</div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Routes>
          <Route path="/login" element={<Login setIsAuthenticated={setIsAuthenticated} />} />
          <Route path="/register" element={<Register />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar setIsAuthenticated={setIsAuthenticated} />
      <div className="pt-16">
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/meus-tenis" element={<TennisManagement />} />
          <Route path="/tenis/:id" element={<TennisDetails />} />
          <Route path="/adicionar-tenis" element={<AddTennis />} />
          <Route path="/editar-tenis/:id" element={<EditTennis />} />
          <Route path="/inventario" element={<Inventory />} />
          <Route path="/movimentacoes" element={<Movements />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </div>
  );
};

export default Index;
