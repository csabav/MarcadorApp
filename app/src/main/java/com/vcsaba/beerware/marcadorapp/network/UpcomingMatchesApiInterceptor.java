package com.vcsaba.beerware.marcadorapp.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vcsaba.beerware.marcadorapp.data.Match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpcomingMatchesApiInterceptor implements Interceptor {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        final ResponseBody body = response.body();
        JsonObject apiResponse = new JsonParser().parse(body.string()).getAsJsonObject();
        body.close();

        JsonArray matches = new JsonArray();
        JsonArray events = apiResponse.get("events").getAsJsonArray();
        for (int i = 0; i < events.size(); i++) {
            JsonObject event = events.get(i).getAsJsonObject();
            JsonObject match = new JsonObject();

            match.addProperty("idHomeTeam", event.get("idHomeTeam").getAsLong());
            match.addProperty("idAwayTeam", event.get("idAwayTeam").getAsLong());
            match.addProperty("dateEvent", event.get("dateEvent").getAsString());
            match.addProperty("strTimeLocal", event.get("strTimeLocal").getAsString());

            matches.add(match);
        }

        final Response.Builder newResponse = response.newBuilder()
                .body(ResponseBody.create(JSON, matches.toString()));
        return newResponse.build();
    }
}
