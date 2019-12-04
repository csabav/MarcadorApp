package com.vcsaba.beerware.marcadorapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM Team ORDER BY name ASC")
    List<Team> getAll();

    @Insert
    long insert(Team team);

    @Update
    void update(Team team);

    @Delete
    void deleteItem(Team team);
}
