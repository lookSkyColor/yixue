package com.jiateng.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jiateng.R;
import com.jiateng.bean.OrderListType;
import com.jiateng.common.widget.OrderInfoView;
import com.jiateng.fragment.OrderFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.jiateng.common.widget.AppTitleView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderInfoActivity extends Activity  {


    @ViewInject(R.id.title_orderInfo)
    private AppTitleView titleOrderInfo;


    @ViewInject(R.id.order_item_info)
    private TextView shopInfo;

    @ViewInject(R.id.order_item_info)
    private TextView schoolName;
    @ViewInject(R.id.subject_name)
    private OrderInfoView recyclerView;
    @ViewInject(R.id.discount_amount)
    private OrderInfoView discountAmount;
    @ViewInject(R.id.order_price)
    private OrderInfoView orderPrice;
    @ViewInject(R.id.order_no)
    private OrderInfoView orderNo;
    @ViewInject(R.id.place_an_order)
    private OrderInfoView placeAnOrder;
    @ViewInject(R.id.time_of_payment)
    private OrderInfoView timeOfPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ViewUtils.inject(this);
        initViewFunction();
    }

    public void initViewFunction() {

        Intent intent = getIntent();
        OrderListType data = (OrderListType) intent.getExtras().get("orderListType");
        schoolName.setText(data.getSchoolName());
        recyclerView.setOrderInfo(data.getSubjectName(),1,String.valueOf(data.getSubjectPrice()));
        discountAmount.setOrderInfo(String.valueOf(data.getDiscountAmount()));
        orderPrice.setOrderInfo(String.valueOf(data.getOrderPrice()));
        orderNo.setOrderInfo(data.getOrderNo());
        placeAnOrder.setOrderInfo(getStringDate(data.getPlaceAnOrder()));
        timeOfPayment.setOrderInfo(getStringDate(data.getTimeOfPayment()));
        titleOrderInfo.onClickTitleListener(v -> {
            finish();
        });

        /*shopInfo.setOnClickListener(v -> {
            startActivity(new Intent(OrderInfoActivity.this, ShopActivity.class));
        });*/
    }

    public static String getStringDate(Date currentTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = formatter.format(currentTime);

        return dateString;

    }
}