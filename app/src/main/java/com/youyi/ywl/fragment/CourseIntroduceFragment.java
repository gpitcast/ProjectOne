package com.youyi.ywl.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.ywl.R;
import com.youyi.ywl.adapter.FitPeopleOrTeachingTargetAdapter;
import com.youyi.ywl.adapter.LecturerIntroduceAdapter;
import com.youyi.ywl.adapter.PriceDescriptionAdapter;
import com.youyi.ywl.adapter.YouhuiReceiverAdapter;
import com.youyi.ywl.adapter.YouhuiVolumeReceiveAdapter;
import com.youyi.ywl.inter.RecyclerViewOnItemClickListener;
import com.youyi.ywl.inter.ReplyIconClickListener;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.util.WebViewUtil;
import com.youyi.ywl.view.BasePopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/16.
 * 精品课程-详情  课程介绍fragment
 */

@SuppressLint("ValidFragment")
public class CourseIntroduceFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView_fit_people)
    XRecyclerView xRecyclerView_fit_people;
    @BindView(R.id.xRecyclerView_teaching_target)
    XRecyclerView xRecyclerView_teaching_target;
    @BindView(R.id.xRecyclerView_lecturer)
    RecyclerView xRecyclerView_lecturer;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.xRecyclerView_price_description)
    XRecyclerView xRecyclerView_price_description;
    @BindView(R.id.ll_base)
    LinearLayout ll_base;

    private FitPeopleOrTeachingTargetAdapter fitPeopleOrTeachingTargetAdapter1;
    private FitPeopleOrTeachingTargetAdapter fitPeopleOrTeachingTargetAdapter2;
    private LecturerIntroduceAdapter lecturerIntroduceAdapter;
    private PriceDescriptionAdapter priceDescriptionAdapter;
    private TextView tv_status;
    private YouhuiVolumeReceiveAdapter youhuiVolumeReceiveAdapter;
    private YouhuiReceiverAdapter youhuiReceiverAdapter;
    private List<HashMap<String, Object>> mTeacherList = new ArrayList<>();//老师列表
    private String id;//课程id

    public CourseIntroduceFragment(String id) {
        this.id = id;
    }

    @Override
    public void onLazyLoad() {
        PostList();
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_course_introduce;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        WebViewUtil.setWebViewSettings(webView);

        initFitPeopleRecyclerView();//初始化适用人群recyclerview
        initTeachingTargetRecyclerView();//初始化教学目标recyclerview
        initLecturerRecyclerView();//初始化讲师介绍recyclerview
        initPriceDescriptionRecyclerView();//初始化价格说明recyclerView
    }

    public void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "video");
        map.put("action", "detail");
        map.put("id", id);
        getJsonUtil().PostJson(getActivity(), map);
    }

    //初始化价格说明recyclerView
    private void initPriceDescriptionRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
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

    //初始化讲师介绍
    private void initLecturerRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        xRecyclerView_lecturer.setLayoutManager(manager);
        lecturerIntroduceAdapter = new LecturerIntroduceAdapter(getActivity(), mTeacherList);
        xRecyclerView_lecturer.setAdapter(lecturerIntroduceAdapter);

        lecturerIntroduceAdapter.setOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                showTeacherIntroduceDialog();
            }
        });
    }

    //显示 老师介绍 dialog
    private void showTeacherIntroduceDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(getActivity(), R.style.mDialog);
        View dialogView = inflater.inflate(R.layout.layout_teacher_introduce_dialog, null);

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

    //初始化 适合人群 recyclerview
    private void initFitPeopleRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        xRecyclerView_fit_people.setLayoutManager(manager);
        fitPeopleOrTeachingTargetAdapter1 = new FitPeopleOrTeachingTargetAdapter(getActivity());
        xRecyclerView_fit_people.setAdapter(fitPeopleOrTeachingTargetAdapter1);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView_fit_people.addHeaderView(headView1);
        xRecyclerView_fit_people.addHeaderView(headView2);
        xRecyclerView_fit_people.setPullRefreshEnabled(false);
        xRecyclerView_fit_people.setLoadingMoreEnabled(false);
        xRecyclerView_fit_people.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

    }

    //显示 教学目的 recyclerview
    private void initTeachingTargetRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        xRecyclerView_teaching_target.setLayoutManager(manager);
        fitPeopleOrTeachingTargetAdapter2 = new FitPeopleOrTeachingTargetAdapter(getActivity());
        xRecyclerView_teaching_target.setAdapter(fitPeopleOrTeachingTargetAdapter2);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView_teaching_target.addHeaderView(headView1);
        xRecyclerView_teaching_target.addHeaderView(headView2);
        xRecyclerView_teaching_target.setPullRefreshEnabled(false);
        xRecyclerView_teaching_target.setLoadingMoreEnabled(false);
        xRecyclerView_teaching_target.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
    }

    @OnClick({R.id.ll_receive_youhui_volume, R.id.ll_youhui_service})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_receive_youhui_volume:
                //领取优惠券
                showReceiveYouhuiVolumePop();
                break;
            case R.id.ll_youhui_service:
                //优惠服务
                showYouhuiServicePop();
                break;
        }
    }

    private View popwindowView2;
    private BasePopupWindow popupWindow2;

    //显示优惠服务的pop
    private void showYouhuiServicePop() {
        if (popupWindow2 == null) {
            popwindowView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_youhui_service_pop, null);
            popwindowView2.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow2 != null) {
                        popupWindow2.dismiss();
                    }
                }
            });

            XRecyclerView xRecyclerView = (XRecyclerView) popwindowView2.findViewById(R.id.xRecyclerView);
            initYouhuiServiceRecycler(xRecyclerView);
        }

        if (popupWindow2 == null) {
            popupWindow2 = new BasePopupWindow(getActivity());
            popupWindow2.setContentView(popwindowView2);
            popupWindow2.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow2.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow2.setOutsideTouchable(true);
            popupWindow2.setFocusable(true);
        }

        //设置popwindow的位置
        popupWindow2.showAtLocation(ll_base, Gravity.BOTTOM, 0, 0);
    }

    //初始化优惠服务recyclerview
    private void initYouhuiServiceRecycler(XRecyclerView xRecyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        xRecyclerView.setLayoutManager(manager);
        youhuiReceiverAdapter = new YouhuiReceiverAdapter(getActivity());
        xRecyclerView.setAdapter(youhuiReceiverAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
    }

    private View popwindowView1;
    private BasePopupWindow popupWindow1;

    //显示领取优惠券的pop
    private void showReceiveYouhuiVolumePop() {
        if (popwindowView1 == null) {
            popwindowView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_receive_youhui_volume_pop, null);
            popwindowView1.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow1 != null) {
                        popupWindow1.dismiss();
                    }
                }
            });

            XRecyclerView xRecyclerView = (XRecyclerView) popwindowView1.findViewById(R.id.xRecyclerView);
            initReceiveYouhuiVolumeRecycler(xRecyclerView);
        }

        if (popupWindow1 == null) {
            popupWindow1 = new BasePopupWindow(getActivity());
            popupWindow1.setContentView(popwindowView1);
            popupWindow1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow1.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow1.setOutsideTouchable(true);
            popupWindow1.setFocusable(true);
        }

        //设置popwindow的位置
        popupWindow1.showAtLocation(ll_base, Gravity.BOTTOM, 0, 0);
    }

    //初始化领取优惠券的pop的recyclerview
    private void initReceiveYouhuiVolumeRecycler(XRecyclerView xRecyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        xRecyclerView.setLayoutManager(manager);
        youhuiVolumeReceiveAdapter = new YouhuiVolumeReceiveAdapter(getActivity());
        xRecyclerView.setAdapter(youhuiVolumeReceiveAdapter);
        View headView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);

        youhuiVolumeReceiveAdapter.setOnIconClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                ToastUtil.show(getActivity(), "点击了第" + position + "个条目", 0);
            }
        });
    }
}
