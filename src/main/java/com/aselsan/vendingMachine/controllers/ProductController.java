/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author asimk
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductRepository productRepository;
    
    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    
    @GetMapping
    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product newProduct){
        return this.productRepository.save(newProduct);
    }
    
    
}
