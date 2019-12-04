package com.vcsaba.beerware.marcadorapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.data.Team;
import com.vcsaba.beerware.marcadorapp.network.NetworkManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private MarcadorDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // splash screen logic
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(
                getApplicationContext(),
                MarcadorDatabase.class,
                "marcador"
        ).build();

        new InitDbTask().execute();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_schedule,
                R.id.navigation_table,
                R.id.navigation_scorers,
                R.id.navigation_profile).build();
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private class InitDbTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            database.teamDao().clearTeams();
            List<Team> dbTeams = database.teamDao().getAll();
            return dbTeams.size() <= 0;
        }

        @Override
        protected void onPostExecute(Boolean isEmpty) {
            if (isEmpty) {
                loadTeamsData();
            } else {
                Log.d("MainActivity", "Database was already initialized.");
            }
        }
    }

    private void loadTeamsData() {
        NetworkManager.getInstance().getTeams().enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(@NonNull Call<List<Team>> call, @NonNull Response<List<Team>> response) {
                if (response.isSuccessful()) {
                    Log.d("MainActivity", "HTTP request was successful!");
                    new InitializeDbTeamsTask().execute(response.body());
                }
                else {
                    Log.d("MainActivity", "HTTP request was not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Team>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("MainActivity", "Network error has occured!");
            }
        });
    }


    private class InitializeDbTeamsTask extends AsyncTask<List<Team>, Void, Void> {
        @Override
        protected Void doInBackground(List<Team>... teams) {
            for (Team team : teams[0]) {
                database.teamDao().insert(team);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            Log.d("MainActivity", "Database has been successfully initialized.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

}
