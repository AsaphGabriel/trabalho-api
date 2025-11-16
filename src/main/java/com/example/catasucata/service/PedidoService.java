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

    // 1. Método para simular a FINALIZAÇÃO DA COMPRA (Criação do Pedido)
    @Transactional
    public Pedido criarPedido(Long clienteId, List<ItemPedido> itensRecebidos) {

        // 1. Validar ID do cliente
        if (clienteId == null) {
            throw new RuntimeException("ID do cliente não pode ser nulo");
        }

        // 2. Validar o Cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        // 3. Criar o novo Pedido
        Pedido novoPedido = new Pedido(cliente);
        Double valorTotal = 0.0;

        for (ItemPedido itemRecebido : itensRecebidos) {

            // 4. Validar ID do item (Movido para garantir que o produto não seja nulo)
            if (itemRecebido == null || itemRecebido.getItem() == null || itemRecebido.getItem().getId() == null) {
                throw new RuntimeException("Item inválido: o ID do produto não pode ser nulo.");
            }

            // 5. Buscar o Item (Produto)
            Long itemId = itemRecebido.getItem().getId();
            Item produto = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + itemId));

            Integer quantidadeComprada = itemRecebido.getQuantidade();
            if (quantidadeComprada == null || quantidadeComprada <= 0) {
                throw new RuntimeException("Quantidade inválida para o produto: " + produto.getNome());
            }

            // 6. Controle de Estoque
            if (produto.getEstoque() < quantidadeComprada) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // 7. Atualizar Estoque
            produto.setEstoque(produto.getEstoque() - quantidadeComprada);
            itemRepository.save(produto);

            // 8. Configurar ItemPedido para o Pedido
            itemRecebido.setPedido(novoPedido);
            itemRecebido.setItem(produto);
            itemRecebido.setPrecoUnitario(produto.getPreco());

            // 9. Calcular Valor Total
            valorTotal += produto.getPreco() * quantidadeComprada;

            // 10. Adicionar o ItemPedido ao Pedido
            novoPedido.getItens().add(itemRecebido);
        }

        // 11. Finalizar Pedido
        novoPedido.setValorTotal(valorTotal);

        // 12. Salvar o Pedido e os Itens
        return pedidoRepository.save(novoPedido);
    }
}