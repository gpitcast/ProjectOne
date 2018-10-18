package com.youyi.ywl.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/30.
 * 学习fragment
 */

public class StudyFragment extends BaseFragment {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.iv_example_explain)
    ImageView iv_example_explain;
    @BindView(R.id.tv_example_explain)
    TextView tv_example_explain;
    @BindView(R.id.iv_online_hearing)
    ImageView iv_online_hearing;
    @BindView(R.id.tv_online_hearing)
    TextView tv_online_hearing;
    @BindView(R.id.iv_vedio_class)
    ImageView iv_vedio_class;
    @BindView(R.id.tv_vedio_class)
    TextView tv_vedio_class;
    @BindView(R.id.iv_onlive_class)
    ImageView iv_onlive_class;
    @BindView(R.id.tv_onlive_class)
    TextView tv_onlive_class;


    private ExampleExplainFragment exampleExplainFragment;
    private OnlineHearingFragment onlineHearingFragment;
    private VedioClassFragment vedioClassFragment;
    private OnliveClassFragment onliveClassFragment;
    private Dialog deleteDialog;
    private int showFragmentIndex = -1;//记录显示的fragment的index

    @Override
    public void onLazyLoad() {
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        ll_back.setVisibility(View.GONE);
        tv_title.setText("学习轨迹");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("清空");

        showExampleExplainFragment();
    }

    //显示习题讲解fragment
    private void showExampleExplainFragment() {
        showFragmentIndex = 0;

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (exampleExplainFragment == null) {
            exampleExplainFragment = new ExampleExplainFragment();
            transaction.add(R.id.container, exampleExplainFragment);
        }
        hideFragment(transaction);
        transaction.show(exampleExplainFragment);
        transaction.commit();
    }

    //显示在线听力的fragment
    public void showOnlineHearingFragment() {
        showFragmentIndex = 1;

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (onlineHearingFragment == null) {
            onlineHearingFragment = new OnlineHearingFragment();
            transaction.add(R.id.container, onlineHearingFragment);
        }
        hideFragment(transaction);
        transaction.show(onlineHearingFragment);
        transaction.commit();
    }

    //显示视频课的fragment
    public void showVedioClassFragment() {
        showFragmentIndex = 2;

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (vedioClassFragment == null) {
            vedioClassFragment = new VedioClassFragment();
            transaction.add(R.id.container, vedioClassFragment);
        }
        hideFragment(transaction);
        transaction.show(vedioClassFragment);
        transaction.commit();
    }

    //显示直播课的fragment
    public void showOnliveClassFragment() {
        showFragmentIndex = 3;

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (onliveClassFragment == null) {
            onliveClassFragment = new OnliveClassFragment();
            transaction.add(R.id.container, onliveClassFragment);
        }
        hideFragment(transaction);
        transaction.show(onliveClassFragment);
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (exampleExplainFragment != null) {
            transaction.hide(exampleExplainFragment);
        }

        if (onlineHearingFragment != null) {
            transaction.hide(onlineHearingFragment);
        }

        if (vedioClassFragment != null) {
            transaction.hide(vedioClassFragment);
        }

        if (onliveClassFragment != null) {
            transaction.hide(onliveClassFragment);
        }
    }

    private int selectedIndex = 0;//记录被选中的条目角标,默认选中第一个

    @OnClick({R.id.ll_example_explain, R.id.ll_online_hearing, R.id.ll_vedio_class, R.id.ll_onlive_class, R.id.tv_right})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_example_explain:
                if (selectedIndex != 0) {
                    navigationBarSelected(0);
                    showExampleExplainFragment();
                    selectedIndex = 0;
                }
                break;
            case R.id.ll_online_hearing:
                if (selectedIndex != 1) {
                    navigationBarSelected(1);
                    showOnlineHearingFragment();
                    selectedIndex = 1;
                }
                break;
            case R.id.ll_vedio_class:
                if (selectedIndex != 2) {
                    navigationBarSelected(2);
                    showVedioClassFragment();
                    selectedIndex = 2;
                }
                break;
            case R.id.ll_onlive_class:
                if (selectedIndex != 3) {
                    navigationBarSelected(3);
                    showOnliveClassFragment();
                    selectedIndex = 3;
                }
                break;
            case R.id.tv_right:
                showDeleteDialog();
                break;
        }
    }

    //显示删除的对话框
    private void showDeleteDialog() {
        deleteDialog = new Dialog(getActivity(), R.style.mDialog);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.delete_learning_dialog, null);
        //取消
        dialogView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteDialog != null) {
                    deleteDialog.dismiss();
                }
            }
        });
        //清空
        dialogView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                if (deleteDialog != null) {
                    deleteDialog.dismiss();
                }
            }
        });

        deleteDialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deleteDialog.setCancelable(false);
        deleteDialog.show();
    }

    //清空数据
    private void deleteData() {
        switch (showFragmentIndex) {
            case 0:
                //习题讲解被选中
                exampleExplainFragment.deleteData();
                break;
            case 1:
                //在线听力被选中
                onlineHearingFragment.deleteData();
                break;
            case 2:
                //视频课被选中
                vedioClassFragment.deleteData();
                break;
            case 3:
                //直播课被选中

                break;
        }
    }

    //改变导航栏状态
    private void navigationBarSelected(int index) {
        switch (index) {
            case 0:
                //选中习题讲解
                iv_example_explain.setImageResource(R.mipmap.icon_example_explain_blue);
                tv_example_explain.setTextColor(getContext().getResources().getColor(R.color.orangeone));

                if (selectedIndex == 1) {
                    iv_online_hearing.setImageResource(R.mipmap.icon_online_hearing_gray);
                    tv_online_hearing.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 2) {
                    iv_vedio_class.setImageResource(R.mipmap.icon_vedio_class_gray);
                    tv_vedio_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 3) {
                    iv_onlive_class.setImageResource(R.mipmap.icon_onlive_class_gray);
                    tv_onlive_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                break;
            case 1:
                //选中在线听力
                if (selectedIndex == 0) {
                    iv_example_explain.setImageResource(R.mipmap.icon_example_explain_gray);
                    tv_example_explain.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }

                iv_online_hearing.setImageResource(R.mipmap.icon_online_hearing_blue);
                tv_online_hearing.setTextColor(getContext().getResources().getColor(R.color.orangeone));

                if (selectedIndex == 2) {
                    iv_vedio_class.setImageResource(R.mipmap.icon_vedio_class_gray);
                    tv_vedio_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 3) {
                    iv_onlive_class.setImageResource(R.mipmap.icon_onlive_class_gray);
                    tv_onlive_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }

                break;
            case 2:
                //选中视频课
                if (selectedIndex == 0) {
                    iv_example_explain.setImageResource(R.mipmap.icon_example_explain_gray);
                    tv_example_explain.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 1) {
                    iv_online_hearing.setImageResource(R.mipmap.icon_online_hearing_gray);
                    tv_online_hearing.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }

                iv_vedio_class.setImageResource(R.mipmap.icon_vedio_class_blue);
                tv_vedio_class.setTextColor(getContext().getResources().getColor(R.color.orangeone));

                if (selectedIndex == 3) {
                    iv_onlive_class.setImageResource(R.mipmap.icon_onlive_class_gray);
                    tv_onlive_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                break;
            case 3:
                //选中直播课
                if (selectedIndex == 0) {
                    iv_example_explain.setImageResource(R.mipmap.icon_example_explain_gray);
                    tv_example_explain.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 1) {
                    iv_online_hearing.setImageResource(R.mipmap.icon_online_hearing_gray);
                    tv_online_hearing.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }
                if (selectedIndex == 2) {
                    iv_vedio_class.setImageResource(R.mipmap.icon_vedio_class_gray);
                    tv_vedio_class.setTextColor(getContext().getResources().getColor(R.color.light_gray16));
                }

                iv_onlive_class.setImageResource(R.mipmap.icon_onlive_class_blue);
                tv_onlive_class.setTextColor(getContext().getResources().getColor(R.color.orangeone));
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (exampleExplainFragment != null && exampleExplainFragment.isVisible()) {
                exampleExplainFragment.loadData();

            }
            if (onlineHearingFragment != null && onlineHearingFragment.isVisible()) {
                onlineHearingFragment.loadData();
            }
            if (vedioClassFragment != null && vedioClassFragment.isVisible()) {
                vedioClassFragment.loadData();
            }
        }
    }

}
