
package com.codebosses.flicks.pojo.episodephotos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodePhotosMainObject {

    private Integer id;
    private List<EpisodePhotosData> stills = new ArrayList<>();
    private List<EpisodePhotosData> backdrops = new ArrayList<>();
    private List<EpisodePhotosData> posters = new ArrayList<>();
    private List<EpisodePhotosData> profiles = new ArrayList<>();

    public List<EpisodePhotosData> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<EpisodePhotosData> profiles) {
        this.profiles = profiles;
    }

    public List<EpisodePhotosData> getPosters() {
        return posters;
    }

    public void setPosters(List<EpisodePhotosData> posters) {
        this.posters = posters;
    }

    public List<EpisodePhotosData> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<EpisodePhotosData> backdrops) {
        this.backdrops = backdrops;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<EpisodePhotosData> getStills() {
        return stills;
    }

    public void setStills(List<EpisodePhotosData> stills) {
        this.stills = stills;
    }

}
