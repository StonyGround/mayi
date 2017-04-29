package com.mayizaixian.myzx.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.DataCleanManager;

import java.io.File;

public class AboutActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 检查升级成功
     */
    protected static final int WHAT_REQUEST_UPDATE_SUCCESS = 3;

    /**
     * 下载apk成功
     */
    private static final int WHAT_DOWNLOAD_SUCCESS = 5;

    /**
     * 下载apk失败
     */
    private static final int WHAT_DOWNLOAD_ERROR = 6;

    private RelativeLayout rl_about_update;
    private RelativeLayout rl_about_cleancache;

    private TextView tv_about_version;

    private File apkFile;

    /**
     * 升级提示Dialog
     */
    private ProgressDialog pd;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_DOWNLOAD_SUCCESS:
                    pd.dismiss();
                    installApk();
                    break;
                case WHAT_DOWNLOAD_ERROR:
                    pd.dismiss();
                    CommonUtils.showMessage(getApplicationContext(), "下载apk失败");
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("关于我们");
        ll_back.setOnClickListener(this);
    }

    @Override
    protected View getChildView() {
        View view = View.inflate(this, R.layout.activity_about, null);

        rl_about_update = (RelativeLayout) view.findViewById(R.id.rl_about_update);
        rl_about_cleancache = (RelativeLayout) view.findViewById(R.id.rl_about_cleancache);
        tv_about_version = (TextView) view.findViewById(R.id.tv_about_version);

        tv_about_version.setText("V" + CommonUtils.getVersion(this));

        rl_about_update.setOnClickListener(this);
        rl_about_cleancache.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about_update:
                if (!CommonUtils.getVersion(this).equals(CacheUtils.getString(this, ConstantUtils.SP_VERSION))) {
                    showHintDownloadDialog();
                } else {
                    CommonUtils.showToast(this, "已是最新版本!");
                }
                break;
            case R.id.rl_about_cleancache:
                DataCleanManager.cleanApplicationData(this);
                CommonUtils.showToast(this, "清除成功");
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 更新Dialog
     */
    private void showHintDownloadDialog() {
        new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(CacheUtils.getString(AboutActivity.this, ConstantUtils.SP_VERSION_DESC))
                .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startDownloadApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 下载apk
     */
    private void startDownloadApk() {
        apkFile = new File(getExternalFilesDir(null), "myzx.apk");
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setProgressNumberFormat("%1d kb/%2d kb");
        pd.show();

        new Thread() {
            public void run() {
                try {
                    CommonUtils.downloadApk(CacheUtils.getString(AboutActivity.this, ConstantUtils.SP_VERSION_URL), apkFile, pd);
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_SUCCESS);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    handler.sendEmptyMessage(WHAT_DOWNLOAD_ERROR);
                }
            }
        }.start();
    }

    /**
     * 安装apk文件
     */
    private void installApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
