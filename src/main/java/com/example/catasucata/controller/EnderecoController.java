package com.example.catasucata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catasucata.model.Cliente;
import com.example.catasucata.model.Endereco;
import com.example.catasucata.repository.ClienteRepository;
import com.example.catasucata.repository.EnderecoRepository;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // POST - Cadastrar Endereço para Cliente
    @PostMapping("/{clienteId}")
    public ResponseEntity<Endereco> cadastrarEndereco(
            @PathVariable Long clienteId,
            @RequestBody Endereco endereco
    ) {
        // Validar ID
        if (clienteId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Validar endereço enviado
        if (endereco == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Buscar cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        // Associar cliente
        endereco.setCliente(cliente);

        // Salvar endereço
        Endereco saved = enderecoRepository.save(endereco);

        return ResponseEntity.status(201).body(saved);
    }
}
