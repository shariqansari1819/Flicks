package com.codebosses.flicks.adapters.reviewsadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.reviews.ReviewsData;
import com.codebosses.flicks.pojo.reviews.ReviewsMainObject;
import com.codebosses.flicks.utils.FontUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder> {

    private Context context;
    private List<ReviewsData> reviewsDataList;
    private LayoutInflater layoutInflater;
    private FontUtils fontUtils;

    public ReviewsAdapter(Context context, List<ReviewsData> reviewsDataList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.reviewsDataList = reviewsDataList;
        fontUtils = FontUtils.getFontUtils(context);
    }

    @NonNull
    @Override
    public ReviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewsHolder(layoutInflater.inflate(R.layout.row_reviews, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsHolder holder, int position) {
        ReviewsData reviewsData = reviewsDataList.get(position);
        holder.textViewName.setText(reviewsData.getAuthor());
        holder.textViewReview.setText(reviewsData.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewsDataList.size();
    }

    class ReviewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewAvatarReviewsRow)
        CircleImageView imageViewAvatar;
        @BindView(R.id.textViewNameReviewsRow)
        TextView textViewName;
        @BindView(R.id.textViewReviewReviewsRow)
        TextView textViewReview;

        public ReviewsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fontUtils.setTextViewRegularFont(textViewName);
            fontUtils.setTextViewLightFont(textViewReview);
        }
    }
}
