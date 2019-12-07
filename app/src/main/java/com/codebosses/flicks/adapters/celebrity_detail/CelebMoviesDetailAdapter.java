package com.codebosses.flicks.adapters.celebrity_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CelebMoviesDetailAdapter extends RecyclerView.Adapter<CelebMoviesDetailAdapter.CelebMoviesDetailHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<CelebMoviesData> moviesResultArrayList = new ArrayList<>();
    private String movieType;

    public CelebMoviesDetailAdapter(Context context, List<CelebMoviesData> moviesResultArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public CelebMoviesDetailAdapter.CelebMoviesDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_similar_movie, parent, false);
        return new CelebMoviesDetailAdapter.CelebMoviesDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebMoviesDetailAdapter.CelebMoviesDetailHolder holder, int position) {
        CelebMoviesData moviesResult = moviesResultArrayList.get(position);
        if (moviesResult != null) {
            if (moviesResult.getPoster_path() != null && !moviesResult.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = moviesResult.getTitle();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            double movieRating = moviesResult.getVote_average();
            holder.materialRatingBar.setRating((float) movieRating / 2);
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class CelebMoviesDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        CelebMoviesDetailHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusMovieClick(getAdapterPosition(), movieType));
        }

    }

}
