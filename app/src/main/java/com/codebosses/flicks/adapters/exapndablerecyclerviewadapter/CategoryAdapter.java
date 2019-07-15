package com.codebosses.flicks.adapters.exapndablerecyclerviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusExpandItems;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryItem;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryHeader;
import com.codebosses.flicks.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CategoryAdapter extends ExpandableRecyclerViewAdapter<CategoryHeaderHolder, CategoryItemHolder> {

    public CategoryAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CategoryHeaderHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_header, parent, false);
        return new CategoryHeaderHolder(view, parent.getContext());
    }

    @Override
    public CategoryItemHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_item, parent, false);
        return new CategoryItemHolder(view, parent.getContext());
    }

    @Override
    public void onBindChildViewHolder(CategoryItemHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final CategoryItem artist = ((CategoryHeader) group).getItems().get(childIndex);
        holder.setArtistName(artist.getName());
        holder.childTextView.setOnClickListener(v -> {
            EventBus.getDefault().post(new EventBusExpandItems(flatPosition, artist.getName()));
        });
    }

    @Override
    public void onBindGroupViewHolder(CategoryHeaderHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
        holder.setIsRecyclable(false);
        if (group.getTitle().equals(EndpointKeys.TRENDING)
                || group.getTitle().equals(EndpointKeys.DISCOVER)
                || group.getTitle().equals(EndpointKeys.OFFLINE)) {
            holder.arrow.setVisibility(View.GONE);
            holder.genreName.setOnClickListener(v -> {
                EventBus.getDefault().post(new EventBusExpandItems(flatPosition, group.getTitle()));
            });
        }
        if (group.getTitle().equals(EndpointKeys.OFFLINE)) {
            holder.viewDivider.setVisibility(View.VISIBLE);
        } else {
            holder.viewDivider.setVisibility(View.GONE);
        }
    }

}