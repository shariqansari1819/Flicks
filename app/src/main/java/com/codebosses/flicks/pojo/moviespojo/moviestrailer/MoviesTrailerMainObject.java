
package com.codebosses.flicks.pojo.moviespojo.moviestrailer;

import java.util.List;

public class MoviesTrailerMainObject {

    private Integer id;
    private List<MoviesTrailerResult> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MoviesTrailerResult> getResults() {
        return results;
    }

    public void setResults(List<MoviesTrailerResult> results) {
        this.results = results;
    }

}
