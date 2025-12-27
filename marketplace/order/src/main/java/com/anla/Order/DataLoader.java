package com.anla.Order;

import com.anla.Order.model.Order;
import com.anla.Order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (orderRepository.count() == 0) {
            // Sample data
            Order order1 = new Order();
            order1.setPelangganId(1L);
            order1.setProdukId(1L);
            order1.setQuantity(2);
            order1.setTotalHarga(30000000.0);
            order1.setStatus("COMPLETED");
            order1.setOrderDate(LocalDate.now().minusDays(5));
            
            Order order2 = new Order();
            order2.setPelangganId(2L);
            order2.setProdukId(2L);
            order2.setQuantity(1);
            order2.setTotalHarga(8000000.0);
            order2.setStatus("PENDING");
            order2.setOrderDate(LocalDate.now().minusDays(2));
            
            orderRepository.save(order1);
            orderRepository.save(order2);
            
            System.out.println("Sample order data loaded!");
        }
    }
}