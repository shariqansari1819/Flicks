package com.codebosses.flicks.adapters.moviesdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.pojo.moviespojo.moviedetail.Genre;
import com.codebosses.flicks.utils.FontUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreListAdapter extends BaseAdapter {

    private List<Genre> genreList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public GenreListAdapter(Context context, List<Genre> genreList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.genreList = genreList;
    }

    @Override
    public int getCount() {
        return genreList.size();
    }

    @Override
    public Object getItem(int position) {
        return genreList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GenreHolder genreHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_genre, parent, false);
            genreHolder = new GenreHolder(convertView);
            convertView.setTag(genreHolder);
        } else {
            genreHolder = (GenreHolder) convertView.getTag();
        }
        genreHolder.textViewGenre.setText(genreList.get(position).getName());

        return convertView;
    }

    class GenreHolder {

        @BindView(R.id.textViewGenreList)
        TextView textViewGenre;

        GenreHolder(View view) {
            ButterKnife.bind(this, view);
            FontUtils.getFontUtils(context).setTextViewRegularFont(textViewGenre);
        }
    }

}
