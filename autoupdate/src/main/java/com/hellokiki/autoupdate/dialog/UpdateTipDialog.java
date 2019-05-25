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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hellokiki.autoupdate.R;
import com.hellokiki.autoupdate.UpdateManager;
import com.hellokiki.autoupdate.bean.Apkinfo;

public class UpdateTipDialog extends DialogFragment implements View.OnClickListener {


    private Apkinfo mApkinfo;

    private TextView mTextViewContent;
    private TextView mTextViewProgress;
    private LinearLayout mLinearLayoutButton;
    private ProgressBar mProgressBar;
    private View mViewLine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_update, null);

        mTextViewProgress = view.findViewById(R.id.tv_progress);
        mLinearLayoutButton = view.findViewById(R.id.ll_button);
        mProgressBar = view.findViewById(R.id.progress);
        mViewLine = view.findViewById(R.id.view_line);

        if (mApkinfo != null) {
            mTextViewContent = view.findViewById(R.id.tv_tip_content);
            mTextViewContent.setText(mApkinfo.getUpdateContent());
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

    public void setDownloadStatus() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewProgress.setVisibility(View.VISIBLE);
        mTextViewContent.setVisibility(View.GONE);
        mLinearLayoutButton.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        mTextViewProgress.setText(progress + "");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
            getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update) {
            if (mApkinfo != null) {
                UpdateManager.getInstance().startDownloadApk(mApkinfo.getUrl());
                if (mApkinfo.getProgressNotifyType() == Apkinfo.PROGRESS_TYPE_DIALOG) {
                    setDownloadStatus();
                } else {
                    dismiss();
                }
            } else {
                dismiss();
            }

        } else if (v.getId() == R.id.btn_cancel) {
            dismiss();
        }
    }
}
