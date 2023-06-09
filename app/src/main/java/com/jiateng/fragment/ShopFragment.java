package com.jiateng.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.jiateng.R;
import com.jiateng.activity.GoodsActivity;
import com.jiateng.adapter.ShopRecyclerHolder;
import com.jiateng.adapter.ShopRecyclerViewAdater;
import com.jiateng.adapter.ShoppingCartAdapter;
import com.jiateng.bean.Category;
import com.jiateng.bean.School;
import com.jiateng.bean.ShopInfo;
import com.jiateng.bean.ShoppingCart;
import com.jiateng.bean.StoreBean;
import com.jiateng.bean.Subject;
import com.jiateng.common.base.BaseFragment;
import com.jiateng.common.utils.PicassoUtil;
import com.jiateng.common.utils.SharedPreferencesUtil;
import com.jiateng.db.impl.ShoppingCartImpl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Title: GoodsFragment
 * @ProjectName: orderFood
 * @date: 2023/2/7 16:52
 * @author: 骆家腾
 */
public class ShopFragment extends BaseFragment {
    @ViewInject(R.id.bottomSheetLayout)
    private BottomSheetLayout bottomSheetLayout;
    private View bottomSheet;
    /*@ViewInject(R.id.shop_goods_car_1)
    private View carInfo;
    @ViewInject(R.id.settlement)
    private TextView settlement;
    @ViewInject(R.id.goods_price)
    private TextView goodsPrice;*/
    private List<ShoppingCart> shoppingCartsData;
    private ShoppingCartImpl shoppingCartDao;
    private String userId;
    private String shopId;
    private ShoppingCartAdapter adapter;

    private List<Subject> goodsList;


    public ShopFragment(School shopInfo) {
        super();
        this.shopId = shopInfo.getId()+"";
        this.userId = String.valueOf(SharedPreferencesUtil.getLong(context, "userId", 0L));
        this.goodsList = shopInfo.getGoodsList();
    }


    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.fragment_goods, null);
        ViewUtils.inject(this, view);
        initGoodsData();
        initGoodsView();
        initGoodsListener();
        /*shoppingCartDao = ShoppingCartImpl.getInstance(context);
        carInfo.setOnClickListener(v -> {
            shoppingCartsData = shoppingCartDao.queryByGoodsByUserIdShopId(userId, shopId);
            showBottomSheet();
        });*/
        return view;
    }

    @Override
    public void onStart() {
        //fragment加载完毕，刷新RecyclerView
        super.onStart();
        rAdapter.notifyDataSetChanged();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setShopId(shopId);
        //goodsPrice.setText(getShopPrice(shoppingCart));
    }

    @Override
    protected void initData() {
        super.initData();
        initGoodsData();
        initCarData();
    }

    public void initCarData() {
        //shoppingCartsData = shoppingCartDao.queryByGoodsByUserIdShopId(userId, shopId);
    }

    /**
     * 从底部滑入
     */
    private void showBottomSheet() {
        //创建要弹出的布局
        bottomSheet = createBottomSheetView();
        //判断打开关闭
        if (bottomSheetLayout.isSheetShowing()) {
            bottomSheetLayout.dismissSheet();
        } else {
            //弹出布局
            bottomSheetLayout.showWithSheetView(bottomSheet);
        }
    }

    /**
     * 从底部弹出的子布局
     *
     * @return
     */
    private View createBottomSheetView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_sheet, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        //清空购物车
        ((TextView) view.findViewById(R.id.cleanShoppingCart)).setOnClickListener(v -> {
            /*shoppingCartDao.clean(userId, shopId);
            shoppingCartsData = shoppingCartDao.queryByGoodsByUserIdShopId(userId, shopId);*/
            adapter.notifyDataSetChanged();
            rAdapter.notifyDataSetChanged();
            bottomSheetLayout.dismissSheet();
            //goodsPrice.setText("0.0");
        });
        ListView carList = view.findViewById(R.id.car_list);
        adapter = new ShoppingCartAdapter(context, shoppingCartsData);
        adapter.notifyDataSetChanged();
        adapter.setOnSelectListener(new ShoppingCartAdapter.OnSelectListener() {
            @Override
            public void onSelectAdd(int position, ShoppingCart shoppingCart) {
                //shoppingCartDao.insertGoods(shoppingCart);
                adapter.notifyDataSetChanged();
               // goodsPrice.setText(getShopPrice(shoppingCart));
            }

            @Override
            public void onSelectReduce(int position) {
                ShoppingCart shoppingCart = shoppingCartsData.get(position);
               // shoppingCartDao.deleteGoods(shoppingCart);
               /* ShoppingCart hasGoods = shoppingCartDao.queryOne(shoppingCart);
                if (hasGoods == null) {
                    shoppingCartsData.remove(position);
                }*/
                adapter.notifyDataSetChanged();
                rAdapter.notifyDataSetChanged();
               // goodsPrice.setText(getShopPrice(shoppingCart));
                if (shoppingCartsData.size() == 0) {
                    bottomSheetLayout.dismissSheet();
                }
            }
        });
        carList.setAdapter(adapter);
        return view;
    }

    private List<Category> categories;
    private LAdapter lAdapter;
    private RAdapter rAdapter;

    /**
     * TODO 从网络上请求数据
     */
    private void initGoodsData() {

        categories = goodsList.stream().map(goods -> new Category(goods.getCategory())).distinct().collect(Collectors.toList());

         goodsList = goodsList;
    }


    @ViewInject(R.id.rv1)
    private RecyclerView rvL;
    @ViewInject(R.id.rv2)
    private RecyclerView rvR;
    @ViewInject(R.id.tv_header)
    private TextView tv_head;

    /**
     * 初始化RecyclerView以及各种组件
     */
    private void initGoodsView() {
        tv_head.setText(categories.get(0).getCategory());
        rvL.setLayoutManager(new LinearLayoutManager(context));
        rvR.setLayoutManager(new LinearLayoutManager(context));
        lAdapter = new LAdapter(context, R.layout.item_goods_left, goodsList);
        lAdapter.bindToRecyclerView(rvL);
        rvL.setAdapter(lAdapter);
        rAdapter = new RAdapter(context, R.layout.item_goods_right, goodsList);
        rvR.setAdapter(rAdapter);
    }

    private boolean moveToTop = false;
    private int index;

    /**
     * 初始化监听事件
     * 如果右侧的列表发生了滚动，停止监听
     * 得到左侧列表点击的条目的标题，遍历右侧标题，如果右侧条目标题和左侧条目标题相同，将右侧条目移动到顶部
     */
    private void initGoodsListener() {
        rAdapter.setShoppingItemClickListener(new ShopRecyclerViewAdater.ShoppingItemClickListener() {

            @Override
            public void addClick(ShopRecyclerHolder holder, List<Subject> data, int position) {
//                String goods  Id = data.get(position).getCategory() + data.get(position).getName();
//                double price = Double.parseDouble(((TextView) holder.getView(R.id.tvPrice)).getText().toString());
               // ShoppingCart shoppingCart = new ShoppingCart(null, userId, shopId, data.get(position).getId()+"", data.get(position).getSubjectName(),data.get(position).getSubjectPrice(), data.get(position).getSubjectImgUrl(), data.get(position).getSubjectNumber());
               // shoppingCartDao.insertGoods(shoppingCart);
                //((ImageView) holder.getView(R.id.addToCar)).setVisibility(View.VISIBLE);
               // ((TextView) holder.getView(R.id.carCount)).setText(shoppingCartDao.queryOne(shoppingCart).getGoodsCount() + "");
                //goodsPrice.setText(getShopPrice(shoppingCart));
                rAdapter.notifyDataSetChanged();
            }

            @Override
            public void reduceClick(ShopRecyclerHolder holder, List<Subject> data, int position) {
                //ShoppingCart shoppingCart = new ShoppingCart(null, userId, shopId, data.get(position).getId()+"", data.get(position).getSubjectName(), data.get(position).getSubjectPrice(), data.get(position).getSubjectImgUrl(),  data.get(position).getSubjectNumber());
               // shoppingCartDao.deleteGoods(shoppingCart);
                //((TextView) holder.getView(R.id.carCount)).setText(shoppingCartDao.queryOne(shoppingCart) == null ? "" : shoppingCartDao.queryOne(shoppingCart).getGoodsCount() + "");
                //goodsPrice.setText(getShopPrice(shoppingCart));
                rAdapter.notifyDataSetChanged();

            }
        });
        lAdapter.setOnItemClickListener(new ShopRecyclerViewAdater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //获取右侧recycler当前状态，如果发生了滚动，结束
                if (rvR.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) return;
                lAdapter.fromClick = true;
                lAdapter.setChecked(position);
                String tag = lAdapter.getmData().get(position).getCategory();
                for (int i = 0; i < rAdapter.getmData().size(); i++) {
                    //根据左边选中的条目获取到右面此条目Title相同的位置索引；
                    if (TextUtils.equals(tag, rAdapter.getmData().get(i).getCategory())) {
                        index = i;
                        moveToPosition_R(index);
                        return;
                    }
                }
            }
        });

        //右侧列表添加滚动侦听器，该侦听器将在滚动状态或位置的任何更改时收到通知。
        rvR.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rvR.getLayoutManager();
                if (moveToTop) { //向下滑动时，只会把该条目显示出来；我们还需要让该条目滑动到顶部；
                    moveToTop = false;
                    int m = index - layoutManager.findFirstVisibleItemPosition();
                    if (m >= 0 && m <= layoutManager.getChildCount()) {
                        int top = layoutManager.getChildAt(m).getTop();
                        rvR.smoothScrollBy(0, top);
                    }
                } else {
                    int index = layoutManager.findFirstVisibleItemPosition();
                    tv_head.setText(rAdapter.getmData().get(index).getCategory());
                    lAdapter.setToPosition(rAdapter.getmData().get(index).getCategory());
                }
            }
        });

        rvR.setOnTouchListener((view, motionEvent) -> {
            lAdapter.fromClick = false;
            return false;
        });
    }

    private String getShopPrice(ShoppingCart shoppingCart) {
        double money = 0.0;
       /* List<ShoppingCart> shoppingCarts = shoppingCartDao.queryByGoodsByUserIdShopId(shoppingCart.getUserId(), shoppingCart.getShopId());
        for (ShoppingCart cart : shoppingCarts) {
            money = money + cart.getGoodsPrice() * cart.getGoodsCount().intValue();
        }
        return String.format("%.1f", money);*/
        return "";
    }


    private void moveToPosition_R(int index) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvR.getLayoutManager();
        int f = layoutManager.findFirstVisibleItemPosition();
        int l = layoutManager.findLastVisibleItemPosition();
        if (index <= f) { //向上移动时
            layoutManager.scrollToPosition(index);
        } else if (index <= l) { //已经再屏幕上面显示时
            int m = index - f;
            if (0 <= m && m <= layoutManager.getChildCount()) {
                int top = layoutManager.getChildAt(m).getTop();
                rvR.smoothScrollBy(0, top);
            }
        } else { //向下移动时
            moveToTop = true;
            layoutManager.scrollToPosition(index);
        }
    }


    class LAdapter extends ShopRecyclerViewAdater<Subject> {

        public LAdapter(Context context, int resLayout, List<Subject> data) {
            super(context, resLayout, data);
        }

        @Override
        public void convert(ShopRecyclerHolder holder, final int position) {
            TextView tv = ((TextView) holder.getView(R.id.tv));
            tv.setText(getmData().get(position).getCategory());
            if (checked == position) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                tv.setBackgroundResource(R.color.colorfff);
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color666));
                tv.setBackgroundResource(R.color.color16333333);
            }

        }

        private int checked; //当前选中项
        public boolean fromClick; //是否是自己点击的

        public void setChecked(int checked) {
            this.checked = checked;
            notifyDataSetChanged();
        }

        //让左边的额条目选中
        public void setToPosition(String title) {
            if (fromClick) return;
            if (TextUtils.equals(title, getmData().get(checked).getCategory())) return;
            if (TextUtils.isEmpty(title)) return;
            for (int i = 0; i < getmData().size(); i++) {
                if (TextUtils.equals(getmData().get(i).getCategory(), title)) {
                    setChecked(i);
                    moveToPosition(i);
                    return;
                }
            }

        }

        private void moveToPosition(int index) {
            //如果选中的条目不在显示范围内，要滑动条目让该条目显示出来
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getRecyclerView().getLayoutManager();
            int f = linearLayoutManager.findFirstVisibleItemPosition();
            int l = linearLayoutManager.findLastVisibleItemPosition();
            if (index <= f || index >= l) {
                linearLayoutManager.scrollToPosition(index);
            }
        }
    }

    class RAdapter extends ShopRecyclerViewAdater<Subject> {
        String goodsId;

        public RAdapter(Context context, int resLayout, List<Subject> data) {
            super(context, resLayout, data);
        }

        @Override
        public void convert(ShopRecyclerHolder holder, final int position) {
            ((TextView) holder.getView(R.id.tvName)).setText(getmData().get(position).getSubjectName());
            ((TextView) holder.getView(R.id.tvPrice)).setText(String.valueOf(getmData().get(position).getSubjectPrice()));
            ((TextView) holder.getView(R.id.shop_goods_count)).setText(getmData().get(position).getSubjectNumber() + "");
            PicassoUtil.setImage(getmData().get(position).getSubjectImgUrl(), (AppCompatImageView) holder.getView(R.id.tv_image));

           // ImageView add = holder.getView(R.id.addToCar);
            //TextView addCount = holder.getView(R.id.carCount);
            ImageView reduce = holder.getView(R.id.reduceFromCar);
            goodsId = getmData().get(position).getCategory() + getmData().get(position).getSubjectName();
            ShoppingCart shoppingCart = new ShoppingCart(null, userId, shopId, getmData().get(position).getId()+"", getmData().get(position).getSubjectName(), getmData().get(position).getSubjectPrice(), getmData().get(position).getSubjectImgUrl(), 1);
            //ShoppingCart goods = shoppingCartDao.queryOne(shoppingCart);
            /*if (goods == null) {
                reduce.setVisibility(View.GONE);
                addCount.setText("");
            } else {
                reduce.setVisibility(View.VISIBLE);
                addCount.setText(goods.getGoodsCount() + "");
                goodsPrice.setText(getShopPrice(shoppingCart));
            }*/
            /*add.setOnClickListener(v -> {
                if (shoppingItemClickListener != null) {
                    shoppingItemClickListener.addClick(holder, getmData(), position);
                }
            });*/
            reduce.setOnClickListener(v -> {
                if (shoppingItemClickListener != null) {
                    shoppingItemClickListener.reduceClick(holder, getmData(), position);
                }
            });
            //悬停的标题头
            FrameLayout headLayout = holder.getView(R.id.stick_header);
            TextView tvHead = holder.getView(R.id.tv_header);
            if (position == 0) {
                headLayout.setVisibility(View.VISIBLE);
                tvHead.setText(getmData().get(position).getCategory());
            } else {
                if (TextUtils.equals(getmData().get(position).getCategory(), getmData().get(position - 1).getCategory())) {
                    headLayout.setVisibility(View.GONE);
                } else {
                    headLayout.setVisibility(View.VISIBLE);
                    tvHead.setText(getmData().get(position).getCategory());
                }
            }
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("shoppingCartInfo", shoppingCart);
                bundle.putSerializable("goods", (Serializable) getmData().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }
    }


}
