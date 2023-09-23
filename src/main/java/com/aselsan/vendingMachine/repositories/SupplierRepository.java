/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aselsan.vendingMachine.repositories;

import com.aselsan.vendingMachine.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author asimk
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findByUsernameAndPassword(String username, String password);
}
