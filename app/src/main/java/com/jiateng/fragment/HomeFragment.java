package com.jiateng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.activity.LoginActivity;
import com.jiateng.activity.ShopActivity;
import com.jiateng.adapter.HomeFragmentAdapter;
import com.jiateng.bean.BannerInfo;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.School;
import com.jiateng.bean.ShopInfo;
import com.jiateng.common.base.BaseFragment;
import com.jiateng.common.utils.MockData;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_home, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();

        long userId = SharedPreferencesUtil.getLong(context, "userId", 0L);
        if(0 != userId){
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userId", String.valueOf(userId));
            FormBody formBody = builder.build();
            final Request request = new Request.Builder()
                    .post(formBody)
                    .url("http://192.168.0.128:8080/school/selectSchoolByUserId")
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if(0 !=  SharedPreferencesUtil.getLong(null,"corporationId",0L)){

                        }
                        OkHttpClient client = new OkHttpClient.Builder()
                                .retryOnConnectionFailure(true) //开启连接失败时重连逻辑
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            Log.e("TAG", "Post请求String同步响应success==" + s);

                            Gson gson = new Gson();
                            JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<List<School>>>() {}.getType());

                           schools = (ArrayList<School>) jsonBean.getResult();
                            getActivity().runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    //TODO mock data
                                    //ArrayList<ShopInfo> shopInfoData = MockData.getShopInfoList();
                                    adapter = new HomeFragmentAdapter(context, schools);
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

                            });



                        } else {
                            Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", "Post请求String同步响应failure==" + e.getMessage());
                    }
                }
            }).start();
        }else {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }

    }
}
