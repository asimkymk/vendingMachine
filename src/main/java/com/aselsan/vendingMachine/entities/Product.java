/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * @author asimk
 */
@Entity
@Table(name="product")
@Data
public class Product {
    @Id
    Long id;
    
    String productName;
    int productPrice;
    int productAmount;
    String productImageUrl;
}
