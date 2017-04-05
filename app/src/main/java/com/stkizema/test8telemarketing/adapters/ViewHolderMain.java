package com.stkizema.test8telemarketing.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderMain extends RecyclerView.ViewHolder {

    @BindView(R.id.img_title)
    public ImageView imgMovie;

    @BindView(R.id.title)
    public TextView tvTitle;

    @BindView(R.id.vote)
    public TextView tvVotes;

    @BindView(R.id.tv_release_date)
    public TextView tvReleaseDate;

    @BindView(R.id.tv_description)
    public TextView tvDescription;

    @BindView(R.id.tv_categories)
    public TextView tvCategories;

    @BindView(R.id.card_view)
    public CardView card;

    public ViewHolderMain(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
