package com.hellokiki.autoupdate.dialog;

import com.hellokiki.autoupdate.bean.Apkinfo;

public interface DialogEventListener {

    void updateButtonClick(Apkinfo apkinfo);

    void cancelButtonClick();

    void installApk(String filePath);

}
