package com.vcsaba.beerware.marcadorapp.ui.table;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.MatchAdapter;
import com.vcsaba.beerware.marcadorapp.adapter.TableAdapter;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.data.TableObject;
import com.vcsaba.beerware.marcadorapp.data.Team;
import com.vcsaba.beerware.marcadorapp.network.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {
    private RecyclerView recyclerView;
    private TableAdapter adapter;
    private MarcadorDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_table, container, false);

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                MarcadorDatabase.class,
                "marcador"
        ).build();

        initRecyclerView(root);

        return root;
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.list_table);
        adapter = new TableAdapter(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE), database);
        loadTableData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    private void loadTableData() {
        NetworkManager.getInstance().getTable().enqueue(new Callback<List<TableObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<TableObject>> call, @NonNull Response<List<TableObject>> response) {
                if (response.isSuccessful()) {
                    List<TableObject> items = response.body();
                    adapter.update(items);
                    Log.d("TableFragment", "HTTP request was successful!");
                }
                else {
                    Log.d("TableFragment", "HTTP request was not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<TableObject>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("TableFragment", "Network error has occured!");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }
}