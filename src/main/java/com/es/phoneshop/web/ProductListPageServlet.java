package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        SortField sortField = getSortField(request);
        SortOrder sortOrder = getSortOrder(request);

        request.setAttribute("products", productDao.findProducts(query, sortField, sortOrder));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    private SortField getSortField(HttpServletRequest request) {
        String sortField = request.getParameter("sortField");
        if (sortField == null) {
            return null;
        }
        return SortField.valueOf(sortField.toUpperCase());
    }

    private SortOrder getSortOrder(HttpServletRequest request) {
        String sortOrder = request.getParameter("sortOrder");
        if (sortOrder == null) {
            return null;
        }
        return SortOrder.valueOf(sortOrder.toUpperCase());
    }
}
