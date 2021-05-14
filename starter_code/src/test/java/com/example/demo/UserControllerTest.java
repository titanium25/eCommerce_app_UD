package com.example.demo;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController; //to call methods of usercontroller class

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {

        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        Cart testCart = new Cart();
        User testUser = new User();
        testUser.setId(0);
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setCart(testCart);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(testUser));
    }

    @Test
    public void createUser() throws Exception {

        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity.getStatusCode());
        assertEquals(200, responseEntity.getStatusCodeValue());

        User user = responseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void loginUser() {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");
        ResponseEntity<User> responseEntity = userController.findByUserName("test");

        assertEquals(404, responseEntity.getStatusCodeValue());
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        assertEquals("test",userResponseEntity.getBody().getUsername());
    }

    @Test
    public void findUserById() {
        ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertEquals(0, userResponseEntity.getBody().getId());
        userResponseEntity = userController.findById(1L);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }


}