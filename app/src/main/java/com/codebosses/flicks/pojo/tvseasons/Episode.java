
package com.codebosses.flicks.pojo.tvseasons;

import android.os.Parcel;
import android.os.Parcelable;

import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Episode implements Parcelable {

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

    protected Episode(Parcel in) {
        air_date = in.readString();
        if (in.readByte() == 0) {
            episode_number = null;
        } else {
            episode_number = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        overview = in.readString();
        production_code = in.readString();
        if (in.readByte() == 0) {
            season_number = null;
        } else {
            season_number = in.readInt();
        }
        if (in.readByte() == 0) {
            show_id = null;
        } else {
            show_id = in.readInt();
        }
        still_path = in.readString();
        if (in.readByte() == 0) {
            vote_average = null;
        } else {
            vote_average = in.readDouble();
        }
        if (in.readByte() == 0) {
            vote_count = null;
        } else {
            vote_count = in.readInt();
        }
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(air_date);
        if (episode_number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(episode_number);
        }
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(production_code);
        if (season_number == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(season_number);
        }
        if (show_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(show_id);
        }
        dest.writeString(still_path);
        if (vote_average == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(vote_average);
        }
        if (vote_count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(vote_count);
        }
    }
}
