package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;
import com.codebosses.flicks.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesGenreAdapter extends RecyclerView.Adapter<MoviesGenreAdapter.MoviesGenreHolder> {

    private List<Genre> genreList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public MoviesGenreAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public MoviesGenreAdapter.MoviesGenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoviesGenreAdapter.MoviesGenreHolder(layoutInflater.inflate(R.layout.row_genre_movies, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesGenreAdapter.MoviesGenreHolder genreHolder, int position) {
        genreHolder.textViewGenre.setText(genreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }


    class MoviesGenreHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewGenreMoviesRow)
        TextView textViewGenre;

        MoviesGenreHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewGenre);
        }
    }


}
