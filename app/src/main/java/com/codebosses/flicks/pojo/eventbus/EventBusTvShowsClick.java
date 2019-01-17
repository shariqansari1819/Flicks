package com.codebosses.flicks.pojo.eventbus;

public class EventBusTvShowsClick {
    private int position;
    private String tvShowType;

    public EventBusTvShowsClick(int position, String tvShowType) {
        setPosition(position);
        setTvShowType(tvShowType);
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
}
