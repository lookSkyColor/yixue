package com.jiateng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jiateng.R;
import com.jiateng.common.utils.MockData;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.jiateng.fragment.HomeFragment;
import com.jiateng.fragment.OrderFragment;
import com.jiateng.fragment.UserFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author LuoJiateng
 */
public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.main_index)
    private RadioGroup main_index;
    @ViewInject(R.id.main_home)
    private RadioButton main_home;
    @ViewInject(R.id.main_order)
    private RadioButton main_order;
    @ViewInject(R.id.main_user)
    private RadioButton main_user;

    private FragmentManager fragmentManager;
    private HomeFragment mHomeFragment;
    private OrderFragment mOrderFragment;
    private UserFragment mUserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        initFragment();
        fragmentManager = getSupportFragmentManager();

        main_index.setOnCheckedChangeListener(this);
        main_home.setChecked(true);
        changeFragment(mHomeFragment, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_home:
                changeFragment(mHomeFragment, true);
                break;
            case R.id.main_order:
                changeFragment(mOrderFragment, true);
                break;
            case R.id.main_user:
                changeFragment(mUserFragment, true);
                break;
            default:
                break;
        }
    }

    private void initFragment(){
        mHomeFragment = new HomeFragment();
        mOrderFragment = new OrderFragment();
        mUserFragment = new UserFragment();
    }

    /**
     * 切换不同的Fragment
     *
     * @param fragment    传入的Fragment
     * @param isFirstInit 用来控制回退栈
     */
    private void changeFragment(Fragment fragment, boolean isFirstInit) {
        //fragment使用流程
        //1、开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //2、将默认占位的Fragment替换为传入的Fragment
        transaction.replace(R.id.main_fragment, fragment);
        if (!isFirstInit) {
            //如果不是第一次识别，将加入回退栈null，消除重影问题
            transaction.addToBackStack(null);
        }

        //3、提交事务
        transaction.commit();
    }
}