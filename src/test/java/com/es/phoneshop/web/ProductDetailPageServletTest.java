package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyViewedProducts.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedProductsService;
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
import java.util.Collections;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private Product product1;
    @Mock
    RecentlyViewedProductsService recentlyViewedProductsService;
    @Mock
    RecentlyViewedProducts recentlyViewedProducts;
    @Mock
    Cart cart;
    @Mock
    CartService cartService;

    @InjectMocks
    private ProductDetailPageServlet servlet = new ProductDetailPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp")).thenReturn(requestDispatcher);
        when(productDao.getProduct(1L)).thenReturn(product1);
        when(request.getPathInfo()).thenReturn("/1");
        when(productDao.getProduct(2L)).thenThrow(NoSuchElementException.class);

        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.UK);

        when(recentlyViewedProductsService.getRecentlyViewedProducts(request)).thenReturn(recentlyViewedProducts);
        when(recentlyViewedProductsService.getRecentlyViewedProducts(request).getRecentlyViewedProducts())
                .thenReturn(new ConcurrentLinkedDeque<>(Collections.singletonList(product1)));
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(productDao).getProduct(1L);
        verify(request).setAttribute("product", productDao.getProduct(1L));
        verify(request).setAttribute("recentProducts", recentlyViewedProducts.getRecentlyViewedProducts());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNonExistedProduct() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/2");

        servlet.doGet(request, response);

        verify(productDao).getProduct(2L);
        verify(response).sendError(404);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("1");

        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostNotNumber() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("one");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Not a number");
    }

    @Test
    public void testDoPostOutOfStock() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("103");
        doThrow(new OutOfStockException("Not enough stock!")).when(cartService).add(cart, 1L, 103);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), anyString());
    }
}
