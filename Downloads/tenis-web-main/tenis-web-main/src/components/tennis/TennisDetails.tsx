
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from '@/components/ui/alert-dialog';
import { ArrowLeft } from 'lucide-react';
import { toast } from 'sonner';

const TennisDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [tennis, setTennis] = useState(null);
  const [loading, setLoading] = useState(true);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

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
        setTennis(data);
      } else {
        toast.error('Erro ao carregar detalhes do tênis');
        navigate('/meus-tenis');
      }
    } catch (error) {
      console.error('Erro ao buscar detalhes:', error);
      toast.error('Erro de conexão');
      navigate('/meus-tenis');
    }

    setLoading(false);
  };

  const handleDelete = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(`https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        toast.success('Tênis excluído com sucesso!');
        navigate('/meus-tenis');
      } else {
        toast.error('Erro ao excluir tênis');
      }
    } catch (error) {
      console.error('Erro ao excluir tênis:', error);
      toast.error('Erro de conexão');
    }
  };

  if (loading) {
    return (
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando detalhes...</div>
      </div>
    );
  }

  if (!tennis) {
    return (
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Tênis não encontrado</div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-center mb-6">
        <Button 
          variant="ghost" 
          size="sm" 
          onClick={() => navigate('/meus-tenis')}
          className="mr-4"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Voltar
        </Button>
        <h1 className="text-3xl font-bold text-gray-900">{tennis.nome}</h1>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <Card>
          <CardContent className="p-0">
            <div className="aspect-square bg-gray-100 flex items-center justify-center">
              {tennis.urlImagem ? (
                <img 
                  src={tennis.urlImagem} 
                  alt={tennis.nome}
                  className="w-full h-full object-cover"
                  onError={(e) => {
                    const target = e.target as HTMLImageElement;
                    target.style.display = 'none';
                    const fallback = target.nextElementSibling as HTMLElement;
                    if (fallback) fallback.style.display = 'flex';
                  }}
                />
              ) : null}
              <div className="flex items-center justify-center text-gray-400" style={{display: tennis.urlImagem ? 'none' : 'flex'}}>
                <span className="text-lg">Imagem indisponível</span>
              </div>
            </div>
          </CardContent>
        </Card>

        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Informações do Produto</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <label className="text-sm font-medium text-gray-600">Nome</label>
                <p className="text-lg font-semibold">{tennis.nome}</p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-600">Código</label>
                <p className="text-lg">{tennis.codigo}</p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-600">Preço</label>
                <p className="text-2xl font-bold text-green-600">R$ {tennis.preco.toFixed(2)}</p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-600">Tamanho</label>
                <p className="text-lg">{tennis.tamanho}</p>
              </div>
              {tennis.descricao && (
                <div>
                  <label className="text-sm font-medium text-gray-600">Descrição</label>
                  <p className="text-gray-900">{tennis.descricao}</p>
                </div>
              )}
            </CardContent>
          </Card>

          <div className="flex space-x-4">
            <Link to={`/editar-tenis/${tennis.id}`} className="flex-1">
              <Button className="w-full bg-orange-600 hover:bg-orange-700">
                Editar Tênis
              </Button>
            </Link>
            <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
              <AlertDialogTrigger asChild>
                <Button variant="outline" className="flex-1 text-red-600 border-red-600 hover:bg-red-50">
                  Excluir Tênis
                </Button>
              </AlertDialogTrigger>
              <AlertDialogContent>
                <AlertDialogHeader>
                  <AlertDialogTitle>Confirmar Exclusão</AlertDialogTitle>
                  <AlertDialogDescription>
                    Tem certeza que deseja excluir o tênis "{tennis.nome}"? Esta ação não pode ser desfeita.
                  </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                  <AlertDialogCancel>Cancelar</AlertDialogCancel>
                  <AlertDialogAction onClick={handleDelete} className="bg-red-600 hover:bg-red-700">
                    Excluir
                  </AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TennisDetails;
