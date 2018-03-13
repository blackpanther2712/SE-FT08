package com.ft08.trailblazelearn.models;

public class Station {

    private int seqNum;
    private String stationName;
    private String instructions;
    private String gps;
    private String stationID;

    public Station(){

    }

    public Station(int seqNum,String stationName, String instructions, String gps) {
        this.seqNum = seqNum;
        this.stationName = stationName;
        this.instructions = instructions;
        this.gps = gps;
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

    public void editStation(int seqNum, String stationName, String instructions, String gps) {
        setSeqNum(seqNum);
        setStationName(stationName);
        setInstructions(instructions);
        setGps(gps);
    }

    @Override
    public String toString() {
        return (getSeqNum()+ " - " +getStationName());
    }
}

