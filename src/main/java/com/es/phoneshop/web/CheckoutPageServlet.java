package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.enums.PaymentMethod;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;

    @Override
    public void init() {
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> parametersMap = getParametersMap(request);
        parametersMap.entrySet()
                .forEach(entry -> parametersExistenceCheck(entry, errors));

        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);

        setOrderItems(order, parametersMap, errors);
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cart.getCartItems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            response.sendRedirect("/phoneshop-servlet-api/order/overview/" + order.getSecureId());
        }
        else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private void setOrderItems(Order order, Map<String, String> parameters, Map<String, String> errors) {
        parameters.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("deliveryDate") && !entry.getKey().equals("paymentMethod"))
                .forEach(entry -> {
                    try {
                        BeanUtils.setProperty(order, entry.getKey(), entry.getValue());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        errors.put("error", "Internal server error");
                    }
                });
        try {
            LocalDate deliveryDate = parseDate(parameters.get("deliveryDate"));
            order.setDeliveryDate(deliveryDate);
        } catch (DateTimeParseException e) {
            errors.put("deliveryDate", "Wrong date format. Date should be like 'dd.MM.uuuu'");
        }
        PaymentMethod paymentMethod = PaymentMethod.valueOf(parameters.get("paymentMethod"));
        order.setPaymentMethod(paymentMethod);
    }

    private LocalDate parseDate(String deliveryDate) throws DateTimeParseException {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return LocalDate.parse(deliveryDate, pattern);
    }

    private Map<String, String> getParametersMap(HttpServletRequest request) {
        List<String> parameterNames = Collections.list(request.getParameterNames());
        Map<String, String> parametersMap = new HashMap<>();
        for (String parameterName : parameterNames) {
            parametersMap.put(parameterName, request.getParameter(parameterName));
        }
        return parametersMap;
    }

    private void parametersExistenceCheck(Map.Entry entry, Map<String, String> errors) {
        if (entry.getValue() == null || entry.getValue() == "") {
            errors.put(entry.getKey().toString(), entry.getKey() + " should not be empty");
        }
    }
}
