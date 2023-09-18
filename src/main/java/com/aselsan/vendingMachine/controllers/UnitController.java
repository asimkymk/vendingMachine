/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Unit;
import com.aselsan.vendingMachine.repositories.UnitRepository;
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
@RequestMapping("/units")
public class UnitController {
    private UnitRepository unitRepository;
    
    public UnitController(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }
    @GetMapping
    public List<Unit> getAllUnits(){
        return this.unitRepository.findAll();
    }
    
    @PostMapping
    public Unit createUnit(@RequestBody Unit newUnit){
        return this.unitRepository.save(newUnit);
    }
    
    @GetMapping("/{unitId}")
    public Unit getOneProduct(@PathVariable Long unitId){
        
        //custom exception
        return this.unitRepository.findById(unitId).orElse(null);
    }
    
    @PutMapping("/{unitId}")
    public Unit updateOneUser(@PathVariable Long unitId, @RequestBody Unit newUnit){
        Optional<Unit> unit = this.unitRepository.findById(unitId);
        if(unit.isPresent()){
            Unit foundUnit = unit.get();
            foundUnit.setUnitAmount(newUnit.getUnitAmount());
            foundUnit.setUnitPrice(newUnit.getUnitPrice());
            this.unitRepository.save(foundUnit);
            return foundUnit;
        }
        else{
            return null;
        }
    }
    
    @DeleteMapping("/{unitId}")
    public void deleteOneProduct(@PathVariable Long unitId){
        this.unitRepository.deleteById(unitId);
    }
}
