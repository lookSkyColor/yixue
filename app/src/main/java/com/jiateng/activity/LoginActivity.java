package com.jiateng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.UserInfo;
import com.jiateng.common.utils.MockData;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.jiateng.common.widget.AppTitleView;
import com.jiateng.fragment.UserFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.annotation.NotNull;
import com.lidroid.xutils.view.annotation.ViewInject;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.aop.interceptor.SpringCglibInterceptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    private EditText edit_input;
    private Button btn_send;
    private String inputContent;

    private FragmentManager manager;
    private FragmentTransaction transaction;


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

                //提交键值对需要用到FormBody,FormBody继承自RequestBody
                FormBody formBody = new FormBody.Builder()
                        //添加键值对(通多Key-value的形式添加键值对参数)
                        .add("iphone", inputPhoneEt.getText().toString())
                        .build();
                final Request request = new Request.Builder()
                        .post(formBody)
                        .url("http://192.168.0.128:8080/message/getMessageCode")
                        .build();

                OkHttpClient client = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true) //开启连接失败时重连逻辑
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call,  IOException e) {
                        Log.e("TAG", "Post请求(键值对)异步响应failure==" + e.getMessage());
                    }

                    @Override
                    public void onResponse( Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.e("TAG", "Post请求(键值对)异步响应Success==" + result);
                    }
                });
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
        String code = inputCodeEt.getText().toString();
        if (!judgePhoneNums(phoneNums)) {
            return;
        }
        if (!judgeCode(code)) {
            return;
        }

        RequestBody requestBody = RequestBody.create("{"+"\"iphone\":"+phoneNums+","+"\"code\":"+code+"}", MediaType.parse("application/json; charset=utf-8"));
        final Request request = new Request.Builder()
                .post(requestBody)
                .url("http://192.168.0.128:8080/login/login")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true) //开启连接失败时重连逻辑
                            .build();
                    Response response = client.newCall(request).execute();
                      if (response.isSuccessful()) {
                          String s = response.body().string();
                          Log.e("TAG", "Post请求String同步响应success==" + s);

                        Gson gson = new Gson();
                        JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<UserInfo>>() {}.getType());


                        if (0 != jsonBean.getCode()){
                            Toast.makeText(LoginActivity.this, jsonBean.getMsg(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                          UserInfo userInfo = (UserInfo) jsonBean.getResult();
                          SharedPreferencesUtil.putLong(LoginActivity.this,"userId",userInfo.getId());
                          if (0 != userInfo.getCorporationId()){
                              SharedPreferencesUtil.putInt(LoginActivity.this,"corporationId",userInfo.getCorporationId());
                          }
                          if (0 != userInfo.getRoleId()){
                              SharedPreferencesUtil.putInt(LoginActivity.this,"roleId",userInfo.getRoleId());
                          }

                          Intent intent = new Intent();
                          //setClass函数的第一个参数是一个Context对象
                          //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                          //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                          intent.setClass(LoginActivity.this, MainActivity.class);
                          startActivity(intent);

                      } else {
                        Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "Post请求String同步响应failure==" + e.getMessage());
                }
            }
        }).start();


    }

    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }


    private  boolean judgeCode(String code){
        if (isMatchLength(code, 4)) {
            return true;
        }
        Toast.makeText(this, "验证码输入有误！", Toast.LENGTH_SHORT).show();
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
