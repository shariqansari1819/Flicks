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
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.Season;
import com.codebosses.flicks.pojo.tvseasons.Episode;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class TvEpisodesAdapter extends RecyclerView.Adapter<TvEpisodesAdapter.TvShowEpisodesHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<Episode> episodeList = new ArrayList<>();
    private String tvType;

    public TvEpisodesAdapter(Context context, List<Episode> episodeList, String tvType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.episodeList = episodeList;
        layoutInflater = LayoutInflater.from(context);
        this.tvType = tvType;
    }

    @NonNull
    @Override
    public TvShowEpisodesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_episodes, parent, false);
        return new TvShowEpisodesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowEpisodesHolder holder, int position) {
        Episode episode = episodeList.get(position);
        if (episode != null) {
            if (episode.getStill_path() != null && !episode.getStill_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + episode.getStill_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .into(holder.imageViewThumbnail);
            String title = episode.getName();
            if (title != null) {
                if (title.length() > 10)
                    title = title.substring(0, 10) + "...";
                holder.textViewMovieTitle.setText(title);
            }
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    class TvShowEpisodesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailEpisodesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameEpisodesRow)
        TextView textViewMovieTitle;

        TvShowEpisodesHolder(@NonNull View itemView) {
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
