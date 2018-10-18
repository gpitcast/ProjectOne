package com.youyi.ywl.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.activity.ExcellentWeikeDetailActivity;
import com.youyi.ywl.adapter.LecturerIntroduceAdapter;
import com.youyi.ywl.adapter.PriceDescriptionAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.util.WebViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/8/13.
 * 精品微课详情 - 课程介绍fragment
 */

@SuppressLint("ValidFragment")
public class CourseIntroduceFragment_Weike extends BaseFragment {
    @BindView(R.id.xRecyclerView_lecturer)
    RecyclerView xRecyclerView_lecturer;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.xRecyclerView_price_description)
    XRecyclerView xRecyclerView_price_description;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_want_learn)
    TextView tv_want_learn;
    @BindView(R.id.tv_already_look)
    TextView tv_already_look;
    @BindView(R.id.tv_course_instroduce)
    TextView tv_course_instroduce;
    @BindView(R.id.tv_fit_people)
    TextView tv_fit_people;
    @BindView(R.id.tv_teaching_target)
    TextView tv_teaching_target;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    private static final String DATA_URL = HttpUtils.Host + "/weike/detail";//微课详情的课程介绍接口
    private LecturerIntroduceAdapter lecturerIntroduceAdapter;//老师列表适配器
    private PriceDescriptionAdapter priceDescriptionAdapter;//价格说明适配器
    private TextView tv_status;
    private String id;
    private List<HashMap<String, Object>> mTeacherList = new ArrayList<>();//老师列表


    public CourseIntroduceFragment_Weike(String id) {
        this.id = id;
    }

    @Override
    public void onLazyLoad() {
        PostList();
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DATA_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    HashMap<String, Object> detailMap = (HashMap<String, Object>) resultMap.get("detail");
                    //封面图
                    if (getActivity()!=null){
                        ((ExcellentWeikeDetailActivity) getActivity()).loadImageView(detailMap.get("img") + "");
                    }
                    //标题
                    tv_title.setText(detailMap.get("title") + "");
                    //想学的人数
                    tv_want_learn.setText(detailMap.get("wantToLearnNums") + " 人想学");
                    //已经观看的人数
                    tv_already_look.setText(detailMap.get("views") + " 人已观看");
                    //课程简介
                    tv_course_instroduce.setText(detailMap.get("intro") + "");
                    //使用人群
                    tv_fit_people.setText(detailMap.get("suitForUser") + "");
                    //教学目标
                    tv_teaching_target.setText(detailMap.get("teachingAims") + "");
                    //老师列表as
                    ArrayList<HashMap<String, Object>> teachersList = (ArrayList<HashMap<String, Object>>) detailMap.get("teachers");
                    mTeacherList.addAll(teachersList);
                    lecturerIntroduceAdapter.notifyDataSetChanged();
                    //说明的webview
                    webView.loadUrl(detailMap.get("url") + "");
                    //是否想学
                    String isWantLearn = resultMap.get("isWantLearn") + "";//0代表用户没有添加想学 1代表用户添加了想学
                    ((ExcellentWeikeDetailActivity) getActivity()).showWantLearnState(isWantLearn);
                } else {
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_course_introduce_weike;
    }

    @Override
    protected void initViewsAndEvents(View view) {

        WebViewUtil.setWebViewSettings(webView);
        scrollView.smoothScrollTo(0, 0);

        initLecturerRecyclerView();//初始化讲师介绍recyclerview
        initPriceDescriptionRecyclerView();//初始化价格说明recyclerView
    }

    //请求课程介绍数据接口
    private void PostList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "weike");
        map.put("action", "detail");
        map.put("id", id);
        getJsonUtil().PostJson(getActivity(), map);
    }


    //初始化讲师介绍
    private void initLecturerRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        xRecyclerView_lecturer.setLayoutManager(manager);
        lecturerIntroduceAdapter = new LecturerIntroduceAdapter(getActivity(), mTeacherList);
        xRecyclerView_lecturer.setAdapter(lecturerIntroduceAdapter);

        lecturerIntroduceAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                showTeacherIntroduceDialog(position);
            }
        });
    }

    //显示 老师介绍 dialog
    private void showTeacherIntroduceDialog(int position) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(getActivity(), R.style.mDialog);
        View dialogView = inflater.inflate(R.layout.layout_teacher_introduce_dialog, null);

        HashMap<String, Object> map = mTeacherList.get(position);
        CircleImageView circleImageView = (CircleImageView) dialogView.findViewById(R.id.circleImageView);
        GlideUtil.loadNetImageView(getActivity(),map.get("img") + "",circleImageView);
        TextView tv_teacher_name = (TextView) dialogView.findViewById(R.id.tv_teacher_name);
        tv_teacher_name.setText(map.get("name") + "");
        TextView tv_teacher_instroction = (TextView) dialogView.findViewById(R.id.tv_teacher_instroction);
        tv_teacher_instroction.setText(map.get("desc") + "");

        //取消图标点击事件
        ((FrameLayout) dialogView.findViewById(R.id.fl_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(false);//设置外部不可点击
        dialog.show();
    }

    //初始化价格说明recyclerView
    private void initPriceDescriptionRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        xRecyclerView_price_description.setLayoutManager(manager);
        priceDescriptionAdapter = new PriceDescriptionAdapter(getActivity());
        xRecyclerView_price_description.setAdapter(priceDescriptionAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView_price_description.addHeaderView(headView1);
        xRecyclerView_price_description.addHeaderView(headView2);
        xRecyclerView_price_description.setPullRefreshEnabled(false);
        xRecyclerView_price_description.setLoadingMoreEnabled(true);
        xRecyclerView_price_description.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

        xRecyclerView_price_description.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
            }
        });

        View footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 60);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        tv_status.setTextSize(10);
        footerView.setBackgroundColor(getResources().getColor(R.color.light_gray5));
        tv_status.setText("我是有底线的");
        tv_status.setTextColor(getResources().getColor(R.color.normal_gray5));
        xRecyclerView_price_description.setFootView(footerView);
        xRecyclerView_price_description.loadMoreComplete();

    }

}
