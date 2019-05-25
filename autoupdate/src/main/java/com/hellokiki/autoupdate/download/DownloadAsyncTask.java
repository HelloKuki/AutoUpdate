package com.hellokiki.autoupdate.download;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadAsyncTask extends AsyncTask<Void, Integer, String> {

    private DownloadListener mDownloadListener;

    private String mDownloadUrl;

    private String mSavePath;

    public DownloadAsyncTask(DownloadListener downloadListener, String downloadUrl, String savePath) {
        mDownloadListener = downloadListener;
        this.mDownloadUrl = downloadUrl;
        this.mSavePath = savePath;
    }

    @Override
    protected void onPostExecute(String s) {
        DownloadManager.getInstance().setToDownload(false);
        File file = new File(s);
        if (mDownloadListener != null) {
            if (file.exists() && file.length() > 0) {
                mDownloadListener.downloadFinish(true, s);
            } else {
                mDownloadListener.downloadFinish(false, s);
            }
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        DownloadManager.getInstance().setToDownload(true);
        mDownloadListener.downloadStart(mDownloadUrl);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        String fileName = mSavePath + "/updateApp" + System.currentTimeMillis() + ".apk";
        try {
            URL url = new URL(mDownloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.connect();
            int fileSize = connection.getContentLength();
            inputStream = connection.getInputStream();
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            long sum = 0L;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                sum += len;
                publishProgress((int) (sum * 100 / fileSize));
            }
            outputStream.flush();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (mDownloadListener != null) {
            mDownloadListener.downloadProgress(values[0]);
        }
    }
}
