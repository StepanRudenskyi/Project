package org.example.petstore.service;

import jakarta.persistence.NoResultException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.petstore.context.OrderProcessingContext;
import org.example.petstore.dto.ReceiptDto;
import org.example.petstore.mapper.OrderMapper;
import org.example.petstore.model.Order;
import org.example.petstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    @Getter
    private OrderProcessingContext orderProcessingContext;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderProcessingContext orderProcessingContext) {
        this.orderRepository = orderRepository;
        this.orderProcessingContext = orderProcessingContext;
    }

    @Override
    public void processOrder(int orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            orderProcessingContext.applyDiscountLogic(order);
        } else {
            throw new NoResultException("Order with ID: " + orderId + " not found");
        }
    }

    @Override
    public ReceiptDto getReceipt(int accountId, int orderId) {
        Optional<Order> orderOptional = orderRepository.findReceipt(accountId, orderId);

        if (orderOptional.isPresent()) {
            Order existingOrder = orderOptional.get();
            return OrderMapper.toDto(existingOrder);
        } else {
            throw new NoResultException("Order with ID: " + orderId + " not found");
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoResultException("Order with ID: " + orderId + " not found"));
    }
}
