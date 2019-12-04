package com.vcsaba.beerware.marcadorapp.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Team")
public class Team {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    public Long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "badgeURL")
    public String badgeURL;

    public Team(Long id, String name, String badgeURL) {
        this.id = id;
        this.name = name;
        this.badgeURL = badgeURL;
    }
}
