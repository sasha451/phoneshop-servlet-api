package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.service.RecentlyViewedProductsService;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.recentlyViewedProducts.RecentlyViewedProducts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Deque;
import java.util.Optional;

public class DefaultRecentlyViewedProductsService implements RecentlyViewedProductsService {

    private ProductDao productDao;
    private static final String RECENT_VIEWS_ATTRIBUTE = "recentViews";

    private DefaultRecentlyViewedProductsService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultRecentlyViewedProductsService getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final DefaultRecentlyViewedProductsService instance = new DefaultRecentlyViewedProductsService();
    }

    @Override
    public RecentlyViewedProducts getRecentlyViewedProducts(HttpServletRequest request) {
        HttpSession session = request.getSession();
        RecentlyViewedProducts recentlyViewedProducts =
                (RecentlyViewedProducts) session.getAttribute(RECENT_VIEWS_ATTRIBUTE);
        if (recentlyViewedProducts == null) {
            recentlyViewedProducts = new RecentlyViewedProducts();
            session.setAttribute(RECENT_VIEWS_ATTRIBUTE, recentlyViewedProducts);
        }
        return recentlyViewedProducts;
    }

    @Override
    public void addProduct(Long productId, RecentlyViewedProducts recentlyViewedProducts) {
        Product product = productDao.getProduct(productId);
        Deque<Product> recentViewsProductList = recentlyViewedProducts.getRecentlyViewedProducts();

        Optional<Product> productOptional = recentViewsProductList.stream()
                .filter(p -> productId.equals(p.getId()))
                .findAny();

        recentViewsProductList.addFirst(product);
        if (productOptional.isPresent()) {
            recentViewsProductList.removeLastOccurrence(product);
        } else if (recentViewsProductList.size() > 3) {
            recentViewsProductList.removeLast();
        }
    }
}
