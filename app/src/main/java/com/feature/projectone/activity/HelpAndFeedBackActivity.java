package com.feature.projectone.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.ShareUtil;
import com.feature.projectone.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/18.
 * 帮助与反馈界面
 */

public class HelpAndFeedBackActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_number)
    EditText et_number;
    @BindView(R.id.ll_submit)
    LinearLayout ll_submit;
    @BindView(R.id.tv_submit)
    TextView tv_submit;

    private static final String bangzhuUrl = HttpUtils.Host + "/bangzhu/add";//意见和反馈接口
    private String contentText;
    private String numberText;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case bangzhuUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = ((Integer) resultMap.get("status")) + "";
                    if ("0".equals(status)) {
                        //成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        et_content.setText("");
                        et_number.setText("");
                    } else {
                        //失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                dismissLoadingDialog();
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_help_and_feedback);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("帮助与反馈");
        listenEtChanged();//设置文本变换按钮变化监听
    }

    /**
     * 监听文本发生变化时,按钮随之变化
     */
    private void listenEtChanged() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //检查所有的editText内容是否为空
    private void checkedText() {
        contentText = et_content.getText().toString().trim();
        numberText = et_number.getText().toString().trim();
        if (!TextUtils.isEmpty(contentText) && contentText.length() > 0 && !TextUtils.isEmpty(numberText) && et_number.length() > 0) {
            ll_submit.setBackgroundResource(R.drawable.bg_login_dark_btn);
            tv_submit.setEnabled(true);
        } else {
            ll_submit.setBackgroundResource(R.drawable.bg_login_light_btn);
            tv_submit.setEnabled(false);
        }
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.ll_common_problem, R.id.ll_forbid_report, R.id.ll_community_aggregation_order, R.id.tv_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_common_problem:
                //常见问题
                startActivity(new Intent(this, CommonProblemActivity.class));
                break;
            case R.id.ll_forbid_report:
                //违规举报
                startActivity(new Intent(this, ForbidReportActivity.class));
                break;
            case R.id.ll_community_aggregation_order:
                //社区集结令
                break;
            case R.id.tv_submit:
                //提交按钮,提交意见反馈
                //提交意见和反馈需要用户登录,检查是否登录
                if (ShareUtil.isExist(this, Constanst.UER_TOKEN)) {
                    //已经登录
                    showLoadingDialog();
                    PostList();
                } else {
                    //未登录
                    ToastUtil.show(this, getResources().getString(R.string.need_login_first), 0);
                }
                break;
        }
    }

    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "bangzhu");
        map.put("action", "add");
        map.put("content", contentText);
        map.put("contact_way", numberText);
        getJsonUtil().PostJson(this, map, tv_submit);
    }
}
