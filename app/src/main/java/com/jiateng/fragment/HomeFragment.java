package com.jiateng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.activity.GoodsActivity;
import com.jiateng.activity.LoginActivity;
import com.jiateng.activity.ShopActivity;
import com.jiateng.adapter.HomeFragmentAdapter;
import com.jiateng.bean.BannerInfo;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.School;
import com.jiateng.common.Constant;
import com.jiateng.common.base.BaseFragment;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Description:
 * @Title: HomeFragment
 * @ProjectName: orderFood
 * @date: 2023/1/10 17:44
 * @author: 骆家腾
 */
public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    private HomeFragmentAdapter adapter;

    private ArrayList<School> schools;
    private ArrayList<String> mBannerList;

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_home, null);
        ViewUtils.inject(this, view);
        initRecycleView();
        return view;
    }

    @Override
    protected void initData() {
        initBannerData();
        long userId = SharedPreferencesUtil.getLong(context, "userId", 0L);
        if (0 != userId) {
            getSchoolData(userId);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initRecycleView() {
        adapter = new HomeFragmentAdapter(context, schools, mBannerList);
        recyclerView.setAdapter(adapter);
        adapter.setMyOnClickListener((view, position) -> {
            int index = position - 1;
            Intent intent = new Intent(context, ShopActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("shopInfo", schools.get(index));
            intent.putExtras(bundle);
            startActivity(intent);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getSchoolData(long userId) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("userId", String.valueOf(userId));
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .post(formBody).header("token", SharedPreferencesUtil.getString(context,"token",""))
                .url(Constant.SCHOOL_URL)
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
                    Map maps = (Map) JSON.parse(s);
                    if (0 != (int)maps.get("code")){
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Gson gson = new Gson();
                    JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<List<School>>>() {
                    }.getType());

                    schools = (ArrayList<School>) jsonBean.getResult();
                    adapter.setShopInfoData(schools);
                    recyclerView.post(() -> adapter.notifyDataSetChanged());
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                }
            }
        });
    }

    private void initBannerData() {
        FormBody.Builder builder = new FormBody.Builder();
        int corporationId = SharedPreferencesUtil.getInt(getContext(), "corporationId", 0);
        builder.add("corporationId", String.valueOf(corporationId));
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .post(formBody).header("token", SharedPreferencesUtil.getString(context,"token",""))
                .url(Constant.BANNER_URL)
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
                    Map maps = (Map) JSON.parse(s);
                    if (0 != (int)maps.get("code")){
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    Gson gson = new Gson();
                    JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<ArrayList<BannerInfo>>>() {
                    }.getType());

                    ArrayList<BannerInfo> bannerInfos = (ArrayList<BannerInfo>) jsonBean.getResult();
                    if (bannerInfos != null && !bannerInfos.isEmpty()) {
                        mBannerList = makeImage(bannerInfos);
                        adapter.setMBannerList(mBannerList);
                        recyclerView.post(() -> adapter.notifyDataSetChanged());
                    } else {

                    }
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                }
            }
        });
    }

    private ArrayList<String> makeImage(List<BannerInfo> BannerInfo) {
        ArrayList<String> data = new ArrayList<>();
        for (BannerInfo bannerInfo : BannerInfo) {
            data.add(bannerInfo.getUrl());
        }
        return data;
    }

}
