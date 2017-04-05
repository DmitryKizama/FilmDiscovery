package com.stkizema.test8telemarketing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.model.credit.actors.CreditCastClient;

import java.util.ArrayList;
import java.util.List;

public class ActorsAdapter extends RecyclerView.Adapter<ViewHolderActors> {

    private static final String IMG_PATH = "https://image.tmdb.org/t/p/w500";

    private Context context;
    private List<CreditCastClient> list;

    public ActorsAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setList(List<CreditCastClient> newlist){
        list.addAll(newlist);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderActors onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_actor, parent, false);
        return new ViewHolderActors(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderActors holder, int position) {
        CreditCastClient actor = list.get(position);
        holder.tvName.setText(actor.getName());
        Ion.with(context)
                .load(IMG_PATH + actor.getProfile_path())
                .withBitmap()
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.defaultimg)
                .intoImageView(holder.imgActor);
    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }
}
