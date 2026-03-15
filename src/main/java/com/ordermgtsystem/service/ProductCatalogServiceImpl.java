package com.ordermgtsystem.service;

import com.ordermgtsystem.dto.ProductListItemResponse;
import com.ordermgtsystem.repository.StockRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {

    private final StockRepository stockRepository;

    public ProductCatalogServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListItemResponse> getProductList() {
        return stockRepository.findAllWithProduct().stream()
                .map(stock -> new ProductListItemResponse(
                        stock.getProduct().getId(),
                        stock.getProduct().getName(),
                        stock.getProduct().getPrice(),
                        stock.getQuantity()
                ))
                .toList();
    }
}
