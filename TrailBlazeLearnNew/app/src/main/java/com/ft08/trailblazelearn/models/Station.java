package com.ft08.trailblazelearn.models;

public class Station {

    private int seqNum;
    private String stationName;
    private String instructions;
    private String gps;
    private String stationID;
    private String address;

    public Station(){

    }

    public Station(int seqNum,String stationName, String instructions, String gps,String address) {
        this.seqNum = seqNum;
        this.stationName = stationName;
        this.instructions = instructions;
        this.gps = gps;
        this.address = address;
        this.stationID = seqNum+"-"+stationName;
    }

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

    @Override
    public String toString() {
        return (getSeqNum()+ " - " +getStationName());
    }
}

