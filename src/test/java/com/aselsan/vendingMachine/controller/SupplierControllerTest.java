/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aselsan.vendingMachine.controller;

/**
 *
 * @author asimk
 */
import com.aselsan.vendingMachine.controllers.SupplierController;
import com.aselsan.vendingMachine.entities.Product;
import com.aselsan.vendingMachine.entities.Supplier;
import com.aselsan.vendingMachine.entities.Unit;
import com.aselsan.vendingMachine.repositories.ProductRepository;
import com.aselsan.vendingMachine.repositories.SupplierRepository;
import com.aselsan.vendingMachine.repositories.UnitRepository;
import com.aselsan.vendingMachine.response.ApiResponse;
import com.aselsan.vendingMachine.utils.HashUtil;
import com.aselsan.vendingMachine.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SupplierControllerTest {

    @InjectMocks
    private SupplierController supplierController;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private UnitRepository unitRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin() {
        Supplier supplier = new Supplier();
        supplier.setUsername("test");
        supplier.setPassword("password");

        when(supplierRepository.findByUsernameAndPassword("test", HashUtil.hashPassword("password")))
                .thenReturn(supplier);

        ApiResponse<String> response = supplierController.login(supplier);

        assertEquals(true, response.getSuccess());
        assertEquals("Giriş başarılı", response.getMessage());
    }

    @Test
    public void testValidateToken() {
        String token = "someValidToken";
        ApiResponse<Boolean> mockResponse = new ApiResponse<>(true, "Valid", true);

        when(JwtUtil.validateToken(token)).thenReturn(mockResponse);

        ApiResponse<Boolean> response = supplierController.validateToken(token);

        assertEquals(true, response.getSuccess());
        assertEquals("Token is valid", response.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> productList = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        ApiResponse<List<Product>> response = supplierController.getAllProducts();

        assertEquals(true, response.getSuccess());
        assertEquals("Products fetched successfully", response.getMessage());
        assertEquals(2, response.getData().size());
    }
    
    @Test
    public void testAddProduct() {
        String token = "someValidToken";
        Product product = new Product();
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));
        when(productRepository.save(product)).thenReturn(product);

        ApiResponse<Optional> response = supplierController.addProduct(token, product);

        assertEquals(true, response.getSuccess());
        assertEquals("Product added successfully", response.getMessage());
    }

    @Test
    public void testAddUnit() {
        String token = "someValidToken";
        Unit unit = new Unit();
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));
        when(unitRepository.save(unit)).thenReturn(unit);

        ApiResponse<Optional> response = supplierController.addUnit(token, unit);

        assertEquals(true, response.getSuccess());
        assertEquals("Unit added successfully", response.getMessage());
    }

    @Test
    public void testUpdateProduct() {
        String token = "someValidToken";
        Long id = 1L;
        Product product = new Product();
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ApiResponse<Optional> response = supplierController.updateProduct(token, id, product);

        assertEquals(true, response.getSuccess());
        assertEquals("Product updated successfully", response.getMessage());
    }

    @Test
    public void testUpdateUnit() {
        String token = "someValidToken";
        Long id = 1L;
        Unit unit = new Unit();
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));
        when(unitRepository.findById(id)).thenReturn(Optional.of(unit));

        ApiResponse<Optional> response = supplierController.updateUnit(token, id, unit);

        assertEquals(true, response.getSuccess());
        assertEquals("Unit updated successfully", response.getMessage());
    }

    @Test
    public void testDeleteProduct() {
        String token = "someValidToken";
        Long id = 1L;
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));

        ApiResponse<Optional> response = supplierController.deleteProduct(token, id);

        assertEquals(true, response.getSuccess());
        assertEquals("Product deleted successfully", response.getMessage());
    }

    @Test
    public void testDeleteUnit() {
        String token = "someValidToken";
        Long id = 1L;
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));

        ApiResponse<Optional> response = supplierController.deleteUnit(token, id);

        assertEquals(true, response.getSuccess());
        assertEquals("Unit deleted successfully", response.getMessage());
    }

    @Test
    public void testInfoUnit() {
        String token = "someValidToken";
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));

        ApiResponse<Optional> response = supplierController.infoUnit(token);

        assertEquals(true, response.getSuccess());
        assertEquals("Unit added successfully", response.getMessage());
    }

    @Test
    public void testCollectMoney() {
        String token = "someValidToken";
        when(JwtUtil.validateToken(token)).thenReturn(new ApiResponse<>(true, "Valid", true));

        ApiResponse<Optional> response = supplierController.collectMoney(token);

        assertEquals(true, response.getSuccess());
        assertEquals("Units updated successfully", response.getMessage());
    }
}
