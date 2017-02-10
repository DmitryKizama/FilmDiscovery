package com.stkizema.test8telemarketing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.db.model.Movie;

import java.util.List;

public class RVAdapterMain extends RecyclerView.Adapter<ViewHolderMain> {

    private List<Movie> list;
    private Context con;

    public RVAdapterMain(Context con, List<Movie> list) {
        this.list = list;
        this.con = con;
    }

    public void setList(List<Movie> list) {
        Log.d("SERVICEPROBLMS", "set list = " + list.size());
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderMain onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_main, parent, false);
        return new ViewHolderMain(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderMain holder, int position) {
//        holder.getAdapterPosition();
        Ion.with(con)
                .load("https://image.tmdb.org/t/p/w500" + list.get(position).getPosterPath())
                .withBitmap()
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.defaultimg)
                .intoImageView(holder.imgMovie);

        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvVotes.setText(list.get(position).getVoteAverage().toString());
        holder.tvPopularity.setText("Votes:" + "\n" + list.get(position).getVoteCount());
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
