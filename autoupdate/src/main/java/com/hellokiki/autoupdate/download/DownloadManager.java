package com.hellokiki.autoupdate.download;

import com.hellokiki.autoupdate.UpdateManager;

public class DownloadManager {

    private static DownloadManager sDownloadManager = new DownloadManager();

    private boolean toDownload;

    public static DownloadManager getInstance() {
        return sDownloadManager;
    }

    public void downloadApk(String url, DownloadListener listener) {
        DownloadAsyncTask asyncTask = new DownloadAsyncTask(listener, url, UpdateManager.getInstance().getSavePath());
        asyncTask.execute();
    }

    public boolean isToDownload() {
        return toDownload;
    }

    public void setToDownload(boolean toDownload) {
        this.toDownload = toDownload;
    }


}
