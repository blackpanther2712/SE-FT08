package com.ft08.trailblazelearn.models;

import java.util.ArrayList;

public class Trail {
    private String trailName;
    private String trailCode;
    private String trailId;
    private String module;
    private String trailDate;

    public Trail(){

    }

    public Trail(String trailName, String trailCode, String trailId, String module, String trailDate) {
        this.trailName = trailName;
        this.trailCode = trailCode;
        this.trailId = trailId;
        this.module = module;
        this.trailDate = trailDate;
    }

    private ArrayList<String> stationList = new ArrayList<String>();

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public void setTrailCode(String trailCode) {
        this.trailCode = trailCode;
    }

    public void setTrailId(String trailId) {
        this.trailId = trailId;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setTrailDate(String trailDate) {
        this.trailDate = trailDate;
    }

    public String getTrailName() {
        return trailName;
    }

    public String getTrailCode() {
        return trailCode;
    }

    public String getTrailId() {
        return trailId;
    }

    public String getModule() {
        return module;
    }

    public String getTrailDate() {
        return trailDate;
    }

    public void addStation(){

    }

    public void updateStation(){

    }

    public void deleteStation(){

    }

    public ArrayList<String> getStationList(){

        return stationList;
    }

}
