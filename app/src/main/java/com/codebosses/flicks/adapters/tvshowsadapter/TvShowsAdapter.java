package com.codebosses.flicks.adapters.tvshowsadapter;

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
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<TvResult> tvResultArrayList = new ArrayList<>();
    private String tvShowType;

    public TvShowsAdapter(Context context, List<TvResult> tvResultArrayList, String tvShowType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.tvResultArrayList = tvResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.tvShowType = tvShowType;
    }

    @NonNull
    @Override
    public TvShowsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_tv_shows, parent, false);
        return new TvShowsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsHolder holder, int position) {
        TvResult tvResult = tvResultArrayList.get(position);
        if (tvResult != null) {
            Glide.with(context)
                    .load(EndpointUrl.POSTER_BASE_URL + "/" + tvResult.getPoster_path())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .into(holder.imageViewThumbnail);
            String title = tvResult.getOriginal_name();
            if (title.length() > 19)
                title = title.substring(0, 20);
            holder.textViewMovieTitle.setText(title);
            holder.textViewMovieYear.setText(tvResult.getFirst_air_date());
            holder.textViewRatingCount.setText(String.valueOf(tvResult.getVote_average()));
            holder.textViewVoteCount.setText(String.valueOf(tvResult.getVote_count()));
        }
    }

    @Override
    public int getItemCount() {
        return tvResultArrayList.size();
    }

    class TvShowsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailTvShowsRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleTvShowsRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewYearTvShowsRow)
        TextView textViewMovieYear;
        @BindView(R.id.textViewAudienceTvShowsRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingTvShowsRow)
        TextView textViewRatingCount;
        @BindView(R.id.textViewVotesTvShowsRow)
        TextView textViewVoteCount;
        @BindView(R.id.textViewVotesCountTvShowsRow)
        TextView textViewVoteCountText;

        TvShowsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewBoldFont(textViewMovieTitle);
            fontUtils.setTextViewRegularFont(textViewMovieYear);
            fontUtils.setTextViewRegularFont(textViewRatingText);
            fontUtils.setTextViewRegularFont(textViewVoteCountText);
            fontUtils.setTextViewBoldFont(textViewVoteCount);
            fontUtils.setTextViewBoldFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusTvShowsClick(getAdapterPosition(), tvShowType));
        }

    }

}
