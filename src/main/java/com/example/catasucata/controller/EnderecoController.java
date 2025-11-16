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

    // POST - Cadastrar um Endereço para um Cliente
    @PostMapping("/{clienteId}")
    public ResponseEntity<Endereco> cadastrarEndereco(
            @PathVariable Long clienteId,
            @RequestBody Endereco endereco
    ) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Associa o cliente ao novo endereço
        endereco.setCliente(cliente);

        // Salva o Endereço (isso atualizará o Endereco no Cliente devido ao CascadeType.ALL)
        Endereco saved = enderecoRepository.save(endereco);
        
        return ResponseEntity.status(201).body(saved);
    }
}