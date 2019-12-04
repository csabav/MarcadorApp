package com.vcsaba.beerware.marcadorapp.ui.home;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.MatchAdapter;
import com.vcsaba.beerware.marcadorapp.adapter.TeamAdapter;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.network.NetworkManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private MarcadorDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                MarcadorDatabase.class,
                "marcador"
        ).build();

        initRecyclerView(root);

        return root;
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.list_upcoming_matches);
        adapter = new MatchAdapter(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE));
        loadUpcomingMatchesData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadUpcomingMatchesData() {
        NetworkManager.getInstance().getUpcomingMatches().enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful()) {
                    adapter.update(response.body());
                    Log.d("HomeFragment", "HTTP request was successful!");
                }
                else {
                    Log.d("HomeFragment", "HTTP request was not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("HomeFragment", "Network error has occured!");
            }
        });
    }
}