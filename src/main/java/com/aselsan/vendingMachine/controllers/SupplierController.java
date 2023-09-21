/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Supplier;
import com.aselsan.vendingMachine.repositories.SupplierRepository;
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
@RequestMapping("/supplier")
public class SupplierController {
    private SupplierRepository supplierRepository;
    
    public SupplierController(SupplierRepository supplierRepository){
        this.supplierRepository = supplierRepository;
    }
    
    @GetMapping
    public List<Supplier> getAllSupplier(){
        return this.supplierRepository.findAll();
    }
    
    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier newSupplier){
        return this.supplierRepository.save(newSupplier);
    }
    
    
}
