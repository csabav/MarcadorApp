package com.vcsaba.beerware.marcadorapp.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TeamsApiInterceptor implements Interceptor {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        final ResponseBody body = response.body();
        JsonObject apiResponse = new JsonParser().parse(body.string()).getAsJsonObject();
        body.close();

        JsonArray parsed_teams = new JsonArray();
        JsonArray teams = apiResponse.get("teams").getAsJsonArray();
        for (int i = 0; i < teams.size(); i++) {
            JsonObject team = teams.get(i).getAsJsonObject();
            JsonObject parsed_team = new JsonObject();

            parsed_team.addProperty("id", team.get("idTeam").getAsLong());
            parsed_team.addProperty("name", team.get("strTeam").getAsString());
            parsed_team.addProperty("badgeURL", team.get("strTeamBadge").getAsString());

            parsed_teams.add(parsed_team);
        }

        final Response.Builder newResponse = response.newBuilder()
                .body(ResponseBody.create(JSON, parsed_teams.toString()));
        return newResponse.build();
    }
}
