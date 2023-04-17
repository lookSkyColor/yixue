package com.jiateng.activity;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiateng.R;
import com.jiateng.adapter.ShoppingCartAdapter;
import com.jiateng.bean.JsonBean;
import com.jiateng.bean.OrderPo;
import com.jiateng.bean.School;
import com.jiateng.bean.ShoppingCart;

import com.jiateng.bean.Subject;
import com.jiateng.common.Constant;
import com.jiateng.common.utils.PayResult;
import com.jiateng.common.utils.PicassoUtil;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.jiateng.common.widget.AppTitleView;
import com.jiateng.db.impl.ShoppingCartImpl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GoodsActivity extends Activity {
    @ViewInject(R.id.shop_goods_title)
    private AppTitleView titleView;
    @ViewInject(R.id.bottomSheetLayout)
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    @ViewInject(R.id.shop_goods_car_2)
    private View carInfo;
    @ViewInject(R.id.car_list)
    private ListView carListView;
    @ViewInject(R.id.shop_goods_hours)
    private TextView shoppingGoodsHours;
   // @ViewInject(R.id.shop_goods_add)
   // private Button addGoodsButton;
    @ViewInject(R.id.shop_goods_price)
    private TextView currentGoodsPrice;
    @ViewInject(R.id.goods_name)
    private TextView currentGoodsName;
    @ViewInject(R.id.goods_img)
    private ImageView currentGoodsImg;
    @ViewInject(R.id.goods_category)
    private TextView goodsCategory;


    private final int SDK_PAY_FLAG = 1;
    private final int SDK_AUTH_FLAG = 2;
    private List<ShoppingCart> shoppingCartsData;
    private ShoppingCartImpl shoppingCartDao;
    private String userId;
    private String goodsId;
    private String shopId;
    private int price;
    private ShoppingCartAdapter adapter;
    private String goodsName;
    private String goodsImgUrl;
    private String schoolName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        ViewUtils.inject(this);

        Bundle bundle = getIntent().getExtras();
        ShoppingCart goodsInfo = (ShoppingCart) bundle.getSerializable("shoppingCartInfo");
        Subject goods = (Subject) bundle.getSerializable("goods");
        userId = goodsInfo.getUserId();
        shopId = goodsInfo.getShopId();
        goodsId = goodsInfo.getGoodsId();
        price = goodsInfo.getGoodsPrice();
        goodsName = goodsInfo.getGoodsName();
        goodsImgUrl = goodsInfo.getGoodsImgUrl();

        //shoppingCartDao = ShoppingCartImpl.getInstance(GoodsActivity.this);
        initCarData();
        shoppingGoodsHours.setText(String.valueOf(goods.getSubjectClassHour()));
        currentGoodsName.setText(goods.getSubjectName());
        currentGoodsPrice.setText(String.valueOf(goods.getSubjectPrice()));
        goodsCategory.setText(goods.getCategory());
        PicassoUtil.setImage(goods.getSubjectImgUrl(), currentGoodsImg);
        adapter = new ShoppingCartAdapter(GoodsActivity.this, shoppingCartsData);

        titleView.onClickTitleListener(v -> {
            finish();
        });
        carInfo.setOnClickListener(v -> {
            showBottomSheet();
        });
        /*addGoodsButton.setOnClickListener(v -> {
            initCarData();
            ShoppingCart shoppingCart = new ShoppingCart(null, userId, shopId, goodsId, goodsName, price, goodsImgUrl, 1);
            //shoppingCartDao.insertGoods(shoppingCart);
            //shoppingCartPrice.setText(getShopPrice(shoppingCart));
        });*/

        findViewById(R.id.settlement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView subjectPriceView = findViewById(R.id.shop_goods_price);
                TextView subjectClassHoursView = findViewById(R.id.shop_goods_hours);
                TextView subjectNameView =findViewById(R.id.goods_name);
                EditText discountAmountView =findViewById(R.id.discount_amount);
                TextView categoryView =findViewById(R.id.goods_category);

                int subjectPrice = Integer.valueOf(subjectPriceView.getText().toString());
                int subjectClassHours = Integer.valueOf(subjectClassHoursView.getText().toString());
                int discountAmount = Integer.valueOf(discountAmountView.getText().toString());
                int orderPrice = subjectPrice - discountAmount;
                String subjectName = subjectNameView.getText().toString();
                String category = categoryView.getText().toString();
                Map<String,Object> bodyParams = new HashMap<>();
                bodyParams.put("orderPrice",orderPrice);
                bodyParams.put("orderNum",1);
                bodyParams.put("subjectPrice",subjectPrice);
                bodyParams.put("discountAmount",discountAmount);
                bodyParams.put("subjectClassHours",subjectClassHours);
                bodyParams.put("category",category);
                bodyParams.put("subjectName",subjectName);
                bodyParams.put("schoolName",SharedPreferencesUtil.getString(GoodsActivity.this,"schoolName",""));
                JSONObject jsonObject = new JSONObject(bodyParams);
                String requestData = jsonObject.toString();
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
                //2.通过RequestBody.create 创建requestBody对象
                RequestBody requestBody = RequestBody.create(mediaType, requestData);
               /* RequestBody requestBody = RequestBody.create("{\n" +
                        "    \"orderPrice\":"+orderPrice+",\n" +
                        "    \"orderNum\":"+1+",\n" +
                        "    \"subjectPrice\":"+subjectPrice+",\n" +
                        "    \"discountAmount\":"+discountAmount+",\n" +
                        "    \"subjectClassHours\":"+subjectClassHours+",\n" +
                        "    \"category\":"+1+",\n" +
                        "    \"subjectName\":"+1+"\n" +
                        "}", MediaType.parse("application/json; charset=utf-8"));*/
                final Request request = new Request.Builder()
                        .post(requestBody).header("token", SharedPreferencesUtil.getString(GoodsActivity.this,"token",""))
                        .url(Constant.CREATE_ORDER_URL)
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
                                Intent intent = new Intent(GoodsActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            Gson gson = new Gson();
                            JsonBean jsonBean = gson.fromJson(s, new TypeToken<JsonBean<String>>() {
                            }.getType());

                            String orderInfo = (String) jsonBean.getResult();
                            // 订单信息
                            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(GoodsActivity.this);
                                    Map<String, String> result = alipay.payV2(orderInfo,true);

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();

                        } else {
                            Intent intent = new Intent();
                            //setClass函数的第一个参数是一个Context对象
                            //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                            //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                            intent.setClass(GoodsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Log.e("TAG", "Post请求String同步响应failure==" + response.body().string());
                        }
                    }
                });


            }
        });
    }


    public void initCarData() {
       // shoppingCartsData = shoppingCartDao.queryByGoodsByUserIdShopId(userId, shopId);
    }

    /**
     * 从底部滑入
     */
    private void showBottomSheet() {
        bottomSheet = createBottomSheetView();
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            initCarData();
            bottomSheetLayout.showWithSheetView(bottomSheet);
        }
    }


    /**
     * 从底部弹出的子布局
     *
     * @return
     */
    private View createBottomSheetView() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet, (ViewGroup) getWindow().getDecorView(), false);
        ListView carList = view.findViewById(R.id.car_list);
        //清空购物车
        ((TextView) view.findViewById(R.id.cleanShoppingCart)).setOnClickListener(v -> {
            //shoppingCartDao.clean(userId, shopId);
           // shoppingCartsData = shoppingCartDao.queryByGoodsByUserIdShopId(userId, shopId);
            adapter.notifyDataSetChanged();
            bottomSheetLayout.dismissSheet();
            //shoppingCartPrice.setText("¥0.0");
        });
        adapter.notifyDataSetChanged();
        adapter.setOnSelectListener(new ShoppingCartAdapter.OnSelectListener() {
            @Override
            public void onSelectAdd(int position, ShoppingCart shoppingCart) {
               // shoppingCartDao.insertGoods(shoppingCart);
                adapter.notifyDataSetChanged();

                //shoppingCartPrice.setText(getShopPrice(shoppingCart));
            }

            @Override
            public void onSelectReduce(int position) {
                ShoppingCart shoppingCart = shoppingCartsData.get(position);
                //shoppingCartDao.deleteGoods(shoppingCart);
                //ShoppingCart hasGoods = shoppingCartDao.queryOne(shoppingCart);
                /*if (hasGoods == null) {
                    shoppingCartsData.remove(position);
                }*/

                //shoppingCartPrice.setText(getShopPrice(shoppingCart));
                adapter.notifyDataSetChanged();
                if (shoppingCartsData.size() == 0) {
                    bottomSheetLayout.dismissSheet();
                }
            }
        });
        carList.setAdapter(adapter);

        return view;
    }

    private String getShopPrice(ShoppingCart shoppingCart) {
        /*double money = 0.0;
        List<ShoppingCart> shoppingCarts = shoppingCartDao.queryByGoodsByUserIdShopId(shoppingCart.getUserId(), shoppingCart.getShopId());
        for (ShoppingCart cart : shoppingCarts) {
            money = money + cart.getGoodsPrice() * cart.getGoodsCount().intValue();
        }
        return String.format("%.1f", money);*/
        return "";
    }

    public void asd (){



    }

    private  Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtils.i(payResult.getMemo()+"______"+payResult.getResult()+"_____"+payResult.getResultStatus());

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(GoodsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        //setClass函数的第一个参数是一个Context对象
                        //Context是一个类，Activity是Context类的子类，也就是说，所有的Activity对象，都可以向上转型为Context对象
                        //setClass函数的第二个参数是一个Class对象，在当前场景下，应该传入需要被启动的Activity类的class对象
                        intent.setClass(GoodsActivity.this, OrderPayActivity.class);
                        startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(GoodsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG:
                    break;
                default:
                    break;
            }
        }
    };

}