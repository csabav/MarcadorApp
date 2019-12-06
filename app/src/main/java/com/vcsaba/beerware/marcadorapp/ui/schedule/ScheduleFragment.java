package com.vcsaba.beerware.marcadorapp.ui.schedule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.MatchAdapter;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.network.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private MatchAdapter adapter;
    private MarcadorDatabase database;
    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                MarcadorDatabase.class,
                "marcador"
        ).build();

        initSpinner(root);
        initRecyclerView(root);

        return root;
    }

    private void initSpinner(View root) {
        spinner = root.findViewById(R.id.spinner_schedule);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_schedule, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(15); // gameday 16 by default
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.list_schedule);
        adapter = new MatchAdapter(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE), database);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = (String)parent.getItemAtPosition(position);
        int gamedaySelected = Integer.parseInt(selected.substring(8));
        loadGamedayMatchesData(gamedaySelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO
    }

    private void loadGamedayMatchesData(int gameday) {
        NetworkManager.getInstance().getGamedayMatches(gameday).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(@NonNull Call<List<Match>> call, @NonNull Response<List<Match>> response) {
                if (response.isSuccessful()) {
                    List<Match> matches = response.body();
                    // TODO matches.sort();
                    adapter.update(matches);
                    Log.d("ScheduleFragment", "HTTP request was successful!");
                }
                else {
                    Log.d("ScheduleFragment", "HTTP request was not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Match>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("ScheduleFragment", "Network error has occured!");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }
}