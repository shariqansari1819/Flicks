package com.codebosses.flicks.adapters.offline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.eventbus.EventBusOfflineClick;
import com.codebosses.flicks.pojo.offlinepojo.OfflineModel;
import com.codebosses.flicks.utils.DateUtils;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineVideosAdapter extends RecyclerView.Adapter<OfflineVideosAdapter.OfflineVideosHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<OfflineModel> offlineModelList = new ArrayList<>();

    public OfflineVideosAdapter(Context context, List<OfflineModel> offlineModelList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.offlineModelList = offlineModelList;
    }

    @NonNull
    @Override
    public OfflineVideosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfflineVideosHolder(layoutInflater.inflate(R.layout.row_offline_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineVideosHolder holder, int position) {
        OfflineModel offlineModel = offlineModelList.get(position);
        if (offlineModel != null) {
            Glide.with(context)
                    .load(offlineModel.getThumbnail())
                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                    .apply(new RequestOptions().error(R.drawable.zootopia_thumbnail))
                    .thumbnail(0.f)
                    .into(holder.imageViewOfflineThubnail);
            holder.textViewDuration.setText(DateUtils.convertSecondsToHMmSs(offlineModel.getDuration()));
            holder.textViewTitle.setText(offlineModel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return offlineModelList.size();
    }

    class OfflineVideosHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewThumbnailRowOffline)
        ImageView imageViewOfflineThubnail;
        @BindView(R.id.textViewDurationRowOffline)
        TextView textViewDuration;
        @BindView(R.id.textViewTitleRowOffline)
        TextView textViewTitle;

        public OfflineVideosHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewTitle);
            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewDuration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventBusOfflineClick(getAdapterPosition()));
                }
            });
        }
    }
}
