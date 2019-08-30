package com.codebosses.flicks.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codebosses.flicks.FlicksApplication;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewChangeLanguage)
    TextView textViewChangeLanguage;
    @BindView(R.id.appBarSetting)
    Toolbar toolbarSetting;
    private AlertDialog alertDialog;

    //    Instance fields....
    private ArrayList<String> languageList = new ArrayList<>();

    //    Font fields....
    FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

//        Setting custom action bar....
        setSupportActionBar(toolbarSetting);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Setting");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ValidUtils.changeToolbarFont(toolbarSetting, this);
        }

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewChangeLanguage);

        languageList.add("English");
        languageList.add("Russian");
        languageList.add("Hindi");
        languageList.add("Spanish");

    }

    @OnClick(R.id.textViewChangeLanguage)
    public void onLanguageChangeClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, languageList);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
