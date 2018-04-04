package com.feature.projectone.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.HomeFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
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

    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.tvOrder)
    TextView tvOrder;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.tvMine)
    TextView tvMine;
    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.imgOrder)
    ImageView imgOrder;
    @BindView(R.id.imgMsg)
    ImageView imgMsg;
    @BindView(R.id.imgMine)
    ImageView imgMine;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.llMsg)
    LinearLayout llMsg;
    @BindView(R.id.llMine)
    LinearLayout llMine;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

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
        PostList();

        homeFragment = new HomeFragment();

        fragmentList.add(homeFragment);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList, null);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());
    }

    /**
     * 请求接口
     */
    private void PostList() {
        OkHttpUtils.post(Constanst.Home_Page)
                .connTimeOut(HttpUtils.DEFAULT_MILLISECONDS)
                .writeTimeOut(HttpUtils.DEFAULT_MILLISECONDS)
                .readTimeOut(HttpUtils.DEFAULT_MILLISECONDS)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String json, Call call, Response response) {
                        Logger.w("地址：" + Constanst.Home_Page + json);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> jsonMap;
                        try {
                            jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
                            });
                            Integer code = (Integer) jsonMap.get("code");
                            if (code == 200) {

                            } else {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.llHome, R.id.llOrder, R.id.llMsg, R.id.llMine})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.llHome:
                viewPager.setCurrentItem(0);
                tvHome.setTextColor(getResources().getColor(R.color.orangeone));
                tvOrder.setTextColor(getResources().getColor(R.color.grayone));
                tvMsg.setTextColor(getResources().getColor(R.color.grayone));
                tvMine.setTextColor(getResources().getColor(R.color.grayone));
                imgHome.setImageResource(R.mipmap.home_2);
                imgOrder.setImageResource(R.mipmap.clipboardwtick_1);
                imgMsg.setImageResource(R.mipmap.bubble_1);
                imgMine.setImageResource(R.mipmap.male_1);
                llHome.setBackgroundResource(R.mipmap.tabbarbg);
                llOrder.setBackgroundResource(0);
                llMsg.setBackgroundColor(0);
                llMine.setBackgroundColor(0);
                break;
            case R.id.llOrder:
                viewPager.setCurrentItem(1);
                tvOrder.setTextColor(getResources().getColor(R.color.orangeone));
                tvHome.setTextColor(getResources().getColor(R.color.grayone));
                tvMsg.setTextColor(getResources().getColor(R.color.grayone));
                tvMine.setTextColor(getResources().getColor(R.color.grayone));
                imgHome.setImageResource(R.mipmap.home_1);
                imgOrder.setImageResource(R.mipmap.clipboardwtick_2);
                imgMsg.setImageResource(R.mipmap.bubble_1);
                imgMine.setImageResource(R.mipmap.male_1);
                llOrder.setBackgroundResource(R.mipmap.tabbarbg);
                llHome.setBackgroundResource(0);
                llMsg.setBackgroundResource(0);
                llMine.setBackgroundResource(0);
                break;
            case R.id.llMsg:
                viewPager.setCurrentItem(2);
                tvMsg.setTextColor(getResources().getColor(R.color.orangeone));
                tvOrder.setTextColor(getResources().getColor(R.color.grayone));
                tvHome.setTextColor(getResources().getColor(R.color.grayone));
                tvMine.setTextColor(getResources().getColor(R.color.grayone));
                imgHome.setImageResource(R.mipmap.home_1);
                imgOrder.setImageResource(R.mipmap.clipboardwtick_1);
                imgMsg.setImageResource(R.mipmap.bubble_2);
                imgMine.setImageResource(R.mipmap.male_1);
                llMsg.setBackgroundResource(R.mipmap.tabbarbg);
                llHome.setBackgroundResource(0);
                llOrder.setBackgroundResource(0);
                llMine.setBackgroundResource(0);
                break;
            case R.id.llMine:
                viewPager.setCurrentItem(3);
                tvMine.setTextColor(getResources().getColor(R.color.orangeone));
                tvOrder.setTextColor(getResources().getColor(R.color.grayone));
                tvMsg.setTextColor(getResources().getColor(R.color.grayone));
                tvHome.setTextColor(getResources().getColor(R.color.grayone));
                imgHome.setImageResource(R.mipmap.home_1);
                imgOrder.setImageResource(R.mipmap.clipboardwtick_1);
                imgMsg.setImageResource(R.mipmap.bubble_1);
                imgMine.setImageResource(R.mipmap.male_2);
                llMsg.setBackgroundResource(R.mipmap.tabbarbg);
                llHome.setBackgroundResource(0);
                llOrder.setBackgroundResource(0);
                llMine.setBackgroundResource(0);
                break;
        }
    }
}
