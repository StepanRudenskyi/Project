package org.example.petstore.service.cart;

import org.example.petstore.model.Cart;
import org.springframework.stereotype.Service;

@Service
public class CartValidator {
    public boolean isCartEmpty(Cart cart) {
        return cart.getProductQuantityMap().isEmpty();
    }
}
