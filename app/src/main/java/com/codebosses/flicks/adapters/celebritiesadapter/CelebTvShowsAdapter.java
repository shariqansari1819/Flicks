package com.codebosses.flicks.adapters.celebritiesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebtvshows.CelebTvShowsData;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityTvShowsClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CelebTvShowsAdapter extends RecyclerView.Adapter<CelebTvShowsAdapter.CelebTvShowsHolder> implements Filterable {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<CelebTvShowsData> celebTvShowsDataList = new ArrayList<>();
    private List<CelebTvShowsData> celebTvShowsDataFilteredList = new ArrayList<>();
    private String movieType;

    public CelebTvShowsAdapter(Context context, List<CelebTvShowsData> celebTvShowsDataList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.celebTvShowsDataList = celebTvShowsDataList;
        this.celebTvShowsDataFilteredList = celebTvShowsDataList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public CelebTvShowsAdapter.CelebTvShowsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_movies, parent, false);
        return new CelebTvShowsAdapter.CelebTvShowsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebTvShowsAdapter.CelebTvShowsHolder holder, int position) {
        CelebTvShowsData celebTvShowsData = celebTvShowsDataFilteredList.get(position);
        if (celebTvShowsData != null) {
            if (celebTvShowsData.getPosterPath() != null && !celebTvShowsData.getPosterPath().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + celebTvShowsData.getPosterPath())
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = celebTvShowsData.getOriginalName();
            if (title != null) {
//                if (title.length() > 17)
//                    title = title.substring(0, 17) + "...";
                holder.textViewMovieTitle.setText(title);
            }
            holder.textViewMovieYear.setText(celebTvShowsData.getFirstAirDate());
            holder.textViewRatingCount.setText(String.valueOf(celebTvShowsData.getVoteAverage()));
            holder.textViewVoteCount.setText(String.valueOf(celebTvShowsData.getVoteCount()));
        }
    }


    @Override
    public int getItemCount() {
        return celebTvShowsDataFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    celebTvShowsDataFilteredList = celebTvShowsDataList;
                } else {
                    List<CelebTvShowsData> filteredList = new ArrayList<>();
                    for (CelebTvShowsData row : celebTvShowsDataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOriginalName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    celebTvShowsDataFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = celebTvShowsDataFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                celebTvShowsDataFilteredList = (ArrayList<CelebTvShowsData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class CelebTvShowsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewTitleMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.textViewYearMoviesRow)
        TextView textViewMovieYear;
        @BindView(R.id.textViewAudienceMainRow)
        TextView textViewRatingText;
        @BindView(R.id.textViewRatingMoviesRow)
        TextView textViewRatingCount;
        @BindView(R.id.textViewVotesMoviesRow)
        TextView textViewVoteCount;
        @BindView(R.id.textViewVotesCountMoviesRow)
        TextView textViewVoteCountText;

        CelebTvShowsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);
            fontUtils.setTextViewLightFont(textViewMovieYear);
            fontUtils.setTextViewLightFont(textViewRatingText);
            fontUtils.setTextViewLightFont(textViewVoteCountText);
            fontUtils.setTextViewRegularFont(textViewVoteCount);
            fontUtils.setTextViewRegularFont(textViewRatingCount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusCelebrityTvShowsClick(getAdapterPosition(), movieType, celebTvShowsDataFilteredList.get(getAdapterPosition()).getId()));
        }

    }

}
