package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exceptions.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultRecentlyViewedProductsService;
import com.es.phoneshop.model.recentlyViewedProducts.RecentlyViewedProducts;
import com.es.phoneshop.service.RecentlyViewedProductsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

public class ProductDetailPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedProductsService recentlyViewedProductsService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        recentlyViewedProductsService = DefaultRecentlyViewedProductsService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = Long.valueOf(request.getPathInfo().substring(1));
            RecentlyViewedProducts recentlyViewedProducts =
                    recentlyViewedProductsService.getRecentlyViewedProducts(request);
            recentlyViewedProductsService.addProduct(productId, recentlyViewedProducts);
            request.setAttribute("recentProducts", recentlyViewedProducts.getRecentlyViewedProducts());
            request.setAttribute("product", productDao.getProduct(productId));
            Cart cart = cartService.getCart(request);
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
        } catch (NoSuchElementException | NumberFormatException e) {
            response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);

        String quantityString = request.getParameter("quantity");
        int quantity;
        try {
            NumberFormat numberFormat = NumberFormat.getInstance(request.getLocale());
            quantity = numberFormat.parse(quantityString).intValue();
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }

        Long productId = Long.valueOf(request.getPathInfo().substring(1));
        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
            return;
        }

        response.sendRedirect(request.getRequestURI() + "?success=true");
    }
}
