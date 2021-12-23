package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @InjectMocks
    ItemController itemController;

    @Mock
    ItemRepository itemRepository;

    Item item;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        item = new Item();
        item.setId(1L);
        item.setName("testItem");
    }

    @Test
    public void testGetItemByIdSuccess() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(item, response.getBody());
    }

    @Test
    public void testGetItemByNameSuccess() {
        when(itemRepository.findByName(anyString())).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains(item));
    }

    @Test
    public void testGetItems() {
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains(item));
    }

}