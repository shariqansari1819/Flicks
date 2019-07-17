package com.codebosses.flicks.database.room_database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.codebosses.flicks.database.dao.FlicksDao;
import com.codebosses.flicks.database.entities.CelebrityEntity;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.database.entities.TvShowEntity;

@Database(entities = {MovieEntity.class, TvShowEntity.class, CelebrityEntity.class}, version = 1,exportSchema = false)
public abstract class FlicksDatabase extends RoomDatabase {
    public abstract FlicksDao getFlicksDao();
}
