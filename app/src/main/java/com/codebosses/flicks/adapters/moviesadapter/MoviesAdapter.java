package com.codebosses.flicks.adapters.moviesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<MoviesResult> moviesResultArrayList = new ArrayList<>();

    public MoviesAdapter(Context context, List<MoviesResult> moviesResultArrayList) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_movies, parent, false);
        return new MoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder holder, int position) {
        MoviesResult moviesResult = moviesResultArrayList.get(position);
        if (moviesResult != null) {
            Glide.with(context)
                    .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPoster_path())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .into(holder.imageViewThumbnail);
            String title = moviesResult.getTitle();
            if (title.length() > 19)
                title = title.substring(0, 20);
            holder.textViewMovieTitle.setText(title);
            holder.textViewMovieYear.setText(moviesResult.getRelease_date());
            holder.textViewRatingCount.setText(String.valueOf(moviesResult.getVote_average()));
            holder.textViewVoteCount.setText(String.valueOf(moviesResult.getVote_count()));
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class MoviesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewThumbnailMainRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewYearMoviesRow)
        TextView textViewMovieYear;
        @BindView(R.id.textViewAudienceMainRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingMoviesRow)
        TextView textViewRatingCount;
        @BindView(R.id.textViewVotesMoviesRow)
        TextView textViewVoteCount;
        @BindView(R.id.textViewVotesCountMoviesRow)
        TextView textViewVoteCountText;

        public MoviesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewBoldFont(textViewMovieTitle);
            fontUtils.setTextViewRegularFont(textViewMovieYear);
            fontUtils.setTextViewRegularFont(textViewRatingText);
            fontUtils.setTextViewRegularFont(textViewVoteCountText);
            fontUtils.setTextViewBoldFont(textViewVoteCount);
            fontUtils.setTextViewBoldFont(textViewRatingCount);
        }
    }

}
