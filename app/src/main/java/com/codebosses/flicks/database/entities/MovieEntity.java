package com.codebosses.flicks.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@Entity
public class MovieEntity {

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private int movieId;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "popularity")
    private double popularity;
    @ColumnInfo(name = "vote_average")
    private double voteAverage;
    @Ignore
    @ServerTimestamp
    private Date date;

    public MovieEntity() {
    }

    public MovieEntity(int movieId, String posterPath, String title, String overview, String releaseDate, double popularity, double voteAverage) {
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}
