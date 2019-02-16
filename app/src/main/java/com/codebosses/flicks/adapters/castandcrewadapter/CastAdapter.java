package com.codebosses.flicks.adapters.castandcrewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<CastData> castDataList = new ArrayList<>();
    private FontUtils fontUtils;

    public CastAdapter(Context context, List<CastData> castDataList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.castDataList = castDataList;
        fontUtils = FontUtils.getFontUtils(context);
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CastHolder(layoutInflater.inflate(R.layout.row_cast_crew, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        CastData castData = castDataList.get(position);
        Glide.with(context)
                .load(EndpointUrl.POSTER_BASE_URL + castData.getProfile_path())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .apply(new RequestOptions().error(R.drawable.zootopia_thumbnail))
                .into(holder.imageViewCelebs);
        holder.textViewCelebName.setText(castData.getName());
        holder.textViewCharacter.setText(castData.getCharacter());
    }

    @Override
    public int getItemCount() {
        return castDataList.size();
    }

    class CastHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewCelebCastCrewRow)
        ImageView imageViewCelebs;
        @BindView(R.id.textViewCelebNameCastCrewRow)
        TextView textViewCelebName;
        @BindView(R.id.textViewCelebCharacterCastCrewRow)
        TextView textViewCharacter;

        public CastHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewBoldFont(textViewCelebName);
            fontUtils.setTextViewRegularFont(textViewCharacter);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusCastAndCrewClick(getAdapterPosition(), EndpointKeys.CAST));
        }
    }
}
