package com.codebosses.flicks.adapters.tvshowsdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.eventbus.EventBusImageClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodePhotosAdapter extends RecyclerView.Adapter<EpisodePhotosAdapter.EpisodePhotosHolder> {

    private List<EpisodePhotosData> episodePhotosDataList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private String clickType;

    public EpisodePhotosAdapter(Context context, List<EpisodePhotosData> episodePhotosDataList, String clickType) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.episodePhotosDataList = episodePhotosDataList;
        this.clickType = clickType;
    }

    @NonNull
    @Override
    public EpisodePhotosAdapter.EpisodePhotosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodePhotosAdapter.EpisodePhotosHolder(layoutInflater.inflate(R.layout.row_photos_episodes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodePhotosAdapter.EpisodePhotosHolder holder, int position) {
        EpisodePhotosData episodePhotosData = episodePhotosDataList.get(position);
        if (episodePhotosData.getFile_path() != null && !episodePhotosData.getFile_path().equals(""))
            Glide.with(context)
                    .load(EndpointUrl.POSTER_BASE_URL + "/" + episodePhotosData.getFile_path())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .thumbnail(0.1f)
                    .into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return episodePhotosDataList.size();
    }


    class EpisodePhotosHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewThumbnailEpisodePhotosRow)
        ImageView imageViewThumbnail;

        EpisodePhotosHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventBusImageClick(getAdapterPosition(), clickType));
                }
            });
        }
    }


}
