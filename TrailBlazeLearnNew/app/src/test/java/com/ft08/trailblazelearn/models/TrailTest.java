package com.ft08.trailblazelearn.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by keerthanadevi on 23/3/18.
 */
public class TrailTest {

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



}