package com.codebosses.flicks.adapters.celebrity_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsData;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CelebTvShowsDetailAdapter extends RecyclerView.Adapter<CelebTvShowsDetailAdapter.CelebTvShowsDetailHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<CelebTvShowsData> moviesResultArrayList = new ArrayList<>();
    private String movieType;

    public CelebTvShowsDetailAdapter(Context context, List<CelebTvShowsData> moviesResultArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public CelebTvShowsDetailAdapter.CelebTvShowsDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_similar_movie, parent, false);
        return new CelebTvShowsDetailAdapter.CelebTvShowsDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebTvShowsDetailAdapter.CelebTvShowsDetailHolder holder, int position) {
        CelebTvShowsData moviesResult = moviesResultArrayList.get(position);
        if (moviesResult != null) {
            if (moviesResult.getPosterPath() != null && !moviesResult.getPosterPath().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = moviesResult.getOriginalName();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            double movieRating = moviesResult.getVoteAverage();
            holder.materialRatingBar.setRating((float) movieRating / 2);
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class CelebTvShowsDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        CelebTvShowsDetailHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), movieType));
        }

    }

}
