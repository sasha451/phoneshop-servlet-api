package com.es.phoneshop.dao;

import com.es.phoneshop.model.cart.Cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    void add(Cart cart, Long productId, int quantity);

    Cart getCart(HttpServletRequest request);
}
