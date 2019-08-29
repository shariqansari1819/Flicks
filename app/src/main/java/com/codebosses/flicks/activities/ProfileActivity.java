package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codebosses.flicks.FlicksApplication;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.appBarProfile)
    Toolbar toolbarProfile;
    @BindView(R.id.imageViewProfile)
    CircleImageView imageViewProfile;
    @BindView(R.id.textViewNameProfile)
    TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarProfile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ValidUtils.changeToolbarFont(toolbarProfile, this);
            getSupportActionBar().setTitle("Profile");
        }

        FontUtils.getFontUtils(this).setTextViewRegularFont(textViewName);
        textViewName.setText(FlicksApplication.getStringValue(EndpointKeys.USER_NAME));
        Glide.with(this)
                .load(FlicksApplication.getStringValue(EndpointKeys.USER_IMAGE))
                .into(imageViewProfile);
    }
}