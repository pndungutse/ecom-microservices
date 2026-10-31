package com.ecomerce.order.service;

import com.ecomerce.order.dto.CartItemRequest;
import com.ecomerce.order.dto.CartItemResponse;
import com.ecomerce.order.model.CartItem;
//import com.ecomerce.order.model.Product;
//import com.ecomerce.order.model.User;
import com.ecomerce.order.repository.CartItemRepository;
//import com.ecomerce.order.repository.ProductRepository;
//import com.ecomerce.order.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private UserRepository userRepository;

    public boolean addToCart(String userId, CartItemRequest cartItemRequest) {
        // look for a product
//        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
//        if (productOpt.isEmpty())
//            return false;
//
//        Product product = productOpt.get();
//
//        if (product.getStockQuantity() < cartItemRequest.getQuantity())
//            return false;
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty())
//            return false;
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId,cartItemRequest.getProductId());
        if (existingCartItem !=null) {
            // updated cart quantity, means we already have one
             existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
             existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
             cartItemRepository.save(existingCartItem);
        } else {
            // create a new cart
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        // look for a product
//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getUserCartItems(String userId) {
//        return userRepository.findById(Long.valueOf(userId))
//                .map(cartItemRepository::findByUser)
//                .orElseGet(List::of);
        return cartItemRepository.findByUserId(userId);

    }


    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setUser(cartItem.getUserId());
        cartItemResponse.setProduct(cartItem.getProductId());
        cartItemResponse.setPrice(cartItem.getPrice());
        cartItemResponse.setQuantity(cartItem.getQuantity());

        return cartItemResponse;
    }

    public void clearCart(String userId) {
//        userRepository.findById(Long.valueOf(userId)).ifPresent(
//                    cartItemRepository::deleteByUser
//                );
        cartItemRepository.deleteByUserId(userId);
    }
}
