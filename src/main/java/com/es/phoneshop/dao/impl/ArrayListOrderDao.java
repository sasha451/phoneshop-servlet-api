package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.Order;

import java.util.NoSuchElementException;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {

    private ArrayListOrderDao() {
    }

    public static ArrayListOrderDao getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final ArrayListOrderDao instance = new ArrayListOrderDao();
    }

    @Override
    public Order getOrderBySecureId(String secureId) {
        if (secureId == null || secureId.equals("")) {
            throw new IllegalArgumentException("SecureId should not be null or empty");
        }
        return items.stream()
                .filter(order -> order.getSecureId().equals(secureId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Item with secureId" + secureId + "was not found"));
    }
}
