package com.youyi.YWL.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youyi.YWL.R;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.util.HttpUtils;
import com.youyi.YWL.util.LoginCarrier;
import com.youyi.YWL.util.LoginInterceptor;
import com.youyi.YWL.util.MsgCodeUtil;
import com.youyi.YWL.util.PhoneNumberCheckedUtil;
import com.youyi.YWL.util.ShareUtil;
import com.youyi.YWL.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/3.
 * 注册界面
 */

public class RegistActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.iv_isAgree)
    ImageView iv_isAgree;
    @BindView(R.id.tv_get_number)
    TextView tv_get_number;
    @BindView(R.id.tv_regist)
    TextView tv_regist;
    @BindView(R.id.tv_student_type)
    TextView tv_student_type;
    @BindView(R.id.tv_teacher_type)
    TextView tv_teacher_type;
    @BindView(R.id.rl_isAgree)
    RelativeLayout rl_isAgree;

    private static final String registUrl = HttpUtils.Host + "/user/register";//注册接口
    private static final String loginUrl = HttpUtils.Host + "/user/login";//登录接口
    private static final String isRegistUrl = HttpUtils.Host + "/user/is_register";//用户是否注册接口
    private int userType = 1;//用户的类型 学生为0,老师为1  默认是学生
    private boolean isCodeWork;//验证码是否超时
    private boolean isAgree = true;//是否同意了协议(默认同意)
    private int restSeconds = Constanst.downSeconds;//验证码倒计时总时长
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            restSeconds = restSeconds - 1;
            if (restSeconds != 0) {
                tv_get_number.setText(restSeconds + "s后重试");
                handler.postDelayed(this, 1000);
            } else {
                isCodeWork = false;
                tv_get_number.setText(getString(R.string.get_number));
                tv_get_number.setEnabled(true);
                handler.removeCallbacks(this);
                restSeconds = Constanst.downSeconds;
            }
        }
    };

    @Override
    protected void Response(String code, String msg, String url, Object result) {

        switch (url) {
            case isRegistUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        // 未被注册，执行注册接口
                        regist();
                    } else {
                        dismissLoadingDialog();
                        //已经被注册，弹出吐司提醒用户
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case registUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //注册成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        //注册成功自动登录
                        autoLogin();
                    } else {
                        dismissLoadingDialog();
                        //注册失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case loginUrl:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    Integer status = (Integer) resultMap.get("status");
                    if (status == 0) {
                        //登录成功
                        //存储登录的用户名和用户密码
                        ShareUtil.putString(this, "username", et_phone.getText().toString().trim());
                        ShareUtil.putString(this, "psw", et_psw.getText().toString().trim());

                        //登录成功后
                        String type = this.getIntent().getStringExtra("type");
                        if (type != null && "退出登录".equals(type)) {
                            //从设置界面退出登录方式跳转到登录界面的,登陆成功跳转到主界面
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        } else if (type != null && "立即登入".equals(type)) {
                            //点击头像的'立即登入'方式跳转到登录界面的,登陆成功跳转到主界面
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            EventBus.getDefault().post("刷新MainActivity");
                        } else if (type != null && "消息登入".equals(type)) {
                            EventBus.getDefault().post("关闭MainActivity");
                            //点击消息界面的登录或者注册跳转到登录界面的,登录成功跳转到主界面
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            EventBus.getDefault().post("刷新MainActivity");
                        } else {
                            //从其他有意图的界面跳转到登录界面的,登录成功跳转到意图跳转的界面,或者点赞等功能的当前界面
                            EventBus.getDefault().post("刷新MainActivity");
                            LoginCarrier invoker = (LoginCarrier) this.getIntent().getParcelableExtra(LoginInterceptor.mINVOKER);
                            invoker.invoke(this);
                        }
                        finish();
                    } else {
                        //登录失败
                        String message = (String) resultMap.get("msg");
                        ToastUtil.show(this, message, 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }


    //注册
    private void regist() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "register");
        map.put("type", userType + "");
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        map.put("is_agree", "1");
        getJsonUtil().PostJson(this, map, tv_regist);
    }

    //自动登录
    private void autoLogin() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "login");
        map.put("type", userType + "");
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_regist);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_regist);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.tv_regist, R.id.ll_isAgree, R.id.tv_get_number, R.id.tv_regist_agreement, R.id.tv_student_type, R.id.tv_teacher_type, R.id.tv_goto_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_regist:
                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_phone_number), 0);
                    return;
                }
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                    return;
                }
                if (et_code.getText().toString().trim() == null || et_code.getText().toString().trim().length() != 6) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_code), 0);
                    return;
                }
                if (!et_code.getText().toString().trim().equals(Constanst.phoneCode)) {
                    ToastUtil.show(this, getString(R.string.phone_code_error), 0);
                    return;
                }
                if (et_psw.getText().toString().trim() == null || et_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_psw), 0);
                    return;
                }
                if (!isAgree) {
                    ToastUtil.show(this, getString(R.string.agree_regist_agreement), 0);
                    return;
                }
                if (!isCodeWork) {
                    ToastUtil.show(this, getString(R.string.phone_code_time_out), 0);
                    return;
                }
                showLoadingDialog();
                ///请求用户是否注册接口
                isRegist();
                break;
            case R.id.ll_isAgree:
                if (isAgree) {
                    iv_isAgree.setVisibility(View.GONE);
                    rl_isAgree.setBackgroundResource(R.drawable.bg_white_line);
                    isAgree = false;
                } else {
                    rl_isAgree.setBackgroundResource(R.drawable.bg_green_circle);
                    iv_isAgree.setVisibility(View.VISIBLE);
                    isAgree = true;
                }
                break;
            case R.id.tv_get_number:
                if (PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    isCodeWork = true;
                    MsgCodeUtil.sendMsg(this, et_phone.getText().toString().trim(), Constanst.registSignature, Constanst.registTemplate_regist);
                    tv_get_number.setText(restSeconds + "s后重试");
                    tv_get_number.setEnabled(false);
                    handler.postDelayed(runnable, 1000);
                } else {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                }
                break;
            case R.id.tv_regist_agreement:
                startActivity(new Intent(this, RegistAgreementActivty.class));
                break;
            case R.id.tv_student_type:
                //学生类型
                if (userType != 1) {
                    //当选中的不是学生类型的时候做操作
                    userType = 1;

                    tv_student_type.setTextColor(getResources().getColor(R.color.orangeone));
                    tv_student_type.setBackground(getResources().getDrawable(R.drawable.bg_blue_small_circle_line));

                    tv_teacher_type.setTextColor(getResources().getColor(R.color.white));
                    tv_teacher_type.setBackground(getResources().getDrawable(R.drawable.bg_blue_choose_student));
                }
                break;
            case R.id.tv_teacher_type:
                //老师类型
                if (userType != 2) {
                    //当选中的不是老师类型的时候做操作
                    userType = 2;

                    tv_student_type.setTextColor(getResources().getColor(R.color.white));
                    tv_student_type.setBackground(getResources().getDrawable(R.drawable.bg_blue_choose_student));

                    tv_teacher_type.setTextColor(getResources().getColor(R.color.orangeone));
                    tv_teacher_type.setBackground(getResources().getDrawable(R.drawable.bg_blue_small_circle_line));
                }
                break;
            case R.id.tv_goto_login:
                //去登录界面
                finish();
                break;
        }
    }

    //是否注册
    private void isRegist() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "is_register");
        map.put("username", et_phone.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_regist);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);//界面消失的时候删除该任务
    }
}
