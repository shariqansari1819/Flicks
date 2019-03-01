
package com.codebosses.flicks.pojo.tvseasons;

import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Episode {

    private String air_date;
    private Integer episode_number;
    private Integer id;
    private String name;
    private String overview;
    private String production_code;
    private Integer season_number;
    private Integer show_id;
    private String still_path;
    private Double vote_average;
    private Integer vote_count;
    private List<CrewData> crew = null;
    private List<CastData> guest_stars = null;

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public Integer getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(Integer episode_number) {
        this.episode_number = episode_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getProduction_code() {
        return production_code;
    }

    public void setProduction_code(String production_code) {
        this.production_code = production_code;
    }

    public Integer getSeason_number() {
        return season_number;
    }

    public void setSeason_number(Integer season_number) {
        this.season_number = season_number;
    }

    public Integer getShow_id() {
        return show_id;
    }

    public void setShow_id(Integer show_id) {
        this.show_id = show_id;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public List<CrewData> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewData> crew) {
        this.crew = crew;
    }

    public List<CastData> getGuest_stars() {
        return guest_stars;
    }

    public void setGuest_stars(List<CastData> guest_stars) {
        this.guest_stars = guest_stars;
    }

}
