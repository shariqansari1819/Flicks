package com.codebosses.flicks.pojo.eventbus;

import android.view.View;

public class EventBusOfflineClick {

    private String type;
    private int position;
    private View view;

    public EventBusOfflineClick(int position, String type, View view) {
        this.position = position;
        this.type = type;
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
