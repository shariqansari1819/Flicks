
package com.codebosses.flicks.pojo.celebritiespojo.celebmovies;

import java.util.List;

public class CelebMoviesMainObject {

    private List<CelebMoviesData> cast = null;
    private List<Object> crew = null;
    private Integer id;

    public List<CelebMoviesData> getCast() {
        return cast;
    }

    public void setCast(List<CelebMoviesData> cast) {
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
