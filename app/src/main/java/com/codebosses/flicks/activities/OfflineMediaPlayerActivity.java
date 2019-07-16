package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.halilibo.bvpkotlin.BetterVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfflineMediaPlayerActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.betterVideoPlayerOfflineMediaPlayer)
    BetterVideoPlayer betterVideoPlayer;
    @BindView(R.id.imageViewCloseOfflineMediaPlayer)
    ImageView imageViewClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_media_player);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            String videoPath = getIntent().getStringExtra(EndpointKeys.VIDEO_PATH);
            betterVideoPlayer.setSource(Uri.parse(videoPath));
            betterVideoPlayer.enableSwipeGestures();
            betterVideoPlayer.setAutoPlay(true);
            betterVideoPlayer.enableControls();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        betterVideoPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        betterVideoPlayer.pause();
    }

    @OnClick(R.id.imageViewCloseOfflineMediaPlayer)
    public void onCloseClick(View view) {
        finish();
    }

}
