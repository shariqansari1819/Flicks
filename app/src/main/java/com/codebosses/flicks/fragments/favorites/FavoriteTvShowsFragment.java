package com.codebosses.flicks.fragments.favorites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.TvShowsDetailActivity;
import com.codebosses.flicks.adapters.favorite.FavoriteTvShowsAdapter;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.database.entities.TvShowEntity;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusRefreshFavoriteList;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

public class FavoriteTvShowsFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.circularProgressBarFavoriteTvShows)
    CircularProgressBar circularProgressBarFavoriteTvShows;
    @BindView(R.id.recyclerViewFavoriteTvShows)
    RecyclerView recyclerViewFavoriteTvShows;

    //    Adapter fields....
    private FavoriteTvShowsAdapter favoriteTvShowsAdapter;
    private List<TvShowEntity> tvShowEntityArrayList = new ArrayList<>();

    public FavoriteTvShowsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tv_shows, container, false);
        ButterKnife.bind(this, view);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        recyclerViewFavoriteTvShows.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteTvShowsAdapter = new FavoriteTvShowsAdapter(getActivity(), tvShowEntityArrayList, EndpointKeys.FAVORITE_TV_SHOW);
        recyclerViewFavoriteTvShows.setAdapter(favoriteTvShowsAdapter);
        recyclerViewFavoriteTvShows.setItemAnimator(new FadeInDownAnimator());
        if (recyclerViewFavoriteTvShows.getItemAnimator() != null)
            recyclerViewFavoriteTvShows.getItemAnimator().setAddDuration(500);
        loadFavoriteTvShow();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusRefreshTvShow(EventBusRefreshFavoriteList eventBusRefreshFavoriteList) {
        favoriteTvShowsAdapter.notifyItemRangeRemoved(0, tvShowEntityArrayList.size());
        tvShowEntityArrayList.clear();
        loadFavoriteTvShow();
    }

    private void loadFavoriteTvShow() {
        new GetAllTvShowsTask().execute();
    }

    class GetAllTvShowsTask extends AsyncTask<Void, Void, List<TvShowEntity>> {

        @Override
        protected List<TvShowEntity> doInBackground(Void... voids) {
            return DatabaseClient.getDatabaseClient(getActivity()).getFlicksDatabase().getFlicksDao().getAllFavoriteTvShows();
        }

        @Override
        protected void onPostExecute(List<TvShowEntity> aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid.size() > 0) {
                for (int i = 0; i < aVoid.size(); i++) {
                    tvShowEntityArrayList.add(aVoid.get(i));
                    favoriteTvShowsAdapter.notifyItemInserted(i);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusTvShowsClick(EventBusTvShowsClick eventBusTvShowsClick) {
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.FAVORITE_TV_SHOW) && ValidUtils.isNetworkAvailable(getContext())) {
            Intent intent = new Intent(getActivity(), TvShowsDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvShowEntityArrayList.get(eventBusTvShowsClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.TV_NAME, tvShowEntityArrayList.get(eventBusTvShowsClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.RATING, tvShowEntityArrayList.get(eventBusTvShowsClick.getPosition()).getVoteAverage());
            startActivity(intent);
        }
    }

}
