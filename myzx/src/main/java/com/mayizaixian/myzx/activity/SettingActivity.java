package com.mayizaixian.myzx.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.bean.ArticleBean;
import com.mayizaixian.myzx.bean.SettingBean;
import com.mayizaixian.myzx.dialog.DialogPicSelector;
import com.mayizaixian.myzx.dialog.DialogProgress;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.DataCleanManager;
import com.mayizaixian.myzx.utils.InitiView;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_setting_upload;
    private RelativeLayout rl_setting_code;
    private RelativeLayout rl_setting_qrcode;
    private RelativeLayout rl_setting_idcard;
    private RelativeLayout rl_setting_bank;
    private RelativeLayout rl_setting_loginpwd;
    private RelativeLayout rl_setting_withdrawpwd;
    private RelativeLayout rl_setting_findpwd;
    private RelativeLayout rl_setting_help;
    private RelativeLayout rl_setting_quit;
    private RelativeLayout rl_setting_about;
    private RelativeLayout rl_setting_cleancache;

    private TextView tv_setting_code;
    private TextView tv_user_idcard;
    private TextView tv_setting_bank;
    private TextView tv_setting_cache;
    private ImageView iv_setting_head;

    private DialogProgress progress;

    private Configuration configuration;

    private UploadManager uploadManager;

    private SettingBean settingBean;
    private SettingBean.DataBean settingData;

    String qiniu_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_title.setText("设置中心");
        ll_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected View getChildView() {

        View view = View.inflate(this, R.layout.activity_setting, null);

        rl_setting_upload = (RelativeLayout) view.findViewById(R.id.rl_setting_upload);
        rl_setting_code = (RelativeLayout) view.findViewById(R.id.rl_setting_code);
        rl_setting_qrcode = (RelativeLayout) view.findViewById(R.id.rl_setting_qrcode);
        rl_setting_idcard = (RelativeLayout) view.findViewById(R.id.rl_setting_idcard);
        rl_setting_bank = (RelativeLayout) view.findViewById(R.id.rl_setting_bank);
        rl_setting_loginpwd = (RelativeLayout) view.findViewById(R.id.rl_setting_loginpwd);
        rl_setting_withdrawpwd = (RelativeLayout) view.findViewById(R.id.rl_setting_withdrawpwd);
        rl_setting_findpwd = (RelativeLayout) view.findViewById(R.id.rl_setting_findpwd);
        rl_setting_help = (RelativeLayout) view.findViewById(R.id.rl_setting_help);
        rl_setting_quit = (RelativeLayout) view.findViewById(R.id.rl_setting_quit);
        rl_setting_about = (RelativeLayout) view.findViewById(R.id.rl_setting_about);
        rl_setting_cleancache = (RelativeLayout) view.findViewById(R.id.rl_setting_cleancache);

        tv_setting_code = (TextView) view.findViewById(R.id.tv_setting_code);
        tv_user_idcard = (TextView) view.findViewById(R.id.tv_user_idcard);
        tv_setting_bank = (TextView) view.findViewById(R.id.tv_setting_bank);
        tv_setting_cache = (TextView) view.findViewById(R.id.tv_setting_cache);
        iv_setting_head = (ImageView) view.findViewById(R.id.iv_setting_head);

//        setCacheSize();

        rl_setting_upload.setOnClickListener(this);
        rl_setting_code.setOnClickListener(this);
        rl_setting_qrcode.setOnClickListener(this);
        rl_setting_idcard.setOnClickListener(this);
        rl_setting_bank.setOnClickListener(this);
        rl_setting_loginpwd.setOnClickListener(this);
        rl_setting_withdrawpwd.setOnClickListener(this);
        rl_setting_findpwd.setOnClickListener(this);
        rl_setting_help.setOnClickListener(this);
        rl_setting_quit.setOnClickListener(this);
        rl_setting_about.setOnClickListener(this);
        rl_setting_cleancache.setOnClickListener(this);

        if (CacheUtils.getString(this, ConstantUtils.SP_SECRET).equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            getQiniuToken();
            getSettingData();
        }
        return view;
    }

    private void setCacheSize() {
        try {
            tv_setting_cache.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSettingData() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.SETTING);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("signature", CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_SIGNATURE));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getSettingData", result);
                settingBean = new Gson().fromJson(result, SettingBean.class);
                settingData = settingBean.getData();
                bindSettingData();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    private void bindSettingData() {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setUseMemCache(true)
                .setCircular(true)
                .build();
        x.image().bind(iv_setting_head, settingData.getUser_img(), imageOptions);
        tv_setting_code.setText(settingData.getInvite());
        tv_setting_bank.setText(settingData.getBank());
        tv_user_idcard.setText(settingData.getCard_id());
    }

    private void getQiniuToken() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.QINIU_TOKEN);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("signature", CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_SIGNATURE));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("getQiniuToken", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    qiniu_token = jsonObject.getString("uptoken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_setting_upload:
                DialogPicSelector dialogPicSelector = new DialogPicSelector(this);
                InitiView.initiBottomDialog(dialogPicSelector);
                dialogPicSelector.show();
                break;
            case R.id.rl_setting_code:
                startWebActivity(ConstantUtils.SETTING_CODE);
                break;
            case R.id.rl_setting_qrcode:
                startWebActivity(ConstantUtils.SETTING_QRCODE);
                break;
            case R.id.rl_setting_idcard:
                startWebActivity(ConstantUtils.SETTING_IDCARD);
                break;
            case R.id.rl_setting_bank:
                startWebActivity(ConstantUtils.SETTING_BANK);
                break;
            case R.id.rl_setting_loginpwd:
                startWebActivity(ConstantUtils.SETTING_LOGINPWD);
                break;
            case R.id.rl_setting_withdrawpwd:
                startWebActivity(ConstantUtils.SETTING_WITHDRAWPWD);
                break;
            case R.id.rl_setting_findpwd:
                startWebActivity(ConstantUtils.SETTING_FINDPWD);
                break;
            case R.id.rl_setting_help:
                startWebActivity(ConstantUtils.SETTING_HELP);
                break;
            case R.id.rl_setting_quit:
                CacheUtils.setString(SettingActivity.this, ConstantUtils.SP_SECRET, "");
                CacheUtils.setString(SettingActivity.this, ConstantUtils.SP_WEB_SIGNATURE, "");
                CacheUtils.setString(SettingActivity.this, ConstantUtils.SP_SIGNATURE, "");
                CommonUtils.showToast(this, "退出成功");
                finish();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_setting_about:
//                if (!CommonUtils.getVersion(this).equals(CacheUtils.getString(this, ConstantUtils.SP_VERSION))) {
//                    showHintDownloadDialog();
//                } else {
//                    CommonUtils.showToast(this, "已是最新版本!");
//                }
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.rl_setting_cleancache:
                DataCleanManager.cleanApplicationData(this);
                CommonUtils.showToast(this, "清除成功");
//                setCacheSize();
                break;
        }

    }


    private void startWebActivity(String url) {
        Intent intent = new Intent();
        intent.putExtra("url", ConstantUtils.HOST + url);
        intent.setClass(SettingActivity.this, WebActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case ConstantUtils.REQUEST_TAKE_PHOTO:
                        File temp = new File(Environment.getExternalStorageDirectory() + "/" + DialogPicSelector.imageDir);
                        DialogPicSelector.photoZoom(this, Uri.fromFile(temp));
                        break;
                    case ConstantUtils.REQUEST_PHOTO_ZOOM:
                        if (data != null)
                            DialogPicSelector.photoZoom(this, data.getData());
                        break;
                    case ConstantUtils.REQUEST_PHOTO_RESULT:
                        Log.e("onActivityResult", data.getData().toString() + "----");
                        if (data != null) {
                            progress = new DialogProgress(this);
                            progress.show();
                            Uri uri = data.getData();
                            Cursor c = getContentResolver().query(uri,
                                    new String[]{MediaStore.Images.Media.DATA}, null,
                                    null, null);
                            if (c != null && c.moveToFirst()) {
                                String filePath = c.getString(0);
                                Log.e("SettingActivity", "filepath" + filePath);
                                Log.e("SettingActivity", "qiniu_token" + qiniu_token);
                                configuration = new Configuration.Builder()
                                        .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                                        .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                                        .connectTimeout(10) // 链接超时。默认 10秒
                                        .responseTimeout(60) // 服务器响应超时。默认 60秒
//                .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
                                        .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
                                        .build();
                                // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
                                uploadManager = new UploadManager(configuration);

                                // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
                                UploadManager uploadManager = new UploadManager();
                                uploadManager.put(filePath, null, qiniu_token,
                                        new UpCompletionHandler() {
                                            @Override
                                            public void complete(String key, ResponseInfo info, JSONObject res) {
                                                //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                                                progress.dismiss();
                                                Toast.makeText(SettingActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                                Log.e("uploadHeadImage", "res----" + res);
                                                try {
                                                    key = res.getString("key");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                uploadHeadImage(key);
                                            }
                                        }, null);
                            }
                        }
                        break;
                }
                break;

        }
    }

    private void uploadHeadImage(String key) {
        Log.e("uploadHeadImage", "key----" + key);
        String token = CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_TOKEN);
        String secret = CacheUtils.getString(SettingActivity.this, ConstantUtils.SP_SECRET);
        String signature = CommonUtils.md5(CommonUtils.md5(token + key) + secret);
        Log.e("uploadHeadImage", "token----" + token);
        Log.e("uploadHeadImage", "secret----" + secret);
        Log.e("uploadHeadImage", "signature----" + signature);
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.UPLOAD_KEY);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", token);
        params.addBodyParameter("signature", signature);
        params.addBodyParameter("path", key);
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("uploadHeadImage", result + "-------");
                getSettingData();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("uploadHeadImage", "error----" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
