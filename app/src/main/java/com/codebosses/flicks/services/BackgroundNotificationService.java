package com.codebosses.flicks.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.codebosses.flicks.R;
import com.codebosses.flicks.activities.TrailerActivity;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;
import java.util.UUID;

public class BackgroundNotificationService extends IntentService {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public BackgroundNotificationService() {
        super("service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel("FLICK_CH_01", "FLICKS", NotificationManager.IMPORTANCE_DEFAULT);

                notificationChannel.setDescription("no sound");
                notificationChannel.setSound(null, null);
                notificationChannel.enableLights(false);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.enableVibration(false);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationBuilder = new NotificationCompat.Builder(this, "id")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(intent.getStringExtra("name"))
                    .setContentText("Downloading video")
                    .setDefaults(0)
                    .setAutoCancel(false);
            notificationManager.notify(0, notificationBuilder.build());

            downloadVideo(intent.getStringExtra("path"),intent.getStringExtra("name"));
        }
    }

    private void downloadVideo(String path,String name) {
//        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String downloadPath = Environment.getExternalStorageDirectory() + File.separator + "Flicks/videos";
        PRDownloader.download(path, downloadPath,name + ".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        int downloadProgress = (int) ((double) (progress.currentBytes * 100) / (double) progress.totalBytes);
                        BackgroundNotificationService.this.updateNotification(downloadProgress);
                    }
                })
                .start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        BackgroundNotificationService.this.onDownloadComplete(true);
                        scanFile(downloadPath);
                        Toast.makeText(BackgroundNotificationService.this, "Video downloaded successfully.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(BackgroundNotificationService.this, "Error downloading video.", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void updateNotification(int currentProgress) {

        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText("Downloaded: " + currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendProgressUpdate(boolean downloadComplete) {
        notificationBuilder.setAutoCancel(true);
        Intent intent = new Intent(TrailerActivity.PROGRESS_UPDATE);
        intent.putExtra("downloadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(BackgroundNotificationService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("Video Download Complete");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
//                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}
