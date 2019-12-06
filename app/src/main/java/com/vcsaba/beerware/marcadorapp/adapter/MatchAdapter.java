package com.vcsaba.beerware.marcadorapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.Match;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private List<Match> items = new ArrayList<>();
    private Context context;
    private SharedPreferences prefs;
    private MarcadorDatabase database;

    public MatchAdapter(Context _context, SharedPreferences _prefs, MarcadorDatabase _database) {
        context = _context;
        prefs = _prefs;
        database = _database;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.match = items.get(position);
        new SetMatchDetailsTask(holder).execute();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<Match> matches) {
        items.clear();
        items.addAll(matches);
        notifyDataSetChanged();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView homeTextView;
        TextView dateScoreTextView;
        TextView awayTextView;
        ImageView homeImageView;
        ImageView awayImageView;
        Match match;

        MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTextView = itemView.findViewById(R.id.text_match_team_home);
            dateScoreTextView = itemView.findViewById(R.id.text_match_datetime_score);
            awayTextView = itemView.findViewById(R.id.text_match_team_away);
            homeImageView = itemView.findViewById(R.id.image_team_home);
            awayImageView = itemView.findViewById(R.id.image_team_away);
        }
    }

    private class SetMatchDetailsTask extends AsyncTask<Void, Void, List<Team>> {
        MatchViewHolder holder;

        public SetMatchDetailsTask(MatchViewHolder _holder) {
            holder = _holder;
        }

        @Override
        protected List<Team> doInBackground(Void... voids) {
            List<Team> teams = new ArrayList<>();
            teams.add(database.teamDao().getOneById(holder.match.homeTeamId));
            teams.add(database.teamDao().getOneById(holder.match.awayTeamId));
            return teams;
        }

        @Override
        protected void onPostExecute(List<Team> teams) {
            Team homeTeam = teams.get(0);
            Team awayTeam = teams.get(1);

            holder.homeTextView.setText(homeTeam.name);
            holder.awayTextView.setText(awayTeam.name);

            String dateScoreText;
            if(holder.match.homeTeamScore != null || holder.match.awayTeamScore != null) {
                dateScoreText = holder.match.homeTeamScore + " - " + holder.match.awayTeamScore;
            } else {
                dateScoreText = holder.match.date.substring(5) + " " + holder.match.time.substring(0, 5);
            }
            holder.dateScoreTextView.setText(dateScoreText);

            new DownloadImageTask(holder.homeImageView).execute(homeTeam.badgeURL);
            new DownloadImageTask(holder.awayImageView).execute(awayTeam.badgeURL);

            Resources resources = context.getResources();
            long id = prefs.getLong(resources.getString(R.string.preference_team_id), 0);
            if (homeTeam.id == id) {
                holder.homeTextView.setTypeface(null, Typeface.BOLD);
            } else if (awayTeam.id == id) {
                holder.awayTextView.setTypeface(null, Typeface.BOLD);
            }
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
