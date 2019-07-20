package com.codebosses.flicks.database;

import android.content.Context;

import androidx.room.Room;

import com.codebosses.flicks.database.room_database.FlicksDatabase;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private FlicksDatabase flicksDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        flicksDatabase = Room.databaseBuilder(mCtx, FlicksDatabase.class, "FlicksDatabase").build();
    }

    public static synchronized DatabaseClient getDatabaseClient(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public FlicksDatabase getFlicksDatabase() {
        return flicksDatabase;
    }
}
