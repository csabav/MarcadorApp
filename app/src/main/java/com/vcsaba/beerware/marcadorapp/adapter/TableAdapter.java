package com.vcsaba.beerware.marcadorapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vcsaba.beerware.marcadorapp.R;
import com.vcsaba.beerware.marcadorapp.data.MarcadorDatabase;
import com.vcsaba.beerware.marcadorapp.data.TableObject;
import com.vcsaba.beerware.marcadorapp.data.Team;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableObjectViewHolder> {
    private List<TableObject> items = new ArrayList<>();
    private Context context;
    private SharedPreferences prefs;
    private MarcadorDatabase database;

    public TableAdapter(Context _context, SharedPreferences _prefs, MarcadorDatabase _database) {
        context = _context;
        prefs = _prefs;
        database = _database;
    }

    @NonNull
    @Override
    public TableObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_table, parent, false);

        TableObjectViewHolder holder = new TableObjectViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TableObjectViewHolder holder, int position) {
        holder.tableObject = items.get(position);
        new SetTableDetailsTask(holder).execute();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(List<TableObject> objects) {
        items.clear();
        items.addAll(objects);
        notifyDataSetChanged();
    }

    class TableObjectViewHolder extends RecyclerView.ViewHolder {
        TextView numberTextView;
        ImageView badgeImageView;
        TextView teamTextView;
        TextView playedTextView;
        TextView winsTextView;
        TextView drawsTextView;
        TextView lossesTextView;
        TextView goalsforTextView;
        TextView goalsagainstTextView;
        TextView goalsdifferenceTextView;
        TextView pointsTextView;

        TableObject tableObject;

        TableObjectViewHolder(@NonNull View itemView) {
            super(itemView);

            numberTextView = itemView.findViewById(R.id.table_item_number);
            badgeImageView = itemView.findViewById(R.id.table_item_image_team);
            teamTextView = itemView.findViewById(R.id.table_item_team);
            playedTextView = itemView.findViewById(R.id.table_item_played);
            winsTextView = itemView.findViewById(R.id.table_item_wins);
            drawsTextView = itemView.findViewById(R.id.table_item_draws);
            lossesTextView = itemView.findViewById(R.id.table_item_losses);
            goalsforTextView = itemView.findViewById(R.id.table_item_goalsfor);
            goalsagainstTextView = itemView.findViewById(R.id.table_item_goalsagainst);
            goalsdifferenceTextView = itemView.findViewById(R.id.table_item_goalsdifference);
            pointsTextView = itemView.findViewById(R.id.table_item_points);
        }
    }

    private class SetTableDetailsTask extends AsyncTask<Void, Void, Team> {
        TableObjectViewHolder holder;

        public SetTableDetailsTask(TableObjectViewHolder _holder) {
            holder = _holder;
        }

        @Override
        protected Team doInBackground(Void... voids) {
            return database.teamDao().getOneById(holder.tableObject.teamId);
        }

        @Override
        protected void onPostExecute(Team team) {
            holder.numberTextView.setText(Integer.toString(holder.getAdapterPosition() + 1));
            new DownloadImageTask(holder.badgeImageView).execute(team.badgeURL);
            holder.teamTextView.setText(team.name);
            holder.playedTextView.setText(holder.tableObject.played);
            holder.winsTextView.setText(holder.tableObject.wins);
            holder.drawsTextView.setText(holder.tableObject.draws);
            holder.lossesTextView.setText(holder.tableObject.losses);
            holder.goalsforTextView.setText(holder.tableObject.goalsfor);
            holder.goalsagainstTextView.setText(holder.tableObject.goalsagainst);
            holder.goalsdifferenceTextView.setText(holder.tableObject.goalsdifference);

            holder.pointsTextView.setText(holder.tableObject.points);

            Resources resources = context.getResources();
            long id = prefs.getLong(resources.getString(R.string.preference_team_id), 0);
            if (team.id == id) {
                holder.teamTextView.setTypeface(null, Typeface.BOLD);
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
