package com.hellokiki.updateexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hellokiki.autoupdate.UpdateManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateManager.getInstance().checkUpdate();
    }
}
