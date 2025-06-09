package ru.test.micro.streampropagation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.test.micro.streampropagation.service.OrderService;
import ru.test.micro.streampropagation.service.OrderServiceImpl;

@SpringBootApplication
public class StreamPropagationApplication {

    public static void main(String[] args) {

        OrderService orderService = SpringApplication.run(StreamPropagationApplication.class, args).getBean(OrderServiceImpl.class);
        orderService.processAllOrders();
        System.out.println("Hello world!");
    }

}
