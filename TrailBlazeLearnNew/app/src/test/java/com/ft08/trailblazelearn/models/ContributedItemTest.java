package com.ft08.trailblazelearn.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContributedItemTest {

    @Test
    public void testOpertaions() {
        ContributedItem ci1 = new ContributedItem(0, "afaque", "abcd.com", "myaudio", "newaudio", "kkkk.com", "2018-03-21 12:14:00");
        ContributedItem ci2 = new ContributedItem(1, "ahmad", "asdd.com", "myvideo", "newvideo", "kasd.com", "2018-03-22 12:14:00");
        ContributedItem ci3 = new ContributedItem(2, "naved", "adas.com", "mypic", "newpic", "kdhhk.com", "2018-03-21 15:14:00");
        ContributedItem ci4 = new ContributedItem(0, "afaque", "abcd.com", "myaudio", "newaudio", "kkkk.com", "2018-03-21 12:14:00");

        assertNotNull(ci1);
        assertNotNull(ci2);
        assertNotNull(ci3);
        assertNotNull(ci4);

        assertNotEquals(ci1, ci4);
        assertEquals(ci1, ci1);
        assertNotEquals(ci1, ci2);

        assertEquals(ci1.getUserName(), ci4.getUserName());
        assertEquals(ci1.getFileURL(), ci4.getFileURL());
        assertEquals(ci1.getTitle(), ci4.getTitle());
        assertEquals(ci1.getDescription(), ci4.getDescription());
        assertEquals(ci1.getOwnerProfilePhotoUrl(), ci4.getOwnerProfilePhotoUrl());
        assertEquals(ci1.getDateTime(), ci4.getDateTime());

        assertNotEquals(ci1.getUserName(), ci2.getUserName());
        assertNotEquals(ci1.getFileURL(), ci2.getFileURL());
        assertNotEquals(ci1.getTitle(), ci2.getTitle());
        assertNotEquals(ci1.getDescription(), ci2.getDescription());
        assertNotEquals(ci1.getOwnerProfilePhotoUrl(), ci2.getOwnerProfilePhotoUrl());
        assertNotEquals(ci1.getDateTime(), ci2.getDateTime());
    }
}