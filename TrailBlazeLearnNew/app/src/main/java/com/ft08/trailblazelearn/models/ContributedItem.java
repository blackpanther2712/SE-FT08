package com.ft08.trailblazelearn.models;

/**
 * Created by neelimabenny on 21/3/18.
 */

public class ContributedItem {

    private String fileURL;
    private String userName;
    private String title;
    private String description;
    public int type;
    private String ownerProfilePhotoUrl;
    private String dateTime;

    public static final int IMAGE_TYPE = 0;
    public static final int AUDIO_TYPE = 1;
    public static final int DOCUMENT_TYPE = 2;
    public static final int VIDEO_TYPE = 3;


    public ContributedItem() {

    }

    public ContributedItem(int type, String userName, String fileURL, String title, String description, String ownerProfilePhotoUrl, String dateTime) {

        this.type = type;
        this.userName = userName;
        this.fileURL = fileURL;
        this.title = title;
        this.description = description;
        this.ownerProfilePhotoUrl = ownerProfilePhotoUrl;
        this.dateTime = dateTime;

    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOwnerProfilePhotoUrl() {
        return ownerProfilePhotoUrl;
    }

    public void setOwnerProfilePhotoUrl(String ownerProfilePhotoUrl) {
        this.ownerProfilePhotoUrl = ownerProfilePhotoUrl;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
