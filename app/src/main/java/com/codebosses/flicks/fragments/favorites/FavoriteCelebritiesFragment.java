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
import com.codebosses.flicks.activities.CelebrityDetailActivity;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.favorite.FavoriteCelebritiesAdapter;
import com.codebosses.flicks.adapters.favorite.FavoriteMoviesAdapter;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.database.entities.CelebrityEntity;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusCelebrityClick;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.eventbus.EventBusRefreshFavoriteList;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

public class FavoriteCelebritiesFragment extends Fragment {

    //        Android fields....
    @BindView(R.id.circularProgressBarFavoriteCelebrities)
    CircularProgressBar circularProgressBar;
    @BindView(R.id.recyclerViewFavoriteCelebrities)
    RecyclerView recyclerView;

    //    Adapter fields....
    private FavoriteCelebritiesAdapter favoriteCelebritiesAdapter;
    private List<CelebrityEntity> celebrityEntityList = new ArrayList<>();

    public FavoriteCelebritiesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_celebrities, container, false);
        ButterKnife.bind(this, view);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteCelebritiesAdapter = new FavoriteCelebritiesAdapter(getActivity(), celebrityEntityList, EndpointKeys.FAVORITE_CELEB);
        recyclerView.setAdapter(favoriteCelebritiesAdapter);
        recyclerView.setItemAnimator(new FadeInDownAnimator());
        if (recyclerView.getItemAnimator() != null)
            recyclerView.getItemAnimator().setAddDuration(500);
        loadFavoriteCelebrities();

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
    public void eventBusRefreshMovies(EventBusRefreshFavoriteList eventBusRefreshFavoriteList) {
        favoriteCelebritiesAdapter.notifyItemRangeRemoved(0, celebrityEntityList.size());
        celebrityEntityList.clear();
        new GetAllCelebritiesTask().execute();
    }

    private void loadFavoriteCelebrities() {
        new GetAllCelebritiesTask().execute();
    }

    class GetAllCelebritiesTask extends AsyncTask<Void, Void, List<CelebrityEntity>> {

        @Override
        protected List<CelebrityEntity> doInBackground(Void... voids) {
            return DatabaseClient.getDatabaseClient(getActivity()).getFlicksDatabase().getFlicksDao().getAllFavoriteCelebrity();
        }

        @Override
        protected void onPostExecute(List<CelebrityEntity> aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid.size() > 0) {
                for (int i = 0; i < aVoid.size(); i++) {
                    celebrityEntityList.add(aVoid.get(i));
                    favoriteCelebritiesAdapter.notifyItemInserted(i);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMovieClick(EventBusCelebrityClick eventBusCelebrityClick) {
        if (eventBusCelebrityClick.getCelebType().equals(EndpointKeys.FAVORITE_CELEB) && ValidUtils.isNetworkAvailable(getContext())) {
            Intent intent = new Intent(getActivity(), CelebrityDetailActivity.class);
            intent.putExtra(EndpointKeys.CELEBRITY_ID, celebrityEntityList.get(eventBusCelebrityClick.getPosition()).getId());
            intent.putExtra(EndpointKeys.CELEB_NAME, celebrityEntityList.get(eventBusCelebrityClick.getPosition()).getName());
            intent.putExtra(EndpointKeys.CELEB_IMAGE, celebrityEntityList.get(eventBusCelebrityClick.getPosition()).getProfilePath());
            startActivity(intent);
        }
    }

}
