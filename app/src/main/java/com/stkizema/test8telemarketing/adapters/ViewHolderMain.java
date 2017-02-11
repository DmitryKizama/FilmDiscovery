package com.stkizema.test8telemarketing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

public class ViewHolderMain extends RecyclerView.ViewHolder {

    public ImageView imgMovie;
    public TextView tvTitle, tvPopularity, tvVotes, tvReleaseDate, tvOriginalTitle, tvDescription;

    public ViewHolderMain(View itemView) {
        super(itemView);
        imgMovie = (ImageView) itemView.findViewById(R.id.img_title);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
//        tvPopularity = (TextView) itemView.findViewById(R.id.tv_popularity);
        tvVotes = (TextView) itemView.findViewById(R.id.vote);
//        tvOriginalTitle = (TextView) itemView.findViewById(R.id.tv_original_title);
        tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
        tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
    }
}
