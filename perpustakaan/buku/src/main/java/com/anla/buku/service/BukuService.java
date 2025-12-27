package com.anla.buku.service;

import com.anla.buku.model.Buku;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class BukuService {
    
    private final CqrsClientService cqrsClient;
    private final AtomicLong idCounter = new AtomicLong(1);
    
    public Buku save(Buku book) {
        book.setBookId(idCounter.getAndIncrement());
        cqrsClient.save(book, book.getBookId().toString());
        return book;
    }
    
    public Buku update(Buku book) {
        cqrsClient.update(book, book.getBookId().toString());
        return book;
    }
    
    public void delete(Long bookId) {
        cqrsClient.delete(bookId.toString());
    }
    
    public Object findById(Long bookId) {
        return cqrsClient.findById(bookId.toString());
    }
    
    public List<Object> findAll() {
        return cqrsClient.findAll();
    }
}