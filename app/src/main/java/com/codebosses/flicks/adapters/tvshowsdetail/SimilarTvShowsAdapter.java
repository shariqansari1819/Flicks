package com.codebosses.flicks.adapters.tvshowsdetail;

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
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class SimilarTvShowsAdapter extends RecyclerView.Adapter<SimilarTvShowsAdapter.SimilarTvShowsHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private String tvType;

    public SimilarTvShowsAdapter(Context context, List<TvResult> tvResultArrayList, String tvType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.tvResultArrayList = tvResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.tvType = tvType;
    }

    @NonNull
    @Override
    public SimilarTvShowsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_similar_movie, parent, false);
        return new SimilarTvShowsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarTvShowsHolder holder, int position) {
        TvResult moviesResult = tvResultArrayList.get(position);
        if (moviesResult != null) {
            if (moviesResult.getPoster_path() != null && !moviesResult.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = moviesResult.getName();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            double movieRating = moviesResult.getVote_average();
            holder.materialRatingBar.setRating((float) movieRating / 2);
        }
    }

    @Override
    public int getItemCount() {
        return tvResultArrayList.size();
    }

    class SimilarTvShowsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        SimilarTvShowsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), tvType));
        }

    }

}
