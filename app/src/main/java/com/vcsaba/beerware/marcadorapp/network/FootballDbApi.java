package com.vcsaba.beerware.marcadorapp.network;

import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.data.TableObject;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FootballDbApi {
    @GET("/api/v1/json/1/eventsnextleague.php")
    Call<List<Match>> getUpcomingMatches(
            @Query("id") Long leagueId
    );

    @GET("/api/v1/json/1/lookup_all_teams.php")
    Call<List<Team>> getTeams(
            @Query("id") Long leagueId
    );

    @GET("/api/v1/json/1/eventsround.php")
    Call<List<Match>> getGamedayMatches(
            @Query("id") Long leagueId,
            @Query("r") int round
    );

    @GET("/api/v1/json/1/lookuptable.php")
    Call<List<TableObject>> getTable(
            @Query("l") Long leagueId,
            @Query("s") String season
    );
}
