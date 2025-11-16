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

    @PostMapping("/{clienteId}") // <--- A ROTA CORRETA
    public ResponseEntity<?> criarPedido(
            @PathVariable Long clienteId,
            @RequestBody List<ItemPedido> itens) {

        try {
            Pedido pedido = pedidoService.criarPedido(clienteId, itens);
            return ResponseEntity.status(201).body(pedido);
        } catch (RuntimeException e) {
            // Retorna a mensagem de erro do service (ex: "Cliente não encontrado")
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}