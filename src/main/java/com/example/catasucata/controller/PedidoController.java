package com.example.catasucata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catasucata.model.ItemPedido;
import com.example.catasucata.model.Pedido;
import com.example.catasucata.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Endpoint para Finalizar Compra e Criar o Pedido
    // Método: POST
    // URL: http://localhost:8080/pedidos
    // Este método implementa a maior parte da lógica do 'Finalizar Compra'
    @PostMapping("/{clienteId}")
    public ResponseEntity<Pedido> finalizarCompra(
            @PathVariable Long clienteId,
            @RequestBody List<ItemPedido> itens
    ) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(clienteId, itens);
            // Retorna 201 Created (Criado com sucesso)
            return ResponseEntity.status(201).body(novoPedido);
        } catch (RuntimeException e) {
            // Retorna 400 Bad Request se houver erro (Cliente não encontrado, Estoque insuficiente, etc.)
            return ResponseEntity.badRequest().header("Error-Message", e.getMessage()).build();
        }
    }
    
    // Implementação pendente: GET /pedidos/{id} e GET para listar (Acompanhar pedido)
    // Deixaremos o Controller simples para focar na funcionalidade de Finalização.
}