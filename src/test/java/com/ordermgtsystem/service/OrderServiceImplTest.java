package com.ordermgtsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ordermgtsystem.domain.Order;
import com.ordermgtsystem.domain.Product;
import com.ordermgtsystem.domain.Stock;
import com.ordermgtsystem.dto.OrderCreateRequest;
import com.ordermgtsystem.dto.OrderResponse;
import com.ordermgtsystem.exception.InsufficientStockException;
import com.ordermgtsystem.repository.OrderRepository;
import com.ordermgtsystem.repository.ProductRepository;
import com.ordermgtsystem.repository.StockRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Laptop", BigDecimal.valueOf(1000));
        setId(product, 1L);
    }

    @Test
    @DisplayName("재고가 1개이고 주문 수량이 1개면 주문 성공")
    void placeOrder_success_whenStockIsOne() {
        OrderCreateRequest request = new OrderCreateRequest(1L, 1);
        Stock stock = new Stock(product, 1);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(stock));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            setId(saved, 100L);
            return saved;
        });

        OrderResponse response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals(100L, response.orderId());
        assertEquals(1L, response.productId());
        assertEquals(1, response.quantity());
        assertEquals(0, stock.getQuantity());
        assertEquals(BigDecimal.valueOf(1000), response.totalPrice());

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("재고가 0개이고 주문 수량이 1개면 주문 실패")
    void placeOrder_fail_whenStockIsZero() {
        OrderCreateRequest request = new OrderCreateRequest(1L, 1);
        Stock stock = new Stock(product, 0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdForUpdate(1L)).thenReturn(Optional.of(stock));

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    private void setId(Object target, Long idValue) {
        try {
            Field idField = target.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(target, idValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Failed to set id by reflection", e);
        }
    }
}