package com.codebosses.flicks.pojo.eventbus;

public class EventBusTvShowDetailId {
    private int tvId;

    public EventBusTvShowDetailId(int tvId) {
        setTvId(tvId);
    }

    public int getTvId() {
        return tvId;
    }

    public void setTvId(int tvId) {
        this.tvId = tvId;
    }
}
