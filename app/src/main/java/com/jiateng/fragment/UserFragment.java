package com.jiateng.fragment;


import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jiateng.R;
import com.jiateng.activity.AddressActivity;
import com.jiateng.activity.FeedbackActivity;
import com.jiateng.activity.LoginActivity;
import com.jiateng.activity.MainActivity;
import com.jiateng.activity.MoreActivity;
import com.jiateng.activity.SettingActivity;
import com.jiateng.activity.StarActivity;
import com.jiateng.common.base.BaseFragment;
import com.jiateng.common.utils.MockData;
import com.jiateng.common.widget.UserItemView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Description:
 * @Title: UserFragment
 * @ProjectName: orderFood
 * @date: 2023/1/10 17:44
 * @author: 骆家腾
 */
public class UserFragment extends BaseFragment {
    @ViewInject(R.id.user_item_address)
    private UserItemView address;
    @ViewInject(R.id.user_item_star)
    private UserItemView start;
    @ViewInject(R.id.user_item_more)
    private UserItemView more;
    @ViewInject(R.id.user_item_feedback)
    private UserItemView report;
    @ViewInject(R.id.user_item_setting)
    private UserItemView setting;
    private TextView tvContent;
    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_user, null);
        ViewUtils.inject(this, view);
        if(null != MockData.getUserId()){
            TextView stateTextView = (TextView) view.findViewById(R.id.lblTitle);
            stateTextView.setText(MockData.getUserId());
        }
        return view;
    }



    @OnClick({R.id.user_item_setting,
            R.id.user_item_address,
            R.id.user_item_star,
            R.id.user_item_more,
            R.id.user_item_feedback,
            R.id.user_item_login})
    private void changeActivity(View view) {
        switch (view.getId()) {
            case R.id.user_item_setting:
                startActivity(new Intent(context, SettingActivity.class));
                break;
            case R.id.user_item_address:
                startActivity(new Intent(context, AddressActivity.class));
                break;
            case R.id.user_item_star:
                startActivity(new Intent(context, StarActivity.class));
                break;
            case R.id.user_item_more:
                startActivity(new Intent(context, MoreActivity.class));
                break;
            case R.id.user_item_feedback:
                startActivity(new Intent(context, FeedbackActivity.class));
                break;
            case R.id.user_item_login:
                Log.d(TAG, "currentFragmentName-------->>  " + getClass().getSimpleName());

                startActivity(new Intent(context, LoginActivity.class));
            default:
                break;
        }
    }




}
