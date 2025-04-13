package com.nhnacademy.springbootmvc;

import com.nhnacademy.springbootmvc.domain.User;
import com.nhnacademy.springbootmvc.exception.UserNotFoundException;
import com.nhnacademy.springbootmvc.service.UserServiceImpl;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class UserServiceImplTest {

    private final UserServiceImpl userService = new UserServiceImpl();

    @Test
    void testExist() {
        assertFalse(userService.exist("test1"));
        assertFalse(userService.exist("admin1"));
        assertTrue(userService.exist("nonexistent"));
    }

    @Test
    void testMatchValid() {
        assertTrue(userService.match("test1", "1234"));
    }

    @Test
    void testMatchInvalidPassword() {
        assertFalse(userService.match("test1", "wrongPassword"));
    }

    @Test
    void testMatchNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> userService.match("nonexist", "1234"));
    }

    @Test
    void testGetUserValid() {
        User user = userService.getUser("admin1");
        assertNotNull(user);
        assertEquals("admin1", user.getId());
        assertEquals("adm1", user.getName());
    }

    @Test
    void testGetUserNonExistent() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser("nonexist"));
    }
}
