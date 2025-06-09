
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const [statsResponse, summaryResponse] = await Promise.all([
        fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/dashboard/stats', {
          headers: { 'Authorization': `Bearer ${token}` }
        }),
        fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/dashboard/summary', {
          headers: { 'Authorization': `Bearer ${token}` }
        })
      ]);

      if (statsResponse.ok && summaryResponse.ok) {
        const statsData = await statsResponse.json();
        const summaryData = await summaryResponse.json();
        setStats(statsData);
        setSummary(summaryData);
      } else {
        toast.error('Erro ao carregar dados do dashboard');
      }
    } catch (error) {
      console.error('Erro ao buscar dados:', error);
      toast.error('Erro de conexão');
    }

    setLoading(false);
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando dashboard...</div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">Visão geral do seu inventário de tênis</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Valor Total</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              R$ {stats?.totalValue?.toFixed(2) || '0.00'}
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Produtos em Estoque</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-blue-600">
              {stats?.productsInStock || 0}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total de Produtos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-purple-600">
              {summary?.totalProducts || 0}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
        <Link to="/adicionar-tenis">
          <Button className="w-full bg-blue-600 hover:bg-blue-700">
            Adicionar Tênis
          </Button>
        </Link>
        <Link to="/inventario">
          <Button className="w-full bg-green-600 hover:bg-green-700">
            Inventário
          </Button>
        </Link>
        <Link to="/movimentacoes">
          <Button className="w-full bg-cyan-600 hover:bg-cyan-700">
            Movimentações
          </Button>
        </Link>
        <Link to="/meus-tenis">
          <Button className="w-full bg-purple-600 hover:bg-purple-700">
            Meus Tênis
          </Button>
        </Link>
      </div>

      {/* Recent Movements */}
      {stats?.recentMovements && stats.recentMovements.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Movimentações Recentes</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {stats.recentMovements.map((movement) => (
                <div key={movement.id} className="flex items-center justify-between p-4 border rounded-lg">
                  <div className="flex items-center space-x-4">
                    <div className={`w-3 h-3 rounded-full ${
                      movement.tipo === 'ENTRADA' ? 'bg-green-500' : 
                      movement.tipo === 'SAIDA' ? 'bg-red-500' : 'bg-yellow-500'
                    }`}></div>
                    <div>
                      <p className="font-medium">{movement.tenis.nome}</p>
                      <p className="text-sm text-gray-600">{movement.descricao}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="font-medium">{movement.tipo}</p>
                    <p className="text-sm text-gray-600">Qtd: {movement.quantidade}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

export default Dashboard;
