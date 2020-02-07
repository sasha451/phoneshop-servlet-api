package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ArrayListProductDaoTest {
    private ArrayListProductDao productDao;

    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        productDao = new ArrayListProductDao();
        productDao.getProductList().add(product1);
        productDao.getProductList().add(product3);

        when(product1.getId()).thenReturn(1L);
        when(product1.getPrice()).thenReturn(new BigDecimal(100));
        when(product1.getStock()).thenReturn(100);

        when(product2.getId()).thenReturn(2L);

        when(product3.getId()).thenReturn(3L);
        when(product3.getPrice()).thenReturn(null);
        when(product3.getStock()).thenReturn(300);
    }

    @Test
    public void testGetProduct() {
        assertEquals(product1.getId(), productDao.getProduct(1L).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetProductNullId() {
        productDao.getProduct(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNonExistentProduct() {
        productDao.getProduct(2L);
    }

    @Test
    public void testFindProducts() {
        assertEquals(productDao.findProducts(), Collections.singletonList(product1));
    }

    @Test
    public void testSave() {
        productDao.save(product2);
        assertTrue(productDao.getProductList().contains(product2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAlreadyExistentProduct() {
        productDao.save(product1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveProductNullId() {
        productDao.save(null);
    }

    @Test
    public void testDelete() {
        productDao.delete(1L);
        productDao.delete(3L);
        assertTrue(productDao.getProductList().isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteNonExistentProduct() {
        productDao.delete(5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteProductNullId() {
        productDao.delete(null);
    }
}
