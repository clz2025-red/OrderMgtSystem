package com.ordermgtsystem.service;

import com.ordermgtsystem.domain.Order;
import com.ordermgtsystem.domain.Product;
import com.ordermgtsystem.domain.Stock;
import com.ordermgtsystem.dto.OrderCreateRequest;
import com.ordermgtsystem.dto.OrderResponse;
import com.ordermgtsystem.exception.ResourceNotFoundException;
import com.ordermgtsystem.repository.OrderRepository;
import com.ordermgtsystem.repository.ProductRepository;
import com.ordermgtsystem.repository.StockRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(
            ProductRepository productRepository,
            StockRepository stockRepository,
            OrderRepository orderRepository
    ) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
        public OrderResponse placeOrder(OrderCreateRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        Stock stock = stockRepository.findByProductIdForUpdate(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock", request.productId()));

        stock.decrease(request.quantity());

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));
        Order order = new Order(product, request.quantity(), totalPrice);
        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getProduct().getId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalPrice(),
                savedOrder.getStatus(),
                savedOrder.getOrderedAt()
        );
    }
}