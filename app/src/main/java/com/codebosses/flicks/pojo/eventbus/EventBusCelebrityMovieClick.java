package com.codebosses.flicks.pojo.eventbus;

public class EventBusCelebrityMovieClick {
    private int position;
    private String type;
    private int movieId;

    public EventBusCelebrityMovieClick(int position, String type, int movieId) {
        this.position = position;
        this.type = type;
        this.movieId = movieId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
