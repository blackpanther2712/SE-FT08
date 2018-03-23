package com.ft08.trailblazelearn.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 24/3/18.
 */
public class TrainerTest {

    @Test
    public void testRemove(){
        ArrayList<Trail> trails = new ArrayList<>();
        assertTrue(trails.isEmpty());
        Trail trail1 = new Trail("trail1","code1","module1","23-03-2018","Keerthi");
        Trail trail2 = new Trail("trail2","code2","module2","05-04-2018","Pari");
        trails.add(trail1);
        trails.add(trail2);
        assertFalse(trails.isEmpty());
        assertTrue(trails.get(1).equals(trail2));
        assertFalse(trail2.equals(trails.get(0)));
        trails.remove(trail2);
        assertNotNull(trails.get(0));
        trails.clear();
        assertFalse(trails.contains(trail1));
    }
}