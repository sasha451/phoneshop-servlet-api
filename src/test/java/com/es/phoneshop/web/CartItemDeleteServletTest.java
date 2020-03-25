package com.es.phoneshop.web;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.model.cart.Cart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartItemDeleteServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @InjectMocks
    private CartItemDeleteServlet cartItemDeleteServlet = new CartItemDeleteServlet();

    @Before
    public void setup() {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getPathInfo()).thenReturn("/1");
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        cartItemDeleteServlet.doPost(request, response);

        verify(response).sendRedirect("/phoneshop-servlet-api/cart?message=Products were deleted successfully");
    }
}
