package com.jiateng.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jiateng.R;
import com.jiateng.common.widget.AppTitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.hutool.aop.interceptor.SpringCglibInterceptor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.login_title)
    private AppTitleView loginTitle;

    // 手机号输入框
    private EditText inputPhoneEt;

    // 验证码输入框
    private EditText inputCodeEt;

    // 获取验证码按钮
    private Button requestCodeBtn;

    // 登录按钮
    private Button commitBtn;

    //倒计时
    int i = 30;

    private Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ViewUtils.inject(this);
        init(); //初始化控件
        loginTitle.onClickTitleListener(v -> {
            finish();
        });
        timeButton = (Button) findViewById(R.id.login_request_code_btn);
        //new倒计时对象,总共的时间,每隔多少秒更新一次时间
        final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

        //设置Button点击事件触发倒计时
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!judgePhoneNums(inputPhoneEt.getText().toString())) {
                    return;
                }
                myCountDownTimer.start();
            }
        });

    }


    private void init() {
        inputPhoneEt = (EditText) findViewById(R.id.login_input_phone_et);
        inputCodeEt = (EditText) findViewById(R.id.login_input_code_et);
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);
        commitBtn = (Button) findViewById(R.id.login_commit_btn);
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();
        switch (v.getId()) {
            case R.id.login_request_code_btn:
// 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                //SMSSDK.getVerificationCode("86", phoneNums);


                break;

            case R.id.login_commit_btn:
//将收到的验证码和手机号提交再次核对
//                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
//                        .getText().toString());
//                createProgressBar();
                if (!judgePhoneNums(phoneNums)) {
                    return;
                }
                break;

        }
    }

    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }


    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            timeButton.setClickable(false);
            timeButton.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            timeButton.setText("重新获取");
            //设置可点击
            timeButton.setClickable(true);
        }
    }

}


//倒计时函数
