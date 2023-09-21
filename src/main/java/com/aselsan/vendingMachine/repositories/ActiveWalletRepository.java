/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.aselsan.vendingMachine.repositories;

import com.aselsan.vendingMachine.entities.ActiveWallet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author asimk
 */
public interface ActiveWalletRepository extends JpaRepository<ActiveWallet,Long> {
    
}
