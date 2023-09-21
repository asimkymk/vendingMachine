package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.ActiveWallet;
import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.repositories.ActiveWalletRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/activeWallet")
public class ActiveWalletController {

    private static final Logger logger = LoggerFactory.getLogger(ActiveWalletController.class);

    private final ActiveWalletRepository activeWalletRepository;

    public ActiveWalletController(ActiveWalletRepository activeWalletRepository) {
        this.activeWalletRepository = activeWalletRepository;
    }

    @GetMapping
    public ApiResponse<ActiveWallet> getActiveWallet() {
        try {
            return new ApiResponse<>(true, "Success", this.activeWalletRepository.findById(1L).orElse(null));
        } catch (Exception e) {
            logger.error("Error fetching active wallet: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    @PutMapping()
    @CrossOrigin
    @Transactional
    public ApiResponse<ActiveWallet> updateWallet(@RequestBody Map<String,Integer> requestBody) {
        try {
            Optional<ActiveWallet> activeWallet = this.activeWalletRepository.findById(1L);
            if (activeWallet.isPresent()) {
                ActiveWallet foundWallet = activeWallet.get();
                 if (requestBody.get("add") != null) {
                    foundWallet.setWalletAmount(foundWallet.getWalletAmount() + requestBody.get("add"));
                    logger.info("Added money to wallet with Price: {}", requestBody.get("add"));
                }
                if (requestBody.get("remove") != null) {
                    foundWallet.setWalletAmount(foundWallet.getWalletAmount() - requestBody.get("remove"));
                }
                this.activeWalletRepository.save(foundWallet);
                return new ApiResponse<>(true, "Wallet updated successfully", foundWallet);
            } else {
                return new ApiResponse<>(false, "Wallet not found", null);
            }
        } catch (Exception e) {
            logger.error("Error updating wallet: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    @PutMapping("/buyProduct")
    @CrossOrigin
    @Transactional
    public ApiResponse<ActiveWallet> buyProduct(@RequestBody Product product) {
        try {
            Optional<ActiveWallet> activeWallet = this.activeWalletRepository.findById(1L);
            if (product.getProductAmount() > 0 && activeWallet.isPresent()) {
                ActiveWallet foundWallet = activeWallet.get();
                if (product.getProductPrice() > foundWallet.getWalletAmount()) {
                    return new ApiResponse<>(false, "Wallet balance is insufficient.", null);
                } else {
                    foundWallet.setWalletAmount(foundWallet.getWalletAmount() - product.getProductPrice());
                    this.activeWalletRepository.save(foundWallet);
                    return new ApiResponse<>(true, "The product has been purchased successfully.", foundWallet);
                }
            } else {
                return new ApiResponse<>(false, "Wallet not found or product amount is zero.", null);
            }
        } catch (Exception e) {
            logger.error("Error purchasing product: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }
}
