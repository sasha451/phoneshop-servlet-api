package com.es.phoneshop.dao;

import com.es.phoneshop.model.enums.AdvancedSearchOption;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends Dao<Product> {
    Product get(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id);

    List<Product> advancedSearch(String descriptionField, BigDecimal minPrice, BigDecimal maxPrice,
                                 AdvancedSearchOption advancedSearchOption);
}
