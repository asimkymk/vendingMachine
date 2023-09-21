package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    private final ProductRepository productRepository;
    
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ApiResponse<>(true, "Success", products);
        } catch (Exception e) {
            logger.error("Error fetching all products: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }
    
    @CrossOrigin
    @PostMapping
    @Transactional
    public ApiResponse<Product> createProduct(@RequestBody Product newProduct) {
        try {
            Product product = productRepository.save(newProduct);
            return new ApiResponse<>(true, "Product created successfully", product);
        } catch (Exception e) {
            logger.error("Error creating product: ", e);
            return new ApiResponse<>(false, "Failed to create product", null);
        }
    }
    
    @GetMapping("/{productId}")
    public ApiResponse<Product> getOneProduct(@PathVariable Long productId) {
        try {
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                return new ApiResponse<>(true, "Product found", product.get());
            }
            return new ApiResponse<>(false, "Product not found", null);
        } catch (Exception e) {
            logger.error("Error fetching product by id: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }
    
    @CrossOrigin
    @PutMapping("/{productId}")
    @Transactional
    public ApiResponse<Product> updateOneProduct(@PathVariable Long productId, @RequestBody Product newProduct) {
        try {
            Optional<Product> product = productRepository.findById(productId);
            if (product.isPresent()) {
                Product foundProduct = product.get();
                foundProduct.setProductAmount(newProduct.getProductAmount());
                productRepository.save(foundProduct);
                return new ApiResponse<>(true, "Product updated successfully", foundProduct);
            }
            return new ApiResponse<>(false, "Product not found", null);
        } catch (Exception e) {
            logger.error("Error updating product: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }
    
    @CrossOrigin
    @PutMapping("/buyProduct/{productId}")
    @Transactional
    public ApiResponse<Product> buyProduct(@PathVariable Long productId) {
        try {
            Optional<Product> product = this.productRepository.findById(productId);
            if (product.isPresent()) {
                Product foundProduct = product.get();
                foundProduct.setProductAmount(foundProduct.getProductAmount() - 1);
                this.productRepository.save(foundProduct);
                return new ApiResponse<>(true, "The product has been purchased successfully.", foundProduct);
            } else {
                return new ApiResponse<>(false, "The product has not been purchased successfully.", null);
            }
        } catch (Exception e) {
            logger.error("Error purchasing product: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    @DeleteMapping("/{productId}")
    @Transactional
    public ApiResponse<String> deleteOneProduct(@PathVariable Long productId) {
        try {
            this.productRepository.deleteById(productId);
            return new ApiResponse<>(true, "Product deleted successfully", null);
        } catch (Exception e) {
            logger.error("Error deleting product: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }
}
