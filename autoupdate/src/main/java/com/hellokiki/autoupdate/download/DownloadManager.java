package com.hellokiki.autoupdate.download;

public class DownloadManager {


    public void downloadApk(String url, DownloadListener listener) {

        DownloadAsyncTask asyncTask=new DownloadAsyncTask(listener,url,"");

    }


}
