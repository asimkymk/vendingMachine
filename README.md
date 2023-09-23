# Vending Machine Backend API Documentation

## Table of Contents

1. [Introduction](#introduction)
2. [Controllers](#controllers)
    - [ProductController](#productcontroller)
    - [UnitController](#unitcontroller)
    - [ActiveWalletController](#activewalletcontroller)
    - [SupplierController](#suppliercontroller)

## Introduction

This document describes the backend API for a Vending Machine application. The backend is implemented using Java Spring Boot and exposes various endpoints for managing products, units, active wallets, and suppliers.

## Controllers

### ProductController

#### Endpoints:

- `GET /products`: Fetches all products.
- `POST /products`: Creates a new product.
- `GET /products/{productId}`: Fetches a single product by its ID.
- `PUT /products/{productId}`: Updates a product by its ID.
- `PUT /products/buyProduct/{productId}`: Buys a product by its ID.
- `DELETE /products/{productId}`: Deletes a product by its ID.

#### Description:

This controller is responsible for handling operations related to products. It uses the `ProductRepository` to interact with the database.

---

### UnitController

#### Endpoints:

- `GET /units`: Fetches all units.
- `POST /units`: Creates a new unit.
- `GET /units/{unitId}`: Fetches a single unit by its ID.
- `PUT /units/{unitId}`: Updates a unit by its ID.
- `PUT /units/activeWallet/{unitId}`: Adds a unit to the wallet.
- `POST /units/activeWallet/refund`: Gets a refund.
- `PUT /units/activeWallet/buyProduct`: Buys a product.
- `DELETE /units/{unitId}`: Deletes a unit by its ID.

#### Description:

This controller is responsible for handling operations related to units. It uses the `UnitRepository` to interact with the database.

---

### ActiveWalletController

#### Endpoints:

- `GET /activeWallet`: Fetches the active wallet.
- `PUT /activeWallet`: Updates the wallet.
- `PUT /activeWallet/buyProduct`: Buys a product.

#### Description:

This controller is responsible for handling operations related to the active wallet. It uses the `ActiveWalletRepository` to interact with the database.

---

### SupplierController

#### Endpoints:

- `POST /supplier/login`: Logs in a supplier.
- `GET /supplier/validateToken`: Validates a token.
- `GET /supplier/products`: Fetches all products.
- `POST /supplier/products`: Adds a new product.
- `POST /supplier/units`: Adds a new unit.
- `PUT /supplier/products/{id}`: Updates a product by its ID.
- `PUT /supplier/units/{id}`: Updates a unit by its ID.
- `DELETE /supplier/products/{id}`: Deletes a product by its ID.
- `DELETE /supplier/units/{id}`: Deletes a unit by its ID.
- `GET /supplier/units/info`: Gets unit info.
- `PUT /supplier/units/collectAllMoney`: Collects all money.

#### Description:

This controller is responsible for handling operations related to suppliers. It uses multiple repositories (`SupplierRepository`, `ProductRepository`, `UnitRepository`, `ActiveWalletRepository`) to interact with the database.

---
