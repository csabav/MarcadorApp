package com.vcsaba.beerware.marcadorapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.data.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private List<Match> items = new ArrayList<>();
    private Context context;
    private SharedPreferences prefs;

    public MatchAdapter(Context _context, SharedPreferences _prefs) {
        context = _context;
        prefs = _prefs;

        items.add(new Match(133740, "Villarreal", 133729, "Ath Madrid", "2019-12-06", "21:00"));
        items.add(new Match(133732, "Levante", 133725, "Valencia", "2019-12-07", "18:30"));
        items.add(new Match(133739, "Barcelona", 133733, "Mallorca", "2019-12-07", "21:00"));
        items.add(new Match(133738, "Real Madrid", 133734, "Espanol", "2019-12-08", "13:00"));
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
        Match match = items.get(position);
        holder.homeTextView.setText(match.homeTeam);
        holder.awayTextView.setText(match.awayTeam);
        holder.dateTextView.setText(match.date + " " + match.time);

        Resources resources = context.getResources();
        long id = prefs.getLong(resources.getString(R.string.preference_team_id), -1);
        if (match.homeTeamId == id) {
            holder.homeTextView.setTypeface(null, Typeface.BOLD);
        }
        else if (match.awayTeamId == id) {
            holder.awayTextView.setTypeface(null, Typeface.BOLD);
        }

        holder.match = match;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        public TextView homeTextView;
        public TextView dateTextView;
        public TextView awayTextView;
        public Match match;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            homeTextView = itemView.findViewById(R.id.text_match_team_home);
            dateTextView = itemView.findViewById(R.id.text_match_datetime);
            awayTextView = itemView.findViewById(R.id.text_match_team_away);
        }
    }
}
