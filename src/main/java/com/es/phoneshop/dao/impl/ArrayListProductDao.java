package com.es.phoneshop.dao.impl;

import com.es.phoneshop.model.enums.SortField;
import com.es.phoneshop.model.enums.SortOrder;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private List<Product> productList = new ArrayList<>();

    private ArrayListProductDao() {
    }

    public static ArrayListProductDao getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final ArrayListProductDao instance = new ArrayListProductDao();
    }

    @Override
    public synchronized Product getProduct(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        return findProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " was not found"));
    }

    private List<Product> defaultSearch() {
        return productList.stream()
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

    @Override
    public synchronized void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product should not be null");
        }
        if (!productAlreadyExistenceCheck(product)) {
            productList.add(product);
        } else {
            throw new IllegalArgumentException("Product with id " + product.getId() + " is already exists");
        }
    }

    private boolean productAlreadyExistenceCheck(Product product) {
        return productList.stream()
                .anyMatch(productFromList -> productFromList.getId().equals(product.getId()));
    }

    List<Product> getProductList() {
        return productList;
    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        findProductById(id)
                .map(product -> productList.remove(product))
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " was not found"));
    }

    private Optional<Product> findProductById(Long id) {
        return productList.stream().
                filter(product -> product.getId().equals(id))
                .findAny();
    }
}
