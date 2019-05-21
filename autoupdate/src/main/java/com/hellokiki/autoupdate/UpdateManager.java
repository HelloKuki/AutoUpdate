package com.hellokiki.autoupdate;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.hellokiki.autoupdate.bean.Apkinfo;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

    private static UpdateManager sManager = new UpdateManager();

    private String mSavePath;

    private boolean mDeleteApk = true;

    public static UpdateManager getInstance() {
        return sManager;
    }

    private UpdateManager() {
        mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/APKUPDATE";
        Log.e("2018", "save path = " + mSavePath);
    }

    public UpdateManager setSavePath(String savePath) {
        mSavePath = savePath;
        return sManager;
    }

    public UpdateManager setDeleteApk(boolean isDeleteApk) {
        mDeleteApk = isDeleteApk;
        return sManager;
    }

    public void checkUpdate() {
        CheckUpdateAsyncTask asyncTask = new CheckUpdateAsyncTask(new CheckUpdateListener() {
            @Override
            public void checkSuccess(Apkinfo apkinfo) {


            }

            @Override
            public void checkFail(String error) {

            }
        });
        asyncTask.execute();
    }


}
