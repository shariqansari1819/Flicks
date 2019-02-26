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
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.Season;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class TvShowSeasonsAdapter extends RecyclerView.Adapter<TvShowSeasonsAdapter.TvShowSeasonsHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<Season> seasonList = new ArrayList<>();
    private String tvType;

    public TvShowSeasonsAdapter(Context context, List<Season> seasonList, String tvType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.seasonList = seasonList;
        layoutInflater = LayoutInflater.from(context);
        this.tvType = tvType;
    }

    @NonNull
    @Override
    public TvShowSeasonsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_similar_movie, parent, false);
        return new TvShowSeasonsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowSeasonsHolder holder, int position) {
        Season season = seasonList.get(position);
        if (season != null) {
            if (season.getPoster_path() != null && !season.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + season.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .into(holder.imageViewThumbnail);
            String title = season.getName();
            if (title != null) {
                if (title.length() > 10)
                    title = title.substring(0, 10) + "...";
                holder.textViewMovieTitle.setText(title);
            }
            holder.materialRatingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return seasonList.size();
    }

    class TvShowSeasonsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        TvShowSeasonsHolder(@NonNull View itemView) {
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
