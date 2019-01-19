package com.codebosses.flicks.pojo.eventbus;

public class EventBusMovieDetailId {
    private int movieId;

    public EventBusMovieDetailId(int movieId) {
        setMovieId(movieId);
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
