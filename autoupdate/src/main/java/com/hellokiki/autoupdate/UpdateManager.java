package com.hellokiki.autoupdate;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
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

    private CheckUpdateAsyncTask mCheckUpdateAsyncTask;

    private UpdateTipDialog mUpdateTipDialog;

    public static UpdateManager getInstance() {
        return sManager;
    }

    private UpdateManager() {
        mSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/APKUPDATE";
        Log.e("2018", "save path = " + mSavePath);
        mUpdateTipDialog = new UpdateTipDialog();
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
        checkUpdate(new InnerCheckUpdateListener());
    }

    public void checkUpdate(CheckUpdateListener listener) {
        if (mCheckUpdateAsyncTask != null) {
            return;
        }
        PackageManager pm = mCurrentActivity.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mCurrentActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
            long currentVersionCode;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                currentVersionCode = packageInfo.getLongVersionCode();
            } else {
                currentVersionCode = packageInfo.versionCode;
            }
            mCheckUpdateAsyncTask = new CheckUpdateAsyncTask(currentVersionCode, listener);
            mCheckUpdateAsyncTask.execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void startDownloadApk(String url) {
        startDownloadApk(url, new InnerDownloadListener());
    }

    public void startDownloadApk(String url, DownloadListener listener) {
        DownloadManager.getInstance().downloadApk(url, listener);
    }

    private class InnerCheckUpdateListener implements CheckUpdateListener {

        @Override
        public void checkSuccess(Apkinfo apkinfo, boolean isNeedUpdate) {
            mCheckUpdateAsyncTask = null;
            if (isNeedUpdate) {
                mUpdateTipDialog.setApkinfo(apkinfo);
                mUpdateTipDialog.show(mCurrentActivity.getFragmentManager(), "UpdateTipDialog");
            }
        }

        @Override
        public void checkFail(String error) {
            mCheckUpdateAsyncTask = null;
            Toast.makeText(mCurrentActivity, "查询更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    private class InnerDownloadListener implements DownloadListener {

        @Override
        public void downloadStart(String url) {
            Log.e("2018", "downloadStart = " + url);
        }

        @Override
        public void downloadProgress(Integer progress) {
            Log.e("2018", "progress = " + progress);
            mUpdateTipDialog.setProgress(progress);
        }

        @Override
        public void downloadFinish(boolean result, String filePath) {
            Log.e("2018", "finish = " + result + "     filePath = " + filePath);
            mUpdateTipDialog.dismiss();
        }
    }


}
