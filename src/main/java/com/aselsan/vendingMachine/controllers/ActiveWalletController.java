/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.ActiveWallet;
import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.repositories.ActiveWalletRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
@RequestMapping("/activeWallet")
public class ActiveWalletController {

    private ActiveWalletRepository activeWalletRepository;

    public ActiveWalletController(ActiveWalletRepository activeWalletRepository) {
        this.activeWalletRepository = activeWalletRepository;
    }

    @GetMapping
    public ActiveWallet getActiveWallet() {
        return this.activeWalletRepository.findById(1l).orElse(null);
    }

    @PutMapping()
    @CrossOrigin
    public Map<String, Object> updateWallet(@RequestBody Map<String, Integer> requestBody) {
        Optional<ActiveWallet> activeWallet = this.activeWalletRepository.findById(1l);
        Map<String, Object> response = new HashMap<>();
        if (activeWallet.isPresent()) {
            ActiveWallet foundWallet = activeWallet.get();
            if (requestBody.get("add") != null) {
                foundWallet.setWalletAmount(foundWallet.getWalletAmount() + requestBody.get("add"));
            }
            if (requestBody.get("remove") != null) {
                foundWallet.setWalletAmount(foundWallet.getWalletAmount() - requestBody.get("remove"));
            }

            this.activeWalletRepository.save(foundWallet);
            response.put("process", true);
            return response;
        } else {
            response.put("process", false);
            return response;
        }
    }

    @PutMapping("/buyProduct")
    @CrossOrigin
    public Map<String, Object> buyProduct(@RequestBody Product product) {
        Optional<ActiveWallet> activeWallet = this.activeWalletRepository.findById(1l);
        Map<String, Object> response = new HashMap<>();
        if (product.getProductAmount() > 0) {
            if (activeWallet.isPresent()) {
                ActiveWallet foundWallet = activeWallet.get();
                if(product.getProductPrice()> foundWallet.getWalletAmount()){
                    response.put("process", false);
                    response.put("message", "Wallet balance is insufficient.");
                    return response;
                }
                else{
                    foundWallet.setWalletAmount(foundWallet.getWalletAmount() -  product.getProductPrice());
                    this.activeWalletRepository.save(foundWallet);
                    response.put("process", true);
                    response.put("message", "The product has been purchased successfully.");
                    return response;
                }
                
                

                
            } else {
                response.put("process", false);
                return response;
            }
        }
        response.put("process", false);
        return response;
        

    }

}
