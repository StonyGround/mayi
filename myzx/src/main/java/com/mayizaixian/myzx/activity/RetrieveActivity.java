package com.mayizaixian.myzx.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.MessageTimer;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RetrieveActivity extends BaseActivity implements View.OnClickListener {
    private static final int SEND_CODE_AGAIN = 1;

    private EditText et_retrieve_username;
    private EditText et_retrieve_code;
    private TextView tv_retrieve_code;
    private TextView tv_retrieve_complete;
    private EditText et_retrieve_pwd;
    private EditText et_retrieve_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_title.setText("找回密码");

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
        View view = View.inflate(this, R.layout.activity_retrieve, null);
        et_retrieve_username = (EditText) view.findViewById(R.id.et_retrieve_username);
        et_retrieve_code = (EditText) view.findViewById(R.id.et_retrieve_code);
        tv_retrieve_code = (TextView) view.findViewById(R.id.tv_retrieve_code);
        tv_retrieve_complete = (TextView) view.findViewById(R.id.tv_retrieve_complete);
        et_retrieve_pwd = (EditText) view.findViewById(R.id.et_retrieve_pwd);
        et_retrieve_again = (EditText) view.findViewById(R.id.et_retrieve_again);

        tv_retrieve_code.setOnClickListener(this);
        tv_retrieve_complete.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_retrieve_code:
                if (!CommonUtils.isMobileNO(et_retrieve_username.getText().toString())) {
                    Toast.makeText(RetrieveActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendCode();

                break;
            case R.id.tv_retrieve_complete:
                if (!CommonUtils.isMobileNO(et_retrieve_username.getText().toString())) {
                    Toast.makeText(RetrieveActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_retrieve_code.getText().toString())) {
                    Toast.makeText(RetrieveActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_retrieve_pwd.getText().toString()) || TextUtils.isEmpty(et_retrieve_again.getText().toString())) {
                    Toast.makeText(RetrieveActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                retrieve();
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void retrieve() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.RETRIEVE);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(RetrieveActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("phone", et_retrieve_username.getText().toString());
        params.addBodyParameter("password", et_retrieve_pwd.getText().toString());
        params.addBodyParameter("captcha", et_retrieve_code.getText().toString());
        params.addBodyParameter("pwd", et_retrieve_again.getText().toString());
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("data");
                    if (status == 200) {
                        Toast.makeText(RetrieveActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (status == 300 || status == 400) {
                        Toast.makeText(RetrieveActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RetrieveActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
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

    private void sendCode() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.FIND_PWD_CODE);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(RetrieveActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("phone", et_retrieve_username.getText().toString());
        Log.e("sendCode", "access_token" + CacheUtils.getString(RetrieveActivity.this, ConstantUtils.SP_TOKEN));
        Log.e("sendCode", "phone" + et_retrieve_username.getText().toString());
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("sendCode", result + "------");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        Toast.makeText(RetrieveActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        new MessageTimer(tv_retrieve_code, 90000, 1000).start();
                    } else if (status == 400) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(RetrieveActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RetrieveActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("RetrieveActivity", ex.toString() + "----------");
                Toast.makeText(RetrieveActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
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
