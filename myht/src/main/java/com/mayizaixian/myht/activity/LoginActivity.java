package com.mayizaixian.myht.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;


import com.mayizaixian.myht.R;
import com.mayizaixian.myht.utils.CacheUtils;
import com.mayizaixian.myht.utils.CommonUtil;
import com.mayizaixian.myht.utils.ConstantUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_reg;
    private AutoCompleteTextView et_login_username;
    private EditText et_login_pwd;
    private TextView tv_login;
    private TextView tv_forget_password;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv_title.setText("登录");

        ll_back.setOnClickListener(this);

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
        if (!TextUtils.isEmpty(userName)) {
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
                if (!CommonUtil.isMobileNO(et_login_username.getText().toString())) {
                    CommonUtil.showToast(this,"手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(et_login_pwd.getText().toString())) {
                    CommonUtil.showToast(this,"密码不能为空");
                    return;
                }
                saveUserName();
                checkAccount();
                break;
            case R.id.tv_forget_password:
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void checkAccount() {
        String userName = et_login_username.getText().toString();
        String pwd = et_login_pwd.getText().toString();

        String sp_pwd = CacheUtils.getString(this, userName);

        if (TextUtils.isEmpty(sp_pwd)){
            CommonUtil.showToast(this,"用户不存在");
            return;
        }

        if(!sp_pwd.equals(pwd)){
            CommonUtil.showToast(this,"密码不正确");
            return;
        }

        CacheUtils.setBoolean(this,ConstantUtils.SP_IS_LOGIN,true);

        CommonUtil.showToast(this,"登录成功");

        finish();
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
}
