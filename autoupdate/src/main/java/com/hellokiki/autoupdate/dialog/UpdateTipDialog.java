package com.hellokiki.autoupdate.dialog;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hellokiki.autoupdate.R;
import com.hellokiki.autoupdate.bean.Apkinfo;

public class UpdateTipDialog extends BaseUpdateDialog implements View.OnClickListener {


    private TextView mTextViewTitle;
    private TextView mTextViewContent;
    private TextView mTextViewProgress;
    private LinearLayout mLinearLayoutButton;
    private ProgressBar mProgressBar;
    private View mViewLine;
    private TextView mButtonOk;
    private TextView mButtonCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.layout_dialog_update, null);

        mTextViewTitle = view.findViewById(R.id.tv_title);
        mTextViewProgress = view.findViewById(R.id.tv_progress);
        mLinearLayoutButton = view.findViewById(R.id.ll_button);
        mProgressBar = view.findViewById(R.id.progress);
        mViewLine = view.findViewById(R.id.view_line);

        if (mApkInfo != null) {
            mTextViewContent = view.findViewById(R.id.tv_tip_content);
            String builder = "版本号：" +
                    mApkInfo.getVersionName() +
                    "\n" +
                    "更新内容：" +
                    mApkInfo.getUpdateContent();
            mTextViewContent.setText(builder);
            mButtonOk = view.findViewById(R.id.btn_update);
            mButtonCancel = view.findViewById(R.id.btn_cancel);
            mButtonOk.setOnClickListener(this);
            mButtonCancel.setOnClickListener(this);
            if (mApkInfo.getUpdateType() == Apkinfo.UPDATE_TYPE_MANDATORY) {
                mButtonCancel.setVisibility(View.GONE);
            } else if (mApkInfo.getUpdateType() == Apkinfo.UPDATE_TYPE_WIFI) {
                mButtonCancel.setVisibility(View.GONE);
                mButtonOk.setText("安装");
                mTextViewTitle.setText("更新（已下载完成）");
            }

        }
        return view;
    }

    @Override
    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        mTextViewProgress.setText(progress + "");
    }

    @Override
    public void showDownloadProgressStatus() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewProgress.setVisibility(View.VISIBLE);
        mTextViewContent.setVisibility(View.GONE);
        mLinearLayoutButton.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
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
            updateButtonClick();
        } else if (v.getId() == R.id.btn_cancel) {
            cancelButtonClick();
        }
    }

}
