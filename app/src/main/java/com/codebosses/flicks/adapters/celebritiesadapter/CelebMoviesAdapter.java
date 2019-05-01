package com.codebosses.flicks.adapters.celebritiesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.moviesadapter.MoviesAdapter;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.celebritiespojo.celebmovies.CelebMoviesData;
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

public class CelebMoviesAdapter extends RecyclerView.Adapter<CelebMoviesAdapter.CelebMoviesHolder>
        implements Filterable {

    private Context context;
    private FontUtils fontUtils;
    private LayoutInflater layoutInflater;
    private List<CelebMoviesData> celebMoviesDataArrayList = new ArrayList<>();
    private List<CelebMoviesData> celebMoviesDataFilteredList = new ArrayList<>();
    private String movieType;

    public CelebMoviesAdapter(Context context, List<CelebMoviesData> celebMoviesDataArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.celebMoviesDataArrayList = celebMoviesDataArrayList;
        this.celebMoviesDataFilteredList = celebMoviesDataArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public CelebMoviesAdapter.CelebMoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_movies, parent, false);
        return new CelebMoviesAdapter.CelebMoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebMoviesAdapter.CelebMoviesHolder holder, int position) {
        CelebMoviesData celebMoviesData = celebMoviesDataFilteredList.get(position);
        if (celebMoviesData != null) {
            if (celebMoviesData.getPoster_path() != null && !celebMoviesData.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + celebMoviesData.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                        .into(holder.imageViewThumbnail);
            String title = celebMoviesData.getTitle();
            if (title != null) {
                if (title.length() > 17)
                    title = title.substring(0, 17) + "...";
                holder.textViewMovieTitle.setText(title);
            }
            holder.textViewMovieYear.setText(celebMoviesData.getRelease_date());
            holder.textViewRatingCount.setText(String.valueOf(celebMoviesData.getVote_average()));
            holder.textViewVoteCount.setText(String.valueOf(celebMoviesData.getVote_count()));
        }
    }


    @Override
    public int getItemCount() {
        return celebMoviesDataFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    celebMoviesDataFilteredList = celebMoviesDataArrayList;
                } else {
                    List<CelebMoviesData> filteredList = new ArrayList<>();
                    for (CelebMoviesData row : celebMoviesDataArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    celebMoviesDataFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = celebMoviesDataFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                celebMoviesDataFilteredList = (ArrayList<CelebMoviesData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class CelebMoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailMainRow)
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

        CelebMoviesHolder(@NonNull View itemView) {
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
            EventBus.getDefault().post(new EventBusMovieClick(getAdapterPosition(), movieType));
        }

    }


}
