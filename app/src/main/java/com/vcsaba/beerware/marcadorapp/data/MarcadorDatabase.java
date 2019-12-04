package com.vcsaba.beerware.marcadorapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Team.class},
        exportSchema = false,
        version = 1
)
public abstract class MarcadorDatabase extends RoomDatabase {
    public abstract TeamDao teamDao();
}
