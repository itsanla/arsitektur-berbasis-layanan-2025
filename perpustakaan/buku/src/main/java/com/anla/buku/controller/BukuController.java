package com.anla.buku.controller;

import com.anla.buku.model.Buku;
import com.anla.buku.service.BukuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buku")
@RequiredArgsConstructor
public class BukuController {
    
    private final BukuService bukuService;
    
    @GetMapping
    public List<Object> getAllBooks() {
        return bukuService.findAll();
    }
    
    @GetMapping("/{bookId}")
    public Object getBookById(@PathVariable Long bookId) {
        return bukuService.findById(bookId);
    }
    
    @PostMapping
    public Buku createBook(@RequestBody Buku book) {
        return bukuService.save(book);
    }
    
    @PutMapping("/{bookId}")
    public Buku updateBook(@PathVariable Long bookId, @RequestBody Buku book) {
        book.setBookId(bookId);
        return bukuService.update(book);
    }
    
    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        bukuService.delete(bookId);
    }
}