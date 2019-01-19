package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.searchadapter.SearchPagerAdapter;
import com.codebosses.flicks.pojo.eventbus.EventBusSearchText;
import com.codebosses.flicks.utils.FontUtils;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

public class SearchActivity extends AppCompatActivity implements TextWatcher {

    //    Android fields....
    @BindView(R.id.appBarSearch)
    Toolbar toolbarSearch;
    @BindView(R.id.editTextSearch)
    AppCompatEditText editTextSearch;
    @BindView(R.id.tabLayoutSearch)
    TabLayout tabLayoutSearch;
    @BindView(R.id.viewPagerSearch)
    ViewPager viewPagerSearch;

    //    Adapter fields....
    private SearchPagerAdapter searchPagerAdapter;

    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

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
                    EventBus.getDefault().post(new EventBusSearchText(editTextSearch.getText().toString()));
                }
            }
        }
    };

}
