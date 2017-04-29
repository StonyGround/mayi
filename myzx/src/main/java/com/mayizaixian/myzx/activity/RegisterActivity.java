package com.mayizaixian.myzx.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.mayizaixian.myzx.utils.MessageTimer;
import com.mayizaixian.myzx.utils.SMSContent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class RegisterActivity extends BaseActivity {

    /**
     * 登录
     */
    private TextView tv_register_login;
    private TextView tv_register_complete;
    private TextView tv_register_code;
    private EditText et_register_username;
    private EditText et_register_pwd;
    private EditText et_register_code;
    private EditText et_pwd_again;
    private SMSContent smsContent;
    private EditText et_register_invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_title.setText("用户注册");
        ll_back.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_register_login:
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    break;
                case R.id.tv_register_code:
                    if (!CommonUtils.isMobileNO(et_register_username.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendCode();
                    break;
                case R.id.tv_register_complete:
                    if (!CommonUtils.isMobileNO(et_register_username.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_register_code.getText().toString().equals("")) {
                        Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_register_pwd.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isCheck) {
                        Toast.makeText(RegisterActivity.this, "注册用户需阅读并同意网站协议", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    register();
                    break;
                case R.id.ll_back:
                    finish();
                    break;
                case R.id.tv_register_agreement:
                    Intent intent = new Intent();
                    intent.putExtra("url", ConstantUtils.AGREEMENT);
                    intent.setClass(RegisterActivity.this, WebActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_register_agree:
                    Log.e("RegisterActivity", "isCheck----------" + isCheck);
                    if (isCheck) {
                        iv_register_agree.setImageResource(R.drawable.complete);
                        isCheck = false;
                    } else {
                        iv_register_agree.setImageResource(R.drawable.complete_clicked);
                        isCheck = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void sendCode() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.SEND_CODE);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(RegisterActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("phone", et_register_username.getText().toString());
        Log.e("sendCode", "access_token" + CacheUtils.getString(RegisterActivity.this, ConstantUtils.SP_TOKEN));
        Log.e("sendCode", "phone" + et_register_username.getText().toString());
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("sendCode", result + "------");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");

                    if (status == 200) {
                        Toast.makeText(RegisterActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                        new MessageTimer(tv_register_code, 90000, 1000).start();

                    } else if (status == 400) {
                        String message = jsonObject.getString("message");
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, ex.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void register() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.REGISTER);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(RegisterActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("phone", et_register_username.getText().toString());
        params.addBodyParameter("password", et_register_pwd.getText().toString());
        params.addBodyParameter("captcha", et_register_code.getText().toString());
//        params.addBodyParameter("pwd", et_pwd_again.getText().toString());
        params.addBodyParameter("pws",et_register_invite.getText().toString());
        Log.e("sendCode", "access_token" + CacheUtils.getString(RegisterActivity.this, ConstantUtils.SP_TOKEN));
        Log.e("sendCode", "phone" + et_register_username.getText().toString());
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("register", result + "------");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 200) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (status == 300) {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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

    private TextView tv_register_agreement;
    private ImageView iv_register_agree;
    private boolean isCheck = true;

    @Override
    protected View getChildView() {
        View view = View.inflate(this, R.layout.activity_register, null);
        tv_register_login = (TextView) view.findViewById(R.id.tv_register_login);
        tv_register_complete = (TextView) view.findViewById(R.id.tv_register_complete);
        tv_register_code = (TextView) view.findViewById(R.id.tv_register_code);
        et_register_username = (EditText) view.findViewById(R.id.et_register_username);
        et_register_pwd = (EditText) view.findViewById(R.id.et_register_pwd);
        et_pwd_again = (EditText) view.findViewById(R.id.et_pwd_again);
        et_register_code = (EditText) view.findViewById(R.id.et_register_code);
        tv_register_agreement = (TextView) view.findViewById(R.id.tv_register_agreement);
        iv_register_agree = (ImageView) view.findViewById(R.id.iv_register_agree);
        et_register_invite= (EditText) view.findViewById(R.id.et_register_invite);

        iv_register_agree.setOnClickListener(mOnClickListener);
        tv_register_agreement.setOnClickListener(mOnClickListener);
        tv_register_login.setOnClickListener(mOnClickListener);
        tv_register_complete.setOnClickListener(mOnClickListener);
        tv_register_code.setOnClickListener(mOnClickListener);

        smsContent = new SMSContent(new Handler(), RegisterActivity.this, et_register_code);
        RegisterActivity.this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContent);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(smsContent);
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
}
