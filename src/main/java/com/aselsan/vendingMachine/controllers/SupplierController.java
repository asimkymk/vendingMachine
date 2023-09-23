/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.ActiveWallet;
import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.entities.Supplier;
import com.aselsan.vendingMachine.entities.Unit;
import com.aselsan.vendingMachine.repositories.ActiveWalletRepository;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import com.aselsan.vendingMachine.repositories.SupplierRepository;
import com.aselsan.vendingMachine.repositories.UnitRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import com.aselsan.vendingMachine.utils.HashUtil;
import com.aselsan.vendingMachine.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ActiveWalletRepository activeWalletRepository;

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

    @GetMapping("/validateToken")
    @CrossOrigin
    public ApiResponse<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                return new ApiResponse<>(true, "Token is valid", true);
            } else {
                return new ApiResponse<>(false, response.getMessage(), false);
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
    @Transactional
    public ApiResponse<Optional> addProduct(@RequestHeader("Authorization") String token, @RequestBody Product product) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                Product newProduct = productRepository.save(product);
                return new ApiResponse(true, "Product added successfully", newProduct);
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @PostMapping("/units")
    @CrossOrigin
    @Transactional
    public ApiResponse<Optional> addUnit(@RequestHeader("Authorization") String token, @RequestBody Unit unit) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                Unit newUnit = unitRepository.save(unit);
                return new ApiResponse(true, "Unit added successfully", newUnit);
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @PutMapping("/products/{id}")
    @CrossOrigin
    public ApiResponse<Optional> updateProduct(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Product product) {

        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                Product existingProduct = productRepository.findById(id).orElse(null);
                if (existingProduct != null) {
                    existingProduct.setProductName(product.getProductName());
                    existingProduct.setProductPrice(product.getProductPrice());
                    existingProduct.setProductAmount(product.getProductAmount());
                    existingProduct.setProductImageUrl(product.getProductImageUrl());
                    productRepository.save(existingProduct);
                    return new ApiResponse(true, "Product updated successfully", existingProduct);
                } else {
                    return new ApiResponse<>(false, "Product not found", null);
                }
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @PutMapping("/units/{id}")
    @CrossOrigin
    public ApiResponse<Optional> updateUnit(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Unit unit) {

        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                Unit existingUnit = unitRepository.findById(id).orElse(null);
                if (existingUnit != null) {
                    existingUnit.setTempAmount(unit.getTempAmount());
                    existingUnit.setUnitAmount(unit.getUnitAmount());
                    existingUnit.setUnitPrice(unit.getUnitPrice());

                    unitRepository.save(existingUnit);
                    return new ApiResponse(true, "Unit updated successfully", existingUnit);
                } else {
                    return new ApiResponse(false, "Unit not found", null);
                }
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @DeleteMapping("/products/{id}")
    @CrossOrigin
    public ApiResponse<Optional> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                try {
                    productRepository.deleteById(id);
                    return new ApiResponse<>(true, "Product deleted successfully", null);
                } catch (Exception e) {
                    return new ApiResponse<>(false, "Product not found", null);
                }
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @DeleteMapping("/units/{id}")
    @CrossOrigin
    public ApiResponse<Optional> deleteUnit(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                try {
                    unitRepository.deleteById(id);
                    return new ApiResponse<>(true, "Unit deleted successfully", null);
                } catch (Exception e) {
                    return new ApiResponse<>(false, "Unit not found", null);
                }
            } else {
                return new ApiResponse(false, response.getMessage(), false);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", false);
        }

    }

    @GetMapping("/units/info")
    @CrossOrigin
    @Transactional
    public ApiResponse<Optional> infoUnit(@RequestHeader("Authorization") String token) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                List<Unit> units = unitRepository.findAll();
                long totalRevenue = 0;
                long totalRevenueWithoutTemp = 0;
                long activeWalletPrice = 0;
                ActiveWallet activeWallet = activeWalletRepository.findById(1l).orElse(null);
                if (activeWallet != null) {
                    activeWalletPrice = activeWallet.getWalletAmount();
                }
                for (int i = 0; i < units.size(); i++) {
                    totalRevenue += units.get(i).getUnitPrice() * (units.get(i).getTempAmount() + units.get(i).getUnitAmount());
                    totalRevenueWithoutTemp += units.get(i).getUnitPrice() * units.get(i).getUnitAmount();
                }
                HashMap<String, Long> rsp = new HashMap<String, Long>();
                rsp.put("totalRevenue", totalRevenue);
                rsp.put("totalRevenueWithoutTemp", totalRevenueWithoutTemp);
                rsp.put("activeWalletPrice", activeWalletPrice);
                return new ApiResponse(true, "Unit added successfully", rsp);
            } else {
                return new ApiResponse(false, response.getMessage(), null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", null);
        }

    }

    @PutMapping("/units/collectAllMoney")
    @CrossOrigin
    @Transactional
    public ApiResponse<Optional> collectMoney(@RequestHeader("Authorization") String token) {
        try {
            ApiResponse<Boolean> response = JwtUtil.validateToken(token);

            if (response.getData()) {
                List<Unit> units = unitRepository.findAll();
                ActiveWallet activeWallet = activeWalletRepository.findById(1l).orElse(null);
                if (activeWallet != null) {
                    activeWallet.setWalletAmount(0);
                    activeWalletRepository.save(activeWallet);
                }
                for (int i = 0; i < units.size(); i++) {
                    Unit unit = units.get(i);
                    unit.setUnitAmount(0);
                    unit.setTempAmount(0);
                    unitRepository.save(unit);
                }

                return new ApiResponse(true, "Units updated successfully", null);
            } else {
                return new ApiResponse(false, response.getMessage(), null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Invalid token", null);
        }

    }

}
