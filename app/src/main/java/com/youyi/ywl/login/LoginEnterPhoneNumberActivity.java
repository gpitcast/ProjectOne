package com.youyi.ywl.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PhoneNumberCheckedUtil;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2018/9/29.
 * 登录 输入手机号码界面
 */

public class LoginEnterPhoneNumberActivity extends BaseActivity implements PlatformActionListener {
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.tv_next_step)
    TextView tv_next_step;

    private static final String CREATE_URL = HttpUtils.Host + "/user2/create";//输入手机号码判断注册还是登录接口
    private static final String NOTIFY_URL = HttpUtils.Host + "/user2/notify";//第三方的opneId登录
    private String type;//第三方登陆类型(qq,sina,weixin)
    private String openId;//微信登录的唯一用户标识openId
    private PlatformDb platDB; //平台授权数据DB

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case CREATE_URL:
                dismissLoadingDialog();

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        String handle = resultMap.get("handle") + "";//用户接下来的操作值
                        if ("register".equals(handle)) {
                            //注册,跳转到验证注册手机界面
                            Intent intent = new Intent(this, LoginYanzhengCodeActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("phone", et_phone_number.getText().toString().trim());
                            startActivity(intent);
                        } else if ("login".equals(handle)) {
                            //登录,跳转到输入密码界面
                            Intent intent = new Intent(this, LoginEnterPswActivity.class);
                            intent.putExtra("phone", et_phone_number.getText().toString().trim());
                            intent.putExtra("type", handle);
                            startActivity(intent);
                        }
                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case NOTIFY_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {

                        HashMap dataMap = (HashMap) resultMap.get("data");
                        Constanst.userNickName = dataMap.get("nickname") + "";
                        Constanst.userRealName = dataMap.get("realname") + "";
                        Constanst.userPhoneNum = dataMap.get("username") + "";
                        Constanst.userHeadImg = dataMap.get("img") + "";
                        Constanst.userSex = dataMap.get("sex") + "";
                        Constanst.userBirthday = dataMap.get("birthday") + "";
                        Constanst.userAddress = dataMap.get("address") + "";
                        Constanst.user_role_id = dataMap.get("role_id") + "";

                        String step = resultMap.get("step") + "";
                        if ("1".equals(step)) {
                            Intent intent = new Intent(this, LoginSelectIdentityActivity.class);
                            startActivity(intent);
                        } else if ("2".equals(step)) {
                            Intent intent = new Intent(this, LoginSelectGradeActivity.class);
                            startActivity(intent);
                        } else if ("0".equals(step)) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else if ("1".equals(status)) {
                        //未绑定手机号码,跳转到绑定手机号码
                        Intent intent = new Intent(this, LoginPhoneNumBindActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("openId", openId);
                        startActivity(intent);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_enter_phone_number);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        tv_next_step.setClickable(false);
        listenEtChanged();//添加输入手机号EditText的监听
    }

    private void listenEtChanged() {
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = et_phone_number.getText().toString().trim();
                if (number != null && number.length() >= 11) {
                    tv_next_step.setClickable(true);
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_white);
                } else {
                    tv_next_step.setClickable(false);
                    tv_next_step.setBackgroundResource(R.drawable.bg_login_next_step_blue);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void afterInitView() {
    }


    @OnClick({R.id.tv_next_step, R.id.iv_wechat_login, R.id.iv_qq_login, R.id.iv_weibo_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                //下一步
                SoftUtil.hideSoft(this);
                String numberStr = et_phone_number.getText().toString().trim();
                if (PhoneNumberCheckedUtil.checkNumber(numberStr)) {
                    PostCreateCountList();
                } else {
                    ToastUtil.show(this, "请输入正确的手机号码", 0);
                }
                break;
            case R.id.iv_wechat_login:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                authorize(wechat);
                break;
            case R.id.iv_qq_login:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                authorize(qq);
                break;
            case R.id.iv_weibo_login:
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                authorize(weibo);
                break;
        }
    }

    /**
     * 第三方授权登录
     *
     * @param plat
     */
    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }

        //判断指定平台是否已经完成授权
        if (plat.isAuthValid()) {
            String token = plat.getDb().getToken();
            String userId = plat.getDb().getUserId();
            String userName = plat.getDb().getUserName();
            String userGender = plat.getDb().getUserGender();
            String userIcon = plat.getDb().getUserIcon();

            String platformNname = plat.getDb().getPlatformNname();
            if (userId != null) {
                //已经授权过,直接进行下一步操作
                if (platformNname.equals(SinaWeibo.NAME)) {
                    //微博授权
                    //请求第三方登录接口
                    type = "sina";
                    openId = userId;
                    PostThridLoginList();
                } else if (platformNname.equals(QQ.NAME)) {
                    //QQ授权
                    //请求第三方登录接口
                    type = "qq";
                    openId = userId;
                    PostThridLoginList();
                } else if (platformNname.equals(Wechat.NAME)) {
                    //微信授权
                    type = "weixin";
                    openId = userId;
                    PostThridLoginList();
                }
                return;
            }
        }

        //true不使用SSO授权,false使用SSO授权
        plat.SSOSetting(false);
        plat.setPlatformActionListener(this);

        //下面两种方法只能调一种
//        plat.authorize();
        //获取用户资料
        plat.showUser(null);
    }


    //授权回调成功
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        String headImageUrl = null;//头像
        String userId = null;//用户Id
        String token = null;//token
        String gender = null;//性别
        String name = null;//用户名

        if (i == Platform.ACTION_USER_INFOR) {

            platDB = platform.getDb();
            if (platform.getName().equals(Wechat.NAME)) {
                //微信登录
                //通过DB获取各种数据
                token = platDB.getToken();
                userId = platDB.getUserId();
                name = platDB.getUserName();
                gender = platDB.getUserGender();
                headImageUrl = platDB.getUserIcon();

                if ("m".equals(gender)) {
                    gender = "1";
                } else {
                    gender = "2";
                }

                //请求第三方登录接口
                type = "weixin";
                openId = userId;
                PostThridLoginList();

            } else if (platform.getName().equals(QQ.NAME)) {
                //QQ登录
                token = platDB.getToken();
                userId = platDB.getUserId();
                name = hashMap.get("nickname").toString();
                gender = hashMap.get("gender").toString();
                headImageUrl = hashMap.get("figureurl_qq_2").toString();// 头像figureurl_qq_2 中等图，figureurl_qq_1缩略图

                //请求第三方登录接口
                type = "qq";
                openId = userId;
                PostThridLoginList();

            } else if (platform.getName().equals(SinaWeibo.NAME)) {
                //微博登录
                token = platDB.getToken();
                userId = platDB.getUserId();
                name = hashMap.get("nickname").toString(); // 名字
                gender = hashMap.get("gender").toString(); // 年龄
                headImageUrl = hashMap.get("figureurl_qq_2").toString(); // 头像figureurl_qq_2 中等图，figureurl_qq_1缩略图

                //请求第三方登录接口
                type = "sina";
                openId = userId;
                PostThridLoginList();

            }
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtil.show(this, "授权回调出错", 0);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtil.show(this, "取消授权回调", 0);
    }

    @Subscribe
    public void onEventMainThread(String str) {
        if (str == null) {
            return;
        }

        switch (str) {
            case "关闭输入手机号码界面":
                finish();
                break;
        }
    }

    /**
     * 请求第三方登录接口
     */
    public void PostThridLoginList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "notify");
        map.put("type", type);
        map.put("type_id", openId);
        getJsonUtil().PostJson(this, map);
    }

    /**
     * 请求创建账号接口
     */
    private void PostCreateCountList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "create");
        map.put("tel", et_phone_number.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_next_step);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
