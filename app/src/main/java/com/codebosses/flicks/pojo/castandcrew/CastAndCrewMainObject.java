
package com.codebosses.flicks.pojo.castandcrew;

import java.util.List;

public class CastAndCrewMainObject {

    private Integer id;
    private List<CastData> cast = null;
    private List<CrewData> crew = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CastData> getCast() {
        return cast;
    }

    public void setCast(List<CastData> cast) {
        this.cast = cast;
    }

    public List<CrewData> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewData> crew) {
        this.crew = crew;
    }

}
