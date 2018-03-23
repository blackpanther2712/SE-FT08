package com.ft08.trailblazelearn.models;

import android.util.Log;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 23/3/18.
 */
public class TrailTest {

  @Test
  public void testEquals(){
    ArrayList<Trail> trails = new ArrayList<>();
    assertTrue(trails.isEmpty());
    Trail trail1 = new Trail("trail1","code1","module1","23-03-2018","Keerthi");
    Trail trail2 = new Trail("trail2","code2","module2","05-04-2018","Pari");
    trails.add(trail1);
    trails.add(trail2);
    assertFalse(trails.isEmpty());
    assertTrue(!trail1.equals(null));
    assertEquals(trail1,trail1);
    assertNotEquals(trail1,trail2);
    assertNotEquals(trail2,new Trail("trail2","code2","module2","05-04-2018","Pari"));
  }

  @Test
  public void testAddStation(){
    Trail trail = new Trail();
    Station station1,station2,station3;
    station1 = trail.addStation(1,"Snow City","Go to snow slide take a photo and post it",
            "1.335443, 103.735183","Snow City, Jurong East");
    station2 = trail.addStation(3,"Jurong Bird Park","Search for blue bird and feed it",
            "1.328253, 103.707217","Jurong Bird Park, Singapore");
    station3 = trail.addStation(6,"Zoo","Cut nails for Lion",
            "1.404638, 103.793109","Singapore Zoo, Singapore");
    assertTrue(!station1.equals(station2));
    assertNotEquals(station3,new Trail().addStation(6,"Zoo","Cut nails for Lion",
            "1.404638, 103.793109","Singapore Zoo, Singapore"));
    assertEquals(station3.getStationName(),"Zoo");

  }

  @Test
  public void testRemove(){
    Trail trail = new Trail();
    Station station1,station2;
    ArrayList<Station> stations = new ArrayList<>();
    assertTrue(stations.isEmpty());
    station1 = trail.addStation(1,"Snow City","Go to snow slide take a photo and post it",
            "1.335443, 103.735183","Snow City, Jurong East");
    station2 = trail.addStation(3,"Jurong Bird Park","Search for blue bird and feed it",
            "1.328253, 103.707217","Jurong Bird Park, Singapore");
    stations.add(station1);
    stations.add(station2);
    assertFalse(stations.isEmpty());
    assertTrue(stations.get(1).equals(station2));
    assertFalse(station2.equals(stations.get(0)));
    stations.remove(station2);
    assertNotNull(stations.get(0));
    stations.clear();
    assertFalse(stations.contains(station1));
    assertTrue(stations.isEmpty());
    try{
      Station station = stations.get(1);
    }
    catch (IndexOutOfBoundsException e){
      System.out.println("station list index out of bound");
    }
  }

  @Test
  public void testEdit(){
    ArrayList<Trail> trails = new ArrayList<>();
    assertTrue(trails.isEmpty());
    Trail trail1 = new Trail("trail1","code1","module1","23-03-2018","Keerthi");
    Trail trail2 = new Trail("trail2","code2","module2","05-04-2018","Pari");
    trails.add(trail1);
    trails.add(trail2);
    trails.set(0,new Trail("trail3","code3","mod3","20-03-2018","Muthu"));
    assertFalse(trails.get(0).equals(trail1));
    assertTrue(trails.get(1).equals(trail2));
  }

  @Test
  public void testGetStation(){
    Trail trail = new Trail();
    Station station1,station2;
    ArrayList<Station> stations = new ArrayList<>();
    assertTrue(stations.isEmpty());
    station1 = trail.addStation(1,"Snow City","Go to snow slide take a photo and post it",
            "1.335443, 103.735183","Snow City, Jurong East");
    station2 = trail.addStation(3,"Jurong Bird Park","Search for blue bird and feed it",
            "1.328253, 103.707217","Jurong Bird Park, Singapore");
    station1.setStationID("1");
    station2.setStationID("2");
    stations.add(station1);
    stations.add(station2);
    assertTrue(station1.getStationID().equals("1"));
    assertFalse(station2.getStationID().equals("1"));
    assertFalse(station1.getStationID().equals(station2.getStationID()));
    assertTrue(station2.getStationID().equals(station2.getStationID()));
  }

}