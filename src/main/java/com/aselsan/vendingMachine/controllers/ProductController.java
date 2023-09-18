/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @GetMapping("/{productId}")
    public Product getOneProduct(@PathVariable Long productId){
        
        //custom exception
        return this.productRepository.findById(productId).orElse(null);
    }
    
    @PutMapping("/{productId}")
    public Product updateOneUser(@PathVariable Long productId, @RequestBody Product newProduct){
        Optional<Product> product = this.productRepository.findById(productId);
        if(product.isPresent()){
            Product foundUser = product.get();
            foundUser.setProductAmount(newProduct.getProductAmount());
            this.productRepository.save(foundUser);
            return foundUser;
        }
        else{
            return null;
        }
    }
    
    @DeleteMapping("/{productId}")
    public void deleteOneProduct(@PathVariable Long productId){
        this.productRepository.deleteById(productId);
    }
}
