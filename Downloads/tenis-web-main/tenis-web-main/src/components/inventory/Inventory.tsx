
import React, { useState, useEffect } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from '@/components/ui/alert-dialog';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Textarea } from '@/components/ui/textarea';
import { toast } from 'sonner';

const Inventory = () => {
  const [inventory, setInventory] = useState([]);
  const [tennisList, setTennisList] = useState([]);
  const [inventoryStats, setInventoryStats] = useState({ totalValue: 0, totalItems: 0 });
  const [loading, setLoading] = useState(true);
  
  // Modal states
  const [addModalOpen, setAddModalOpen] = useState(false);
  const [adjustModalOpen, setAdjustModalOpen] = useState(false);
  const [removeDialogOpen, setRemoveDialogOpen] = useState(false);
  const [selectedItem, setSelectedItem] = useState(null);
  
  // Form states
  const [addForm, setAddForm] = useState({
    tenisId: '',
    quantidade: '',
    descricao: ''
  });
  const [adjustForm, setAdjustForm] = useState({
    newQuantity: '',
    description: ''
  });
  const [removeForm, setRemoveForm] = useState({
    quantity: '',
    description: ''
  });

  useEffect(() => {
    fetchInventoryData();
    fetchTennisList();
  }, []);

  const fetchInventoryData = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const [inventoryResponse, valueResponse, countResponse] = await Promise.all([
        fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory', {
          headers: { 'Authorization': `Bearer ${token}` }
        }),
        fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory/value', {
          headers: { 'Authorization': `Bearer ${token}` }
        }),
        fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory/count', {
          headers: { 'Authorization': `Bearer ${token}` }
        })
      ]);

      if (inventoryResponse.ok && valueResponse.ok && countResponse.ok) {
        const inventoryData = await inventoryResponse.json();
        const valueData = await valueResponse.json();
        const countData = await countResponse.json();
        
        setInventory(inventoryData);
        setInventoryStats({
          totalValue: valueData.totalValue || 0,
          totalItems: countData.totalItems || 0
        });
      }
    } catch (error) {
      console.error('Erro ao buscar inventário:', error);
      toast.error('Erro de conexão');
    }

    setLoading(false);
  };

  const fetchTennisList = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/tenis', {
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        const data = await response.json();
        setTennisList(data);
      }
    } catch (error) {
      console.error('Erro ao buscar tênis:', error);
    }
  };

  const handleAddToInventory = async (e) => {
    e.preventDefault();
    
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch('https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          tenisId: parseInt(addForm.tenisId),
          quantidade: parseInt(addForm.quantidade),
          descricao: addForm.descricao
        }),
      });

      if (response.ok) {
        toast.success('Item adicionado ao inventário!');
        setAddModalOpen(false);
        setAddForm({ tenisId: '', quantidade: '', descricao: '' });
        fetchInventoryData();
      } else {
        toast.error('Erro ao adicionar item ao inventário');
      }
    } catch (error) {
      console.error('Erro:', error);
      toast.error('Erro de conexão');
    }
  };

  const handleAdjustQuantity = async (e) => {
    e.preventDefault();
    
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(
        `https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory/adjust/${selectedItem.tenisId}?newQuantity=${adjustForm.newQuantity}&description=${encodeURIComponent(adjustForm.description)}`,
        {
          method: 'PUT',
          headers: { 'Authorization': `Bearer ${token}` }
        }
      );

      if (response.ok) {
        toast.success('Quantidade ajustada com sucesso!');
        setAdjustModalOpen(false);
        setAdjustForm({ newQuantity: '', description: '' });
        setSelectedItem(null);
        fetchInventoryData();
      } else {
        toast.error('Erro ao ajustar quantidade');
      }
    } catch (error) {
      console.error('Erro:', error);
      toast.error('Erro de conexão');
    }
  };

  const handleRemoveFromInventory = async () => {
    const token = localStorage.getItem('authToken');
    
    try {
      const response = await fetch(
        `https://tenisinventory-eqb9asgvheh0h5d5.brazilsouth-01.azurewebsites.net/api/inventory/remove/${selectedItem.tenisId}?quantity=${removeForm.quantity}&description=${encodeURIComponent(removeForm.description || '')}`,
        {
          method: 'POST',
          headers: { 'Authorization': `Bearer ${token}` }
        }
      );

      if (response.ok) {
        toast.success('Item removido do inventário!');
        setRemoveDialogOpen(false);
        setRemoveForm({ quantity: '', description: '' });
        setSelectedItem(null);
        fetchInventoryData();
      } else {
        toast.error('Erro ao remover item do inventário');
      }
    } catch (error) {
      console.error('Erro:', error);
      toast.error('Erro de conexão');
    }
  };

  const openRemoveDialog = (item) => {
    setSelectedItem(item);
    setRemoveForm({ quantity: '1', description: '' });
    setRemoveDialogOpen(true);
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">Carregando inventário...</div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Inventário</h1>
          <p className="text-gray-600">Gerencie seu estoque de tênis</p>
        </div>
        <Dialog open={addModalOpen} onOpenChange={setAddModalOpen}>
          <DialogTrigger asChild>
            <Button className="bg-green-600 hover:bg-green-700">
              Adicionar
            </Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Adicionar ao Inventário</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleAddToInventory} className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-1">Item</label>
                <Select value={addForm.tenisId} onValueChange={(value) => setAddForm({...addForm, tenisId: value})}>
                  <SelectTrigger>
                    <SelectValue placeholder="Selecione um tênis" />
                  </SelectTrigger>
                  <SelectContent>
                    {tennisList.map((tennis) => (
                      <SelectItem key={tennis.id} value={tennis.id.toString()}>
                        {tennis.nome} - {tennis.codigo}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Quantidade atual</label>
                <Input
                  type="number"
                  value={addForm.quantidade}
                  onChange={(e) => setAddForm({...addForm, quantidade: e.target.value})}
                  placeholder="Nova quantidade"
                  min="1"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">Descrição</label>
                <Textarea
                  value={addForm.descricao}
                  onChange={(e) => setAddForm({...addForm, descricao: e.target.value})}
                  placeholder="Descrição"
                  rows={3}
                />
              </div>
              <div className="flex space-x-2">
                <Button type="button" variant="outline" onClick={() => setAddModalOpen(false)} className="flex-1">
                  Cancelar
                </Button>
                <Button type="submit" className="flex-1 bg-green-600 hover:bg-green-700">
                  Adicionar
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Total de Itens</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {inventoryStats.totalItems}
            </div>
          </CardContent>
        </Card>
        
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-gray-600">Valor Total</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              R$ {inventoryStats.totalValue.toFixed(2)}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Inventory Items */}
      {inventory.length === 0 ? (
        <Card>
          <CardContent className="text-center py-12">
            <p className="text-gray-600 mb-4">Nenhum item no inventário.</p>
            <Dialog open={addModalOpen} onOpenChange={setAddModalOpen}>
              <DialogTrigger asChild>
                <Button className="bg-green-600 hover:bg-green-700">
                  Adicionar Primeiro Item
                </Button>
              </DialogTrigger>
            </Dialog>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          <h2 className="text-xl font-semibold">Tênis</h2>
          {inventory.map((item) => (
            <Card key={item.id}>
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-start space-x-4">
                    <div className="w-16 h-16 bg-gray-100 rounded-lg flex items-center justify-center">
                      {item.tenisUrlImagem ? (
                        <img 
                          src={item.tenisUrlImagem} 
                          alt={item.tenisNome}
                          className="w-full h-full object-cover rounded-lg"
                          onError={(e) => {
                            const target = e.target as HTMLImageElement;
                            target.style.display = 'none';
                            const fallback = target.nextElementSibling as HTMLElement;
                            if (fallback) fallback.style.display = 'flex';
                          }}
                        />
                      ) : null}
                      <div className="text-xs text-gray-400" style={{display: item.tenisUrlImagem ? 'none' : 'flex'}}>
                        N/A
                      </div>
                    </div>
                    <div>
                      <h3 className="font-semibold">{item.tenisNome}</h3>
                      <p className="text-sm text-gray-600">Código: {item.tenisCodigo}</p>
                      <p className="text-sm text-gray-600">Quantidade: {item.quantidade}</p>
                      <p className="text-sm text-gray-600">Preço Unit.: R$ {item.precoUnitario.toFixed(2)}</p>
                      <p className="text-sm font-semibold text-green-600">Total: R$ {item.valorTotal.toFixed(2)}</p>
                    </div>
                  </div>
                  <div className="flex space-x-2">
                    <AlertDialog open={removeDialogOpen && selectedItem?.id === item.id} onOpenChange={setRemoveDialogOpen}>
                      <AlertDialogTrigger asChild>
                        <Button 
                          variant="outline" 
                          size="sm" 
                          className="text-red-600 border-red-600 hover:bg-red-50"
                          onClick={() => openRemoveDialog(item)}
                        >
                          Remover
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>Confirmar Remoção</AlertDialogTitle>
                          <AlertDialogDescription>
                            Tem certeza que deseja remover itens do {item.tenisNome} do inventário?
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <div className="space-y-4">
                          <div>
                            <label className="block text-sm font-medium mb-1">Quantidade a remover</label>
                            <Input
                              type="number"
                              value={removeForm.quantity}
                              onChange={(e) => setRemoveForm({...removeForm, quantity: e.target.value})}
                              min="1"
                              max={item.quantidade}
                              required
                            />
                          </div>
                          <div>
                            <label className="block text-sm font-medium mb-1">Descrição (opcional)</label>
                            <Textarea
                              value={removeForm.description}
                              onChange={(e) => setRemoveForm({...removeForm, description: e.target.value})}
                              placeholder="Motivo da remoção"
                              rows={2}
                            />
                          </div>
                        </div>
                        <AlertDialogFooter>
                          <AlertDialogCancel>Cancelar</AlertDialogCancel>
                          <AlertDialogAction onClick={handleRemoveFromInventory} className="bg-red-600 hover:bg-red-700">
                            Remover
                          </AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
                    
                    <Button 
                      variant="outline" 
                      size="sm" 
                      className="text-orange-600 border-orange-600 hover:bg-orange-50"
                      onClick={() => {
                        setSelectedItem(item);
                        setAdjustForm({ newQuantity: item.quantidade.toString(), description: '' });
                        setAdjustModalOpen(true);
                      }}
                    >
                      Ajustar
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {/* Adjust Modal */}
      <Dialog open={adjustModalOpen} onOpenChange={setAdjustModalOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Ajustar Inventário</DialogTitle>
          </DialogHeader>
          <form onSubmit={handleAdjustQuantity} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">Item</label>
              <div className="text-sm text-gray-600">
                {selectedItem?.tenisNome} - Quantidade atual: {selectedItem?.quantidade}
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Nova quantidade</label>
              <Input
                type="number"
                value={adjustForm.newQuantity}
                onChange={(e) => setAdjustForm({...adjustForm, newQuantity: e.target.value})}
                min="0"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Descrição</label>
              <Textarea
                value={adjustForm.description}
                onChange={(e) => setAdjustForm({...adjustForm, description: e.target.value})}
                placeholder="Motivo do ajuste"
                rows={3}
              />
            </div>
            <div className="flex space-x-2">
              <Button 
                type="button" 
                variant="outline" 
                onClick={() => setAdjustModalOpen(false)} 
                className="flex-1"
              >
                Cancelar
              </Button>
              <Button type="submit" className="flex-1 bg-green-600 hover:bg-green-700">
                Ajustar
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Inventory;
