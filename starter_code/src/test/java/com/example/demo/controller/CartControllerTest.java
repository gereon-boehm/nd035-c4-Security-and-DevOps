package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartRepository cartRepository;

    private ModifyCartRequest modifyCartRequest;
    private User user;
    private Item item;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUsername("testUser");
        user.setCart(new Cart());

        item = new Item();
        item.setId(1L);
        item.setName("testItem");
        item.setPrice(BigDecimal.valueOf(4.99));

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
    }

    @Test
    public void testAddToCartSuccess() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();

        assertTrue(responseCart.getItems().contains(item));

    }

    @Test
    public void testRemoveFromCartSuccess() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        cartController.addTocart(modifyCartRequest);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart responseCart = response.getBody();

        assertFalse(responseCart.getItems().contains(item));

    }

    @Test
    public void testRemoveFromCartItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}