package com.example.demo;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController = mock(CartController.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        Cart cart = new Cart();
        User user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setCart(cart);
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        Item item = new Item();
        item.setId(0L);
        item.setName("Square Widget");
        item.setPrice(BigDecimal.valueOf(100));
        item.setDescription("A widget that is square");
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Square Widget")).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void addToCartTest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("testUser");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(200), response.getBody().getTotal());
    }

    @Test
    public void deleteFromCartTest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUser");
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setQuantity(2);
        ResponseEntity<Cart> cartResponse = cartController.addTocart(modifyCartRequest);
        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());

        Cart cart = cartResponse.getBody();
        assertNotNull(cart);
        ResponseEntity<Cart> cartResponseEntity1 = cartController.removeFromcart(modifyCartRequest);
        assertEquals(cartResponseEntity1.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(cart.getItems().size(), 0);
    }

}