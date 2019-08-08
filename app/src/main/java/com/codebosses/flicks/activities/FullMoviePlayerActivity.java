package com.codebosses.flicks.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.halilibo.bvpkotlin.BetterVideoPlayer;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class FullMoviePlayerActivity extends AppCompatActivity {

    private BetterVideoPlayer betterVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_movie_player);

        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);

        if (getIntent() != null) {
            String movieUrl = getIntent().getStringExtra(EndpointKeys.MOVIE_URL);
            String movieName = getIntent().getStringExtra(EndpointKeys.MOVIE_TITLE);
            String moviePosterPath = getIntent().getStringExtra(EndpointKeys.MOVIES_IMAGES);
            if (!movieUrl.isEmpty()) {
                jcVideoPlayerStandard.setUp(movieUrl
                        , JCVideoPlayerStandard.FULLSCREEN_ORIENTATION, movieName);
                Glide.with(this)
                        .load(moviePosterPath)
                        .into(jcVideoPlayerStandard.thumbImageView);
//                betterVideoPlayer.setSource(Uri.parse(movieUrl));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}