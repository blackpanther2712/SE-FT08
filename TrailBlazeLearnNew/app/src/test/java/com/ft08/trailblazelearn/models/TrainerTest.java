package com.ft08.trailblazelearn.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class TrainerTest {
    @Test
    public void testAddTrail() {
        Trainer trainer = new Trainer();
        Trail trail1 = new Trail("trail1","code1","module1","23-03-2018","Keerthi");
        Trail trail2 = new Trail("trail2","code2","module2","05-04-2018","Pari");
        Trail trail3 = new Trail("trail3","code3","module3","25-04-2018","Muthu");
        assertTrue(!trail1.equals(trail2));
        assertNotEquals(trail3,new Trail("trail3","code3","module3","25-04-2018","Muthu")) ;
        assertEquals(trail3.getModule(),"module3");
    }

    @Test
    public void testGetTrail() {
        Trainer trainer = new Trainer();
        Trail trail1,trail2,trail3;
        ArrayList<Trail> trails = new ArrayList<>();
        assertTrue(trails.isEmpty());
        trail1 = new Trail("trail1","code1","module1","23-03-2018","Keerthi");
        trail2 = new Trail("trail2","code2","module2","05-04-2018","Pari");
        trail1.setTrailID("1");
        trail2.setTrailID("2");
        trails.add(trail1);
        trails.add(trail2);
        assertTrue(trail1.getTrailID().equals("1"));
        assertFalse(trail2.getTrailID().equals("1"));
        assertFalse(trail1.getTrailID().equals(trail2.getTrailID()));
        assertTrue(trail2.getTrailID().equals(trail2.getTrailID()));
    }

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
        assertTrue(trails.isEmpty());
        try{
            Trail trail = trails.get(1);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("trail list index out of bound");
        }
    }
}