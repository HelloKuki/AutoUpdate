package com.hellokiki.autoupdate.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.hellokiki.autoupdate.R;
import com.hellokiki.autoupdate.UpdateManager;
import com.hellokiki.autoupdate.bean.Apkinfo;

public class UpdateTipDialog extends DialogFragment implements View.OnClickListener {


    private Apkinfo mApkinfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_update, null);
        if (mApkinfo != null) {
            ((TextView) view.findViewById(R.id.tv_tip_content)).setText(mApkinfo.getUpdateContent());
            view.findViewById(R.id.btn_update).setOnClickListener(this);
            view.findViewById(R.id.btn_cancel).setOnClickListener(this);
            if (mApkinfo.getUpdateType() == Apkinfo.UPDATE_TYPE_MANDATORY) {
                view.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
            }
        }
        return view;
    }

    public void setApkinfo(Apkinfo apkinfo) {
        this.mApkinfo = apkinfo;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.btn_update) {
            if (mApkinfo != null)
                UpdateManager.getInstance().startDownloadApk(mApkinfo.getUrl());
        } else if (v.getId() == R.id.btn_cancel) {

        }
    }
}
