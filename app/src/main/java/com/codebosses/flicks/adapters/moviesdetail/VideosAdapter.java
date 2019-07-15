package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();

    public VideosAdapter(Context context, List<MoviesTrailerResult> moviesTrailerResultList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.moviesTrailerResultList = moviesTrailerResultList;
    }

    @NonNull
    @Override
    public VideosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_video, parent, false);
        return new VideosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosHolder holder, int position) {
        MoviesTrailerResult moviesTrailerResult = moviesTrailerResultList.get(position);
        holder.textViewNameVideo.setText(moviesTrailerResult.getName());
        Glide.with(context)
                .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResult.getKey() + "/hqdefault.jpg")
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .thumbnail(0.1f)
                .into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return moviesTrailerResultList.size();
    }

    class VideosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailVideoRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewNameVideoRow)
        TextView textViewNameVideo;
        @BindView(R.id.imageButtonPlayVideoRow)
        ImageButton imageButtonPlay;

        public VideosHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            imageButtonPlay.setOnClickListener(this);
            imageViewThumbnail.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusPlayVideo(getAdapterPosition()));
        }
    }
}
