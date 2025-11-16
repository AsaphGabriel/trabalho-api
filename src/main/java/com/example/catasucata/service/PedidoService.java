package com.example.catasucata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catasucata.model.Cliente;
import com.example.catasucata.model.Item;
import com.example.catasucata.model.ItemPedido;
import com.example.catasucata.model.Pedido;
import com.example.catasucata.repository.ClienteRepository;
import com.example.catasucata.repository.ItemRepository;
import com.example.catasucata.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ItemRepository itemRepository;


    // Métodos de lógica de negócios virão aqui

    // 1. Método para simular a FINALIZAÇÃO DA COMPRA (Criação do Pedido)
    @Transactional // Garante que a operação seja atômica (ou salva tudo ou não salva nada)
    public Pedido criarPedido(Long clienteId, List<ItemPedido> itensRecebidos) {
        
        // 1. Validar o Cliente (Usuário)
        Cliente cliente = clienteRepository.findById(clienteId)
                                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // 2. Criar o novo Pedido
        Pedido novoPedido = new Pedido(cliente);
        
        Double valorTotal = 0.0;

        for (ItemPedido itemRecebido : itensRecebidos) {
            
            // 3. Validar e Buscar o Item (Produto)
            Item produto = itemRepository.findById(itemRecebido.getItem().getId())
                                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemRecebido.getItem().getId()));

            Integer quantidadeComprada = itemRecebido.getQuantidade();
            
            // 4. Controle de Estoque (Validação)
            if (produto.getEstoque() < quantidadeComprada) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // 5. Atualizar o Estoque (Atualizar Estoque)
            produto.setEstoque(produto.getEstoque() - quantidadeComprada);
            itemRepository.save(produto); // Salva o item com o novo estoque
            
            // 6. Configurar ItemPedido
            itemRecebido.setPedido(novoPedido);
            itemRecebido.setItem(produto);
            itemRecebido.setPrecoUnitario(produto.getPreco()); // Preço do momento da compra
            
            // 7. Calcular o Valor Total do Pedido
            valorTotal += produto.getPreco() * quantidadeComprada;
            
            // Adicionar ItemPedido à lista do novo pedido (para JPA salvar)
            novoPedido.getItens().add(itemRecebido);
        }

        // 8. Finalizar o Pedido (Calcular Valor Total)
        novoPedido.setValorTotal(valorTotal);

        // 9. Salvar o Pedido e os ItensPedido
        return pedidoRepository.save(novoPedido);
    }
}