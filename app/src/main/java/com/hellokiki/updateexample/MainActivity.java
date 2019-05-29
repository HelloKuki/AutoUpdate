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
        UpdateManager.getInstance()
                .setUrl("http://192.168.23.217:8899/apk/checkUpdate")
//                .setDeleteApk(false)
                .checkUpdate();
    }
}
