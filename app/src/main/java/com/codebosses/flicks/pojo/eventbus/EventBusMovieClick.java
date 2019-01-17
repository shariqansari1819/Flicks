package com.codebosses.flicks.pojo.eventbus;

public class EventBusMovieClick {
    private int position;
    private String movieType;

    public EventBusMovieClick(int position, String movieType) {
        setPosition(position);
        setMovieType(movieType);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMovieType() {
        return movieType;
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }
}
