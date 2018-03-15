package com.ft08.trailblazelearn.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class Trail {
    private String module;
    private Date trailDate;
    private String trailID;
    private String trailCode;
    private String trailName;
    //private int sequenceNum;
    private ArrayList<Station> stations;

    public Trail(){
        stations = new ArrayList<Station> ();
    }
    public Trail(String trailName,String trailCode, String module, Date trailDate) {
        this.trailName = trailName;
        this.trailCode = trailCode;
        this.module = module;
        this.trailDate = trailDate;
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        this.trailID = formatter.format(trailDate)+"-"+trailCode;
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

    public String getTrailCode() {
        return trailCode;
    }

    public void setTrailCode(String trailCode) {
        this.trailCode = trailCode;
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
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    public Station addStation(int sequenceNum,String stationName, String instructions, String gps) {
        Station station = new Station(sequenceNum,stationName, instructions, gps);
        stations.add(station);
        return station;
    }

    public void removeStation(String stationID) {
        Station station = getStation(stationID);
        if(station != null) {
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

    public Station editStation(int seqNum,String stationName, String instructions, String gps,String stationID) {
        removeStation(stationID);
        Station station = addStation(seqNum,stationName,instructions,gps);
        return station;
    }

    @Override
    public String toString() {
        return getTrailName();
    }
}
