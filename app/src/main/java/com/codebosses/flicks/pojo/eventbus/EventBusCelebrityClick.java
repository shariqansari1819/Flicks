package com.codebosses.flicks.pojo.eventbus;

public class EventBusCelebrityClick {
    private int position;
    private String celebType;

    public EventBusCelebrityClick(int position, String celebType) {
        setPosition(position);
        setCelebType(celebType);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getCelebType() {
        return celebType;
    }

    public void setCelebType(String celebType) {
        this.celebType = celebType;
    }
}
