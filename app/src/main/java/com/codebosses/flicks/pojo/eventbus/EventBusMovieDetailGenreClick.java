package com.codebosses.flicks.pojo.eventbus;

public class EventBusMovieDetailGenreClick {
    private int position;

    public EventBusMovieDetailGenreClick(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
