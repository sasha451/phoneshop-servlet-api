package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.OrderService;

import java.util.ArrayList;
import java.util.UUID;

public class DefaultOrderService implements OrderService {

    private OrderDao orderDao;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static DefaultOrderService getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final DefaultOrderService instance = new DefaultOrderService();
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setId(UUID.randomUUID().getMostSignificantBits());
        order.setCartItems(new ArrayList<>(cart.getCartItems()));
        order.setTotalPrice(cart.getTotalPrice());
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        return orderDao.getOrderBySecureId(secureId);
    }
}
