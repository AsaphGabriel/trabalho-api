package com.example.catasucata.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catasucata.model.Cliente;
import com.example.catasucata.repository.ClienteRepository;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // POST - Fazer Cadastro
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        // Define a data de cadastro automaticamente antes de salvar
        cliente.setDataCadastro(LocalDateTime.now()); 
        
        // Aqui, futuramente, você adicionará a lógica de criptografia da senha
        
        Cliente saved = clienteRepository.save(cliente);
        return ResponseEntity.status(201).body(saved);
    }

    // GET - Listar todos os clientes (Apenas para fins de teste/Administrador)
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }
}