package com.codebosses.flicks.adapters.celebritiesadapter;

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
import com.codebosses.flicks.pojo.celebritiespojo.CelebritiesResult;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
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

public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.CelebritiesHolder> {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<CelebritiesResult> celebritiesResultArrayList = new ArrayList<>();
    private String celebType;

    public CelebritiesAdapter(Context context, List<CelebritiesResult> celebritiesResultArrayList, String celebType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.celebritiesResultArrayList = celebritiesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.celebType = celebType;
    }

    @NonNull
    @Override
    public CelebritiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_celebrities, parent, false);
        return new CelebritiesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebritiesHolder holder, int position) {
        CelebritiesResult celebritiesResult = celebritiesResultArrayList.get(position);
        if (celebritiesResult != null) {
            if (celebritiesResult.getProfile_path() != null && !celebritiesResult.getProfile_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + celebritiesResult.getProfile_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .into(holder.imageViewThumbnail);
            String title = celebritiesResult.getName();
            if (title.length() > 17)
                title = title.substring(0, 17) + "...";
            holder.textViewMovieTitle.setText(title);
            holder.textViewRatingCount.setText(String.valueOf(celebritiesResult.getPopularity()));
        }
    }

    @Override
    public int getItemCount() {
        return celebritiesResultArrayList.size();
    }

    class CelebritiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailCelebritiesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleCelebritiesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewAudienceCelebritiesRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingCelebritiesRow)
        TextView textViewRatingCount;

        CelebritiesHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewBoldFont(textViewMovieTitle);
            fontUtils.setTextViewRegularFont(textViewRatingText);
            fontUtils.setTextViewBoldFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusCelebrityClick(getAdapterPosition(), celebType));
        }

    }

}
