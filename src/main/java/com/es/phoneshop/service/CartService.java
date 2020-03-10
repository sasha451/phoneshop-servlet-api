package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    void add(Cart cart, Long productId, int quantity);

    Cart getCart(HttpServletRequest request);

    void update(Cart cart, long productId, int quantity);

    void delete(Cart cart, Long productId);
}
