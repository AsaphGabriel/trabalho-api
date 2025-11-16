package com.example.catasucata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.catasucata.model.Item;
import com.example.catasucata.repository.ItemRepository;
import java.util.List;
import java.util.Optional;

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
        Item saved = itemRepository.save(item);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> buscarPorId(@PathVariable Long id) {
        Optional<Item> opt = itemRepository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> atualizar(@PathVariable Long id, @RequestBody Item itemAtualizado) {
        Optional<Item> opt = itemRepository.findById(id);

        if (opt.isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        Item existente = opt.get();

        existente.setNome(itemAtualizado.getNome());
        existente.setDescricao(itemAtualizado.getDescricao());
        existente.setPreco(itemAtualizado.getPreco());

        Item salvo = itemRepository.save(existente);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!itemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
