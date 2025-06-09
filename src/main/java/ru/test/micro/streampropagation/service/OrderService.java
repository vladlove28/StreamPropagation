package ru.test.micro.streampropagation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.test.micro.streampropagation.entity.Order;

import java.util.List;

public interface OrderService {

    void processAllOrders();

    void updateOrder(Long orderId);

    void updateAllOrders();

    List<Order> getAllOrders();

    List<Order> getAllWithItems();

    void processWithLocking(Long id);
}
