package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.favorite.FavoritePagerAdapter;
import com.codebosses.flicks.adapters.trending.TrendingPagerAdapter;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesListActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.constraintLayoutFavoritesList)
    ConstraintLayout constraintLayoutFavoritesList;
    @BindView(R.id.tabLayoutFavoritesList)
    SmartTabLayout smartTabLayoutFavoritesList;
    @BindView(R.id.viewPagerFavoritesList)
    ViewPager viewPagerFavoritesList;
    @BindView(R.id.appBarFavoritesList)
    Toolbar toolbarFavoritesList;

    //    Instance fields....
    private FavoritePagerAdapter favoritePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);
        ButterKnife.bind(this);

//        Setting custom action bar....
        setSupportActionBar(toolbarFavoritesList);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Favorite List");
            ValidUtils.changeToolbarFont(toolbarFavoritesList, this);
        }

        //        Setting pager adapter....
        favoritePagerAdapter = new FavoritePagerAdapter(getSupportFragmentManager(), this);
        viewPagerFavoritesList.setAdapter(favoritePagerAdapter);

        smartTabLayoutFavoritesList.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View tab = inflater.inflate(R.layout.layout_custom_tab, container, false);
                TextView customText = tab.findViewById(R.id.textViewCustomTab);
                FontUtils.getFontUtils(FavoritesListActivity.this).setTextViewRegularFont(customText);
                switch (position) {
                    case 0:
                        customText.setText(adapter.getPageTitle(position));
                        break;
                    case 1:
                        customText.setText(adapter.getPageTitle(position));
                        break;
                    case 2:
                        customText.setText(adapter.getPageTitle(position));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return tab;
            }
        });
        smartTabLayoutFavoritesList.setViewPager(viewPagerFavoritesList);
        viewPagerFavoritesList.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}