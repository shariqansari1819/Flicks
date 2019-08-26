package com.codebosses.flicks.fragments.trending;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
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
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class FragmentTrending extends BaseFragment {

    //    Android fields....
    @BindView(R.id.constraintLayoutTrending)
    ConstraintLayout constraintLayoutTrending;
    @BindView(R.id.tabLayoutTrending)
    SmartTabLayout tabLayoutTrending;
    @BindView(R.id.viewPagerTrending)
    public ViewPager viewPagerTrending;

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
        trendingAdapter = new TrendingPagerAdapter(getChildFragmentManager(), getActivity());
        viewPagerTrending.setAdapter(trendingAdapter);

        tabLayoutTrending.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View tab = inflater.inflate(R.layout.layout_custom_tab, container, false);
                TextView customText = tab.findViewById(R.id.textViewCustomTab);
                FontUtils.getFontUtils(getActivity()).setTextViewRegularFont(customText);
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
        tabLayoutTrending.setViewPager(viewPagerTrending);

        viewPagerTrending.setOffscreenPageLimit(2);

//        changeTabsFont();

        return view;
    }

//    private void changeTabsFont() {
//        ViewGroup vg = (ViewGroup) tabLayoutTrending.getChildAt(0);
//        int tabsCount = vg.getChildCount();
//        for (int j = 0; j < tabsCount; j++) {
//            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
//            int tabChildsCount = vgTab.getChildCount();
//            for (int i = 0; i < tabChildsCount; i++) {
//                View tabViewChild = vgTab.getChildAt(i);
//                if (tabViewChild instanceof TextView) {
//                    FontUtils.getFontUtils(getActivity()).setTextViewRegularFont((TextView) tabViewChild);
//                }
//            }
//        }
//    }

}