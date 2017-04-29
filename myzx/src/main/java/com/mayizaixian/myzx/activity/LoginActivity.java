package com.mayizaixian.myzx.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mayizaixian.myzx.R;
import com.mayizaixian.myzx.bean.LoginBean;
import com.mayizaixian.myzx.utils.CacheUtils;
import com.mayizaixian.myzx.utils.CommonUtils;
import com.mayizaixian.myzx.utils.ConstantUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_reg;
    private AutoCompleteTextView et_login_username;
    private EditText et_login_pwd;
    private TextView tv_login;
    private LoginBean loginBean;
    private TextView tv_forget_password;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_title.setText("登录");

        ll_back.setOnClickListener(this);

        url = getIntent().getStringExtra("url");
    }

    @Override
    protected View getChildView() {
        View view = View.inflate(this, R.layout.activity_login, null);
        tv_reg = (TextView) view.findViewById(R.id.tv_reg);
        et_login_username = (AutoCompleteTextView) view.findViewById(R.id.et_login_username);
        et_login_pwd = (EditText) view.findViewById(R.id.et_login_pwd);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_forget_password = (TextView) view.findViewById(R.id.tv_forget_password);

        tv_reg.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);

        String userName = CacheUtils.getString(this, ConstantUtils.SP_USER_NAME);
        Log.e("LoginActivity", "userName---" + userName);
        if (!userName.equals("")) {
            String[] userNameArr = userName.split(",");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNameArr);
            et_login_username.setAdapter(adapter);
            et_login_username.setThreshold(1);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reg:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_login:
                if (!CommonUtils.isMobileNO(et_login_username.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_login_pwd.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLoginData();
                break;
            case R.id.tv_forget_password:
                startActivity(new Intent(this, RetrieveActivity.class));
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    public void getLoginData() {
        RequestParams params = new RequestParams(ConstantUtils.HOST + ConstantUtils.LOGIN);
        params.addHeader("User-Agent", "MAYI_ZX_ANDROID");
        params.addBodyParameter("access_token", CacheUtils.getString(LoginActivity.this, ConstantUtils.SP_TOKEN));
        params.addBodyParameter("phone", et_login_username.getText().toString());
        params.addBodyParameter("password", et_login_pwd.getText().toString());
        Log.e("getLoginData", "access_token" + CacheUtils.getString(LoginActivity.this, ConstantUtils.SP_TOKEN));
        x.http().request(HttpMethod.POST, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("getLoginData", result + "-----");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("data");
                    if (status == 200) {
                        loginBean = new Gson().fromJson(result, LoginBean.class);

                        String uid = loginBean.getData().getUid();
                        String keep_code = loginBean.getData().getKeep_code();
                        String secret = loginBean.getData().getSecret();

                        String token = CacheUtils.getString(LoginActivity.this, ConstantUtils.SP_TOKEN);
                        String web_signature = CommonUtils.md5(CommonUtils.md5(token + uid) + secret);
                        String signature = CommonUtils.md5(CommonUtils.md5(token) + secret);

                        Log.e("LoginActivity", "uid" + uid);
                        Log.e("LoginActivity", "token--" + token);
                        Log.e("LoginActivity", "secret--" + secret);
                        Log.e("LoginActivity", "CommonUtils.md5(token + uid)--" + CommonUtils.md5(token + uid));
                        Log.e("LoginActivity", "web_signature--" + web_signature);

                        CacheUtils.setString(LoginActivity.this, ConstantUtils.SP_UID, uid);
                        CacheUtils.setString(LoginActivity.this, ConstantUtils.SP_SECRET, secret);
                        CacheUtils.setString(LoginActivity.this, ConstantUtils.SP_KEEP_CODE, keep_code);
                        CacheUtils.setString(LoginActivity.this, ConstantUtils.SP_WEB_SIGNATURE, web_signature);
                        CacheUtils.setString(LoginActivity.this, ConstantUtils.SP_SIGNATURE, signature);

                        saveUserName();

                        if (url != null) {
                            Log.e("LoginActivity", "url--" + url);
                            Intent intent = new Intent();
                            intent.putExtra("url", url);
                            LoginActivity.this.setResult(RESULT_OK, intent);
                        }
                        LoginActivity.this.finish();
                    } else if (status == 300) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
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

    private void saveUserName() {
        String sUserName = CacheUtils.getString(this, ConstantUtils.SP_USER_NAME);
        if (!sUserName.contains(",")) {
            sUserName = "";
        }
        if (!sUserName.contains(et_login_username.getText().toString())) {
            CacheUtils.setString(this, ConstantUtils.SP_USER_NAME, sUserName + et_login_username.getText().toString() + ",");
        }
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
