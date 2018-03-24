package com.ft08.trailblazelearn.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParticipantTest {

    @Test
    public void testParticipantOperations() {
        Participant p1 = new Participant("abcd", "afaque", "abcd.com");
        Participant p2 = new Participant("abcd", "afaque", "abcd.com");
        Participant p3 = new Participant("hijk", "ahmad", "kkkk.com");

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);

        assertEquals(p1, p1);
        assertNotEquals(p1, p2);
        assertNotEquals(p1, p3);

        assertEquals(p1.getName(), p2.getName());
        assertNotEquals(p1.getName(), p3.getName());

        assertEquals(p1.getUserId(), p2.getUserId());
        assertNotEquals(p1.getUserId(), p3.getUserId());

        assertEquals(p1.getImage(), p2.getImage());
        assertNotEquals(p1.getImage(), p3.getImage());
    }
}