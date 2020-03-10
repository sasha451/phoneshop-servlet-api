package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;
    @Mock
    CartService cartService;
    @Mock
    Cart cart;
    @Mock
    RequestDispatcher requestDispatcher;
    @InjectMocks
    CartPageServlet cartPageServlet = new CartPageServlet();

    @Before
    public void setup() {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getRequestDispatcher("/WEB-INF/pages/cart.jsp")).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(Locale.UK);
        when(request.getParameterValues("productId")).thenReturn(new String[]{"1", "2"});
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        cartPageServlet.doGet(request, response);

        verify(request).setAttribute("cart", cart);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"3", "5"});

        cartPageServlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void doPostWithErrors() throws ServletException, IOException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"oo", "222"});
        doThrow(new OutOfStockException("Not enough stock!")).when(cartService).update(cart, 2L, 222);

        cartPageServlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any(HashMap.class));
    }
}
