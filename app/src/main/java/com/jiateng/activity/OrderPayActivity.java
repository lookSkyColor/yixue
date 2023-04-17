package com.jiateng.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.PayTask;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.adapter.ShoppingCartAdapter;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.ShoppingCart;
import com.jiateng.bean.Subject;
import com.jiateng.common.Constant;
import com.jiateng.common.utils.PayResult;
import com.jiateng.common.utils.PicassoUtil;
import com.jiateng.common.widget.AppTitleView;
import com.jiateng.db.impl.ShoppingCartImpl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OrderPayActivity extends Activity {


    @ViewInject(R.id.order_pay_title)
    private AppTitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_pay);
        ViewUtils.inject(this);


        titleView.onClickTitleListener(v -> {
            finish();
        });
    }



}