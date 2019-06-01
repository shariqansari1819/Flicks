package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.searchadapter.SearchPagerAdapter;
import com.codebosses.flicks.pojo.eventbus.EventBusSearchText;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    //    Android fields....
    @BindView(R.id.appBarSearch)
    Toolbar toolbarSearch;
    @BindView(R.id.editTextSearch)
    AppCompatEditText editTextSearch;
    @BindView(R.id.imageViewCloseSearch)
    AppCompatImageView imageViewClose;
    @BindView(R.id.tabLayoutSearch)
    TabLayout tabLayoutSearch;
    @BindView(R.id.viewPagerSearch)
    ViewPager viewPagerSearch;

    //    Adapter fields....
    private SearchPagerAdapter searchPagerAdapter;

    //    Instance fields....
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

//        Setting custom action bar...
        setSupportActionBar(toolbarSearch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        FontUtils.getFontUtils(this).setEditTextRegularFont(editTextSearch);
        editTextSearch.addTextChangedListener(this);

//        Adapter fields initialization....
        searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), this);
        viewPagerSearch.setAdapter(searchPagerAdapter);
        tabLayoutSearch.setupWithViewPager(viewPagerSearch);
        viewPagerSearch.setOffscreenPageLimit(2);
        changeTabsFont();

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
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showInterstitial();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.removeCallbacks(input_finish_checker);
        if (s.toString().isEmpty()) {
            imageViewClose.setVisibility(View.GONE);
        } else {
            imageViewClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            last_text_edit = System.currentTimeMillis();
            handler.postDelayed(input_finish_checker, delay);
        }
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                if (editTextSearch.getText().toString().length() != 0) {
                    String text = editTextSearch.getText().toString();
                    ValidUtils.hideKeyboardFromActivity(SearchActivity.this);
                    EventBus.getDefault().post(new EventBusSearchText(text));
                }
            }
        }
    };

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayoutSearch.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    FontUtils.getFontUtils(this).setTextViewRegularFont((TextView) tabViewChild);
                }
            }
        }
    }

    @OnClick(R.id.imageViewCloseSearch)
    public void onCloseClick(View view) {
        editTextSearch.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
