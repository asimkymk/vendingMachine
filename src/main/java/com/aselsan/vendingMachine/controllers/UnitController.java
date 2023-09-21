/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Unit;
import com.aselsan.vendingMachine.repositories.UnitRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping
    public List<Unit> getAllUnits() {
        return this.unitRepository.findAll();
    }

    @GetMapping("/activeWallet")
    public Map<String, Integer> getActiveWallett() {
        List<Unit> units = this.unitRepository.findAll();
        int activeWallet = 0;
        for (int i = 0; i < units.size(); i++) {

            // Print all elements of List
            activeWallet += units.get(i).getTempAmount() * units.get(i).getUnitPrice();
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("walletAmount", activeWallet);
        return response;
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit newUnit) {
        return this.unitRepository.save(newUnit);
    }

    @GetMapping("/{unitId}")
    public Unit getOneProduct(@PathVariable Long unitId) {

        //custom exception
        return this.unitRepository.findById(unitId).orElse(null);
    }

    @PutMapping("/{unitId}")
    public Unit updateOneUser(@PathVariable Long unitId, @RequestBody Unit newUnit) {
        Optional<Unit> unit = this.unitRepository.findById(unitId);
        if (unit.isPresent()) {
            Unit foundUnit = unit.get();
            foundUnit.setUnitAmount(newUnit.getUnitAmount());
            foundUnit.setUnitPrice(newUnit.getUnitPrice());
            foundUnit.setTempAmount(newUnit.getTempAmount());
            this.unitRepository.save(foundUnit);
            return foundUnit;
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PutMapping("/activeWallet/{unitId}")
    public Map<String, Boolean> addUnitToWallet(@PathVariable Long unitId) {
        Optional<Unit> unit = this.unitRepository.findById(unitId);
        Map<String, Boolean> response = new HashMap<>();
        if (unit.isPresent()) {
            Unit foundUnit = unit.get();
            int tempAmount = foundUnit.getTempAmount() + 1;
            foundUnit.setTempAmount(tempAmount);
            this.unitRepository.save(foundUnit);
            response.put("process", true);
            return response;
        } else {
            response.put("process", false);
            return response;
        }
    }
    @CrossOrigin
    @PostMapping("/activeWallet/refund")
    public Map<String, Object> getRefund(@RequestBody Map<String, Integer> requestBody) {
        Sort sort = Sort.by(Sort.Direction.DESC, "unitPrice");
        List<Unit> units = this.unitRepository.findAll(sort);
        int activeWallet = 0;
        Map<String, Object> response = new HashMap<>();
        int refundAmount;
        try{
            refundAmount = requestBody.get("refundAmount");
        }
        catch(Exception e){
            return null;
        }
        
        for (int i = 0; i < units.size(); i++) {

            // Print all elements of List
            activeWallet += units.get(i).getTempAmount() * units.get(i).getUnitPrice();
        }
        if (activeWallet == refundAmount) {
            for (int i = 0; i < units.size(); i++) {
                units.get(i).setTempAmount(0);
                this.unitRepository.save(units.get(i));
            }
            response.put("process", true);
            response.put("wallet", refundAmount);
            response.put("message", "Refund process is completed successfuly.");
            return response;
        } else {
            int index = 0;
            int tmp = refundAmount;
            while(tmp>0 && index <units.size()){
                Unit unit = units.get(index);
                
                int calculatedUnitAmount = tmp / unit.getUnitPrice();
                if (unit.getUnitAmount() > calculatedUnitAmount) {
                    tmp %= unit.getUnitPrice();
                    unit.setUnitAmount(unit.getUnitAmount() - calculatedUnitAmount);
                    this.unitRepository.save(unit);
                }
                index++;
            }
            if(tmp == 0){
                response.put("process", true);
                response.put("wallet", refundAmount);
                response.put("message", "Refund process is completed succesfully.");
                return response;
            }
            response.put("process", false);
            response.put("wallet", refundAmount - tmp);
            response.put("message", "Refund process is not completed successfuly because there is no enough money for the refund procces in the machine..");
            return response;

        }
    }
    
    @CrossOrigin
    @PutMapping("/activeWallet/buyProduct")
    public Map<String, Object> buyProduct() {
        Sort sort = Sort.by(Sort.Direction.DESC, "unitPrice");
        List<Unit> units = this.unitRepository.findAll(sort);
       
        Map<String, Object> response = new HashMap<>();
        
        for (int i = 0; i < units.size(); i++) {

            Unit unit = units.get(i);
            
            int tmp = unit.getTempAmount();
            unit.setTempAmount(0);
            unit.setUnitAmount(unit.getUnitAmount() + tmp);
            this.unitRepository.save(unit);
            
        }
        
        response.put("process", true);
        response.put("message", "Buying process is completed successfuly.");
        return response;
    
    }

    @DeleteMapping("/{unitId}")
    public void deleteOneProduct(@PathVariable Long unitId) {
        this.unitRepository.deleteById(unitId);
    }
}
