/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.entities.Supplier;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import com.aselsan.vendingMachine.repositories.SupplierRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import com.aselsan.vendingMachine.utils.HashUtil;
import com.aselsan.vendingMachine.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author asimk
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ProductRepository productRepository;
    @CrossOrigin
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody Supplier supplier) {
        String hashedPassword = HashUtil.hashPassword(supplier.getPassword());
        Supplier existingSupplier = supplierRepository.findByUsernameAndPassword(supplier.getUsername(), hashedPassword);
        if (existingSupplier != null) {
            String token = JwtUtil.generateToken(existingSupplier.getUsername());
            return new ApiResponse<>(true, "Giriş başarılı", token);
        } else {
            return new ApiResponse<>(false, "Kullanıcı adı veya şifre yanlış", null);
        }
    }
    
    @PutMapping("/updateProduct")
    public ApiResponse<Product> updateProduct(@RequestBody Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);
        if (existingProduct != null) {
            existingProduct.setProductName(product.getProductName());
            existingProduct.setProductPrice(product.getProductPrice());
            existingProduct.setProductAmount(product.getProductAmount());
            existingProduct.setProductImageUrl(product.getProductImageUrl());
            productRepository.save(existingProduct);
            return new ApiResponse<>(true, "Ürün başarıyla güncellendi", existingProduct);
        } else {
            return new ApiResponse<>(false, "Ürün bulunamadı", null);
        }
    }
    @GetMapping("/validateToken")
    @CrossOrigin
    public ApiResponse<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            Claims claims = JwtUtil.parseToken(token);
            Date expiration = claims.getExpiration();
            if (expiration.after(new Date())) {
                return new ApiResponse<>(true, "Token is valid", true);
            } else {
                return new ApiResponse<>(false, "Token has expired", false);
            }
        } catch (Exception e) {
            return new ApiResponse<>(false, "Invalid token", false);
        }
    }
    @GetMapping("/products")
    public ApiResponse<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ApiResponse<>(true, "Products fetched successfully", products);
    }
    
    @PostMapping("/products")
    @CrossOrigin
    public ApiResponse<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productRepository.save(product);
        return new ApiResponse<>(true, "Product added successfully", newProduct);
    }
    @PutMapping("/products/{id}")
    @CrossOrigin
    public ApiResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setProductName(product.getProductName());
            existingProduct.setProductPrice(product.getProductPrice());
            existingProduct.setProductAmount(product.getProductAmount());
            existingProduct.setProductImageUrl(product.getProductImageUrl());
            productRepository.save(existingProduct);
            return new ApiResponse<>(true, "Product updated successfully", existingProduct);
        } else {
            return new ApiResponse<>(false, "Product not found", null);
        }
    }
    @DeleteMapping("/products/{id}")
    @CrossOrigin
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
            return new ApiResponse<>(true, "Product deleted successfully", "Deleted");
        } catch (Exception e) {
            return new ApiResponse<>(false, "Product not found", null);
        }
    }
}
