package com.codebosses.flicks.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.codebosses.flicks.database.entities.CelebrityEntity;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.database.entities.TvShowEntity;

import java.util.List;

@Dao
public interface FlicksDao {

    @Query("SELECT * FROM movieentity")
    List<MovieEntity> getAllFavoriteMovies();

    @Query("SELECT * FROM tvshowentity")
    List<TvShowEntity> getAllFavoriteTvShows();

    @Query("SELECT * FROM celebrityentity")
    List<CelebrityEntity> getAllFavoriteCelebrity();

    @Insert
    void insertMovie(MovieEntity movieEntity);

    @Insert
    void insertTvShow(TvShowEntity tvShowEntity);

    @Insert
    void insertCelebrity(CelebrityEntity celebrityEntity);

    @Query("SELECT * FROM movieentity WHERE movie_id = :movieId")
    MovieEntity getFavoriteMovieById(int movieId);

    @Query("SELECT * FROM tvshowentity WHERE id = :tvShowId")
    TvShowEntity getFavoriteTvShowById(int tvShowId);

    @Query("SELECT * FROM celebrityentity WHERE id = :celebId")
    CelebrityEntity getFavoriteCelebById(int celebId);

    @Query("DELETE FROM movieentity WHERE movie_id = :movieId")
    void deleteMovieById(int movieId);

    @Query("DELETE FROM tvshowentity WHERE id = :tvsShowId")
    void deleteTvShowById(int tvsShowId);

    @Query("DELETE FROM celebrityentity WHERE id = :celebId")
    void deleteCelebById(int celebId);

}
