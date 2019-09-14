package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.offline.OfflineVideosAdapter;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.pojo.eventbus.EventBusOfflineClick;
import com.codebosses.flicks.pojo.offlinepojo.OfflineModel;
import com.codebosses.flicks.utils.SortingUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.recyclerViewOffline)
    RecyclerView recyclerViewOffline;
    @BindView(R.id.appBarOffline)
    Toolbar toolbarOffline;
//    @BindView(R.id.adView)
//    AdView adView;

    //    Adapter fields...
    private OfflineVideosAdapter offlineVideosAdapter;
    private List<OfflineModel> offlineModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        ButterKnife.bind(this);

//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });

//        Setting custom action bar....
        setSupportActionBar(toolbarOffline);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.offline));
            ValidUtils.changeToolbarFont(toolbarOffline, this);
        }

//        Setting layout manager....
        recyclerViewOffline.setLayoutManager(new LinearLayoutManager(this));

        //        Setting item animator....
        recyclerViewOffline.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOffline.getItemAnimator().setAddDuration(500);

        //        Setting empty adapter....
        offlineVideosAdapter = new OfflineVideosAdapter(this, offlineModelList);
        recyclerViewOffline.setAdapter(offlineVideosAdapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getOfflineList();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusOfflineVideoClick(EventBusOfflineClick eventBusOfflineClick) {
        if (eventBusOfflineClick.getType().equals("offline")) {
            Intent intent = new Intent(this, OfflineMediaPlayerActivity.class);
            intent.putExtra(EndpointKeys.VIDEO_PATH, offlineModelList.get(eventBusOfflineClick.getPosition()).getPath());
            startActivity(intent);
        } else if (eventBusOfflineClick.getType().equals("menu")) {
            PopupMenu popup = new PopupMenu(OfflineActivity.this, eventBusOfflineClick.getView());
            popup.getMenuInflater().inflate(R.menu.menu_offline_delete, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menuDelete:
                            File file = new File(offlineModelList.get(eventBusOfflineClick.getPosition()).getPath());
                            boolean isDeleted = file.delete();
                            if (isDeleted) {
                                offlineVideosAdapter.notifyItemRemoved(eventBusOfflineClick.getPosition());
                                offlineModelList.remove(eventBusOfflineClick.getPosition());
                            }
                            return true;
                    }
                    return true;
                }
            });

            popup.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            int counter = 0;
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    counter++;
                }
            }
            if (counter == grantResults.length) {
                getOfflineList();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOfflineList() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Flicks");
        File videoFiles = new File(file + "/videos");
        offlineModelList.clear();
        File[] contents = videoFiles.listFiles();
        if (contents == null) {

        } else if (contents.length == 0) {

        } else {

            if (videoFiles.isDirectory()) {

                for (File f : videoFiles.listFiles()) {
                    if (f.isFile()) {
                        String fileName = f.getName();
                        String date = new Date(f.lastModified()).toString();
                        String fileUrl = videoFiles + File.separator + fileName;
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(this, Uri.fromFile(new File(fileUrl)));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long timeInMillisec = Long.parseLong(time);
                        retriever.release();
                        offlineModelList.add(new OfflineModel(fileUrl, fileName, timeInMillisec, fileUrl, date));
                    }
                }
                SortingUtils.sortOfflineVideosByDate(offlineModelList);
                offlineVideosAdapter.notifyItemRangeInserted(0, offlineModelList.size());
            } else {
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, "Error", duration);
                toast.show();
            }

        }
    }
}