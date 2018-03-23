package com.ft08.trailblazelearn.models;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 23/3/18.
 */
public class StationTest {

    @Test
    public void testEquals(){
        ArrayList<Station> stations = new ArrayList<>();
        assertTrue(stations.isEmpty());
        Station station1,station2,station3;
        station1 = new Station(1,"Snow City","Go to snow slide take a photo and post it",
                "1.335443, 103.735183","Snow City, Jurong East");
        station2 = new Station(3,"Jurong Bird Park","Search for blue bird and feed it",
                "1.328253, 103.707217","Jurong Bird Park, Singapore");
        stations.add(station1);
        stations.add(station2);
        assertFalse(stations.isEmpty());
        assertTrue(!station1.equals(null));
        assertEquals(station1,station1);
        assertNotEquals(station1,station2);
        assertNotEquals(station2,new Station(3,"Jurong Bird Park","Search for blue bird and feed it",
                "1.328253, 103.707217","Jurong Bird Park, Singapore"));
    }

    @Test
    public void testEdit(){
        ArrayList<Station> stations = new ArrayList<>();
        assertTrue(stations.isEmpty());
        Station station1,station2,station3;
        station1 = new Station(1,"Snow City","Go to snow slide take a photo and post it",
                "1.335443, 103.735183","Snow City, Jurong East");
        station2 = new Station(3,"Jurong Bird Park","Search for blue bird and feed it",
                "1.328253, 103.707217","Jurong Bird Park, Singapore");
        stations.add(station1);
        stations.add(station2);
        stations.set(0,new Station(6,"Zoo","Cut nails for Lion",
                "1.404638, 103.793109","Singapore Zoo, Singapore"));
        assertFalse(stations.get(0).equals(station1));
        assertTrue(stations.get(1).equals(station2));
    }

}