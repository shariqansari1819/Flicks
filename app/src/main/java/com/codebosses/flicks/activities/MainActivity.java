package com.codebosses.flicks.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.fragments.celebritiesfragments.FragmentTopRatedCelebrities;
import com.codebosses.flicks.fragments.discoverfragments.DiscoverFragment;
import com.codebosses.flicks.fragments.genrefragments.FragmentGenre;
import com.codebosses.flicks.fragments.moviesfragments.FragmentInTheater;
import com.codebosses.flicks.fragments.moviesfragments.FragmentLatestMovies;
import com.codebosses.flicks.fragments.moviesfragments.FragmentTopRatedMovies;
import com.codebosses.flicks.fragments.moviesfragments.FragmentUpcomingMovies;
import com.codebosses.flicks.fragments.trending.FragmentTrending;
import com.codebosses.flicks.fragments.tvfragments.FragmentLatestTvShows;
import com.codebosses.flicks.fragments.tvfragments.FragmentTopRatedTvShows;
import com.codebosses.flicks.fragments.tvfragments.FragmentTvShowsAiringToday;
import com.codebosses.flicks.fragments.tvfragments.FragmentTvShowsOnAir;
import com.codebosses.flicks.pojo.eventbus.EventBusSelectedItem;
import com.codebosses.flicks.utils.FontUtils;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codebosses.flicks.common.Constants.DATA_KEY_1;
import static com.codebosses.flicks.common.Constants.DATA_KEY_2;
import static com.codebosses.flicks.utils.backstack.FragmentUtils.addAdditionalTabFragment;
import static com.codebosses.flicks.utils.backstack.FragmentUtils.addInitialTabFragment;
import static com.codebosses.flicks.utils.backstack.FragmentUtils.addShowHideFragment;
import static com.codebosses.flicks.utils.backstack.FragmentUtils.removeFragment;
import static com.codebosses.flicks.utils.backstack.FragmentUtils.showHideTabFragment;
import static com.codebosses.flicks.utils.backstack.StackListManager.updateStackIndex;
import static com.codebosses.flicks.utils.backstack.StackListManager.updateStackToIndexFirst;
import static com.codebosses.flicks.utils.backstack.StackListManager.updateTabStackIndex;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionCallback {

    //    Android fields....
    @BindView(R.id.drawerLayoutMain)
    DrawerLayout drawerLayoutMain;
    @BindView(R.id.appBarMain)
    Toolbar toolbarMain;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewTitle;

    ActionBarDrawerToggle actionBarDrawerToggle;

    //    Font fields....
    private FontUtils fontUtils;

    //    Fragment fields...
    private DiscoverFragment discoverFragment;
    private FragmentTrending fragmentTrending;
    private FragmentUpcomingMovies fragmentUpcomingMovies;
    private FragmentTopRatedMovies fragmentTopRatedMovies;
    private FragmentLatestMovies fragmentLatestMovies;
    private FragmentInTheater fragmentInTheater;
    private FragmentTopRatedTvShows fragmentTopRatedTvShows;
    private FragmentLatestTvShows fragmentLatestTvShows;
    private FragmentTvShowsOnAir fragmentTvShowsOnAir;
    private FragmentTvShowsAiringToday fragmentTvShowsAiringToday;
    private FragmentTopRatedCelebrities fragmentTopRatedCelebrities;
    private FragmentGenre fragmentGenre;

    private Fragment currentFragment;

    //    TODO: Instance fields....
    private int index, currentFragmentIndex;
    private int interstitialAddCounter;

    //    Stack fields....
    private Map<String, Stack<Fragment>> stacks;
    private String currentTab;
    private List<String> stackList;
    private List<String> menuStacks;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Data data = new Data.Builder()
//                .putString(EndpointKeys.NOTIFICATION_TYPE, "Movies")
//                .build();
//        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(constraints).build();
//        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

//        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
//                .observe(this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        Log.d("Worker", workInfo.getState().name() + "\n");
//                    }
//                });

//        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 12, TimeUnit.HOURS)
//                .setConstraints(constraints)
//                .build();
//        WorkManager.getInstance().enqueue(periodicWorkRequest);

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this);
        appUpdaterUtils.setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        if (isUpdateAvailable) {
                            new AppUpdater(MainActivity.this)
                                    .setDisplay(Display.DIALOG)
                                    .showAppUpdated(true)
                                    .start();
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {

                    }
                }).start();

//        Setting custom action bar....
        setSupportActionBar(toolbarMain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

//        Setting custom font....
        setCustomFont();

//        Setting drawer layout....
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutMain, toolbarMain, R.string.open, R.string.close);
        drawerLayoutMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        initializeFragments();

        createStacks();

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_admob_id));
        AdRequest adRequestInterstitial = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequestInterstitial);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                showInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

//        Event listeners....
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInterstitial();
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    private void initializeFragments() {
        discoverFragment = new DiscoverFragment();
        fragmentTrending = new FragmentTrending();
        fragmentUpcomingMovies = new FragmentUpcomingMovies();
        fragmentTopRatedMovies = new FragmentTopRatedMovies();
        fragmentLatestMovies = new FragmentLatestMovies();
        fragmentInTheater = new FragmentInTheater();
        fragmentTopRatedTvShows = new FragmentTopRatedTvShows();
        fragmentLatestTvShows = new FragmentLatestTvShows();
        fragmentTopRatedCelebrities = new FragmentTopRatedCelebrities();
        fragmentTvShowsOnAir = new FragmentTvShowsOnAir();
        fragmentTvShowsAiringToday = new FragmentTvShowsAiringToday();
        fragmentGenre = new FragmentGenre();
    }

    private void createStacks() {


        stacks = new LinkedHashMap<>();
//        stacks.put(EndpointKeys.DISCOVER, new Stack<Fragment>());
        stacks.put(EndpointKeys.TRENDING, new Stack<Fragment>());
        stacks.put(EndpointKeys.UPCOMING_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.LATEST_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.IN_THEATER, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_TV_SHOWS, new Stack<Fragment>());
        stacks.put(EndpointKeys.LATEST_TV_SHOWS, new Stack<Fragment>());
        stacks.put(EndpointKeys.TV_SHOWS_ON_THE_AIR, new Stack<Fragment>());
        stacks.put(EndpointKeys.TV_SHOWS_AIRING_TODAY, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_CELEBRITIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.GENRE, new Stack<>());

        menuStacks = new ArrayList<>();
        menuStacks.add(EndpointKeys.TRENDING);

        stackList = new ArrayList<>();
//        stackList.add(EndpointKeys.DISCOVER);
        stackList.add(EndpointKeys.TRENDING);
        stackList.add(EndpointKeys.UPCOMING_MOVIES);
        stackList.add(EndpointKeys.TOP_RATED_MOVIES);
        stackList.add(EndpointKeys.LATEST_MOVIES);
        stackList.add(EndpointKeys.IN_THEATER);
        stackList.add(EndpointKeys.TOP_RATED_TV_SHOWS);
        stackList.add(EndpointKeys.LATEST_TV_SHOWS);
        stackList.add(EndpointKeys.TV_SHOWS_ON_THE_AIR);
        stackList.add(EndpointKeys.TV_SHOWS_AIRING_TODAY);
        stackList.add(EndpointKeys.TOP_RATED_CELEBRITIES);
        stackList.add(EndpointKeys.GENRE);

        setAppBarTitle(getResources().getString(R.string.trending));
        selectedTab(EndpointKeys.TRENDING);
    }

    private void selectedTab(String tabId) {

        currentTab = tabId;
        BaseFragment.setCurrentTab(currentTab);

        if (stacks.get(tabId).size() == 0) {
            /*
             * First time this tab is selected. So add first fragment of that tab.
             * We are adding a new fragment which is not present in stack. So add to stack is true.
             */
            switch (tabId) {
//                case EndpointKeys.DISCOVER:
//                    addInitialTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.DISCOVER, discoverFragment, R.id.frameLayoutFragmentContainer, true);
//                    resolveStackLists(tabId);
//                    assignCurrentFragment(discoverFragment);
//                    break;
                case EndpointKeys.TRENDING:
                    addInitialTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TRENDING, fragmentTrending, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTrending);
                    break;
                case EndpointKeys.UPCOMING_MOVIES:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.UPCOMING_MOVIES, fragmentUpcomingMovies, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentUpcomingMovies);
                    break;
                case EndpointKeys.TOP_RATED_MOVIES:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TOP_RATED_MOVIES, fragmentTopRatedMovies, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTopRatedMovies);
                    break;
                case EndpointKeys.LATEST_MOVIES:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.LATEST_MOVIES, fragmentLatestMovies, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentLatestMovies);
                    break;
                case EndpointKeys.IN_THEATER:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.IN_THEATER, fragmentInTheater, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentInTheater);
                    break;
                case EndpointKeys.TOP_RATED_TV_SHOWS:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TOP_RATED_TV_SHOWS, fragmentTopRatedTvShows, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTopRatedTvShows);
                    break;
                case EndpointKeys.LATEST_TV_SHOWS:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.LATEST_TV_SHOWS, fragmentLatestTvShows, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentLatestTvShows);
                    break;
                case EndpointKeys.TV_SHOWS_ON_THE_AIR:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TV_SHOWS_ON_THE_AIR, fragmentTvShowsOnAir, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTvShowsOnAir);
                    break;
                case EndpointKeys.TV_SHOWS_AIRING_TODAY:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TV_SHOWS_AIRING_TODAY, fragmentTvShowsAiringToday, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTvShowsAiringToday);
                    break;
                case EndpointKeys.TOP_RATED_CELEBRITIES:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TOP_RATED_CELEBRITIES, fragmentTopRatedCelebrities, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTopRatedCelebrities);
                    break;
                case EndpointKeys.GENRE:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.GENRE, fragmentGenre, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentGenre);
                    break;
            }
        } else {
            /*
             * We are switching tabs, and target tab already has at least one fragment.
             * Show the target fragment
             */
            showHideTabFragment(getSupportFragmentManager(), stacks.get(tabId).lastElement(), currentFragment);
            resolveStackLists(tabId);
            assignCurrentFragment(stacks.get(tabId).lastElement());
        }
    }

    private void popFragment() {
        /*
         * Select the second last fragment in current tab's stack,
         * which will be shown after the fragment transaction given below
         */
        Fragment fragment = stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 4);

        /*pop current fragment from stack */
        stacks.get(currentTab).pop();

        removeFragment(getSupportFragmentManager(), fragment, currentFragment);

        assignCurrentFragment(fragment);
    }

    private void resolveBackPressed() {
        if (currentTab.equals(EndpointKeys.TRENDING)) {
            if (fragmentTrending.viewPagerTrending.getCurrentItem() != 0) {
                fragmentTrending.viewPagerTrending.setCurrentItem(0);
            } else {
                resolveStack();
            }
        } else {
            resolveStack();
        }

    }

    private void resolveStack() {
        int stackValue = 0;
        if (stacks.get(currentTab).size() == 1) {
            Stack<Fragment> value = stacks.get(stackList.get(1));
            if (value.size() > 1) {
                stackValue = value.size();
                popAndNavigateToPreviousMenu();
            }
            if (stackValue <= 1) {
                if (menuStacks.size() > 1) {
                    navigateToPreviousMenu();
                } else {
                    finish();
                }
            }
        } else {
            popFragment();
        }
    }

    /*Pops the last fragment inside particular tab and goes to the second tab in the stack*/
    private void popAndNavigateToPreviousMenu() {
        String tempCurrent = stackList.get(0);
        currentTab = stackList.get(1);
        BaseFragment.setCurrentTab(currentTab);
        resolveTabPositions(currentTab);
        showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
        updateStackToIndexFirst(stackList, tempCurrent);
        menuStacks.remove(0);
    }

    private void navigateToPreviousMenu() {
        menuStacks.remove(0);
        currentTab = menuStacks.get(0);
        BaseFragment.setCurrentTab(currentTab);
        resolveTabPositions(currentTab);
        showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
    }

    public void showFragment(Bundle bundle, Fragment fragmentToAdd) {
        String tab = bundle.getString(DATA_KEY_1);
        boolean shouldAdd = bundle.getBoolean(DATA_KEY_2);
        addShowHideFragment(getSupportFragmentManager(), stacks, tab, fragmentToAdd, getCurrentFragmentFromShownStack(), R.id.frameLayoutFragmentContainer, shouldAdd);
        assignCurrentFragment(fragmentToAdd);
    }

    private int resolveTabPositions(String currentTab) {
        switch (currentTab) {
//            case EndpointKeys.DISCOVER:
//                index = 0;
//                setAppBarTitle(getResources().getString(R.string.discover));
//                break;
            case EndpointKeys.TRENDING:
                index = 0;
                setAppBarTitle(getResources().getString(R.string.trending));
                break;
            case EndpointKeys.UPCOMING_MOVIES:
                index = 1;
                setAppBarTitle(getResources().getString(R.string.upcoming_movies));
                break;
            case EndpointKeys.TOP_RATED_MOVIES:
                index = 2;
                setAppBarTitle(getResources().getString(R.string.top_rated_movies));
                break;
            case EndpointKeys.LATEST_MOVIES:
                index = 3;
                setAppBarTitle(getResources().getString(R.string.latest_movies));
                break;
            case EndpointKeys.IN_THEATER:
                setAppBarTitle(getResources().getString(R.string.in_theater));
                index = 4;
                break;
            case EndpointKeys.TOP_RATED_TV_SHOWS:
                setAppBarTitle(getResources().getString(R.string.top_rated_tv_shows));
                index = 5;
                break;
            case EndpointKeys.LATEST_TV_SHOWS:
                setAppBarTitle(getResources().getString(R.string.latest_tv_shows));
                index = 6;
                break;
            case EndpointKeys.TV_SHOWS_ON_THE_AIR:
                index = 7;
                setAppBarTitle(getResources().getString(R.string.tv_shows_on_the_air));
                break;
            case EndpointKeys.TV_SHOWS_AIRING_TODAY:
                index = 8;
                setAppBarTitle(getResources().getString(R.string.tv_shows_on_the_air_today));
                break;
            case EndpointKeys.TOP_RATED_CELEBRITIES:
                setAppBarTitle(getResources().getString(R.string.top_rated_celebrities));
                index = 9;
                break;
            case EndpointKeys.GENRE:
                setAppBarTitle(getResources().getString(R.string.genre));
                index = 10;
                break;
        }
//        if (index == 2) {
//            textViewAppBarTitle.setVisibility(View.GONE);
//            editTextSearchAppBar.setVisibility(View.VISIBLE);
//        } else {
//            textViewAppBarTitle.setVisibility(View.VISIBLE);
//            editTextSearchAppBar.setVisibility(View.GONE);
//        }
//        imageViews[currentFragmentIndex].setSelected(false);
        currentFragmentIndex = index;
        return index;
    }

    private void showAdOnListClick() {
        if (interstitialAddCounter <= 2) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_admob_id));
            AdRequest adRequestInterstitial = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequestInterstitial);
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showInterstitial();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }
            });
            showInterstitial();
        }
    }

    private void resolveStackLists(String tabId) {
        updateStackIndex(stackList, tabId);
        updateTabStackIndex(menuStacks, tabId);
    }

    private Fragment getCurrentFragmentFromShownStack() {
        return stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 1);
    }

    private void assignCurrentFragment(Fragment current) {
        currentFragment = current;
    }

    private void setAppBarTitle(String title) {
        textViewTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        interstitialAddCounter = 0;
    }

    private void setCustomFont() {
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:
                startActivity(new Intent(this, SearchActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.menuCheckUpdate:
                new AppUpdater(MainActivity.this)
                        .setDisplay(Display.DIALOG)
                        .showAppUpdated(true)
                        .start();
                return true;
            case R.id.menuShare:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "To watch any movie and TV show download this app https://play.google.com/store/apps/details?id=com.codebosses.flicksapp&hl=en";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.menuPrivacy:
                String url = "https://codebosses.blogspot.com/p/privacy-policy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, "Open using"));
                return true;
            case R.id.menuRateUs:
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMain.closeDrawer(GravityCompat.START);
        } else {
            resolveBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSelectedItem(EventBusSelectedItem eventBusSelectedItem) {
        drawerLayoutMain.closeDrawer(GravityCompat.START);
        setAppBarTitle(eventBusSelectedItem.getTitle());
        interstitialAddCounter++;
        showAdOnListClick();
        switch (eventBusSelectedItem.getTitle()) {
//            case EndpointKeys.DISCOVER:
//                index = 0;
//                selectedTab(EndpointKeys.DISCOVER);
//                break;
            case EndpointKeys.TRENDING:
                index = 0;
                selectedTab(EndpointKeys.TRENDING);
                break;
            case EndpointKeys.UPCOMING_MOVIES:
                index = 1;
                selectedTab(EndpointKeys.UPCOMING_MOVIES);
                break;
            case EndpointKeys.TOP_RATED_MOVIES:
                index = 2;
                selectedTab(EndpointKeys.TOP_RATED_MOVIES);
                break;
            case EndpointKeys.LATEST_MOVIES:
                index = 3;
                selectedTab(EndpointKeys.LATEST_MOVIES);
                break;
            case EndpointKeys.IN_THEATER:
                index = 4;
                selectedTab(EndpointKeys.IN_THEATER);
                break;
            case EndpointKeys.TOP_RATED_TV_SHOWS:
                index = 5;
                selectedTab(EndpointKeys.TOP_RATED_TV_SHOWS);
                break;
            case EndpointKeys.LATEST_TV_SHOWS:
                index = 6;
                selectedTab(EndpointKeys.LATEST_TV_SHOWS);
                break;
            case EndpointKeys.TV_SHOWS_ON_THE_AIR:
                index = 7;
                selectedTab(EndpointKeys.TV_SHOWS_ON_THE_AIR);
                break;
            case EndpointKeys.TV_SHOWS_AIRING_TODAY:
                index = 8;
                selectedTab(EndpointKeys.TV_SHOWS_AIRING_TODAY);
                break;
            case EndpointKeys.TOP_RATED_CELEBRITIES:
                index = 9;
                selectedTab(EndpointKeys.TOP_RATED_CELEBRITIES);
                break;
            case EndpointKeys.GENRE:
                index = 10;
                selectedTab(EndpointKeys.GENRE);
                break;
        }
    }


    @Override
    public void onFragmentInteractionCallback(Bundle bundle) {

    }
}