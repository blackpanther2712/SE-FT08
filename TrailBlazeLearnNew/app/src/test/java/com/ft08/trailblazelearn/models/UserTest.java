package com.ft08.trailblazelearn.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserOperations() {
        User user1 = new User("id123", "Afaque", "randomimage");
        User user2 = new User("id123", "Omaira", "randomimage");
        User user3 = new User("id123", "Omaira", "randomimage");

        assertNotNull(user1);
        assertNotNull(user2);
        assertEquals(user1, user1);
        assertEquals(user2, user2);
        assertNotEquals(user1, user2);
        assertNotEquals(user1, user3);

        assertNotEquals(user1.getName(), user3.getName());
        assertEquals(user1.getUserId(), user3.getUserId());
        assertEquals(user1.getImage(), user3.getImage());
    }
}