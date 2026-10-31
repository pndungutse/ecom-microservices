package com.ecomerce.order.controller;


import com.ecomerce.order.dto.CartItemRequest;
import com.ecomerce.order.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private com.ecomerce.order.service.CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest cartItemRequest) {
        if (!cartService.addToCart(userId, cartItemRequest)) {
            return ResponseEntity.badRequest().body("Product not found, User not found or product out of stock.");
        }
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> deleteFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable String productId) {
        boolean removed = cartService.deleteItemFromCart(userId, productId);
        if (!removed) {
            return ResponseEntity.badRequest().body("Product in cart not found or User not found.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Product " + productId + " removed from cart.");
        }
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getUserCartItems(@RequestHeader("X-User-ID") String userId) {
        return new ResponseEntity<List<CartItem>>(cartService.getUserCartItems(userId), HttpStatus.OK);
    }
}
