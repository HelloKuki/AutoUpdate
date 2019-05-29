package com.hellokiki.autoupdate;

import android.os.AsyncTask;
import android.util.Log;

import com.hellokiki.autoupdate.bean.Apkinfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

public class CheckUpdateAsyncTask extends AsyncTask<Void, Void, String> {

    private CheckUpdateListener mUpdateListener;

    private long mCurrentVersionCode;

    private String mUrl;

    private HashMap<String, String> mMap;

    private JsonParseListener mListener;

    CheckUpdateAsyncTask(String url, HashMap<String, String> map, long currentVersionCode, CheckUpdateListener updateListener) {
        mUrl = url;
        this.mMap = map;
        mCurrentVersionCode = currentVersionCode;
        mUpdateListener = updateListener;
        mListener = new JsonParseListener() {
            @Override
            public Apkinfo parse(String json) {
                Apkinfo apkinfo = new Apkinfo();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    apkinfo.setName(jsonObject.getJSONObject("data").getString("app_name"));
                    apkinfo.setMinSdkVersion(jsonObject.getJSONObject("data").getString("min_sdk_version"));
                    apkinfo.setPackageName(jsonObject.getJSONObject("data").getString("package_name"));
                    apkinfo.setUpdateContent(jsonObject.getJSONObject("data").getString("update_content"));
                    apkinfo.setUpdateType(jsonObject.getJSONObject("data").getInt("update_type"));
                    apkinfo.setUrl(jsonObject.getJSONObject("data").getString("download_url"));
                    apkinfo.setVersionCode(jsonObject.getJSONObject("data").getString("version_code"));
                    apkinfo.setVersionName(jsonObject.getJSONObject("data").getString("version_name"));
                    apkinfo.setProgressNotifyType(jsonObject.getJSONObject("data").getInt("progress_notify_type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return apkinfo;
            }

            @Override
            public boolean resultCodeIsTrue(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    return jsonObject.getInt("code") == 1000;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    CheckUpdateAsyncTask(String url, HashMap<String, String> map, long currentVersionCode, CheckUpdateListener updateListener, JsonParseListener jsonParseListener) {
        mUrl = url;
        this.mMap = map;
        mCurrentVersionCode = currentVersionCode;
        mUpdateListener = updateListener;
        this.mListener = jsonParseListener;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            if (mUpdateListener != null) {
                Log.e("2018", "response = " + s);
                if (mListener.resultCodeIsTrue(s)) {
                    Apkinfo apkinfo = mListener.parse(s);
                    mUpdateListener.checkSuccess(apkinfo, Long.parseLong(apkinfo.getVersionCode()) > mCurrentVersionCode);
                } else {
                    mUpdateListener.checkFail(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            Log.e("2018", "开始请求");
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);

            Iterator iterator = mMap.keySet().iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                builder.append(key).append("=").append(mMap.get(key));
                builder.append("&");
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            bufferedOutputStream.write(builder.toString().getBytes(StandardCharsets.UTF_8));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            Log.e("2018", "connection.getResponseCode() = " + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                Log.e("2018", "请求完成");
                String result = outputStream.toString();
                connection.disconnect();
                return result;
            }

        } catch (Exception e) {
            Log.e("2018", "请求异常");
            e.printStackTrace();
        } finally {
            Log.e("2018", "请求finally");
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
