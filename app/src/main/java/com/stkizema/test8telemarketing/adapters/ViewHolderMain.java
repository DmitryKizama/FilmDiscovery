package com.stkizema.test8telemarketing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

public class ViewHolderMain extends RecyclerView.ViewHolder {

    public ImageView imgMovie;
    public TextView tvTitle, tvPopularity, tvVotes, tvReleaseDate;

    public ViewHolderMain(View itemView) {
        super(itemView);
        imgMovie = (ImageView) itemView.findViewById(R.id.img_title);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
        tvPopularity = (TextView) itemView.findViewById(R.id.popularity);
        tvVotes = (TextView) itemView.findViewById(R.id.vote);
    }
}
