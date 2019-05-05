package com.codebosses.flicks.pojo.eventbus;

public class EventBusPlayVideo {
    private int position;

    public EventBusPlayVideo(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
