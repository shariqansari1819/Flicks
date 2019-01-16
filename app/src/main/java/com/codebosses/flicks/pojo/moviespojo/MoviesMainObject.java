
package com.codebosses.flicks.pojo.moviespojo;

import java.util.List;

public class MoviesMainObject {

    private List<MoviesResult> results = null;
    private Integer page;
    private Integer total_results;
    private MoviesDates dates;
    private Integer total_pages;

    public List<MoviesResult> getResults() {
        return results;
    }

    public void setResults(List<MoviesResult> results) {
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public MoviesDates getDates() {
        return dates;
    }

    public void setDates(MoviesDates dates) {
        this.dates = dates;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

}
