package com.anla.buku;

import com.anla.buku.model.Buku;
import com.anla.buku.service.CqrsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CqrsClientService cqrsClient;

    @Override
    public void run(String... args) {
        cqrsClient.save(new Buku(1L, "Java Programming", "John Doe", "Politeknik Negeri Padang", 2023), "1");
    }
}