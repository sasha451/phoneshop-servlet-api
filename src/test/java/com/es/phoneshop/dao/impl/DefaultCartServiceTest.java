package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    private static final String CART_ATTRIBUTE = "cartAttribute";

    private ArrayList<CartItem> cartItems;
    @Mock
    private ProductDao productDao;
    @Mock
    CartItem cartItem1;
    @Mock
    CartItem cartItem2;
    @Mock
    HttpSession httpSession;
    @Mock
    HttpServletRequest request;
    @Mock
    Cart cart;
    @Mock
    Product product1;
    @Mock
    Product product5;
    @InjectMocks
    private DefaultCartService defaultCartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        when(cartItem1.getProductId()).thenReturn(1L);
        when(cartItem1.getQuantity()).thenReturn(15);

        cartItems = new ArrayList<>();
        cartItems.addAll(Arrays.asList(cartItem1, cartItem2));
        when(cart.getCartItems()).thenReturn(cartItems);

        when(productDao.getProduct(1L)).thenReturn(product1);
        when(productDao.getProduct(5L)).thenReturn(product5);
        when(product1.getStock()).thenReturn(100);
        when(product5.getStock()).thenReturn(30);

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(CART_ATTRIBUTE)).thenReturn(null);
    }

    @Test
    public void testGetCart() {
        defaultCartService.getCart(request);
        verify(httpSession).setAttribute(eq(CART_ATTRIBUTE), any(Cart.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullQuantity() {
        defaultCartService.add(cart, 5L, 0);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddTooBigQuantity() {
        defaultCartService.add(cart, 1L, 90);
    }

    @Test
    public void testAddExistenceProduct() {
        defaultCartService.add(cart, 1L, 40);

        verify(cartItem1).setQuantity(55);
    }

    @Test
    public void testAddProduct() {
        defaultCartService.add(cart, 5L, 20);

        Optional<CartItem> cartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(5L))
                .findAny();
        assertTrue(cartItemOptional.isPresent());
    }
}
