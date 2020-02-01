package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private List<Product> productList = new ArrayList<>();

    @Override
    public synchronized Product getProduct(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        return findProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " was not found"));
    }

    @Override
    public synchronized List<Product> findProducts() {
        return productList.stream()
                .filter(product -> product.getPrice() != null && product.getStock() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized void save(Product product) {
        if(product == null) {
            throw new IllegalArgumentException("Product should not be null");
        }
        if (productList.stream()
                .noneMatch(productFromList -> productFromList.getId().equals(product.getId()))) {
            productList.add(product);
        }
        else {
            throw new IllegalArgumentException("Product with id " + product.getId() + " is already exists");
        }
     }

    List<Product> getProductList() {
        return productList;
    }

    @Override
    public synchronized void delete(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Id should not be null");
        }
        findProductById(id)
                .map(product -> productList.remove(product))
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " was not found"));
    }

    private Optional<Product> findProductById(Long id) {
        return  productList.stream().
                filter(product -> product.getId().equals(id))
                .findAny();
    }
}
