package com.codebosses.flicks.pojo.eventbus;

public class EventBusSearchText {
    private String searchText;

    public EventBusSearchText(String searchText) {
        setSearchText(searchText);
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
