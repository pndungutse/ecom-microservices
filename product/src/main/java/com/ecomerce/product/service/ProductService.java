package com.ecomerce.product.service;

import com.ecomerce.product.dto.ProductRequest;
import com.ecomerce.product.dto.ProductResponse;
import com.ecomerce.product.model.Product;
import com.ecomerce.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromProductRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(product -> {
                    updateProductFromProductRequest(product, productRequest);
                    Product savedProduct = productRepository.save(product);
                    return mapToProductResponse(savedProduct);
                });
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        productResponse.setActive(savedProduct.isActive());

        return productResponse;
    }

    private void updateProductFromProductRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setActive(productRequest.isActive());
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrue()
                .stream().map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
