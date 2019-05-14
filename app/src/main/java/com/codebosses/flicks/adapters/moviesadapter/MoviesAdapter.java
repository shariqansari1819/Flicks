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
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<MoviesResult> moviesResultArrayList = new ArrayList<>();
    private String movieType;

    public MoviesAdapter(Context context, List<MoviesResult> moviesResultArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
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
            if (moviesResult.getPoster_path() != null && !moviesResult.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .into(holder.imageViewThumbnail);
            String title = moviesResult.getTitle();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            holder.textViewMovieYear.setText(moviesResult.getRelease_date());
            holder.textViewRatingCount.setText(String.valueOf(moviesResult.getVote_average()));
            holder.textViewVoteCount.setText(String.valueOf(moviesResult.getVote_count()));
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailMoviesRow)
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

        MoviesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);
            fontUtils.setTextViewLightFont(textViewMovieYear);
            fontUtils.setTextViewLightFont(textViewRatingText);
            fontUtils.setTextViewLightFont(textViewVoteCountText);
            fontUtils.setTextViewRegularFont(textViewVoteCount);
            fontUtils.setTextViewRegularFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusMovieClick(getAdapterPosition(), movieType));
        }

    }

}
