package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
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
            cart.getCartItems().add(new CartItem(productDao.getProduct(productId), quantity));
        });

        recalculateCartTotalPrice(cart);
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

    @Override
    public void update(Cart cart, long productId, int quantity) {
        Product product = productDao.getProduct(productId);
        if (product.getStock() < quantity) {
            throw new OutOfStockException("Not enough stock! Available " + product.getStock());
        }
        getCurrentCartItemById(productId, cart).ifPresentOrElse(cartItem -> {
            cartItem.setQuantity(quantity);
            recalculateCartTotalPrice(cart);
        }, () -> {
            cart.getCartItems().add(new CartItem(product, quantity));
        });
    }

    @Override
    public void delete(Cart cart, Long productId) {
        Product product = productDao.getProduct(productId);
        Optional<CartItem> cartItemOptional = getCurrentCartItemById(productId, cart);
        cartItemOptional.ifPresent(cartItem -> cart.getCartItems().remove(cartItem));
        recalculateCartTotalPrice(cart);
    }

    private Optional<CartItem> getCurrentCartItemById(Long productId, Cart cart) {
        return cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findAny();
    }

    private void recalculateCartTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
    }
}
