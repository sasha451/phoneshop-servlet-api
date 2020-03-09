package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.CartService;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class DefaultCartService implements CartService {

    private ProductDao productDao;
    private static final String CART_ATTRIBUTE = "cartAttribute";

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final DefaultCartService instance = new DefaultCartService();
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Invalid quantity value");
        }
        int quantityInCartItem = getCurrentCartItemById(productId, cart).map(CartItem::getQuantity).orElse(0);
        if (productDao.getProduct(productId).getStock() < quantityInCartItem + quantity) {
            throw new OutOfStockException("Not enough stock! Available " +
                    (productDao.getProduct(productId).getStock() - quantityInCartItem));
        }
        getCurrentCartItemById(productId, cart).ifPresentOrElse(cartItem -> {
            cartItem.setQuantity(quantityInCartItem + quantity);
        }, () -> {
            cart.getCartItems().add(new CartItem(productId, quantity));
        });
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Cart cart = (Cart) httpSession.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            httpSession.setAttribute(CART_ATTRIBUTE, cart);
        }
        return cart;
    }

    private Optional<CartItem> getCurrentCartItemById(Long productId, Cart cart) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findAny();
    }
}
