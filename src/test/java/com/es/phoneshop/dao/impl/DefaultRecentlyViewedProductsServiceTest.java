package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyViewedProducts.RecentlyViewedProducts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentlyViewedProductsServiceTest {

    private static final String RECENT_VIEWS_ATTRIBUTE = "recentViews";
    Deque<Product> recentViewsProductList;

    @Mock
    HttpServletRequest request;
    @Mock
    HttpSession httpSession;
    @Mock
    ProductDao productDao;
    @Mock
    Product product1;
    @Mock
    Product product2;
    @Mock
    Product product3;
    @Mock
    Product product4;
    @Mock
    RecentlyViewedProducts recentlyViewedProducts;
    @InjectMocks
    private DefaultRecentlyViewedProductsService defaultRecentlyViewedProductsService =
            DefaultRecentlyViewedProductsService.getInstance();

    @Before
    public void setup() {
        recentViewsProductList = new ConcurrentLinkedDeque<>();
        recentViewsProductList.add(product1);
        recentViewsProductList.add(product2);

        when(product1.getId()).thenReturn(1L);

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(RECENT_VIEWS_ATTRIBUTE)).thenReturn(null);

        when(productDao.getProduct(1L)).thenReturn(product1);
        when(productDao.getProduct(4L)).thenReturn(product4);
        when(recentlyViewedProducts.getRecentlyViewedProducts()).thenReturn(recentViewsProductList);
    }

    @Test
    public void testGetRecentlyViewedProducts() {
        defaultRecentlyViewedProductsService.getRecentlyViewedProducts(request);
        verify(httpSession).setAttribute(eq(RECENT_VIEWS_ATTRIBUTE), any(RecentlyViewedProducts.class));
    }

    @Test
    public void testAddProduct() {
        defaultRecentlyViewedProductsService.addProduct(1L, recentlyViewedProducts);

        assertEquals(recentlyViewedProducts.getRecentlyViewedProducts(), recentViewsProductList);
    }

    @Test
    public void testAddFourthProduct() {
        recentViewsProductList.add(product3);

        defaultRecentlyViewedProductsService.addProduct(4L, recentlyViewedProducts);

        assertTrue(recentlyViewedProducts.getRecentlyViewedProducts().contains(product4)
                && !recentlyViewedProducts.getRecentlyViewedProducts().contains(product3));
    }
}
