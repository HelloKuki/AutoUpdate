package com.hellokiki.autoupdate;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hellokiki.autoupdate.bean.Apkinfo;
import com.hellokiki.autoupdate.dialog.BaseUpdateDialog;
import com.hellokiki.autoupdate.dialog.DialogEventListener;
import com.hellokiki.autoupdate.dialog.UpdateTipDialog;
import com.hellokiki.autoupdate.download.DownloadListener;
import com.hellokiki.autoupdate.download.DownloadManager;
import com.hellokiki.autoupdate.utils.ApkUtils;
import com.hellokiki.autoupdate.utils.NetWorkUtils;

import java.io.File;
import java.util.HashMap;

public class UpdateManager {

    private static UpdateManager sManager = new UpdateManager();

    private String mSavePath;

    private boolean mDeleteApk = true;

    private Activity mCurrentActivity = null;

    private CheckUpdateAsyncTask mCheckUpdateAsyncTask;

    private BaseUpdateDialog mUpdateTipDialog;

    private String mUrl;

    private String mAppKey;

    private int mNotifyType = Apkinfo.PROGRESS_TYPE_DIALOG;

    private CheckUpdateListener mCheckUpdateListener;

    private DownloadListener mDownloadListener;

    private JsonParseListener mJsonParseListener;

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

    public UpdateManager setUrl(String url) {
        mUrl = url;
        return sManager;
    }

    void setAppKey(String appKey) {
        mAppKey = appKey;
    }

    public UpdateManager setDeleteApk(boolean isDeleteApk) {
        mDeleteApk = isDeleteApk;
        return sManager;
    }

    public UpdateManager setCheckUpdateListener(CheckUpdateListener checkUpdateListener) {
        mCheckUpdateListener = checkUpdateListener;
        return sManager;
    }

    public UpdateManager setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
        return sManager;
    }

    public UpdateManager setUpdateDialog(BaseUpdateDialog updateDialog) {
        mUpdateTipDialog = updateDialog;
        return sManager;
    }

    public UpdateManager setJosnParseListener(JsonParseListener listener) {
        mJsonParseListener = listener;
        return sManager;
    }


    void init(Application app) {
        Log.e("2018update", "app");
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mCurrentActivity = activity;
                Log.e("2018update", "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                mCurrentActivity = activity;
                Log.e("2018update", "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mCurrentActivity = null;
                Log.e("2018update", "onActivityPaused");
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

    private void deleteHistoryApk() {
        if (!mDeleteApk) {
            return;
        }
        File file = new File(mSavePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                file1.delete();
            }
        }
    }

    public void checkUpdate() {
        checkUpdate(mCheckUpdateListener == null ? new InnerCheckUpdateListener() : mCheckUpdateListener);
    }

    private void checkUpdate(CheckUpdateListener listener) {
        deleteHistoryApk();
        if (mCheckUpdateAsyncTask != null) {
            Log.i("autoupdate", "check update request is running");
            return;
        }
        mUpdateTipDialog.setEventListener(new DialogEventListener() {
            @Override
            public void updateButtonClick(Apkinfo apkinfo) {
                if (apkinfo != null) {
                    UpdateManager.getInstance().startDownloadApk(apkinfo.getUrl());
                    if (apkinfo.getProgressNotifyType() == Apkinfo.PROGRESS_TYPE_DIALOG) {
                        mUpdateTipDialog.showDownloadProgressStatus();
                    } else {
                        mUpdateTipDialog.dismiss();
                    }
                } else {
                    mUpdateTipDialog.dismiss();
                }
            }

            @Override
            public void cancelButtonClick() {
                mUpdateTipDialog.dismiss();
            }

            @Override
            public void installApk(String filePath) {
                mUpdateTipDialog.dismiss();
                ApkUtils.installApk(mCurrentActivity, filePath);
            }
        });
        PackageManager pm = mCurrentActivity.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(mCurrentActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
            long currentVersionCode;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                currentVersionCode = packageInfo.getLongVersionCode();
            } else {
                currentVersionCode = packageInfo.versionCode;
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("app_key", mAppKey);
            if (mJsonParseListener != null) {
                mCheckUpdateAsyncTask = new CheckUpdateAsyncTask(mUrl, map, currentVersionCode, listener, mJsonParseListener);
            } else {
                mCheckUpdateAsyncTask = new CheckUpdateAsyncTask(mUrl, map, currentVersionCode, listener);
            }
            mCheckUpdateAsyncTask.execute();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void startDownloadApk(String url) {
        DownloadManager.getInstance().downloadApk(url, mDownloadListener == null ? new InnerDownloadListener() : mDownloadListener);
    }


    private class InnerCheckUpdateListener implements CheckUpdateListener {

        @Override
        public void checkSuccess(Apkinfo apkinfo, boolean isNeedUpdate) {
            Log.e("2018", apkinfo.toString());
            Log.e("2018", isNeedUpdate + "");
            mCheckUpdateAsyncTask = null;
            mNotifyType = apkinfo.getProgressNotifyType();
            mUpdateTipDialog.setApkInfo(apkinfo);
            if (isNeedUpdate && apkinfo.getUpdateType() != Apkinfo.UPDATE_TYPE_WIFI) {
                mUpdateTipDialog.show(mCurrentActivity.getFragmentManager(), "UpdateTipDialog");
            } else if (isNeedUpdate && apkinfo.getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                Log.e("2018", "wifi status = " + NetWorkUtils.isWifiConnect(mCurrentActivity));
                if (NetWorkUtils.isWifiConnect(mCurrentActivity)) {
                    startDownloadApk(apkinfo.getUrl());
                } else {
                    mUpdateTipDialog.show(mCurrentActivity.getFragmentManager(), "UpdateTipDialog");
                }
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
            if (mUpdateTipDialog.getApkInfo().getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                return;
            }
            if (mNotifyType == Apkinfo.PROGRESS_TYPE_NOTIFY) {
                NotificationUpdateManager.getInstance().createNotification(mCurrentActivity);
            }
        }

        @Override
        public void downloadProgress(Integer progress) {
            Log.e("2018", "progress = " + progress);
            if (mUpdateTipDialog.getApkInfo().getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                return;
            }
            if (mNotifyType == Apkinfo.PROGRESS_TYPE_DIALOG) {
                mUpdateTipDialog.setProgress(progress);
            } else {
                NotificationUpdateManager.getInstance().updateProgress(progress);
            }

        }

        @Override
        public void downloadFinish(boolean result, String filePath) {
            Log.e("2018", "finish = " + result + "     filePath = " + filePath);
            if (mUpdateTipDialog.getApkInfo().getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                mUpdateTipDialog.setInstallApk(filePath);
                mUpdateTipDialog.show(mCurrentActivity.getFragmentManager(), "UpdateTipDialog");
            } else {
                if (mNotifyType == Apkinfo.PROGRESS_TYPE_DIALOG) {
                    mUpdateTipDialog.dismiss();
                } else {
                    NotificationUpdateManager.getInstance().dismiss();
                }
                ApkUtils.installApk(mCurrentActivity, filePath);
            }

        }
    }


}
