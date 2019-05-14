package com.codebosses.flicks.fragments.trending;


import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.trending.TrendingPagerAdapter;
import com.codebosses.flicks.fragments.base.BaseFragment;
import com.codebosses.flicks.utils.FontUtils;
import com.google.android.material.tabs.TabLayout;

public class FragmentTrending extends BaseFragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrending)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.tabLayoutTrending)
    TabLayout tabLayoutTrending;
    @BindView(R.id.viewPagerTrending)
    ViewPager viewPagerTrending;

    //    Instance fields....
    private TrendingPagerAdapter trendingAdapter;

    public FragmentTrending() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);
        ButterKnife.bind(this, view);

//        Setting pager adapter....
        trendingAdapter = new TrendingPagerAdapter(getChildFragmentManager(),getActivity());
        viewPagerTrending.setAdapter(trendingAdapter);
        tabLayoutTrending.setupWithViewPager(viewPagerTrending);

        viewPagerTrending.setOffscreenPageLimit(2);

        changeTabsFont();

        return view;
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayoutTrending.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    FontUtils.getFontUtils(getActivity()).setTextViewRegularFont((TextView) tabViewChild);
                }
            }
        }
    }

}