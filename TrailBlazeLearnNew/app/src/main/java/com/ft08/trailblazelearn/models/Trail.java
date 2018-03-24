package com.ft08.trailblazelearn.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class Trail {
    private String trailName;
    private String module;
    private String trailDate;
    private String trailID;
    private String trailCode;
    private String userId;
    private String trailKey;
    private ArrayList<Station> stations;


    /*Default Constructor
    */
    public Trail(){
        stations = new ArrayList<Station> ();
    }


    /*Constructor with Parameter
     */
    public Trail(String trailName, String trailCode, String module, String trailDate, String addedBy) {
        this.trailName = trailName;
        this.trailCode = trailCode;
        this.module = module;
        this.trailDate = trailDate;
        this.userId = addedBy;
        this.trailID = convertFormat(trailDate) + "-" + trailCode;
        stations = new ArrayList<Station> ();
    }


        /*
        Getters and setters for private variables
        1.TrailName,2.TrailCode,3.Module,4.trailDate,5.trailID,6.Stations<Station>
         */
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


    public String getTrailCode() {
        return trailCode;
    }
    public void setTrailCode(String trailCode) {
        this.trailCode = trailCode;
    }


    public String getTrailDate() {
        return trailDate;
    }
    public void setTrailDate(String trailDate) {
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


    public String getuserId() {
        return userId;
    }
    public void setUserId(String addedBy) {
        this.userId = addedBy;
    }


    public String getTrailKey() {
        return trailKey;
    }
    public void setTrailKey(String trailKey) {
        this.trailKey = trailKey;
    }


    /*
   ToString method for trailName
     */
    @Override
    public String toString() {
        return getTrailName();
    }


    /*
 getting a individual station from the stationList
  */
    public Station getStation(String stationID) {
        for (Station station : stations) {
            if (station.getStationID().equals(stationID)) {
                return station;
            }
        }
        return null;
    }

    /*
    Adding a station
     */
    public Station addStation(int sequenceNum,String stationName, String instructions, String gps,String address) {
        Station station = new Station(sequenceNum,stationName, instructions, gps,address);
        stations.add(station);
        return station;
    }



    /*
  Updating a station
   */
    public Station editStation(String stationName, String instructions, String gps,String stationID,String address) {
        Station station = getStation(stationID);
        if(station!=null){
            station.editStation(stationName,instructions,gps,address);
        }
        return station;
    }


     /*
    Deleting a station
     */
    public void removeStation(String stationID) {
        Station station = getStation(stationID);
        if(station != null) {
            stations.remove(station);
        }
    }


     /*
    Editing a Trail
     */

    public void editTrail(String trailName, String code, String module, String trailDate, String newTrailId, String userId) {
        setTrailName(trailName);
        setTrailCode(code);
        setModule(module);
        setTrailDate(trailDate);
        setTrailID(newTrailId);
        setUserId(userId);
    }


    /*
   Customised Conversion of date format
    */
    public String convertFormat(String date) {
        String formattedDate = "", temp = "";
        for(int i = date.length() - 1; i >= 0; i--) {
            if(date.charAt(i) != '-') {
                temp += date.charAt(i);
            }
            else {
                StringBuilder sb = new StringBuilder();
                formattedDate += sb.append(temp).reverse();
                temp = "";
            }
        }
        formattedDate += (new StringBuilder()).append(temp).reverse();
        return formattedDate;
    }
}
