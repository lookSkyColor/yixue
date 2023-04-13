package com.jiateng.adapter;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.activity.LoginActivity;
import com.jiateng.activity.MainActivity;
import com.jiateng.bean.BannerInfo;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.School;
import com.jiateng.bean.ShopInfo;
import com.jiateng.bean.UserInfo;
import com.jiateng.common.utils.MockData;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Description:
 * @Title: HomeFragmentAdapter
 * @ProjectName: orderFood
 * @date: 2023/2/6 12:48
 * @author: 骆家腾
 */
public class HomeFragmentAdapter extends RecyclerView.Adapter {

    private int currentItem = 0;
    private static final int BANNER = 0;
    private static final int LIST = 1;
    private Context context;
    private ArrayList<School> shopInfoData;
    private MyOnClickListener myOnClickListener;

    public HomeFragmentAdapter(Context context, ArrayList<School> shopInfoData) {
        this.context = context;
        this.shopInfoData = shopInfoData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            View itemView = View.inflate(context, R.layout.banner_home, null);
            return new BannerHolder(context, itemView);
        } else {
            View listItemView = View.inflate(context, R.layout.item_home_shop, null);
            return new ListHolder(context, listItemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BANNER) {
            BannerHolder bannerHolder = (BannerHolder) holder;
            FormBody.Builder builder = new FormBody.Builder();
            int corporationId = SharedPreferencesUtil.getInt(null, "corporationId", 0);
            builder.add("corporationId", String.valueOf(corporationId));
            FormBody formBody = builder.build();
            final Request request = new Request.Builder()
                    .post(formBody)
                    .url("http://192.168.0.128:8080/home/toHome")
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if(0 !=  SharedPreferencesUtil.getLong(context,"corporationId",0L)){

                        }
                        OkHttpClient client = new OkHttpClient.Builder()
                                .retryOnConnectionFailure(true) //开启连接失败时重连逻辑
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            Log.e("TAG", "Post请求String同步响应success==" + s);

                            Gson gson = new Gson();
                            JsonBean jsonBean  = gson.fromJson(s, new TypeToken<JsonBean<ArrayList<BannerInfo>>>() {}.getType());

                           ArrayList<BannerInfo> bannerInfos = (ArrayList<BannerInfo>) jsonBean.getResult();
                            if (bannerInfos !=null && !bannerInfos.isEmpty()){
                                //TODO 修改为从服务器请求的数据，删除mokaImage这个方法
                                bannerHolder.setData(mokaImage(bannerInfos));
                            }else {
                            }


                        } else {
                            Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                            //TODO 修改为从服务器请求的数据，删除mokaImage这个方法

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        //TODO 修改为从服务器请求的数据，删除mokaImage这个方法
                        Log.e("TAG", "Post请求String同步响应failure==" + e.getMessage());
                    }
                }
            }).start();


        } else {
            ListHolder listHolder = (ListHolder) holder;
            int index = position - 1;
            listHolder.setData(shopInfoData.get(index), position);
        }
    }

    @Override
    public int getItemCount() {

        if (null == shopInfoData){
            return 1;
        }
        return 1 + (shopInfoData.isEmpty()?0:shopInfoData.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == BANNER) {
            currentItem = BANNER;
        } else {
            currentItem = LIST;
        }
        return currentItem;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        private ImageView shopImg;
        private TextView shopName;
        private TextView monthlySales;
        private TextView shopSpace;
        private TextView shopOpenTime;

        private View view;

        public ListHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            shopImg = itemView.findViewById(R.id.shop_img);
            shopName = itemView.findViewById(R.id.list_item_shop_name);
            monthlySales = itemView.findViewById(R.id.list_item_shop_monthlySales);
            shopSpace = itemView.findViewById(R.id.list_item_shop_space);
            shopOpenTime = itemView.findViewById(R.id.list_item_shop_openTime);
        }

        public void setData(School shopInfo, int position) {
            shopImg.setTag(position);
            Picasso.get().load(shopInfo.getSchoolImage()).fit().into(shopImg);
            shopName.setTag(position);
            shopName.setText(shopInfo.getSchoolName());
            monthlySales.setTag(position);
            monthlySales.setText(shopInfo.getSchoolContent());
            shopSpace.setTag(position);
            shopSpace.setText(shopInfo.getSchoolSubject());
            shopOpenTime.setTag(position);
            shopOpenTime.setText(shopInfo.getSchoolAddress());

            view.setOnClickListener(v -> {
                if (myOnClickListener != null) {
                    myOnClickListener.viewClick(view, position);
                }
            });
        }
    }

    /**
     * 轮播图处理
     */
    class BannerHolder extends RecyclerView.ViewHolder {
        private Banner banner;

        public BannerHolder(Context context, View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
        }

        public void setData(ArrayList<String> data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    banner.setAdapter(new BannerImageAdapter<String>(data) {
                        @Override
                        public void onBindView(BannerImageHolder holder, String data, int position, int size) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                            Bitmap bitmap =
                                    getHttpBitmap(data);
                            runOnUiThread(new Runnable() {
                                @Override
                                 public void run() {
                                    holder.imageView.setImageBitmap(bitmap);
                                }
                            });

                                }
                            }).start();

                        }
                    });


            banner.isAutoLoop(true);
            banner.setIndicator(new CircleIndicator(context));
            banner.start();
                }
            });
        }

    }
    final Handler mHandler = new Handler();

    private Thread mUiThread;

    public final void runOnUiThread(Runnable action) {
            if (Thread.currentThread() != mUiThread) {
                    mHandler.post(action);
                } else {
                    action.run();
                 }
         }

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    /**
     * 点击事件回调接口.
     */
    public interface MyOnClickListener {
        void viewClick(View view, int position);
    }

    //TODO 将来获取banner从服务器中请求的数据后，删除这些数据
    private ArrayList<String> mokaImage( List<BannerInfo> BannerInfo) {
        ArrayList<String> data = new ArrayList<>();
        for (BannerInfo bannerInfo:BannerInfo) {
            data.add(bannerInfo.getUrl());
        }
        return data;
    }


    /**
     * 从服务器取图片
     *http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            Log.d(TAG, url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            //conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
