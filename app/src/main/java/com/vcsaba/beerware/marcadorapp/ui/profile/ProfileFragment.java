package com.vcsaba.beerware.marcadorapp.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.TeamAdapter;
import com.vcsaba.beerware.marcadorapp.data.Team;

public class ProfileFragment extends Fragment implements TeamAdapter.TeamSavedListener {
    private RecyclerView recyclerView;
    private TeamAdapter adapter;

    private View root;
    private Button btnSave;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        initRecyclerView(root);

        btnSave = root.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = adapter.getLastSelectedPosition();
                Team team = adapter.getItem(position);
                onItemSaved(team);
            }
        });

        return root;
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.list_teams);
        adapter = new TeamAdapter(this, getContext(), getActivity().getPreferences(Context.MODE_PRIVATE));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemSaved(Team team) {
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(getString(R.string.preference_team_id), team.id);
        editor.apply();

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.profile_layout), getString(R.string.message_team_saved), Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();

        int navHeight = getActivity().findViewById(R.id.nav_view).getHeight();
        params.height = navHeight;
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);

        snackbarView.setLayoutParams(params);
        snackbar.show();
    }
}