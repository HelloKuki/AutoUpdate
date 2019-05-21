package com.hellokiki.autoupdate.download;

public interface DownloadListener {

    void downloadStart(String url);

    void downloadProgress(Long progress);

    void downloadFinish(boolean result, String filePath);

}
