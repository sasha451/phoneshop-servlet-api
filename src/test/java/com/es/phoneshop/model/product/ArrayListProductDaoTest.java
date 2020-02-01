package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ArrayListProductDaoTest
{
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

        when(product1.getId()).thenReturn(1L);
        when(product1.getPrice()).thenReturn(new BigDecimal(100));
        when(product1.getStock()).thenReturn(100);

        when(product3.getId()).thenReturn(3L);
        when(product3.getPrice()).thenReturn(new BigDecimal(300));
        when(product3.getStock()).thenReturn(300);
    }

    @Test
    public void testGetProduct() {
        product2 =  productDao.getProduct(1L);
        assertEquals(product1.getId(), product2.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductException() {
        productDao.getProduct(2L);
    }

    @Test
    public void testFindProducts() {
        assertEquals(productDao.findProducts().size(), 1);
    }

    @Test
    public void testSave() {
        productDao.save(product3);
        assertEquals(productDao.findProducts().size(), 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveException() {
        productDao.save(product1);
    }

    @Test
    public void deleteTest() {
        productDao.delete(1L);
        assertEquals(productDao.findProducts().size(), 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDelete() {
        productDao.getProduct(5L);
    }
}
