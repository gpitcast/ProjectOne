package com.feature.projectone.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.FragmentPagerAdapter;
import com.feature.projectone.fragment.NewsFragment;
import com.feature.projectone.util.CommonUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsActivity extends BaseActivity {

    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgTitleRight)
    ImageView imgTitleRight;
    @BindView(R.id.recyclerTitle)
    RecyclerView recyclerTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private CommonAdapter<String> navigationAdapter;
    private ArrayList<String> navigationList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tvTitle.setText("资讯头条");

        navigationList.add("热点推荐");
        navigationList.add("教育资讯");
        navigationList.add("热点推荐");
        navigationList.add("教育视频");
        navigationList.add("学习资讯");
        navigationList.add("学习资讯");
        for (int i = 0; i < navigationList.size(); i++) {
            NewsFragment newsFragment = new NewsFragment();
            fragmentList.add(newsFragment);
        }
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(fragmentList.size());

        navigationAdapter = new CommonAdapter<String>(this, R.layout.adapter_navigation, navigationList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tvContent, navigationList.get(position));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.width = CommonUtil.getScreenWidth(NewsActivity.this) / 4;
                holder.getView(R.id.llItem).setLayoutParams(layoutParams);
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerTitle.setLayoutManager(manager);
        recyclerTitle.setAdapter(navigationAdapter);

    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.tvBack, R.id.imgTitleRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
            case R.id.imgTitleRight:

                break;
        }
    }
}
