package com.stkizema.test8telemarketing.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.koushikdutta.ion.Ion;
import com.stkizema.test8telemarketing.R;
import com.stkizema.test8telemarketing.db.CategoryHelper;
import com.stkizema.test8telemarketing.db.model.Category;
import com.stkizema.test8telemarketing.db.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<ViewHolderMain> {

    private static final String IMG_PATH = "https://image.tmdb.org/t/p/w500";
    private static final String NOTHING = "";

    private List<Movie> list;
    private Context con;

    public MoviesAdapter(Context con, List<Movie> list) {
        this.list = list;
        this.con = con;
    }

    public void setList(List<Movie> list) {
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
        Movie movie = list.get(position);
        Ion.with(con)
                .load(IMG_PATH + movie.getPosterPath())
                .withBitmap()
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.defaultimg)
                .intoImageView(holder.imgMovie);
//        holder.tvOriginalTitle.setText("Original title: " + list.get(position).getOriginalTitle());
        holder.tvTitle.setText(movie.getTitle());
        holder.tvVotes.setText(movie.getVoteAverage() + NOTHING);
        holder.tvReleaseDate.setText(movie.getReleaseDate());
        holder.tvDescription.setText(movie.getOverview());

        holder.tvCategories.setText(getCategoriesStr(movie.getId()));
    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }


    private String getCategoriesStr(Integer id) {
        List<Category> listCat = CategoryHelper.getCategoriesByMovieId(id);
        String str = NOTHING;
        if (listCat != null) {
            for (Category category : listCat) {
                if (category != null) {
                    str = str + category.getName() + ", ";
                }
            }
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

}
