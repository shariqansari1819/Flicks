package com.codebosses.flicks.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.FlicksApplication;
import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.LoginActivity;
import com.codebosses.flicks.activities.MainActivity;
import com.codebosses.flicks.adapters.exapndablerecyclerviewadapter.CategoryAdapter;
import com.codebosses.flicks.database.DatabaseClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusExpandItems;
import com.codebosses.flicks.pojo.eventbus.EventBusSelectedItem;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryHeader;
import com.codebosses.flicks.pojo.expandrecyclerviewpojo.CategoryItem;
import com.codebosses.flicks.utils.FontUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentNavigationView extends Fragment {

    @BindView(R.id.recyclerViewNavigation)
    RecyclerView recyclerViewNavigation;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.textViewSignUpHeadingNavigation)
    TextView textViewHeading;
    @BindView(R.id.buttonSignUpNavigation)
    Button buttonSignUp;
    @BindView(R.id.textViewAlreadyHaveAccountNavigation)
    TextView textViewAlreadyHaveAccount;
    @BindView(R.id.textViewLogIn)
    TextView textViewLogIn;
    @BindView(R.id.constraintUserInfoNavigation)
    ConstraintLayout constraintLayoutUserInfo;
    @BindView(R.id.frameLayoutLogoNavigation)
    ConstraintLayout constraintLayoutLogo;
    @BindView(R.id.imageViewProfileNavigation)
    CircleImageView imageViewProfile;
    @BindView(R.id.textViewNameNavigation)
    TextView textViewName;
    @BindView(R.id.buttonLogOutNavigation)
    Button buttonLogOut;

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
    @BindString(R.string.trending)
    String trending;
    @BindString(R.string.discover)
    String discover;
    @BindString(R.string.offline)
    String offline;
    @BindString(R.string.account)
    String account;

    public FragmentNavigationView() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        FontUtils fontUtils = FontUtils.getFontUtils(getActivity());
        fontUtils.setTextViewRegularFont(textViewHeading);
        fontUtils.setTextViewRegularFont(textViewAlreadyHaveAccount);
        fontUtils.setTextViewBoldFont(textViewLogIn);
        fontUtils.setButtonRegularFont(buttonSignUp);
        fontUtils.setTextViewRegularFont(textViewName);

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

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            constraintLayoutUserInfo.setVisibility(View.VISIBLE);
            constraintLayoutLogo.setVisibility(View.INVISIBLE);
            Glide.with(this)
                    .load(FlicksApplication.getStringValue(EndpointKeys.USER_IMAGE))
                    .apply(new RequestOptions().placeholder(R.drawable.avatar))
                    .thumbnail(0.1f)
                    .into(imageViewProfile);
            textViewName.setText(FlicksApplication.getStringValue(EndpointKeys.USER_NAME));
        } else {
            constraintLayoutUserInfo.setVisibility(View.INVISIBLE);
            constraintLayoutLogo.setVisibility(View.VISIBLE);
        }

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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return Arrays.asList(
//                makeDiscoverHeader(),
                    makeTrendingHeader(),
                    makeMovieHeader(),
                    makeTvHeader(),
                    makeCelebriyHeader(),
                    makeGenreHeader(),
                    makeAccountHeader(),
                    makeOfflineHeader());
        } else {
            return Arrays.asList(
//                makeDiscoverHeader(),
                    makeTrendingHeader(),
                    makeMovieHeader(),
                    makeTvHeader(),
                    makeCelebriyHeader(),
                    makeGenreHeader(),
                    makeOfflineHeader());
        }
    }

//    private CategoryHeader makeDiscoverHeader() {
//        return new CategoryHeader(discover, new ArrayList<CategoryItem>(), R.drawable.ic_action_discover);
//    }

    private CategoryHeader makeTrendingHeader() {
        return new CategoryHeader(trending, new ArrayList<CategoryItem>(), R.drawable.ic_action_trending);
    }

    private CategoryHeader makeOfflineHeader() {
        return new CategoryHeader(offline, new ArrayList<CategoryItem>(), R.drawable.ic_download);
    }

    private CategoryHeader makeMovieHeader() {
        return new CategoryHeader(movies, makeMovieItems(), R.drawable.ic_action_movies);
    }

    private List<CategoryItem> makeMovieItems() {
        CategoryItem upcomingMovies = new CategoryItem(getResources().getString(R.string.upcoming_movies), false);
        CategoryItem topRatedMovies = new CategoryItem(getResources().getString(R.string.top_rated_movies), false);
        CategoryItem latestMovies = new CategoryItem(getResources().getString(R.string.latest_movies), false);
        CategoryItem inTheater = new CategoryItem(getResources().getString(R.string.in_theater), false);

        return Arrays.asList(upcomingMovies, topRatedMovies, latestMovies, inTheater);
    }

    private CategoryHeader makeTvHeader() {
        return new CategoryHeader(tvShows, makeTvItems(), R.drawable.ic_action_tv);
    }


    private List<CategoryItem> makeTvItems() {
        CategoryItem topRatedTvShows = new CategoryItem(getResources().getString(R.string.top_rated_tv_shows), false);
        CategoryItem latestTvShows = new CategoryItem(getResources().getString(R.string.latest_tv_shows), false);
        CategoryItem tvShowsOnTheAir = new CategoryItem(getResources().getString(R.string.tv_shows_on_the_air), false);
        CategoryItem tvShowsAiringToday = new CategoryItem(getResources().getString(R.string.tv_shows_on_the_air_today), false);
        return Arrays.asList(topRatedTvShows, latestTvShows, tvShowsOnTheAir, tvShowsAiringToday);
    }

    private CategoryHeader makeCelebriyHeader() {
        return new CategoryHeader(celebrities, makeCelebrityItems(), R.drawable.ic_action_celebrity);
    }

    private List<CategoryItem> makeCelebrityItems() {
        CategoryItem topRatedCelebrity = new CategoryItem(getResources().getString(R.string.top_rated_celebrities), false);
        return Arrays.asList(topRatedCelebrity);
    }

    private CategoryHeader makeGenreHeader() {
        return new CategoryHeader(genre, makeGenreItems(), R.drawable.ic_action_genre);
    }

    private CategoryHeader makeAccountHeader() {
        return new CategoryHeader(account, makeAccountItems(), R.drawable.ic_vector_person);
    }

    private List<CategoryItem> makeGenreItems() {
        CategoryItem topRatedTvShows = new CategoryItem(getResources().getString(R.string.movies), false);
        CategoryItem latestTvShows = new CategoryItem(getResources().getString(R.string.tv_shows), false);
        return Arrays.asList(topRatedTvShows, latestTvShows);
    }

    private List<CategoryItem> makeAccountItems() {
        CategoryItem profile = new CategoryItem(getResources().getString(R.string.my_profile), false);
//        CategoryItem settings = new CategoryItem(getResources().getString(R.string.setting), false);
        CategoryItem favoriteList = new CategoryItem(getResources().getString(R.string.favorite_list), false);
        CategoryItem signOutItem = new CategoryItem(getResources().getString(R.string.sign_out), false);
        return Arrays.asList(profile,favoriteList,signOutItem);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusExpandItem(EventBusExpandItems eventBusExpandItems) {
        EventBus.getDefault().post(new EventBusSelectedItem(eventBusExpandItems.getTitle()));
    }

    @OnClick({R.id.textViewAlreadyHaveAccountNavigation, R.id.textViewLogIn, R.id.buttonSignUpNavigation})
    public void onLogInClick(View view) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.buttonLogOutNavigation)
    public void onLogOutClick(View view) {
        ((MainActivity) getActivity()).logOut();
    }

}