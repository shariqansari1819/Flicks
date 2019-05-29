package com.codebosses.flicks.pojo.moviespojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalId {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
