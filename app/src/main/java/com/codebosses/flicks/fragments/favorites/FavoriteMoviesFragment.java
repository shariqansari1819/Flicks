package com.codebosses.flicks.fragments.favorites;


import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.MoviesDetailActivity;
import com.codebosses.flicks.adapters.favorite.FavoriteMoviesAdapter;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.database.entities.MovieEntity;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.utils.ValidUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;

public class FavoriteMoviesFragment extends Fragment {

    //    Android fields....
    @BindView(R.id.circularProgressBarFavoriteMovies)
    CircularProgressBar circularProgressBarFavoriteMovies;
    @BindView(R.id.recyclerViewFavoriteMovies)
    RecyclerView recyclerViewFavoriteMovies;

    //    Adapter fields....
    private FavoriteMoviesAdapter favoriteMoviesAdapter;
    private List<MovieEntity> movieEntityList = new ArrayList<>();

    public FavoriteMoviesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        ButterKnife.bind(this, view);

        recyclerViewFavoriteMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(getActivity(), movieEntityList, EndpointKeys.FAVORITE_MOVIE);
        recyclerViewFavoriteMovies.setAdapter(favoriteMoviesAdapter);
        recyclerViewFavoriteMovies.setItemAnimator(new FadeInDownAnimator());
        if (recyclerViewFavoriteMovies.getItemAnimator() != null)
            recyclerViewFavoriteMovies.getItemAnimator().setAddDuration(500);
        loadFavoriteMovies();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void loadFavoriteMovies() {
        new GetAllMoviesTask().execute();
    }

    class GetAllMoviesTask extends AsyncTask<Void, Void, List<MovieEntity>> {

        @Override
        protected List<MovieEntity> doInBackground(Void... voids) {
            return DatabaseClient.getDatabaseClient(getActivity()).getFlicksDatabase().getFlicksDao().getAllFavoriteMovies();
        }

        @Override
        protected void onPostExecute(List<MovieEntity> aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid.size() > 0) {
                for (int i = 0; i < aVoid.size(); i++) {
                    movieEntityList.add(aVoid.get(i));
                    favoriteMoviesAdapter.notifyItemInserted(i);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusMovieClick(EventBusMovieClick eventBusMovieClick) {
        if (eventBusMovieClick.getMovieType().equals(EndpointKeys.FAVORITE_MOVIE) && ValidUtils.isNetworkAvailable(getContext())) {
            Intent intent = new Intent(getActivity(), MoviesDetailActivity.class);
            intent.putExtra(EndpointKeys.MOVIE_ID, movieEntityList.get(eventBusMovieClick.getPosition()).getMovieId());
            intent.putExtra(EndpointKeys.MOVIE_TITLE, movieEntityList.get(eventBusMovieClick.getPosition()).getTitle());
            intent.putExtra(EndpointKeys.RATING, movieEntityList.get(eventBusMovieClick.getPosition()).getVoteAverage());
            startActivity(intent);
        }
    }

}
