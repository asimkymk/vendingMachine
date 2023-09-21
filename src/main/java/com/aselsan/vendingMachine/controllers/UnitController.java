/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controllers;

import com.aselsan.vendingMachine.entities.Unit;
import com.aselsan.vendingMachine.repositories.UnitRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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
    private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

    private UnitRepository unitRepository;

    public UnitController(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @GetMapping
    public ApiResponse<List<Unit>> getAllUnits() {
        try {
            return new ApiResponse<>(true, "Success", this.unitRepository.findAll());
        } catch (Exception e) {
            logger.error("Error fetching all units: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    


    @PostMapping
    @CrossOrigin
    @Transactional
    public ApiResponse<Unit> createUnit(@RequestBody Unit newUnit) {
        try {
            Unit savedUnit = this.unitRepository.save(newUnit);
            logger.info("Unit created successfully with ID: {}", savedUnit.getId());
            return new ApiResponse<>(true, "Unit created successfully", savedUnit);
        } catch (Exception e) {
            logger.error("Error creating unit: ", e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }


    @GetMapping("/{unitId}")
    public ApiResponse<Unit> getOneUnit(@PathVariable Long unitId) {
        try {
            Optional<Unit> unit = this.unitRepository.findById(unitId);
            if (unit.isPresent()) {
                logger.info("Unit found with ID: {}", unitId);
                return new ApiResponse<>(true, "Unit found", unit.get());
            } else {
                logger.warn("Unit not found with ID: {}", unitId);
                return new ApiResponse<>(false, "Unit not found", null);
            }
        } catch (Exception e) {
            logger.error("Error finding unit with ID: {}", unitId, e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    
    @PutMapping("/{unitId}")
    public ApiResponse<Unit> updateOneUnit(@PathVariable Long unitId, @RequestBody Unit newUnit) {
        try {
            Optional<Unit> unit = this.unitRepository.findById(unitId);
            if (unit.isPresent()) {
                Unit foundUnit = unit.get();
                foundUnit.setUnitAmount(newUnit.getUnitAmount());
                foundUnit.setUnitPrice(newUnit.getUnitPrice());
                foundUnit.setTempAmount(newUnit.getTempAmount());

                Unit updatedUnit = this.unitRepository.save(foundUnit);

                logger.info("Unit updated with ID: {}", unitId);
                return new ApiResponse<>(true, "Unit updated successfully", updatedUnit);
            } else {
                logger.warn("Unit not found with ID: {}", unitId);
                return new ApiResponse<>(false, "Unit not found", null);
            }
        } catch (Exception e) {
            logger.error("Error updating unit with ID: {}", unitId, e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }


    @CrossOrigin
    @PutMapping("/activeWallet/{unitId}")
    @Transactional
    public ApiResponse<String> addUnitToWallet(@PathVariable Long unitId) {
        Map<String, Boolean> response = new HashMap<>();
        try {
            Optional<Unit> unit = this.unitRepository.findById(unitId);
            if (unit.isPresent()) {
                Unit foundUnit = unit.get();
                int tempAmount = foundUnit.getTempAmount() + 1;
                foundUnit.setTempAmount(tempAmount);
                this.unitRepository.save(foundUnit);

                logger.info("Added unit to wallet with ID: {}", unitId);
                return new ApiResponse<>(true, "Unit successfully added to wallet", null);
            } else {
                logger.warn("Unit not found with ID: {}", unitId);
                return new ApiResponse<>(false, "Unit not found", null);
            }
        } catch (Exception e) {
            logger.error("Error adding unit to wallet with ID: {}", unitId, e);
            return new ApiResponse<>(false, "Database error!", null);
        }
    }

    @CrossOrigin
    @PostMapping("/activeWallet/refund")
    @Transactional
    public ApiResponse<Map<String, Object>> getRefund(@RequestBody Map<String, Integer> requestBody) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, "unitPrice");
            List<Unit> units = this.unitRepository.findAll(sort);
            int activeWallet = 0;
            int refundAmount;

            try {
                refundAmount = requestBody.get("refundAmount");
            } catch (Exception e) {
                logger.error("Invalid request format", e);
                return new ApiResponse<>(false, "Invalid request format", null);
            }

            for (Unit unit : units) {
                activeWallet += unit.getTempAmount() * unit.getUnitPrice();
            }

            if (activeWallet == refundAmount) {
                for (Unit unit : units) {
                    unit.setTempAmount(0);
                    this.unitRepository.save(unit);
                }
                logger.info("Refund completed successfully for amount: {}", refundAmount);
                response.put("wallet", refundAmount);
                return new ApiResponse<>(true, "Refund completed successfully", response);
            } else {
                int tmp = refundAmount;
                for (Unit unit : units) {
                    int calculatedUnitAmount = tmp / unit.getUnitPrice();
                    if (unit.getUnitAmount() >= calculatedUnitAmount) {
                        tmp %= unit.getUnitPrice();
                        unit.setUnitAmount(unit.getUnitAmount() - calculatedUnitAmount);
                        this.unitRepository.save(unit);
                    }
                }
                if (tmp == 0) {
                    logger.info("Partial refund completed successfully for amount: {}", refundAmount);
                    
                    response.put("wallet", refundAmount);
                    return new ApiResponse<>(true, "Partial refund completed successfully", response);
                }
                logger.warn("Refund failed for amount: {}", refundAmount);
                
                response.put("wallet", refundAmount - tmp);
                return new ApiResponse<>(false, "Refund failed", response);
            }
        } catch (Exception e) {
            logger.error("Error during refund", e);
            return new ApiResponse<>(false, "An error occurred during the refund process", null);
        }
    }

    
    @CrossOrigin
    @PutMapping("/activeWallet/buyProduct")
    @Transactional
    public ApiResponse<Optional> buyProduct() {
        try {
            Sort sort = Sort.by(Sort.Direction.DESC, "unitPrice");
            List<Unit> units = this.unitRepository.findAll(sort);

            for (Unit unit : units) {
                int tmp = unit.getTempAmount();
                unit.setTempAmount(0);
                unit.setUnitAmount(unit.getUnitAmount() + tmp);
                this.unitRepository.save(unit);
            }
            logger.info("Buying process completed successfully");
            return new ApiResponse<>(true, "Buying process completed successfully", null);
        } catch (Exception e) {
            logger.error("Error during buying process", e);
            return new ApiResponse<>(false, "An error occurred during the buying process", null);
        }
    }

    @DeleteMapping("/{unitId}")
    @CrossOrigin
    @Transactional
    public ApiResponse<Void> deleteOneProduct(@PathVariable Long unitId) {
        try {
            this.unitRepository.deleteById(unitId);
            logger.info("Unit with ID {} deleted successfully", unitId);
            return new ApiResponse<>(true, "Unit deleted successfully", null);
        } catch (Exception e) {
            logger.error("Error during deleting unit with ID {}", unitId, e);
            return new ApiResponse<>(false, "An error occurred during the deletion process", null);
        }
    }

}
