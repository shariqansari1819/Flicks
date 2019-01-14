package com.codebosses.flicks.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.fragments.FragmentNavigationView;

public class MainActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.drawerLayoutMain)
    DrawerLayout drawerLayoutMain;
    @BindView(R.id.appBarMain)
    Toolbar toolbarMain;
    @BindView(R.id.textViewAppBarMainTitle)
    TextView textViewTitle;

    ActionBarDrawerToggle actionBarDrawerToggle;

    //    Resource fields....


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

//        Setting drawer layout....
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutMain, toolbarMain, R.string.open, R.string.close);
        drawerLayoutMain.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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
                return true;
        }
        return false;
    }
}
