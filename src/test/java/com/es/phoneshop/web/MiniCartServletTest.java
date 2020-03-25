package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Cart cart;
    @Mock
    private CartService cartService;
    @InjectMocks
    private MiniCartServlet servlet = new MiniCartServlet();

    @Before
    public void setup() {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getRequestDispatcher("/WEB-INF/components/miniCart.jsp")).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute("cart", cart);
        verify(request).getRequestDispatcher("/WEB-INF/components/miniCart.jsp");
        verify(requestDispatcher).include(request, response);

    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);
        verify(request).setAttribute("cart", cart);
        verify(request).getRequestDispatcher("/WEB-INF/components/miniCart.jsp");
        verify(requestDispatcher).include(request, response);

    }
}
