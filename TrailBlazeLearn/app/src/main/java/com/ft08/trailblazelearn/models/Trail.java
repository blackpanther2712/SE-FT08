package com.ft08.trailblazelearn.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

/**
 * Created by keerthanadevi on 08,March,2018
 */
public class Trail {
    private String trailName;
    private String module;
    private Date trailDate;
    private String trailID;
    private ArrayList<Station> stations;
    private int sequenceNum;




    public Trail(String trailName, String module, Date trailDate) {
        this.trailName = trailName;
        this.module = module;
        this.trailDate = trailDate;
        DateFormat form = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date d=trailDate;
        this.trailID = form.format(d)+trailName;
        stations = new ArrayList<Station> ();
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Date getTrailDate() {
        return trailDate;
    }

    public void setTrailDate(Date trailDate) {
        this.trailDate = trailDate;
    }

    public String getTrailID() {
        return trailID;
    }

    public void setTrailID(String trailID) {
        this.trailID = trailID;
    }

    public List<Station> getStations() {
        return new ArrayList<Station>(stations);
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

//    public int getTrailNum() {
//        return trailNum;
//    }
//
//    public void setTrailNum(int trailNum) {
//        this.trailNum = trailNum;
//    }

    public Station addStation(String stationName, String instructions, String gps){
        Station station = new Station(++sequenceNum,stationName,instructions,gps);
        stations.add(station);
        return station;
    }

    public void removeStation(String stationID){
        Station station = getStation(stationID);
        if(station!=null){
            stations.remove(station);
        }
    }

    public Station getStation(String stationID) {
        for (Station station : stations) {
            if (station.getStationID().equals(stationID)) {
                return station;
            }
        }
        return null;
    }

    public Station editStation(int seqNum,String stationName, String instructions, String gps,String stationID){
        Station station = getStation(stationID);
        if (station != null) {
            station.editStation(seqNum,stationName,instructions,gps);
        }
        return station;
    }

    public Trail editTrail(String trailName, String module, Date trailDate){
        setTrailName(trailName);
        setModule(module);
        setTrailDate(trailDate);
        return this;
    }
    @Override
    public String toString() {
        return getTrailName();
    }

}
