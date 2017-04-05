package com.stkizema.test8telemarketing.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.util.UiHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderActors extends RecyclerView.ViewHolder {

    private static final int RAW_NUMBER = 3;

    @BindView(R.id.img_actor)
    public ImageView imgActor;

    @BindView(R.id.tv_name_actor)
    public TextView tvName;

    public ViewHolderActors(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.getLayoutParams().width = UiHelper.getW() / RAW_NUMBER;
    }
}
