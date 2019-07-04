
package com.codebosses.flicks.pojo.celebritiespojo.celebtvshows;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CelebTvShowsMainObject {

    @SerializedName("cast")
    @Expose
    private List<CelebTvShowsData> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Object> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;

    public List<CelebTvShowsData> getCast() {
        return cast;
    }

    public void setCast(List<CelebTvShowsData> cast) {
        this.cast = cast;
    }

    public List<Object> getCrew() {
        return crew;
    }

    public void setCrew(List<Object> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
