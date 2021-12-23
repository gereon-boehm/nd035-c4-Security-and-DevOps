package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    UserRepository userRepository;

    @Mock
    OrderRepository orderRepository;

    User user;
    UserOrder userOrder;
    Item item;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setUsername("testUser");

        item = new Item();
        item.setId(1L);
        item.setName("testItem");

        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        user.setCart(cart);
        cart.setUser(user);

        userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
    }

    @Test
    public void testSubmitOrderSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertTrue(order.getItems().contains(item));
    }

    @Test
    public void testGetOrdersForUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orderList = response.getBody();

        assertNotNull(orderList);
        assertTrue(orderList.contains(userOrder));

    }

    @Test
    public void testGetOrdersForUserNotFound() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("notfound");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}