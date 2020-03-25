package com.es.phoneshop.service;

import com.es.phoneshop.model.recentlyViewedProducts.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedProductsService {
    RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request);

    void addProduct(Long productId, RecentlyViewedProducts recentlyViewedProducts);
}
