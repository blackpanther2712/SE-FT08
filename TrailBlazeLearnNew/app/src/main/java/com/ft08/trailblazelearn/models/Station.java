package com.ft08.trailblazelearn.models;

public class Station {

    private int seqNum;
    private String stationName;
    private String instructions;
    private String gps;
    private String stationID;
    private String address;

    /*Default Constructor
    */
    public Station(){}


    /*Constructor with Parameters
     */
    public Station(int seqNum,String stationName, String instructions, String gps,String address) {
        this.seqNum = seqNum;
        this.stationName = stationName;
        this.instructions = instructions;
        this.gps = gps;
        this.address = address;
        this.stationID = seqNum+"-"+stationName;
    }


    /*
      Getters and setters for private variables
      1.stationName,2.instructions,3.seqNum,4.gps,5.stationID,6.address.
     */
    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }


    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }


    public int getSeqNum() { return seqNum; }
    public void setSeqNum(int seqNum) { this.seqNum = seqNum; }


    public String getGps() { return gps; }
    public void setGps(String gps) { this.gps = gps; }


    public String getStationID() { return stationID; }
    public void setStationID(String stationID) { this.stationID = stationID; }


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }


    /*
      editing a station value passed from Trail
     */
    public void editStation(String name,String inst,String gps,String address){
           setStationName(name);
           setInstructions(inst);
           setGps(gps);
           setAddress(address);
    }

    /*
   ToString method for stationName
     */
    @Override
    public String toString() {
        return (getSeqNum()+ " - " +getStationName());
    }
}

