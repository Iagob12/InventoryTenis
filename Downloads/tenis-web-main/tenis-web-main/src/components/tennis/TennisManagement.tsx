
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from '@/components/ui/alert-dialog';
import { toast } from 'sonner';

const TennisManagement = () => {
  const [tennisList, setTennisList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [selectedTennis, setSelectedTennis] = useState(null);

  useEffect(() => {
    fetchTennis();
  }, []);

  const fetchTennis = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis', {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data = await response.json();
        setTennisList(data);
      } else {
        toast.error('Erro ao carregar tênis');
      }
    } catch (error) {
      console.error('Erro ao buscar tênis:', error);
      toast.error('Erro de conexão');
    }

    setLoading(false);
  };

  const handleDelete = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(`https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis/${selectedTennis.id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        toast.success('Tênis excluído com sucesso!');
        setDeleteDialogOpen(false);
        setSelectedTennis(null);
        fetchTennis();
      } else {
        toast.error('Erro ao excluir tênis');
      }
    } catch (error) {
      console.error('Erro ao excluir tênis:', error);
      toast.error('Erro de conexão');
    }
  };

  const openDeleteDialog = (tennis) => {
    setSelectedTennis(tennis);
    setDeleteDialogOpen(true);
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando tênis...</div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Meus Tênis</h1>
          <p className="text-gray-600">Gerencie sua coleção de tênis</p>
        </div>
        <Link to="/adicionar-tenis">
          <Button className="bg-green-600 hover:bg-green-700">
            Adicionar Tênis
          </Button>
        </Link>
      </div>

      {tennisList.length === 0 ? (
        <Card>
          <CardContent className="text-center py-12">
            <p className="text-gray-600 mb-4">Nenhum tênis cadastrado ainda.</p>
            <Link to="/adicionar-tenis">
              <Button className="bg-green-600 hover:bg-green-700">
                Adicionar Primeiro Tênis
              </Button>
            </Link>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {tennisList.map((tennis) => (
            <Card key={tennis.id} className="overflow-hidden">
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
                    <span className="text-sm">Imagem indisponível</span>
                  </div>
                </div>
                <div className="p-4">
                  <h3 className="text-lg font-semibold mb-1">{tennis.nome}</h3>
                  <p className="text-sm text-gray-600 mb-2">Código: {tennis.codigo}</p>
                  <p className="text-lg font-bold text-green-600 mb-2">R$ {tennis.preco.toFixed(2)}</p>
                  <p className="text-sm text-gray-600 mb-4">Tamanho: {tennis.tamanho}</p>
                  
                  <div className="flex space-x-2">
                    <Link to={`/tenis/${tennis.id}`} className="flex-1">
                      <Button variant="outline" size="sm" className="w-full">
                        Ver
                      </Button>
                    </Link>
                    <Link to={`/editar-tenis/${tennis.id}`} className="flex-1">
                      <Button variant="outline" size="sm" className="w-full text-orange-600 border-orange-600">
                        Editar
                      </Button>
                    </Link>
                    <AlertDialog open={deleteDialogOpen && selectedTennis?.id === tennis.id} onOpenChange={setDeleteDialogOpen}>
                      <AlertDialogTrigger asChild>
                        <Button 
                          variant="outline" 
                          size="sm" 
                          className="text-red-600 border-red-600 hover:bg-red-50"
                          onClick={() => openDeleteDialog(tennis)}
                        >
                          Excluir
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>Confirmar Exclusão</AlertDialogTitle>
                          <AlertDialogDescription>
                            Tem certeza que deseja excluir o tênis "{selectedTennis?.nome}"? Esta ação não pode ser desfeita.
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
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default TennisManagement;
