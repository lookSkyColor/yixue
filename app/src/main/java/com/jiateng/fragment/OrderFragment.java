package com.jiateng.fragment;



import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.activity.GoodsActivity;
import com.jiateng.activity.LoginActivity;
import com.jiateng.activity.OrderInfoActivity;
import com.jiateng.activity.ShopActivity;
import com.jiateng.adapter.HomeFragmentAdapter;
import com.jiateng.adapter.OrderAdapter;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.Order;
import com.jiateng.bean.OrderListType;
import com.jiateng.bean.School;
import com.jiateng.common.Constant;
import com.jiateng.common.base.BaseFragment;
import com.jiateng.common.utils.RetrofitUtils;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description:
 * @Title: OrderFragment
 * @ProjectName: orderFood
 * @date: 2023/1/10 17:44
 * @author: 骆家腾
 */
public class OrderFragment extends BaseFragment implements OrderAdapter.OrderItemClickCallBack {
    @ViewInject(R.id.list_item_order_history)
    private ListView orderListView;
    private ArrayList<OrderListType> orders;
    private RetrofitUtils retrofitUtils;
    private OrderAdapter adapter;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_order, null);
        ViewUtils.inject(this, view);
        retrofitUtils = RetrofitUtils.getInstance();

        return view;
    }

    @Override
    protected void initData() {
        super.initData();

        long userId = SharedPreferencesUtil.getLong(context, "userId", 0L);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("userId", String.valueOf(userId));
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .post(formBody).header("token", SharedPreferencesUtil.getString(context,"token",""))
                .url(Constant.QUERY_ORDER_LIST_URL)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //开启连接失败时重连逻辑
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("TAG", "onFailure" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String s = response.body().string();
                    Log.e("TAG", "Post请求String同步响应success==" + s);

                    Gson gson = new Gson();
                    JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<List<OrderListType>>>() {
                    }.getType());
                    if (0 != jsonBean.getCode()){
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                     orders = (ArrayList<OrderListType>) jsonBean.getResult();
                    new Thread(() -> {
                             //do something takes long time in the work-thread
                            runOnUiThread(() -> {
                                orderListView.setAdapter(new OrderAdapter(orders, OrderFragment.this));
                                orderListView.setFriction(ViewConfiguration.getScrollFriction() * 3);
                                });
                    }).start();

                    //orderListView.post(() -> adapter.notifyDataSetChanged());
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                }
            }
        });


//        setListener();
    }


   /* @Override
    public void clickShopName(View view) {
        int index = (int) view.getTag();
        OrderListType order = orders.get(index);
        startActivity(new Intent(context, ShopActivity.class));
    }*/

    @Override
    public void clickOrderInfo(View view) {
        int index = (int) view.getTag();
        OrderListType order = orders.get(index);
        //TODO 设置Intent传递商店信息到下一个页面
        Intent intent = new Intent(context, OrderInfoActivity.class);
        intent.putExtra("orderListType", order);
        startActivity(intent);
    }



    public final void runOnUiThread(Runnable action) {
            if (Thread.currentThread() != mUiThread) {
                mHandler.post(action);
                 } else {
                     action.run();
                 }
         }

    final Handler mHandler = new Handler();
    private Thread mUiThread;

}
