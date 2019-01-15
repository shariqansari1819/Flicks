package com.codebosses.flicks.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.exapndablerecyclerviewadapter.CategoryAdapter;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusExpandItems;
import com.codebosses.flicks.pojo.eventbus.EventBusSelectedItem;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryItem;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryHeader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNavigationView extends Fragment {

    @BindView(R.id.recyclerViewNavigation)
    RecyclerView recyclerViewNavigation;

    //    Adapter fields....
    CategoryAdapter adapter;

    //    Binding resources....
    @BindString(R.string.movies)
    String movies;
    @BindString(R.string.tv_shows)
    String tvShows;
    @BindString(R.string.celebrities)
    String celebrities;
    @BindString(R.string.genre)
    String genre;

    public FragmentNavigationView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

//        Disabling default animation of recyclerview....
        RecyclerView.ItemAnimator animator = recyclerViewNavigation.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        adapter = new CategoryAdapter(makeCategories());
        recyclerViewNavigation.setLayoutManager(layoutManager);
        recyclerViewNavigation.setAdapter(adapter);

        if (savedInstanceState != null) {
            adapter.onRestoreInstanceState(savedInstanceState);
        }

        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    private List<CategoryHeader> makeCategories() {
        return Arrays.asList(
                makeMovieHeader(),
                makeTvHeader(),
                makeCelebriyHeader(),
                makeGenreHeader());
//                makeBluegrassGenre());
    }

    private CategoryHeader makeMovieHeader() {
        return new CategoryHeader(movies, makeMovieItems(), R.drawable.ic_electric_guitar);
    }

    private List<CategoryItem> makeMovieItems() {
        CategoryItem upcomingMovies = new CategoryItem(getResources().getString(R.string.upcoming_movies), false);
        CategoryItem topRatedMovies = new CategoryItem(getResources().getString(R.string.top_rated_movies), false);
        CategoryItem latestMovies = new CategoryItem(getResources().getString(R.string.latest_movies), false);
        CategoryItem inTheater = new CategoryItem(getResources().getString(R.string.in_theater), false);

        return Arrays.asList(upcomingMovies, topRatedMovies, latestMovies, inTheater);
    }

    private CategoryHeader makeTvHeader() {
        return new CategoryHeader(tvShows, makeTvItems(), R.drawable.ic_saxaphone);
    }


    private List<CategoryItem> makeTvItems() {
        CategoryItem topRatedTvShows = new CategoryItem(getResources().getString(R.string.top_rated_tv_shows), false);
        CategoryItem latestTvShows = new CategoryItem(getResources().getString(R.string.latest_tv_shows), false);

        return Arrays.asList(topRatedTvShows, latestTvShows);
    }

    private CategoryHeader makeCelebriyHeader() {
        return new CategoryHeader(celebrities, makeCelebrityItems(), R.drawable.ic_violin);
    }

    private List<CategoryItem> makeCelebrityItems() {
        CategoryItem topRatedCelebrity = new CategoryItem(getResources().getString(R.string.top_rated_celebrities), false);

        return Arrays.asList(topRatedCelebrity);
    }

    private CategoryHeader makeGenreHeader() {
        return new CategoryHeader(genre, makeGenreItems(), R.drawable.ic_maracas);
    }


    private List<CategoryItem> makeGenreItems() {
        CategoryItem action = new CategoryItem(getResources().getString(R.string.action), false);
        CategoryItem animated = new CategoryItem(getResources().getString(R.string.animated), false);
        CategoryItem drama = new CategoryItem(getResources().getString(R.string.drama), false);
        CategoryItem science_fiction = new CategoryItem(getResources().getString(R.string.sceince_fiction), false);

        return Arrays.asList(action, animated, drama, science_fiction);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusExpandItem(EventBusExpandItems eventBusExpandItems) {
        EventBus.getDefault().post(new EventBusSelectedItem(eventBusExpandItems.getTitle()));
//        switch (eventBusExpandItems.getTitle()) {
//            case EndpointKeys.UPCOMING_MOVIES:
//
//                break;
//            case EndpointKeys.TOP_RATED_MOVIES:
//                break;
//            case EndpointKeys.LATEST_MOVIES:
//                break;
//            case EndpointKeys.IN_THEATER:
//                break;
//            case EndpointKeys.TOP_RATED_TV_SHOWS:
//                break;
//            case EndpointKeys.LATEST_TV_SHOWS:
//                break;
//            case EndpointKeys.TOP_RATED_CELEBRITIES:
//                break;
//            case EndpointKeys.ACTION:
//                break;
//            case EndpointKeys.ANIMATED:
//                break;
//            case EndpointKeys.DRAMA:
//                break;
//            case EndpointKeys.SCIENCE_FICTION:
//                break;
//        }

    }

//    public CategoryHeader makeBluegrassGenre() {
//        return new CategoryHeader("Bluegrass", makeBluegrassArtists(), R.drawable.ic_banjo);
//    }
//
//
//    public List<CategoryItem> makeBluegrassArtists() {
//        CategoryItem billMonroe = new CategoryItem("Bill Monroe", false);
//        CategoryItem earlScruggs = new CategoryItem("Earl Scruggs", false);
//        CategoryItem osborneBrothers = new CategoryItem("Osborne Brothers", true);
//        CategoryItem johnHartford = new CategoryItem("John Hartford", false);
//
//        return Arrays.asList(billMonroe, earlScruggs, osborneBrothers, johnHartford);
//    }


//    private void funGetUserSites() {
//        mBind.mPb.setVisibility(View.VISIBLE);
//        mUserSitesCall = Api.WEB_SERVICE.getUserSites("55555", GlobalClass.getInstance().mPref.getString("id", ""));
//        mUserSitesCall.enqueue(new Callback<UserSitesModel>() {
//            @Override
//            public void onResponse(Call<UserSitesModel> call, retrofit2.Response<UserSitesModel> response) {
//                mBind.mPb.setVisibility(View.INVISIBLE);
//                if (response != null && response.isSuccessful()) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("mFlag", "email");
//                    switch (response.body().getStatus()) {
//                        case "0":
////                            ((MainActivity) getActivity()).fnLoadFragAdd("CREATE SITE", true, null);
//                            bundle.putBoolean("site_exist", false);
//                            break;
//                        case "1":
//                            bundle.putBoolean("site_exist", true);
//                            break;
//                    }
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getResources().getString(R.string.mail_sent), R.color.success);
//                    ((MainActivity) getActivity()).fnLoadFragAdd("SUCCESS EMAIL PHONE", false, bundle);
//                } else {
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.response_is_null), R.color.error);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserSitesModel> call, Throwable error) {
//                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
//                    return;
//                }
//                mBind.mPb.setVisibility(View.INVISIBLE);
//                if (error != null) {
//                    if (error.getMessage().contains("No address associated with hostname")) {
//                        GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.internet_or_server_error), R.color.error);
//                    } else {
//                        GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, error.getMessage() + "", R.color.error);
//                    }
//                } else {
//                    GlobalClass.getInstance().snakeBar(((MainActivity) getActivity()).mBind.mRoot, getString(R.string.error_is_null), R.color.error);
//                }
//            }
//        });
//    }

}
