
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { toast } from 'sonner';

const EditTennis = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nome: '',
    codigo: '',
    descricao: '',
    preco: '',
    tamanho: '',
    urlImagem: ''
  });
  const [loading, setLoading] = useState(false);
  const [initialLoading, setInitialLoading] = useState(true);

  useEffect(() => {
    fetchTennisDetails();
  }, [id]);

  const fetchTennisDetails = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(`https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis/${id}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data = await response.json();
        setFormData({
          nome: data.nome,
          codigo: data.codigo,
          descricao: data.descricao || '',
          preco: data.preco.toString(),
          tamanho: data.tamanho.toString(),
          urlImagem: data.urlImagem || ''
        });
      } else {
        toast.error('Erro ao carregar dados do tênis');
        navigate('/meus-tenis');
      }
    } catch (error) {
      console.error('Erro ao buscar tênis:', error);
      toast.error('Erro de conexão');
    }

    setInitialLoading(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(`https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          ...formData,
          preco: parseFloat(formData.preco),
          tamanho: parseFloat(formData.tamanho)
        }),
      });

      if (response.ok) {
        toast.success('Tênis atualizado com sucesso!');
        navigate('/meus-tenis');
      } else {
        toast.error('Erro ao atualizar tênis');
      }
    } catch (error) {
      console.error('Erro ao atualizar tênis:', error);
      toast.error('Erro de conexão');
    }

    setLoading(false);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  if (initialLoading) {
    return (
      <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando dados...</div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-6">
        <Link to="/meus-tenis">
          <Button variant="outline" className="mb-4">
            ← Voltar
          </Button>
        </Link>
        <h1 className="text-3xl font-bold text-gray-900">Editar Tênis</h1>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Editar Informações</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Nome do Tênis *
                </label>
                <Input
                  type="text"
                  name="nome"
                  value={formData.nome}
                  onChange={handleChange}
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Código *
                </label>
                <Input
                  type="text"
                  name="codigo"
                  value={formData.codigo}
                  onChange={handleChange}
                  required
                />
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Preço (R$) *
                </label>
                <Input
                  type="number"
                  name="preco"
                  value={formData.preco}
                  onChange={handleChange}
                  step="0.01"
                  min="0"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Tamanho *
                </label>
                <Input
                  type="number"
                  name="tamanho"
                  value={formData.tamanho}
                  onChange={handleChange}
                  step="0.5"
                  min="1"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                URL da Imagem
              </label>
              <Input
                type="url"
                name="urlImagem"
                value={formData.urlImagem}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Descrição
              </label>
              <Textarea
                name="descricao"
                value={formData.descricao}
                onChange={handleChange}
                rows={4}
              />
            </div>

            <div className="flex space-x-4 pt-4">
              <Button 
                type="button" 
                variant="outline" 
                onClick={() => navigate('/meus-tenis')}
                className="flex-1"
              >
                Cancelar
              </Button>
              <Button 
                type="submit" 
                className="flex-1 bg-green-600 hover:bg-green-700"
                disabled={loading}
              >
                {loading ? 'Salvando...' : 'Salvar Alterações'}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default EditTennis;
