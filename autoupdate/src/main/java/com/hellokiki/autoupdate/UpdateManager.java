package com.hellokiki.autoupdate;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hellokiki.autoupdate.bean.Apkinfo;
import com.hellokiki.autoupdate.dialog.UpdateTipDialog;
import com.hellokiki.autoupdate.download.DownloadListener;
import com.hellokiki.autoupdate.download.DownloadManager;

public class UpdateManager {

    private static UpdateManager sManager = new UpdateManager();

    private String mSavePath;

    private boolean mDeleteApk = true;

    private Activity mCurrentActivity = null;

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

    public String getSavePath() {
        return mSavePath;
    }

    public UpdateManager setDeleteApk(boolean isDeleteApk) {
        mDeleteApk = isDeleteApk;
        return sManager;
    }

    public void setApplicationLife(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mCurrentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mCurrentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public void checkUpdate() {
        CheckUpdateAsyncTask asyncTask = new CheckUpdateAsyncTask(new CheckUpdateListener() {
            @Override
            public void checkSuccess(Apkinfo apkinfo) {
                UpdateTipDialog dialog = new UpdateTipDialog();
                dialog.setApkinfo(apkinfo);
                dialog.show(mCurrentActivity.getFragmentManager(), "UpdateTipDialog");
            }

            @Override
            public void checkFail(String error) {
                Toast.makeText(mCurrentActivity,"查询更新失败",Toast.LENGTH_SHORT).show();
            }
        });
        asyncTask.execute();
    }

    public void startDownloadApk(String url) {
        DownloadManager.getInstance().downloadApk(url, new DownloadListener() {
            @Override
            public void downloadStart(String url) {
                Log.e("2018", "downloadStart = " + url);
            }

            @Override
            public void downloadProgress(Long progress) {
                Log.e("2018", "progress = " + progress);
            }

            @Override
            public void downloadFinish(boolean result, String filePath) {
                Log.e("2018", "finish = " + result + "     filePath = " + filePath);
            }
        });
    }


}
