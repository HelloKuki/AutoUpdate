package com.hellokiki.autoupdate.dialog;


import android.app.DialogFragment;

import com.hellokiki.autoupdate.UpdateManager;
import com.hellokiki.autoupdate.bean.Apkinfo;

public abstract class BaseUpdateDialog extends DialogFragment {

    protected Apkinfo mApkInfo;

    private DialogEventListener mEventListener;

    private String mFilePath;

    public void setApkInfo(Apkinfo info) {
        this.mApkInfo = info;
    }

    public Apkinfo getApkInfo() {
        return mApkInfo;
    }

    public void setEventListener(DialogEventListener eventListener) {
        mEventListener = eventListener;
    }

    /**
     * 更新下载进度
     */
    public abstract void setProgress(int progress);

    /**
     * 切换下载状态
     */
    public abstract void showDownloadProgressStatus();

    /**
     * 切换到安装apk状态（静默下载完成）
     */
    public void setInstallApk(String filePath) {
        mFilePath = filePath;
    }


    /**
     * 确定按钮点击事件
     */
    protected void updateButtonClick() {
        if (mEventListener != null) {
            if (mApkInfo.getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                mEventListener.installApk(mFilePath);
            } else {
                mEventListener.updateButtonClick(mApkInfo);
            }
        }
    }

    /**
     * 取消按钮点击事件
     */
    protected void cancelButtonClick() {
        if (mEventListener != null) {
            mEventListener.cancelButtonClick();
        }
    }

}
