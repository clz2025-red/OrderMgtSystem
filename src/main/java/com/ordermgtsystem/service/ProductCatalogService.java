package com.ordermgtsystem.service;

import com.ordermgtsystem.dto.ProductListItemResponse;
import java.util.List;

public interface ProductCatalogService {

    List<ProductListItemResponse> getProductList();
}
