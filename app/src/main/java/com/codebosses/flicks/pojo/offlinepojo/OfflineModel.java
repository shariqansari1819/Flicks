package com.codebosses.flicks.pojo.offlinepojo;

public class OfflineModel {

    private String path, name, thumbnail,date;
    private long duration;

    public OfflineModel(String path, String name, long duration, String thumbnail,String date) {
        this.path = path;
        this.name = name;
        this.duration = duration;
        this.thumbnail = thumbnail;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
