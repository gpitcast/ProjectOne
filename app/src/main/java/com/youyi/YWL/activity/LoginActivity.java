package com.youyi.YWL.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youyi.YWL.R;
import com.youyi.YWL.other.Constanst;
import com.youyi.YWL.other.MyApplication;
import com.youyi.YWL.util.HttpUtils;
import com.youyi.YWL.util.LoginCarrier;
import com.youyi.YWL.util.LoginInterceptor;
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
 * 登录界面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_psw)
    EditText et_psw;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_student_type)
    TextView tv_student_type;
    @BindView(R.id.tv_teacher_type)
    TextView tv_teacher_type;

    private static final String loginUrl = HttpUtils.Host + "/user/login";//登录接口
    private static final String isLoginUrl = HttpUtils.Host + "/user/is_login";//查看用户是否登陆
    private int userType = 1;//用户的类型 学生为0,老师为1  默认是学生
    private String type;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

        switch (url) {
            //是否登录接口
            case isLoginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //该账号处于登录状态，弹出吐司提醒用户
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        dismissLoadingDialog();
                    } else {
                        //该账号处于未登录状态，执行登录操作
                        Login();
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(this, msg, 0);
                }
                break;
            //登录接口
            case loginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = (Integer) resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //登录成功
                        //存储登录的用户名和用户密码
                        ShareUtil.putString(this, "username", et_phone.getText().toString().trim());
                        ShareUtil.putString(this, "psw", et_psw.getText().toString().trim());
                        dismissLoadingDialog();

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
                        dismissLoadingDialog();
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            type = intent.getStringExtra("type");
        }
    }

    //登录
    private void Login() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "login");
        map.put("type", userType + "");
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_login);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login);
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

    @OnClick({R.id.tv_login, R.id.tv_regist, R.id.tv_forget_psw, R.id.tv_student_type, R.id.tv_teacher_type, R.id.iv_wechat_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_phone_number), 0);
                    return;
                }
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
                    return;
                }
                if (et_psw.getText().toString().trim() == null || et_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(this, getString(R.string.write_right_phone_psw), 0);
                    return;
                }
                showLoadingDialog();
                //请求用户是否登陆接口
                isLogin();
                break;
            case R.id.tv_regist:
                //快速注册
                startActivity(new Intent(this, RegistActivity.class));
                break;
            case R.id.tv_forget_psw:
                //忘记密码
                startActivity(new Intent(this, ForgetPswActivity.class));
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
            case R.id.iv_wechat_login:
                //微信登录
                if (!MyApplication.wxapi.isWXAppInstalled()) {
                    ToastUtil.show(this, "您还未安装微信客户端", 0);
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                MyApplication.wxapi.sendReq(req);

                break;
        }
    }

    //用户是否登陆
    private void isLogin() {
        String user_token = ShareUtil.getString(this, "user_token");
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "is_login");
        if (user_token != null && user_token.length() > 0) {
            map.put("user_token", user_token);
        }
        getJsonUtil().PostJson(this, map, tv_login);
    }

    @Override
    public void onBackPressed() {
        if (type != null && "退出登录".equals(type)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }
}
