package com.vcsaba.beerware.marcadorapp.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.adapter.TeamAdapter;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ProfileFragment extends Fragment implements TeamAdapter.TeamSavedListener {
    private RecyclerView recyclerView;
    private TeamAdapter adapter;

    private View root;
    private Button btnSave;

    private MarcadorDatabase database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        database = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                MarcadorDatabase.class,
                "marcador"
        ).build();

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
        adapter = new TeamAdapter(getContext(), getActivity().getPreferences(Context.MODE_PRIVATE));
        new GetAllTeamsTask().execute();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
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

    private class GetAllTeamsTask extends AsyncTask<Void, Void, List<Team>> {
        @Override
        protected List<Team> doInBackground(Void... voids) {
            return database.teamDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Team> teams) {
            adapter.update(teams);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView _imageView) {
            this.imageView = _imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap logo = null;
            try {
                InputStream in = new URL(url).openStream();
                logo = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }
}