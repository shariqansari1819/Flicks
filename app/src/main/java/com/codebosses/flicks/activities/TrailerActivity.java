package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;

import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

public class TrailerActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.youtubePlayerViewTrailer)
    YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;

    //    Instance fields....
    private String youtubeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            youtubeKey = getIntent().getStringExtra(EndpointKeys.YOUTUBE_KEY);

            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
                @Override
                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            super.onReady();
                            TrailerActivity.this.youTubePlayer = youTubePlayer;
                            youTubePlayer.loadVideo(youtubeKey, 0);
                        }

                    });
                }
            }, true);

        }
    }

    @OnClick(R.id.imageViewCloseTrailer)
    public void onCloseClick(View view) {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (youTubePlayer != null) {
            youTubePlayer.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (youTubePlayer != null) {
            youTubePlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
