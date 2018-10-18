package com.youyi.ywl.login;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/30.
 * 登录 选择身份界面
 */

public class LoginSelectIdentityActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_student_title)
    TextView tv_student_title;
    @BindView(R.id.tv_student_content)
    TextView tv_student_content;
    @BindView(R.id.iv_student_icon)
    ImageView iv_student_icon;
    @BindView(R.id.tv_teacher_title)
    TextView tv_teacher_title;
    @BindView(R.id.tv_teacher_content)
    TextView tv_teacher_content;
    @BindView(R.id.iv_teacher_icon)
    ImageView iv_teacher_icon;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;

    private static final String ROLE_URL = HttpUtils.Host + "/user2/setRole";

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case ROLE_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        Intent intent = new Intent(this, LoginSelectGradeActivity.class);
                        startActivity(intent);
                        finish();
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
        setContentView(R.layout.activity_login_select_identity);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("选择身份");
    }

    @Override
    public void afterInitView() {
    }

    private int selectedType = 1;//记录选中的状态 1代表学生 2代表老师 默认选择学生
    private boolean isNext = false;

    @OnClick({R.id.ll_back, R.id.ll_student, R.id.ll_teacher, R.id.tv_next_step, R.id.tv_user_protocol})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_student:
                if (selectedType == 2) {
                    tv_teacher_title.setTextColor(getResources().getColor(R.color.normal_blue4));
                    tv_teacher_content.setTextColor(getResources().getColor(R.color.normal_blue4));
                    iv_teacher_icon.setImageResource(R.mipmap.icon_no_choosed_circle_blue);
                }

                if (selectedType != 1) {
                    selectedType = 1;
                    tv_student_title.setTextColor(getResources().getColor(R.color.white));
                    tv_student_content.setTextColor(getResources().getColor(R.color.white));
                    iv_student_icon.setImageResource(R.mipmap.icon_choosed_circle_white);
                }

                if (!isNext) {
                    isNext = true;
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
                }
                break;
            case R.id.ll_teacher:
                if (selectedType == 1) {
                    tv_student_title.setTextColor(getResources().getColor(R.color.normal_blue4));
                    tv_student_content.setTextColor(getResources().getColor(R.color.normal_blue4));
                    iv_student_icon.setImageResource(R.mipmap.icon_no_choosed_circle_blue);
                }

                if (selectedType != 2) {
                    selectedType = 2;
                    tv_teacher_title.setTextColor(getResources().getColor(R.color.white));
                    tv_teacher_content.setTextColor(getResources().getColor(R.color.white));
                    iv_teacher_icon.setImageResource(R.mipmap.icon_choosed_circle_white);
                }

                if (!isNext) {
                    isNext = true;
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
                }
                break;
            case R.id.tv_next_step:
                //下一步
                PostSetRoleList();
                break;
            case R.id.tv_user_protocol:
                //用户协议
                Intent intent = new Intent(this, LoginUserProtocolActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 请求设置身份接口
     */
    private void PostSetRoleList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "setRole");
        map.put("role", selectedType + "");
        getJsonUtil().PostJson(this, map, tv_next_step);
    }
}
