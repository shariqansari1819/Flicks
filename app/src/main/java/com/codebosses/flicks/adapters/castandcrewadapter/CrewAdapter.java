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
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<CrewData> crewDataList = new ArrayList<>();
    private FontUtils fontUtils;

    public CrewAdapter(Context context, List<CrewData> crewDataList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.crewDataList = crewDataList;
        fontUtils = FontUtils.getFontUtils(context);
    }

    @NonNull
    @Override
    public CrewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CrewHolder(layoutInflater.inflate(R.layout.row_cast_crew, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CrewHolder holder, int position) {
        CrewData crewData = crewDataList.get(position);
        String celebName = crewData.getName();
        String characterName = crewData.getJob();

        if (celebName.length() > 10) {
            celebName = celebName.substring(0, 10) + "...";
        }
        if (characterName.length() > 11) {
            characterName = characterName.substring(0, 11) + "...";
        }
        Glide.with(context)
                .load(EndpointUrl.POSTER_BASE_URL + crewData.getProfile_path())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                .apply(new RequestOptions().error(R.drawable.zootopia_thumbnail))
                .into(holder.imageViewCelebs);
        holder.textViewCelebName.setText(celebName);
        holder.textViewCharacter.setText(characterName);
    }

    @Override
    public int getItemCount() {
        return crewDataList.size();
    }

    class CrewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewCelebCastCrewRow)
        ImageView imageViewCelebs;
        @BindView(R.id.textViewCelebNameCastCrewRow)
        TextView textViewCelebName;
        @BindView(R.id.textViewCelebCharacterCastCrewRow)
        TextView textViewCharacter;

        public CrewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewCelebName);
            fontUtils.setTextViewRegularFont(textViewCharacter);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusCastAndCrewClick(getAdapterPosition(), EndpointKeys.CREW));
        }
    }
}
