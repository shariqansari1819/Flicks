package com.codebosses.flicks.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.fragments.genrefragments.FragmentAction;
import com.codebosses.flicks.fragments.genrefragments.FragmentAnimated;
import com.codebosses.flicks.fragments.genrefragments.FragmentDrama;
import com.codebosses.flicks.fragments.genrefragments.FragmentScienceFiction;
import com.codebosses.flicks.fragments.moviesfragments.FragmentInTheater;
import com.codebosses.flicks.fragments.moviesfragments.FragmentLatestMovies;
import com.codebosses.flicks.fragments.moviesfragments.FragmentTopRatedMovies;
import com.codebosses.flicks.fragments.moviesfragments.FragmentUpcomingMovies;
import com.codebosses.flicks.fragments.tvfragments.FragmentLatestTvShows;
import com.codebosses.flicks.fragments.celebritiesfragments.FragmentTopRatedCelebrities;
import com.codebosses.flicks.fragments.tvfragments.FragmentTopRatedTvShows;
import com.codebosses.flicks.pojo.eventbus.EventBusSelectedItem;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewTitle;

    ActionBarDrawerToggle actionBarDrawerToggle;

    //    Font fields....
    private FontUtils fontUtils;

    //    Fragment fields...
    private FragmentUpcomingMovies fragmentUpcomingMovies;
    private FragmentTopRatedMovies fragmentTopRatedMovies;
    private FragmentLatestMovies fragmentLatestMovies;
    private FragmentInTheater fragmentInTheater;
    private FragmentTopRatedTvShows fragmentTopRatedTvShows;
    private FragmentLatestTvShows fragmentLatestTvShows;
    private FragmentTopRatedCelebrities fragmentTopRatedCelebrities;
    private FragmentAction fragmentAction;
    private FragmentAnimated fragmentAnimated;
    private FragmentDrama fragmentDrama;
    private FragmentScienceFiction fragmentScienceFiction;
    private Fragment currentFragment;

    private FragmentManager fragmentManager;

    //    TODO: Instance fields....
    private int index, currentFragmentIndex;

    //    Stack fields....
    private Map<String, Stack<Fragment>> stacks;
    private String currentTab;
    private List<String> stackList;
    private List<String> menuStacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        //        Fragment manager fields initialization....
        fragmentManager = getSupportFragmentManager();
        initializeFragments();


        createStacks();

//        Event listeners....
        EventBus.getDefault().register(this);
    }

    private void initializeFragments() {
        fragmentUpcomingMovies = new FragmentUpcomingMovies();
        fragmentTopRatedMovies = new FragmentTopRatedMovies();
        fragmentLatestMovies = new FragmentLatestMovies();
        fragmentInTheater = new FragmentInTheater();
        fragmentTopRatedTvShows = new FragmentTopRatedTvShows();
        fragmentLatestTvShows = new FragmentLatestTvShows();
        fragmentTopRatedCelebrities = new FragmentTopRatedCelebrities();
        fragmentAction = new FragmentAction();
        fragmentAnimated = new FragmentAnimated();
        fragmentDrama = new FragmentDrama();
        fragmentScienceFiction = new FragmentScienceFiction();
    }

    private void createStacks() {

//        imageViews = new ImageView[]{imageViewHome, imageViewFriendsMap, imageViewSuggestedUsers, imageViewConversation, imageViewMore};

        stacks = new LinkedHashMap<>();
        stacks.put(EndpointKeys.UPCOMING_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.LATEST_MOVIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.IN_THEATER, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_TV_SHOWS, new Stack<Fragment>());
        stacks.put(EndpointKeys.LATEST_TV_SHOWS, new Stack<Fragment>());
        stacks.put(EndpointKeys.TOP_RATED_CELEBRITIES, new Stack<Fragment>());
        stacks.put(EndpointKeys.ACTION, new Stack<Fragment>());
        stacks.put(EndpointKeys.ANIMATED, new Stack<Fragment>());
        stacks.put(EndpointKeys.DRAMA, new Stack<Fragment>());
        stacks.put(EndpointKeys.SCIENCE_FICTION, new Stack<Fragment>());

        menuStacks = new ArrayList<>();
        menuStacks.add(EndpointKeys.UPCOMING_MOVIES);

        stackList = new ArrayList<>();
        stackList.add(EndpointKeys.UPCOMING_MOVIES);
        stackList.add(EndpointKeys.TOP_RATED_MOVIES);
        stackList.add(EndpointKeys.LATEST_MOVIES);
        stackList.add(EndpointKeys.IN_THEATER);
        stackList.add(EndpointKeys.TOP_RATED_TV_SHOWS);
        stackList.add(EndpointKeys.LATEST_TV_SHOWS);
        stackList.add(EndpointKeys.TOP_RATED_CELEBRITIES);
        stackList.add(EndpointKeys.ACTION);
        stackList.add(EndpointKeys.ANIMATED);
        stackList.add(EndpointKeys.DRAMA);
        stackList.add(EndpointKeys.SCIENCE_FICTION);

//        imageViews[0].setSelected(true);
        setAppBarTitle(getResources().getString(R.string.upcoming_movies));
        selectedTab(EndpointKeys.UPCOMING_MOVIES);
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
                case EndpointKeys.UPCOMING_MOVIES:
                    addInitialTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.UPCOMING_MOVIES, fragmentUpcomingMovies, R.id.frameLayoutFragmentContainer, true);
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
                case EndpointKeys.TOP_RATED_CELEBRITIES:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.TOP_RATED_CELEBRITIES, fragmentTopRatedCelebrities, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentTopRatedCelebrities);
                    break;
                case EndpointKeys.ACTION:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.ACTION, fragmentAction, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentAction);
                    break;
                case EndpointKeys.ANIMATED:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.ANIMATED, fragmentAnimated, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentAnimated);
                    break;
                case EndpointKeys.DRAMA:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.DRAMA, fragmentDrama, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentDrama);
                    break;
                case EndpointKeys.SCIENCE_FICTION:
                    addAdditionalTabFragment(getSupportFragmentManager(), stacks, EndpointKeys.SCIENCE_FICTION, fragmentScienceFiction, currentFragment, R.id.frameLayoutFragmentContainer, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(fragmentScienceFiction);
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
            case EndpointKeys.UPCOMING_MOVIES:
                index = 0;
                setAppBarTitle(getResources().getString(R.string.upcoming_movies));
                break;
            case EndpointKeys.TOP_RATED_MOVIES:
                index = 1;
                setAppBarTitle(getResources().getString(R.string.top_rated_movies));
                break;
            case EndpointKeys.LATEST_MOVIES:
                index = 2;
                setAppBarTitle(getResources().getString(R.string.latest_movies));
                break;
            case EndpointKeys.IN_THEATER:
                setAppBarTitle(getResources().getString(R.string.in_theater));
                index = 3;
                break;
            case EndpointKeys.TOP_RATED_TV_SHOWS:
                setAppBarTitle(getResources().getString(R.string.top_rated_tv_shows));
                index = 4;
                break;
            case EndpointKeys.LATEST_TV_SHOWS:
                setAppBarTitle(getResources().getString(R.string.latest_tv_shows));
                index = 4;
                break;
            case EndpointKeys.TOP_RATED_CELEBRITIES:
                setAppBarTitle(getResources().getString(R.string.top_rated_celebrities));
                index = 4;
                break;
            case EndpointKeys.ACTION:
                setAppBarTitle(getResources().getString(R.string.action));
                index = 4;
                break;
            case EndpointKeys.ANIMATED:
                setAppBarTitle(getResources().getString(R.string.animated));
                index = 4;
                break;
            case EndpointKeys.DRAMA:
                setAppBarTitle(getResources().getString(R.string.drama));
                index = 4;
                break;
            case EndpointKeys.SCIENCE_FICTION:
                setAppBarTitle(getResources().getString(R.string.sceince_fiction));
                index = 4;
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
    }

    private void setCustomFont() {
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewBoldFont(textViewTitle);
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
        switch (eventBusSelectedItem.getTitle()) {
            case EndpointKeys.UPCOMING_MOVIES:
                index = 0;
                selectedTab(EndpointKeys.UPCOMING_MOVIES);
                break;
            case EndpointKeys.TOP_RATED_MOVIES:
                index = 1;
                selectedTab(EndpointKeys.TOP_RATED_MOVIES);
                break;
            case EndpointKeys.LATEST_MOVIES:
                index = 2;
                selectedTab(EndpointKeys.LATEST_MOVIES);
                break;
            case EndpointKeys.IN_THEATER:
                index = 3;
                selectedTab(EndpointKeys.IN_THEATER);
                break;
            case EndpointKeys.TOP_RATED_TV_SHOWS:
                index = 4;
                selectedTab(EndpointKeys.TOP_RATED_TV_SHOWS);
                break;
            case EndpointKeys.LATEST_TV_SHOWS:
                index = 5;
                selectedTab(EndpointKeys.LATEST_TV_SHOWS);
                break;
            case EndpointKeys.TOP_RATED_CELEBRITIES:
                index = 6;
                selectedTab(EndpointKeys.TOP_RATED_CELEBRITIES);
                break;
            case EndpointKeys.ACTION:
                index = 7;
                selectedTab(EndpointKeys.ACTION);
                break;
            case EndpointKeys.ANIMATED:
                index = 8;
                selectedTab(EndpointKeys.ANIMATED);
                break;
            case EndpointKeys.DRAMA:
                index = 9;
                selectedTab(EndpointKeys.DRAMA);
                break;
            case EndpointKeys.SCIENCE_FICTION:
                index = 10;
                selectedTab(EndpointKeys.SCIENCE_FICTION);
                break;
        }
    }


    @Override
    public void onFragmentInteractionCallback(Bundle bundle) {

    }

}