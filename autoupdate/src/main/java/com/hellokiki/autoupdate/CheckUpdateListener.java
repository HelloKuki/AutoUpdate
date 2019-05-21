package com.hellokiki.autoupdate;

import com.hellokiki.autoupdate.bean.Apkinfo;

public interface CheckUpdateListener {

    void checkSuccess(Apkinfo apkinfo);

    void checkFail(String error);
}
