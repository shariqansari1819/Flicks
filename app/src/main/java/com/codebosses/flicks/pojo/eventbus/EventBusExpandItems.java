package com.codebosses.flicks.pojo.eventbus;

public class EventBusExpandItems {
    private int position;
    private String title;

    public EventBusExpandItems(int position, String title) {
        setPosition(position);
        setTitle(title);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
