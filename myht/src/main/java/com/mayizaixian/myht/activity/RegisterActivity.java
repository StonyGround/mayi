package com.mayizaixian.myht.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayizaixian.myht.R;
import com.mayizaixian.myht.utils.CacheUtils;
import com.mayizaixian.myht.utils.CommonUtil;


public class RegisterActivity extends BaseActivity {

    /**
     * 登录
     */
    private TextView tv_register_login;
    private TextView tv_register_complete;
    private TextView tv_register_code;
    private EditText et_register_username;
    private EditText et_register_pwd;

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
                    if (!CommonUtil.isMobileNO(et_register_username.getText().toString())) {
                        CommonUtil.showToast(RegisterActivity.this, "手机号码格式不正确");
                        return;
                    }
                    break;
                case R.id.tv_register_complete:
                    if (!CommonUtil.isMobileNO(et_register_username.getText().toString())) {
                        CommonUtil.showToast(RegisterActivity.this, "手机号码格式不正确");
                        return;
                    }
                    if (TextUtils.isEmpty(et_register_pwd.getText().toString())) {
                        CommonUtil.showToast(RegisterActivity.this, "密码不能为空");
                        return;
                    }
                    register();
                    break;
                case R.id.ll_back:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void register() {
        String userName = et_register_username.getText().toString();
        String pwd = et_register_pwd.getText().toString();

        CacheUtils.setString(this, userName, pwd);

        CommonUtil.showToast(this,"注册成功");
        finish();
    }

    @Override
    protected View getChildView() {
        View view = View.inflate(this, R.layout.activity_register, null);
        tv_register_login = (TextView) view.findViewById(R.id.tv_register_login);
        tv_register_complete = (TextView) view.findViewById(R.id.tv_register_complete);
        tv_register_code = (TextView) view.findViewById(R.id.tv_register_code);
        et_register_username = (EditText) view.findViewById(R.id.et_register_username);
        et_register_pwd = (EditText) view.findViewById(R.id.et_register_pwd);

        tv_register_login.setOnClickListener(mOnClickListener);
        tv_register_complete.setOnClickListener(mOnClickListener);
        tv_register_code.setOnClickListener(mOnClickListener);

        return view;
    }

}
