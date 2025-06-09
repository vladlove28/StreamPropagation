package ru.test.micro.streampropagation.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.micro.streampropagation.entity.Order;
import ru.test.micro.streampropagation.entity.OrderItem;
import ru.test.micro.streampropagation.repository.OrderItemRepository;
import ru.test.micro.streampropagation.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public void processAllOrders() {
        //может быть очень большая выборка (1M/2M+)
        try (Stream<Order> orders = orderRepository.findAllWithJoinFetchPropagation()) {
            orders.forEach(order -> {
                Long orderId = order.getId();
                for (OrderItem item : order.getItems()) {
                    auditTotalQuantity(orderId, item.getQuantity());
                }
            });
        }
    }


    private void auditTotalQuantity(Long orderId, int totalQuantity) {
        log.info("Order {} has total quantity: {}", orderId, totalQuantity);
    }

    @Override
    public void updateOrder(Long orderId) {
        Optional<Order> opt = orderRepository.findById(orderId);
        opt.ifPresent(order -> order.setCustomer("Updated"));
    }

    @Override
    @Transactional
    public void updateAllOrders() {
        List<Order> orders = orderRepository.findAllWithoutJoin();

        for (Order order : orders) {
            updateOrder(order.getId());
        }

        updateGlobalOrderStats(orders);
    }

    private void updateGlobalOrderStats(List<Order> orders) {
        int total = orders.size();
        if (total > 1000) {
            throw new IllegalStateException("Too many orders to process at once");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithoutJoin();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllWithItems() {
        return orderRepository.findAllWithJoinFetch();
    }

    @Override
    @Transactional
    public void processWithLocking(Long id) {
        Optional<Order> opt = orderRepository.findWithOptimisticLockById(id);
        // Optional<Order> opt = orderRepository.findWithPessimisticLockById(id);
        opt.ifPresent(order -> order.setCustomer("Locked Update"));
    }
}
