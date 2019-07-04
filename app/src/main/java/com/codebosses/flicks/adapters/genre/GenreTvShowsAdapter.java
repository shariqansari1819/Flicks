package com.codebosses.flicks.adapters.genre;

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
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class GenreTvShowsAdapter extends RecyclerView.Adapter<GenreTvShowsAdapter.GenreTvShowsHolder> {

    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<TvResult> tvShowsResultArrayList = new ArrayList<>();
    private String tvShowType;

    public GenreTvShowsAdapter(Context context, List<TvResult> tvShowsResultArrayList, String tvShowType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.tvShowsResultArrayList = tvShowsResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.tvShowType = tvShowType;
    }

    @NonNull
    @Override
    public GenreTvShowsAdapter.GenreTvShowsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_genre, parent, false);
        return new GenreTvShowsAdapter.GenreTvShowsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreTvShowsAdapter.GenreTvShowsHolder holder, int position) {
        TvResult tvResult = tvShowsResultArrayList.get(position);
        if (tvResult != null) {
            if (tvResult.getPoster_path() != null && !tvResult.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + tvResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = tvResult.getOriginal_name();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            double movieRating = tvResult.getVote_average();
            holder.materialRatingBar.setRating((float) movieRating / 2);
        }
    }

    @Override
    public int getItemCount() {
        return tvShowsResultArrayList.size();
    }

    class GenreTvShowsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        GenreTvShowsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), tvShowType));
        }

    }
}
