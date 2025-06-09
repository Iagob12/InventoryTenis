
import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { toast } from 'sonner';

const Movements = () => {
  const [movements, setMovements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({
    tipo: 'Todos',
    dataInicio: '',
    dataFim: ''
  });

  useEffect(() => {
    fetchMovements();
  }, []);

  const fetchMovements = async (filterParams = null) => {
    const token = localStorage.getItem('authToken');
    let url = 'https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/movimentacoes';
    
    if (filterParams?.tipo && filterParams.tipo !== 'Todos') {
      url = `https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/movimentacoes/tipo/${filterParams.tipo}`;
    } else if (filterParams?.dataInicio && filterParams?.dataFim) {
      url = `https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/movimentacoes/periodo?inicio=${filterParams.dataInicio}T00:00:00&fim=${filterParams.dataFim}T23:59:59`;
    }
    
    try {
      const response = await fetch(url, {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data = await response.json();
        setMovements(data);
      } else {
        toast.error('Erro ao carregar movimentações');
      }
    } catch (error) {
      console.error('Erro ao buscar movimentações:', error);
      toast.error('Erro de conexão');
    }

    setLoading(false);
  };

  const handleFilter = () => {
    setLoading(true);
    fetchMovements(filters);
  };

  const clearFilters = () => {
    setFilters({
      tipo: 'Todos',
      dataInicio: '',
      dataFim: ''
    });
    setLoading(true);
    fetchMovements();
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getTipoColor = (tipo) => {
    switch (tipo) {
      case 'ENTRADA':
        return 'text-green-600 bg-green-100';
      case 'SAIDA':
        return 'text-red-600 bg-red-100';
      case 'AJUSTE':
        return 'text-yellow-600 bg-yellow-100';
      case 'TRANSFERENCIA':
        return 'text-blue-600 bg-blue-100';
      default:
        return 'text-gray-600 bg-gray-100';
    }
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando movimentações...</div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Movimentações</h1>
        <p className="text-gray-600">Histórico de movimentações do inventário</p>
      </div>

      {/* Filters */}
      <Card className="mb-6">
        <CardHeader>
          <CardTitle>Filtros</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">Tipo</label>
              <Select value={filters.tipo} onValueChange={(value) => setFilters({...filters, tipo: value})}>
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Todos">Todos</SelectItem>
                  <SelectItem value="ENTRADA">Entrada</SelectItem>
                  <SelectItem value="SAIDA">Saída</SelectItem>
                  <SelectItem value="AJUSTE">Ajuste</SelectItem>
                  <SelectItem value="TRANSFERENCIA">Transferência</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Data Início</label>
              <Input
                type="date"
                value={filters.dataInicio}
                onChange={(e) => setFilters({...filters, dataInicio: e.target.value})}
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Data Fim</label>
              <Input
                type="date"
                value={filters.dataFim}
                onChange={(e) => setFilters({...filters, dataFim: e.target.value})}
              />
            </div>
            <div className="flex items-end space-x-2">
              <Button onClick={handleFilter} className="bg-blue-600 hover:bg-blue-700">
                Filtrar
              </Button>
              <Button variant="outline" onClick={clearFilters}>
                Limpar
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Movements List */}
      {movements.length === 0 ? (
        <Card>
          <CardContent className="text-center py-12">
            <p className="text-gray-600">Nenhuma movimentação encontrada.</p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          {movements.map((movement) => (
            <Card key={movement.id}>
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-start space-x-4">
                    <div className={`w-3 h-3 rounded-full mt-2 ${
                      movement.tipo === 'ENTRADA' ? 'bg-green-500' : 
                      movement.tipo === 'SAIDA' ? 'bg-red-500' : 
                      movement.tipo === 'AJUSTE' ? 'bg-yellow-500' : 'bg-blue-500'
                    }`}></div>
                    <div className="flex-1">
                      <div className="flex items-center space-x-3 mb-2">
                        <h3 className="font-semibold">{movement.tenis.nome}</h3>
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getTipoColor(movement.tipo)}`}>
                          {movement.tipo}
                        </span>
                      </div>
                      <p className="text-sm text-gray-600 mb-1">
                        Código: {movement.tenis.codigo} | Tamanho: {movement.tenis.tamanho}
                      </p>
                      <p className="text-sm text-gray-600 mb-1">
                        Quantidade: {movement.quantidade}
                      </p>
                      {movement.descricao && (
                        <p className="text-sm text-gray-600 mb-1">
                          {movement.descricao}
                        </p>
                      )}
                    </div>
                  </div>
                  <div className="text-right text-sm text-gray-500">
                    {formatDate(movement.dataMovimentacao)}
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default Movements;
