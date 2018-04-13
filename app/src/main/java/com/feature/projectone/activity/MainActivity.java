package com.feature.projectone.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.HomeFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.RbDrawableUtil;
import com.feature.projectone.util.ToastUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

//    @BindView(R.id.tvHome)
//    TextView tvHome;
//    @BindView(R.id.tvOrder)
//    TextView tvOrder;
//    @BindView(R.id.tvMsg)
//    TextView tvMsg;
//    @BindView(R.id.tvMine)
//    TextView tvMine;
//    @BindView(R.id.imgHome)
//    ImageView imgHome;
//    @BindView(R.id.imgOrder)
//    ImageView imgOrder;
//    @BindView(R.id.imgMsg)
//    ImageView imgMsg;
//    @BindView(R.id.imgMine)
//    ImageView imgMine;
//    @BindView(R.id.llHome)
//    LinearLayout llHome;
//    @BindView(R.id.llOrder)
//    LinearLayout llOrder;
//    @BindView(R.id.llMsg)
//    LinearLayout llMsg;
//    @BindView(R.id.llMine)
//    LinearLayout llMine;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rb_home_page)
    RadioButton rb_home_page;
    @BindView(R.id.rb_order_list)
    RadioButton rb_order_list;
    @BindView(R.id.rb_shopping_car)
    RadioButton rb_shopping_car;
    @BindView(R.id.rb_message)
    RadioButton rb_message;
    @BindView(R.id.rb_mine)
    RadioButton rb_mine;
    @BindView(R.id.line_home_page)
    View line_home_page;
    @BindView(R.id.line_order_list)
    View line_order_list;
    @BindView(R.id.line_message)
    View line_message;
    @BindView(R.id.line_mine)
    View line_mine;
    @BindView(R.id.rb_group)
    RadioGroup rb_group;

    private FragmentPagerAdapter pagerAdapter;
    private Fragment homeFragment, orderFragment, msgFragment, mineFragment;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();


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

        homeFragment = new HomeFragment();
        fragmentList.add(homeFragment);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, null);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        initRb();//初始化一些radiobutton的属性
    }

    private void initRb() {
        RbDrawableUtil.setEbImgSize(rb_home_page);
        RbDrawableUtil.setEbImgSize(rb_order_list);
        RbDrawableUtil.setEbImgSize(rb_shopping_car);
        RbDrawableUtil.setEbImgSize(rb_message);
        RbDrawableUtil.setEbImgSize(rb_mine);

        rb_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home_page:
                        ToastUtil.show(MainActivity.this, "选中了第一个", 0);
                        line_home_page.setBackgroundColor(getResources().getColor(R.color.orangeone1));
                        line_order_list.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_message.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_mine.setBackgroundColor(getResources().getColor(R.color.grayone));
                        break;
                    case R.id.rb_order_list:
                        ToastUtil.show(MainActivity.this, "选中了第二个", 0);
                        line_home_page.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_order_list.setBackgroundColor(getResources().getColor(R.color.orangeone1));
                        line_message.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_mine.setBackgroundColor(getResources().getColor(R.color.grayone));
                        break;
                    case R.id.rb_shopping_car:
                        ToastUtil.show(MainActivity.this, "选中了第三个", 0);
                        line_home_page.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_order_list.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_message.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_mine.setBackgroundColor(getResources().getColor(R.color.grayone));
                        break;
                    case R.id.rb_message:
                        ToastUtil.show(MainActivity.this, "选中了第四个", 0);
                        line_home_page.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_order_list.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_message.setBackgroundColor(getResources().getColor(R.color.orangeone1));
                        line_mine.setBackgroundColor(getResources().getColor(R.color.grayone));
                        break;
                    case R.id.rb_mine:
                        ToastUtil.show(MainActivity.this, "选中了第五个", 0);
                        line_home_page.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_order_list.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_message.setBackgroundColor(getResources().getColor(R.color.grayone));
                        line_mine.setBackgroundColor(getResources().getColor(R.color.orangeone1));
                        break;
                }
            }
        });
        rb_group.check(R.id.rb_home_page);//默认选中首页
    }


    @Override
    public void afterInitView() {
    }

    @OnClick({})
    public void Onclick(View view) {
        switch (view.getId()) {
        }
    }
}
