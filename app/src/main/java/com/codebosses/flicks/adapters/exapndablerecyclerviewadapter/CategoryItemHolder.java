package com.codebosses.flicks.adapters.exapndablerecyclerviewadapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.FragmentNavigationView;
import com.codebosses.flicks.pojo.eventbus.EventBusExpandItems;
import com.codebosses.flicks.utils.FontUtils;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import org.greenrobot.eventbus.EventBus;

public class CategoryItemHolder extends ChildViewHolder {

    public TextView childTextView;

    public CategoryItemHolder(View itemView, Context context) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
        FontUtils.getFontUtils(context).setTextViewBoldFont(childTextView);
    }

    public void setArtistName(String name) {
        childTextView.setText(name);
    }

}
