package com.ft08.trailblazelearn.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class PostTest {

    @Test
    public void testPostOperations() {
        Post post1 = new Post("myfirstpost", "afaque", "abcd.com", "efgh.com");
        Post post2 = new Post("mysecondpost", "ahmad", "abad.com", "efkh.com");
        Post post3 = new Post("mythirdpost", "naved", "hqri.com", "lmno.com");
        Post post4 = new Post("myfirstpost", "afaque", "abcd.com", "efgh.com");

        assertNotNull(post1);
        assertNotNull(post2);
        assertNotNull(post3);

        assertNotEquals(post1, post4);
        assertNotEquals(post1, post2);
        assertEquals(post1, post1);

        assertEquals(post1.getName(), post4.getName());
        assertNotEquals(post1.getName(), post2.getName());

        assertEquals(post1.getOwnerProfilePhotoUrl(), post4.getOwnerProfilePhotoUrl());
        assertNotEquals(post2.getOwnerProfilePhotoUrl(), post1.getOwnerProfilePhotoUrl());

        assertEquals(post1.getText(), post4.getText());
        assertNotEquals(post2.getText(), post3.getText());

        assertEquals(post1.getPhotoUrl(), post4.getPhotoUrl());
        assertNotEquals(post2.getPhotoUrl(), post1.getPhotoUrl());
    }
}