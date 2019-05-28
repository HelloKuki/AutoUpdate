package com.hellokiki.updateexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hellokiki.autoupdate.CheckUpdateListener;
import com.hellokiki.autoupdate.UpdateManager;
import com.hellokiki.autoupdate.bean.Apkinfo;
import com.hellokiki.autoupdate.download.DownloadListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateManager.getInstance().checkUpdate();

        UpdateManager.getInstance().checkUpdate(new CheckUpdateListener() {
            @Override
            public void checkSuccess(Apkinfo apkinfo, boolean isNeedUpdate) {

            }

            @Override
            public void checkFail(String error) {

            }
        }).setDownloadListener(new DownloadListener() {
            @Override
            public void downloadStart(String url) {

            }

            @Override
            public void downloadProgress(Integer progress) {

            }

            @Override
            public void downloadFinish(boolean result, String filePath) {

            }
        });

    }
}
