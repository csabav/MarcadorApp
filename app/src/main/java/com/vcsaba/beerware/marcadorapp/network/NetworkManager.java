package com.vcsaba.beerware.marcadorapp.network;

import android.content.Context;

import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static final String SERVICE_URL = "https://www.thesportsdb.com";
    private static final Long LEAGUE_ID = 4335L;

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public Call<List<Match>> getUpcomingMatches() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_URL)
                .client(new OkHttpClient.Builder().addInterceptor(new UpcomingMatchesApiInterceptor()).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FootballDbApi api = retrofit.create(FootballDbApi.class);

        return api.getUpcomingMatches(LEAGUE_ID);
    }

    public Call<List<Team>> getTeams() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_URL)
                .client(new OkHttpClient.Builder().addInterceptor(new TeamsApiInterceptor()).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FootballDbApi api = retrofit.create(FootballDbApi.class);

        return api.getTeams(LEAGUE_ID);
    }
}