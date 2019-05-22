package com.codebosses.flicks.pojo.eventbus;

public class EventBusPagerImageClick {

    private int position;

    public EventBusPagerImageClick(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
