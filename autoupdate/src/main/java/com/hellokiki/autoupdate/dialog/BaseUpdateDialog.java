package com.hellokiki.autoupdate.dialog;


import android.app.DialogFragment;

import com.hellokiki.autoupdate.UpdateManager;
import com.hellokiki.autoupdate.bean.Apkinfo;

public abstract class BaseUpdateDialog extends DialogFragment {

    protected Apkinfo mApkInfo;

    public void setApkInfo(Apkinfo info) {
        this.mApkInfo = info;
    }

    /**
     * 更新下载进度
     */
    public abstract void setProgress(int progress);

    /**
     * 切换下载状态
     */
    public abstract void showDownloadProgress();

    /**
     * 确定按钮点击事件
     */
    protected void updateButtonClick() {
        if (mApkInfo != null) {
            UpdateManager.getInstance().startDownloadApk(mApkInfo.getUrl());
            if (mApkInfo.getProgressNotifyType() == Apkinfo.PROGRESS_TYPE_DIALOG) {
                showDownloadProgress();
            } else {
                dismiss();
            }
        } else {
            dismiss();
        }
    }

    /**
     * 取消按钮点击事件
     */
    protected void cancelButtonClick() {
        dismiss();
    }

}
