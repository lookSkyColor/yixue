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

import com.bumptech.glide.Glide;
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
    private ArrayList<String> mBannerList;
    private MyOnClickListener myOnClickListener;

    public void setShopInfoData(ArrayList<School> shopInfoData) {
        this.shopInfoData = shopInfoData;
    }

    public void setMBannerList(ArrayList<String> mBannerList) {
        this.mBannerList = mBannerList;
    }

    public HomeFragmentAdapter(Context context, ArrayList<School> shopInfoData, ArrayList<String> bannerList) {
        this.context = context;
        this.shopInfoData = shopInfoData;
        this.mBannerList = bannerList;
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
            bannerHolder.setData(mBannerList);
        } else {
            ListHolder listHolder = (ListHolder) holder;
            int index = position - 1;
            listHolder.setData(shopInfoData.get(index), position);
        }
    }

    @Override
    public int getItemCount() {

        if (null == shopInfoData) {
            return 1;
        }
        return 1 + (shopInfoData.isEmpty() ? 0 : shopInfoData.size());
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
            banner.isAutoLoop(true);
            banner.setIndicator(new CircleIndicator(context));
            banner.start();
            banner.setAdapter(new BannerImageAdapter<String>(data) {
                @Override
                public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                    Glide.with(context).load(data).into(holder.imageView);
                }
            });
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
}
