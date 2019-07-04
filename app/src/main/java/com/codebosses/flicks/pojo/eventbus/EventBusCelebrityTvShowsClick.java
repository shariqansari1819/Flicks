package com.codebosses.flicks.pojo.eventbus;

public class EventBusCelebrityTvShowsClick {
    private int position;
    private String tvShowType;
    private int id;

    public EventBusCelebrityTvShowsClick(int position, String tvShowType, int id) {
        this.position = position;
        this.tvShowType = tvShowType;
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTvShowType() {
        return tvShowType;
    }

    public void setTvShowType(String tvShowType) {
        this.tvShowType = tvShowType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
