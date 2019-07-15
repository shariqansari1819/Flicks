package com.codebosses.flicks.pojo.eventbus;

public class EventBusOfflineClick {
    private int position;

    public EventBusOfflineClick(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
