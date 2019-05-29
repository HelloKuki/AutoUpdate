package com.hellokiki.autoupdate;

import com.hellokiki.autoupdate.bean.Apkinfo;

public interface JsonParseListener {

    Apkinfo parse(String json);

    boolean resultCodeIsTrue(String json);

}
