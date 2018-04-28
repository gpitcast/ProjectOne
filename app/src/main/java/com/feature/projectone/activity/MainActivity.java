package com.feature.projectone.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.HomeFragment;
import com.feature.projectone.fragment.MessageFragment;
import com.feature.projectone.fragment.MineFragment;
import com.feature.projectone.fragment.OrderFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.iv_order)
    ImageView iv_order;
    @BindView(R.id.tv_order)
    TextView tv_order;
    @BindView(R.id.iv_message)
    ImageView iv_message;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.iv_mine)
    ImageView iv_mine;
    @BindView(R.id.tv_mine)
    TextView tv_mine;
    @BindView(R.id.iv_shopping_car)
    ImageView iv_shopping_car;
    @BindView(R.id.tv_shopping_car)
    TextView tv_shopping_car;

    private Fragment homeFragment, orderFragment, messageFragment, mineFragment;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        showHomeFragment();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (orderFragment != null) {
            transaction.hide(orderFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
    }

    //显示首页fragment
    private void showHomeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            transaction.add(R.id.framelayout, homeFragment);
        }
        hideFragment(transaction);
        transaction.show(homeFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示订单fragment
    private void showOrderFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (orderFragment == null) {
            orderFragment = new OrderFragment();
            transaction.add(R.id.framelayout, orderFragment);
        }
        hideFragment(transaction);
        transaction.show(orderFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示首页fragment
    private void showMessageFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            transaction.add(R.id.framelayout, messageFragment);
        }
        hideFragment(transaction);
        transaction.show(messageFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    //显示首页fragment
    private void showMineFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mineFragment == null) {
            mineFragment = new MineFragment();
            transaction.add(R.id.framelayout, mineFragment);
        }
        hideFragment(transaction);
        transaction.show(mineFragment);//显示选中的fragment
        transaction.commit();//提交
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.rl_home, R.id.rl_order, R.id.ll_shopping_car, R.id.rl_message, R.id.rl_mine})
    public void Onclick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//操作fragment的事物
        switch (view.getId()) {
            case R.id.rl_home:
                //首页底部导航栏点击事件
                homeSelected();//改变导航栏图标
                showHomeFragment();//显示首页的fragment
                break;
            case R.id.rl_order:
                //订单底部导航栏点击事件
                orderSelected();//改变导航栏图标
                showOrderFragment();//显示选中的fragment
                break;
            case R.id.ll_shopping_car:
                //购物车底部导航栏点击事件
                shopCarSelected();//改变导航栏图标
                break;
            case R.id.rl_message:
                //消息底部导航栏点击事件
                messageSelected();//改变导航栏图标
                showMessageFragment();//显示选中的fragment
                break;
            case R.id.rl_mine:
                //我的底部导航栏点击事件
                mineSelected();//改变导航栏图标
                showMineFragment();//显示选中的fragment
                break;
        }
    }

    //首页被选中
    private void homeSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_red);
        tv_home.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }

    //订单被选中
    private void orderSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_red);
        tv_order.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }


    //购物车被选中
    private void shopCarSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_red);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.orangeone1));
    }

    //消息被选中
    private void messageSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_red);
        tv_message.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_mine.setImageResource(R.mipmap.icon_mine_gray);
        tv_mine.setTextColor(getResources().getColor(R.color.grayone));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }

    //我的被选中
    private void mineSelected() {
        iv_home.setImageResource(R.mipmap.icon_home_page_gray);
        tv_home.setTextColor(getResources().getColor(R.color.grayone));
        iv_order.setImageResource(R.mipmap.icon_order_list_gray);
        tv_order.setTextColor(getResources().getColor(R.color.grayone));
        iv_message.setImageResource(R.mipmap.icon_message_gray);
        tv_message.setTextColor(getResources().getColor(R.color.grayone));
        iv_mine.setImageResource(R.mipmap.icon_mine_red);
        tv_mine.setTextColor(getResources().getColor(R.color.orangeone1));
        iv_shopping_car.setImageResource(R.mipmap.icon_shopping_car_gray);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.grayone));
    }
}
