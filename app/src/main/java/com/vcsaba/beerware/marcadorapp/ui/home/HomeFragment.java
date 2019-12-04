package com.vcsaba.beerware.marcadorapp.ui.home;

import android.content.Context;
import android.os.Bundle;
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

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.MatchAdapter;
import com.vcsaba.beerware.marcadorapp.adapter.TeamAdapter;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MatchAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initRecyclerView(root);
        return root;
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.list_upcoming_matches);
        adapter = new MatchAdapter(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}