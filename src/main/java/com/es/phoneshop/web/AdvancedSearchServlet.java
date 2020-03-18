package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.enums.AdvancedSearchOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchServlet extends HttpServlet {
    private ProductDao productDao;

    @Override
    public void init() throws ServletException {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String descriptionField = null;
        BigDecimal maxPrice = null;
        BigDecimal minPrice = null;
        AdvancedSearchOption advancedSearchOption = null;
        Map<String, String> errors = new HashMap<>();
        try {
            descriptionField = request.getParameter("description");
            minPrice = getMinPrice(request);
            maxPrice = getMaxPrice(request);
            advancedSearchOption = getAdvancedSearchOption(request);
        } catch (NumberFormatException e) {
            errors.put("error", "Wrong price value");
            request.setAttribute("errors", errors);
        }
        request.setAttribute("products", productDao.advancedSearch(descriptionField, minPrice, maxPrice,
                advancedSearchOption));
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearchPage.jsp").forward(request, response);
    }

    private BigDecimal getMaxPrice(HttpServletRequest request) {
        String maxPriceString = request.getParameter("maxPrice");
        Integer maxPrice = Integer.parseInt(maxPriceString);
        return new BigDecimal(maxPrice);
    }

    private BigDecimal getMinPrice(HttpServletRequest request) {
        String minPriceString = request.getParameter("minPrice");
        Integer minPrice = Integer.parseInt(minPriceString);
        return new BigDecimal(minPrice);
    }

    private AdvancedSearchOption getAdvancedSearchOption(HttpServletRequest request) {
        String advancedSearchOption = request.getParameter("advancedSearchOptions");
        advancedSearchOption = advancedSearchOption.replaceAll(" ", "_");
        return AdvancedSearchOption.valueOf(advancedSearchOption.toUpperCase());
    }
}
