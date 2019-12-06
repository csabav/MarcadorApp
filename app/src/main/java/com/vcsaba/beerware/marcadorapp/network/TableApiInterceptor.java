package com.vcsaba.beerware.marcadorapp.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TableApiInterceptor implements Interceptor {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        final ResponseBody body = response.body();
        JsonObject apiResponse = new JsonParser().parse(body.string()).getAsJsonObject();
        body.close();

        JsonArray tableObjects = new JsonArray();
        JsonArray teams = apiResponse.get("table").getAsJsonArray();
        for (int i = 0; i < teams.size(); i++) {
            JsonObject team = teams.get(i).getAsJsonObject();
            JsonObject tableObject = new JsonObject();

            tableObject.addProperty("teamId", team.get("teamid").getAsLong());
            tableObject.addProperty("played", team.get("played").getAsString());
            tableObject.addProperty("goalsfor", team.get("goalsfor").getAsString());
            tableObject.addProperty("goalsagainst", team.get("goalsagainst").getAsString());
            tableObject.addProperty("goalsdifference", team.get("goalsdifference").getAsString());
            tableObject.addProperty("wins", team.get("win").getAsString());
            tableObject.addProperty("draws", team.get("draw").getAsString());
            tableObject.addProperty("losses", team.get("loss").getAsString());
            tableObject.addProperty("points", team.get("total").getAsString());

            tableObjects.add(tableObject);
        }

        final Response.Builder newResponse = response.newBuilder()
                .body(ResponseBody.create(JSON, tableObjects.toString()));
        return newResponse.build();
    }
}
