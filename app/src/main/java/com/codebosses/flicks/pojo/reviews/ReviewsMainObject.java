
package com.codebosses.flicks.pojo.reviews;

import java.util.List;

public class ReviewsMainObject {

    private Integer id;
    private Integer page;
    private List<ReviewsData> results = null;
    private Integer total_pages;
    private Integer total_results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<ReviewsData> getResults() {
        return results;
    }

    public void setResults(List<ReviewsData> results) {
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

}
