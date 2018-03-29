package com.feature.projectone.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.feature.projectone.R;
import com.feature.projectone.activity.NewsActivity;
import com.feature.projectone.other.NewsAdapter;
import com.feature.projectone.util.CommonUtil;
import com.feature.projectone.util.DividerUtil;
import com.feature.projectone.util.GlideImageLoader;
import com.feature.projectone.util.GlideOvalImageLoader;
import com.feature.projectone.util.MiddleDividerUtil;
import com.yanzhenjie.sofia.Sofia;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.recyclerType)
    RecyclerView recyclerType;
    @BindView(R.id.recyclerPublishClass)
    RecyclerView recyclerPublishClass;
    @BindView(R.id.recyclerExcellentCourse)
    RecyclerView recyclerExcellentCourse;
    @BindView(R.id.banner)
    Banner banner;


    private CommonAdapter<String> typeAdapter;
    private ArrayList<String> typeList = new ArrayList<>();
    private CommonAdapter<String> publishClassAdapter;
    private ArrayList<String> publishClassList = new ArrayList<>();
    private CommonAdapter<String> excellentCourseAdapter;
    private ArrayList<String> excellentCourseList = new ArrayList<>();
    private ArrayList<Integer> bannerImgIdList = new ArrayList<>();
    private int imgIds[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private int typeImageIds[] = {R.mipmap.news, R.mipmap.down, R.mipmap.tv, R.mipmap.book, R.mipmap.calendar, R.mipmap.microphone, R.mipmap.gift, R.mipmap.umbrella};
    private String typeContents[] = {"新闻资讯", "资源下载", "精选微课", "精品课程", "我的课程", "热门直播", "积分商城", "线下辅导"};

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        Sofia.with(getActivity())
                .statusBarBackground(ContextCompat.getColor(getActivity(), R.color.orangeone));
//                .navigationBarBackground(ContextCompat.getDrawable(getActivity(), R.color.orangeone));
        for (int i = 0; i < 8; i++) {
            typeList.add(i + "");
        }
        for (int i = 0; i < 4; i++) {
            publishClassList.add(i + "");
        }
        for (int i = 0; i < 4; i++) {
            excellentCourseList.add(i + "");
            bannerImgIdList.add(R.mipmap.a);
        }
        banner.setImages(bannerImgIdList).setImageLoader(new GlideOvalImageLoader()).start();
        typeAdapter = new CommonAdapter<String>(getActivity(), R.layout.adapter_type, typeList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tvContent, typeContents[position]);
                Glide.with(getActivity()).load(typeImageIds[position]).into((ImageView) holder.getView(R.id.imgIcon));

            }
        };
        recyclerType.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerType.addItemDecoration(new DividerUtil(0,0,0,CommonUtil.dip2px(getActivity(),20)));
        recyclerType.setAdapter(typeAdapter);

        publishClassAdapter = new CommonAdapter<String>(getActivity(), R.layout.adapter_publishclass, publishClassList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tvContent, "高阶美术教程");
                Glide.with(getActivity()).load(R.mipmap.a).into((ImageView) holder.getView(R.id.imgIcon));
            }
        };
        recyclerPublishClass.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerPublishClass.addItemDecoration(new MiddleDividerUtil( CommonUtil.dip2px(getActivity(),7), 0, CommonUtil.dip2px(getActivity(), 12)));
        recyclerPublishClass.setAdapter(publishClassAdapter);

        excellentCourseAdapter = new CommonAdapter<String>(getActivity(), R.layout.adapter_excellentcourse, excellentCourseList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tvTitle, "八年级");
                holder.setText(R.id.tvTeacher, "主讲教师：李佳文");
                holder.setText(R.id.tvIntroduce, "课也是古代专门负责");
            }
        };
        recyclerExcellentCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerExcellentCourse.addItemDecoration(new DividerUtil(0, 0, 0, CommonUtil.dip2px(getActivity(),17)));
        recyclerExcellentCourse.setAdapter(excellentCourseAdapter);


        initListener();
    }

    private void initListener() {

        typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(position==0){
                    Intent intent=new Intent(getActivity(), NewsActivity.class);

                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        }
}
