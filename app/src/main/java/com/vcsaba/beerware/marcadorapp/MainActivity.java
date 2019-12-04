package com.vcsaba.beerware.marcadorapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

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

        initDatabase();

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

    private void initDatabase() {
        // ellenorizzuk, hogy inicializalva volt e mar a DB, ha nem akkor tegyuk meg
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                List<Team> teams = database.teamDao().getAll();
                return teams.size() > 0 ? false : true;
            }

            @Override
            protected void onPostExecute(Boolean isEmpty) {
                if (isEmpty) {
                    final List<Team> items = new ArrayList<>();
                    items.add(new Team((long) 134221, "Alaves", "https://www.thesportsdb.com/images/media/team/badge/vwqswq1420325494.png"));
                    items.add(new Team((long) 133727, "Ath Bilbao", "https://www.thesportsdb.com/images/media/team/badge/1gs1c31549394822.png"));
                    items.add(new Team((long) 133729, "Atlético de Madrid", "https://www.thesportsdb.com/images/media/team/badge/big56a1490135063.png"));
                    items.add(new Team((long) 133739, "Barcelona", "https://www.thesportsdb.com/images/media/team/badge/xqwpup1473502878.png"));

                    new AsyncTask<List<Team>, Void, Void>() {
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
                    }.execute(items);
                }
                else {
                    Log.d("MainActivity", "Database was already initialized.");
                }
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }

}
