package com.youyi.ywl.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/31.
 * 我的积分 - 首页界面
 */

public class MyCreditMainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_current_credit)
    TextView tv_current_credit;
    @BindView(R.id.tv_today_credit)
    TextView tv_today_credit;
    @BindView(R.id.tv_sign_in_credit)
    TextView tv_sign_in_credit;
    @BindView(R.id.tv_goto_sign_in)
    TextView tv_goto_sign_in;
    @BindView(R.id.tv_ws_date_credit)
    TextView tv_ws_date_credit;
    @BindView(R.id.tv_goto_perfect_personal_informmation)
    TextView tv_goto_perfect_personal_informmation;
    @BindView(R.id.tv_interest_credit)
    TextView tv_interest_credit;
    @BindView(R.id.tv_goto_receive)
    TextView tv_goto_receive;

    private static final String CREDIT_URL = HttpUtils.Host + "/user/myScore";//我的积分界面接口

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case CREDIT_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");

                        //当前积分
                        tv_current_credit.setText(dataMap.get("score") + "");
                        //今日获得积分
                        tv_today_credit.setText("当日获得积分 " + dataMap.get("todayScore") + " 分");
                        //用户签到
                        HashMap<String, Object> userSignDataMap = (HashMap<String, Object>) dataMap.get("userSignData");
                        tv_sign_in_credit.setText("奖励 " + userSignDataMap.get("score") + " 分");
                        String isSignIn = userSignDataMap.get("isSignIn") + "";//0代表用户没有签到 1用户已经签到
                        if ("0".equals(isSignIn)) {
                            //没有签到
                            tv_goto_sign_in.setBackgroundResource(R.drawable.bg_blue_dark_btn);
                            tv_goto_sign_in.setText("去签到");
                            tv_goto_sign_in.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //去签到按钮
                                    Intent intent = new Intent(MyCreditMainActivity.this, SignInActivity.class);
                                    intent.putExtra("isFromMyCredit", true);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //已签到
                            tv_goto_sign_in.setBackgroundResource(R.drawable.bg_dark_gray_half_circle);
                            tv_goto_sign_in.setText("已签到");
                            tv_goto_sign_in.setOnClickListener(null);
                        }

                        //用户完善资料
                        HashMap<String, Object> wsDataMap = (HashMap<String, Object>) dataMap.get("wsData");
                        tv_ws_date_credit.setText("奖励" + wsDataMap.get("score") + "分");
                        String isWs = wsDataMap.get("isWs") + "";//0代表用户没有完善了个人资料 1代表用户已经完善了个人资料
                        if ("0".equals(isWs)) {
                            //没有完善
                            tv_goto_perfect_personal_informmation.setBackgroundResource(R.drawable.bg_blue_half_circle_normal_line);
                            tv_goto_perfect_personal_informmation.setText("去完善");
                            tv_goto_perfect_personal_informmation.setTextColor(getResources().getColor(R.color.orangeone));
                            tv_goto_perfect_personal_informmation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //去完善个人信息
                                    Intent intent = new Intent(MyCreditMainActivity.this, UpdatePersonalDataActivity.class);
                                    intent.putExtra("isFromMyCredit", true);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //已经完善
                            tv_goto_perfect_personal_informmation.setBackgroundResource(R.drawable.bg_gray_half_circle_normal_line);
                            tv_goto_perfect_personal_informmation.setText("已完善");
                            tv_goto_perfect_personal_informmation.setTextColor(getResources().getColor(R.color.normal_gray2));
                            tv_goto_perfect_personal_informmation.setOnClickListener(null);
                        }

                        //用户设置角色和兴趣
                        HashMap<String, Object> roleDataMap = (HashMap<String, Object>) dataMap.get("roleData");
                        tv_interest_credit.setText("奖励" + roleDataMap.get("score") + "分");
                        String isRole = roleDataMap.get("isRole") + "";//0代表用户没有设置兴趣 1代表用户已经设置兴趣
                        if ("0".equals(isRole)) {
                            //没有设置兴趣
                            tv_goto_receive.setBackgroundResource(R.drawable.bg_blue_half_circle_normal_line);
                            tv_goto_receive.setText("去领取");
                            tv_goto_receive.setTextColor(getResources().getColor(R.color.orangeone));
                            tv_goto_receive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //选择标签或者更换标签
                                    Intent intent = new Intent(MyCreditMainActivity.this, UpdateTagActivity.class);
                                    intent.putExtra("isFromMyCredit", true);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            //设置了兴趣
                            tv_goto_receive.setBackgroundResource(R.drawable.bg_gray_half_circle_normal_line);
                            tv_goto_receive.setText("已领取");
                            tv_goto_receive.setTextColor(getResources().getColor(R.color.normal_gray2));
                            tv_goto_receive.setOnClickListener(null);
                        }

                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_credit_main);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("我的积分");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("积分说明");

        EventBus.getDefault().register(this);

        PostCreditList();//请求我的积分界面接口
    }

    //请求我的积分界面接口
    private void PostCreditList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "myScore");
        getJsonUtil().PostJson(this, map);
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.ll_credit_list, R.id.ll_credit_mall, R.id.iv_sign_in_instroction, R.id.iv_course_instroction})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_credit_list:
                //积分明细
                startActivity(new Intent(this, MyCreditListActivity.class));
                break;
            case R.id.ll_credit_mall:
                //积分商城
                startActivity(new Intent(this, CreditMallActivity.class));
                break;
            case R.id.iv_sign_in_instroction:
                //签到说明
                showSignInDialog();
                break;
            case R.id.iv_course_instroction:
                //课程评价说明
                showCourseDialog();
                break;
        }
    }

    private Dialog courseDialog;

    //显示课程说明的dialog
    private void showCourseDialog() {
        if (courseDialog == null) {
            courseDialog = new Dialog(this, R.style.mDialog);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.layout_rule_instroction_dialog, null);
            ((TextView) dialogView.findViewById(R.id.tv_content)).setText("主动评价一门收费课程可获得10个积分");
            ((TextView) dialogView.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (courseDialog != null) {
                        courseDialog.dismiss();
                    }
                }
            });
            ((TextView) dialogView.findViewById(R.id.tv_detail_rule)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(MyCreditMainActivity.this, "课程评价的详细规则", 0);
                    if (courseDialog != null) {
                        courseDialog.dismiss();
                    }
                }
            });
            courseDialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            courseDialog.setCancelable(false);
            courseDialog.show();
        } else {
            courseDialog.show();
        }
    }

    private Dialog signInDialog;

    //显示签到说明的dialog
    private void showSignInDialog() {
        if (signInDialog == null) {
            signInDialog = new Dialog(this, R.style.mDialog);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.layout_rule_instroction_dialog, null);
            ((TextView) dialogView.findViewById(R.id.tv_content)).setText("每日签到可获得相应积分,连续签到更高积分哟");
            ((TextView) dialogView.findViewById(R.id.tv_cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (signInDialog != null) {
                        signInDialog.dismiss();
                    }
                }
            });
            ((TextView) dialogView.findViewById(R.id.tv_detail_rule)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(MyCreditMainActivity.this, "签到说明的详细规则", 0);
                    if (signInDialog != null) {
                        signInDialog.dismiss();
                    }
                }
            });
            signInDialog.setContentView(dialogView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            signInDialog.setCancelable(false);
            signInDialog.show();
        } else {
            signInDialog.show();
        }
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }
        switch (str) {
            case "刷新我的积分首页数据":
                PostCreditList();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
