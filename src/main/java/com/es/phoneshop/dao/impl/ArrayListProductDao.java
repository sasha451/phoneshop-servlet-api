package com.es.phoneshop.dao.impl;

import com.es.phoneshop.model.enums.AdvancedSearchOption;
import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayListProductDao extends AbstractDao<Product> implements ProductDao {

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }

    private List<Product> advancedSearchByDescriptionAllWords(String descriptionField) {
        return items.stream()
                .filter(item -> item.getDescription().equals(descriptionField))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> advancedSearch(String descriptionField, BigDecimal minPrice, BigDecimal maxPrice, AdvancedSearchOption advancedSearchOption) {
        if (descriptionField == null && minPrice == null && maxPrice == null) {
            return new ArrayList<>();
        }
        else {
            if (advancedSearchOption != null && advancedSearchOption.equals(AdvancedSearchOption.ANY_WORD) && descriptionField != null
                    && maxPrice == null && minPrice == null) {
                return querySearch(descriptionField);
            }
        }
        if (advancedSearchOption != null && advancedSearchOption.equals(AdvancedSearchOption.ALL_WORDS) && descriptionField != null
                && maxPrice == null && minPrice == null) {
            return advancedSearchByDescriptionAllWords(descriptionField);
        }
        else if (descriptionField == null && maxPrice == null) {
            return advancedSearchByMinPrice(minPrice);
        } else if (descriptionField == null && minPrice == null) {
            return advancedSearchByMaxPrice(maxPrice);
        }
        if (descriptionField != null && advancedSearchOption != null
                && advancedSearchOption.equals(AdvancedSearchOption.ANY_WORD)) {
            return advancedSearchAllFieldsAnyWord(descriptionField, maxPrice, minPrice);
        }
       return advancedSearchAllFieldsAllWords(descriptionField, maxPrice, minPrice);
    }

    private List<Product> advancedSearchAllFieldsAllWords(String description, BigDecimal maxPrice,
                                                          BigDecimal minPrice) {
        return items.stream()
                .filter(item -> item.getDescription().equals(description))
                .filter(item -> item.getPrice().compareTo(minPrice) > 0)
                .filter(item -> item.getPrice().compareTo(maxPrice) < 0)
                .collect(Collectors.toList());
    }
    private List<Product> advancedSearchAllFieldsAnyWord(String description, BigDecimal maxPrice,
                                                          BigDecimal minPrice) {
        return querySearch(description).stream()
                .filter(item -> item.getPrice().compareTo(minPrice) > 0)
                .filter(item -> item.getPrice().compareTo(maxPrice) < 0)
                .collect(Collectors.toList());
    }

    private List<Product> advancedSearchByMinPrice(BigDecimal minPrice) {
        return items.stream()
                .filter(item -> item.getPrice().compareTo(minPrice) > 0)
                .collect(Collectors.toList());
    }

    private List<Product> advancedSearchByMaxPrice(BigDecimal maxPrice) {
        return items.stream()
                .filter(item -> item.getPrice().compareTo(maxPrice) < 0)
                .collect(Collectors.toList());
    }

    private List<Product> defaultSearch() {
        return items.stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }

    private List<Product> querySearch(String query) {
        String[] words = query.split(" ");
        return defaultSearch().stream()
                .collect(Collectors.toMap(Function.identity(), product -> Arrays.stream(words)
                        .filter(word -> product.getDescription().contains(word))
                        .count()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Product> sortProducts(SortField sortField, SortOrder sortOrder, List<Product> productList) {
        Comparator<Product> comparator = null;
        switch (sortField) {
            case DESCRIPTION:
                comparator = Comparator.comparing(product -> product.getDescription().toLowerCase());
                break;
            case PRICE:
                comparator = Comparator.comparing(Product::getPrice);
                break;
        }

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        productList.sort(comparator);
        return productList;
    }

    @Override
    public synchronized List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        if (query != null && sortField == null) {
            return querySearch(query);
        } else if (query == null && sortField == null) {
            return defaultSearch();
        } else if (query == null) {
            return sortProducts(sortField, sortOrder, defaultSearch());
        } else
            return sortProducts(sortField, sortOrder, querySearch(query));
    }

    List<Product> getItems() {
        return items;
    }
}
