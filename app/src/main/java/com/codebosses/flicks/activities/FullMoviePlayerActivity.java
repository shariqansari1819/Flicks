package com.codebosses.flicks.activities;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullMoviePlayerActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.exoPlayerFullMovie)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.circularProgressBarFullMovie)
    CircularProgressBar circularProgressBarFullMovie;

    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady;
    private String movieUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_movie_player);
        ButterKnife.bind(this);


        if (getIntent() != null) {
            movieUrl = getIntent().getStringExtra(EndpointKeys.MOVIE_URL);
            String movieName = getIntent().getStringExtra(EndpointKeys.MOVIE_TITLE);
            String moviePosterPath = getIntent().getStringExtra(EndpointKeys.MOVIES_IMAGES);
            if (!movieUrl.isEmpty()) {
                circularProgressBarFullMovie.setVisibility(View.VISIBLE);
                hideSystemUi();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultRenderersFactory(this), new DefaultTrackSelector(), new DefaultLoadControl());
            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, playbackPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(movieUrl));
        player.prepare(mediaSource, true, false);
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                String status = "";
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        status = "Loading";
                        circularProgressBarFullMovie.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_ENDED:
                        status = "End";
                        circularProgressBarFullMovie.setVisibility(View.GONE);
                        break;
                    case Player.STATE_IDLE:
                        status = "Idle";
//                        circularProgressBarFullMovie.setVisibility(View.VISIBLE);
                        break;
                    case Player.STATE_READY:
                        status = playWhenReady ? "Playing" : "Paused";
                        circularProgressBarFullMovie.setVisibility(View.GONE);
                        break;
                    default:
                        status = "Idle";
                        break;
                }

            }
        });
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = "exoplayer-flicks";

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else if (uri.getLastPathSegment().contains("m3u8")) {
            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);
        } else {
            DefaultDashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                    new DefaultHttpDataSourceFactory("ua", new TransferListener() {
                        @Override
                        public void onTransferInitializing(DataSource source, DataSpec dataSpec, boolean isNetwork) {

                        }

                        @Override
                        public void onTransferStart(DataSource source, DataSpec dataSpec, boolean isNetwork) {

                        }

                        @Override
                        public void onBytesTransferred(DataSource source, DataSpec dataSpec, boolean isNetwork, int bytesTransferred) {

                        }

                        @Override
                        public void onTransferEnd(DataSource source, DataSpec dataSpec, boolean isNetwork) {

                        }
                    }));
            DefaultHttpDataSourceFactory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent);
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory).createMediaSource(uri);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    private void showSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        simpleExoPlayerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}