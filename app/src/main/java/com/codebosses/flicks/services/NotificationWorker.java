package com.codebosses.flicks.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import com.codebosses.flicks.R;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;
import com.codebosses.flicks.pojo.moviespojo.latestmovies.LatestMoviesMainObject;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationWorker extends Worker {

    Call<LatestMoviesMainObject> latestMoviesMainObjectCall;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
//        String notificationType = getInputData().getString(EndpointKeys.NOTIFICATION_TYPE);
//        displayNotification("Hi", "Completeing");
//        getLatestMovie("en-US");

        return Result.success();
    }

    private void getLatestMovie(String language) {
        latestMoviesMainObjectCall = ApiClient.getClient().create(Api.class).getLatestMovie(EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        latestMoviesMainObjectCall.enqueue(new Callback<LatestMoviesMainObject>() {
            @Override
            public void onResponse(Call<LatestMoviesMainObject> call, retrofit2.Response<LatestMoviesMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    LatestMoviesMainObject latestMoviesMainObject = response.body();
                    if (latestMoviesMainObject != null) {
                        displayNotification(latestMoviesMainObject.getTitle() != null ? latestMoviesMainObject.getTitle() : "New Movie", "Checkout this amazing movie.");
                    }
                }
            }

            @Override
            public void onFailure(Call<LatestMoviesMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }

                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {

                    } else {

                    }
                } else {

                }
            }
        });
    }

    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.drawable.logo);

        notificationManager.notify(1, notification.build());
    }
}
