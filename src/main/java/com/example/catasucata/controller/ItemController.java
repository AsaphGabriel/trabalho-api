package com.example.catasucata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catasucata.model.Item;
import com.example.catasucata.repository.ItemRepository;

@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<Item> listar() {
        return itemRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Item> criar(@RequestBody Item item) {
        if (item == null || item.getNome() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (item.getPreco() <= 0.0) {
            // Preço inválido
            return ResponseEntity.badRequest().build();
        }
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(@PathVariable Long id, @RequestBody Item itemAtualizado) {
        return itemRepository.findById(id).map(itemExistente -> {
            itemExistente.setNome(itemAtualizado.getNome());
            itemExistente.setDescricao(itemAtualizado.getDescricao());
            itemExistente.setPreco(itemAtualizado.getPreco());
            itemExistente.setEstoque(itemAtualizado.getEstoque());
            Item salvo = itemRepository.save(itemExistente);
            return ResponseEntity.ok(salvo);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
