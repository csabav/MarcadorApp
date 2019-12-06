package com.vcsaba.beerware.marcadorapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private final List<Team> items;
    private Context context;
    private SharedPreferences prefs;

    private int lastSelectedPosition = -1;
    private RadioButton lastCheckedRadioButton = null;
    private long selectedTeamId;

    public TeamAdapter(Context _context, SharedPreferences _prefs) {
        context = _context;
        prefs = _prefs;
        items = new ArrayList<>();

        Resources resources = context.getResources();
        selectedTeamId = prefs.getLong(resources.getString(R.string.preference_team_id), -1L);
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, final int position) {
        Team team = items.get(position);
        holder.textView.setText(team.name);
        holder.radioButton.setChecked(false);
        new DownloadImageTask(holder.imageView).execute(team.badgeURL);

        if (lastSelectedPosition > -1 && lastCheckedRadioButton != null) {
            if (position == lastSelectedPosition) {
                holder.radioButton.setChecked(true);
            }
        } else if (team.id == selectedTeamId) {
            lastSelectedPosition = position;
            lastCheckedRadioButton = holder.radioButton;
            holder.radioButton.setChecked(true);
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v;

                lastSelectedPosition = position;
                if (lastCheckedRadioButton != null) {
                    lastCheckedRadioButton.setChecked(false);
                }
                radioButton.setChecked(true);
                lastCheckedRadioButton = radioButton;
            }
        });

        holder.team = team;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface TeamSavedListener {
        void onItemSaved(Team item);
    }

    public void addItem(Team team) {
        items.add(team);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<Team> teams) {
        items.clear();
        items.addAll(teams);
        notifyDataSetChanged();
    }

    public Team getItem(int position) {
        return items.get(position);
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;
        public ImageView imageView;
        public TextView textView;
        public Team team;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_team);
            imageView = itemView.findViewById(R.id.table_item_image_team);
            textView = itemView.findViewById(R.id.text_team_name);
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
}
