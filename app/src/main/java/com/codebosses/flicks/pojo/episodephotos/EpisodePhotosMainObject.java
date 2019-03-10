
package com.codebosses.flicks.pojo.episodephotos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodePhotosMainObject {

    private Integer id;
    private List<EpisodePhotosData> stills = null;

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
