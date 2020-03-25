package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] quantities = request.getParameterValues("quantity");
        String[] productIds = request.getParameterValues("productId");
        NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());

        Cart cart = cartService.getCart(request);
        Map<Long, String> errors = new HashMap<>();
        for (int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);
            int quantity;
            try {
                quantity = numberFormat.parse(quantities[i]).intValue();
            } catch (ParseException e) {
                errors.put(productId, "Not a number");
                continue;
            }
            try {
                cartService.update(cart, productId, quantity);
            } catch (OutOfStockException e) {
                errors.put(productId, e.getMessage());
            }
        }
        if (errors.isEmpty()) {
            String message = "Products added to cart successfully";
            response.sendRedirect(request.getRequestURI() + "?message=" + message);
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }
}
