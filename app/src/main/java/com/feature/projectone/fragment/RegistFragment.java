package com.feature.projectone.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyuncs.profile.DefaultProfile;
import com.feature.projectone.R;
import com.feature.projectone.activity.MainActivity;
import com.feature.projectone.activity.RegistAgreementActivty;
import com.feature.projectone.fragment.BaseFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.MsgCodeUtil;
import com.feature.projectone.util.PhoneNumberCheckedUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.TvUtil;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
 * 注册fragment
 */

public class RegistFragment extends BaseFragment {
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
    @BindView(R.id.ll_get_number)
    LinearLayout ll_get_number;
    @BindView(R.id.ll_regist)
    LinearLayout ll_regist;
    @BindView(R.id.tv_regist)
    TextView tv_regist;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.rl_teacher)
    RelativeLayout rl_teacher;
    @BindView(R.id.ll_student)
    LinearLayout ll_student;
    @BindView(R.id.ll_teacher)
    LinearLayout ll_teacher;
    @BindView(R.id.iv_student)
    ImageView iv_student;
    @BindView(R.id.tv_student)
    TextView tv_student;
    @BindView(R.id.iv_teacher)
    ImageView iv_teacher;
    @BindView(R.id.tv_teacher)
    TextView tv_teacher;


    private static final String registUrl = HttpUtils.Host + "/user/register";//注册接口
    private static final String loginUrl = HttpUtils.Host + "/user/login";//登录接口
    private static final String isRegistUrl = HttpUtils.Host + "/user/is_register";//用户是否注册接口
    private String phoneText;
    private String pswText;
    private String coedText;
    private int type = 1;//注册的类型(学生为1,老师为2)默认为1
    private boolean isCodeWork;//验证码是否超时
    private boolean isAgree;//是否同意了协议
    private int restSeconds = 300;//验证码倒计时总时长
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
                ll_get_number.setBackgroundResource(R.drawable.bg_get_number_orange);
                tv_get_number.setTextColor(getResources().getColor(R.color.normal_orange));
                tv_get_number.setText(getString(R.string.get_number));
                TvUtil.addUnderLine(tv_get_number);
                tv_get_number.setEnabled(true);
                handler.removeCallbacks(this);
                restSeconds = 300;
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
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
            case registUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //注册成功
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                        //注册成功自动登录
                        autoLogin();
                    } else {
                        dismissLoadingDialog();
                        //注册失败
                        ToastUtil.show(getActivity(), resultMap.get("msg") + "", 0);
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
            case loginUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    Integer status = (Integer) resultMap.get("status");
                    if (status == 0) {
                        //登录成功
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        //登录失败
                        String message = (String) resultMap.get("msg");
                        ToastUtil.show(getActivity(), message, 0);
                    }
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(getActivity(), msg, 0);
                }
                break;
        }
    }

    //注册
    private void regist() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "register");
        map.put("type", type);
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        map.put("is_agree", "1");
        getJsonUtil().PostJson(getActivity(), map, ll_regist);
    }

    //自动登录
    private void autoLogin() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "login");
        map.put("type", "1");
        map.put("username", et_phone.getText().toString().trim());
        map.put("password", et_psw.getText().toString().trim());
        getJsonUtil().PostJson(getActivity(), map, ll_regist);
    }

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
        return R.layout.fragment_regist;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        EtDrawableLeftUtil.setEtImgSize(et_phone);
        EtDrawableLeftUtil.setEtImgSize(et_code);
        EtDrawableLeftUtil.setEtImgSize(et_psw);
        TvUtil.addUnderLine(tv_get_number);
        listenEtChanged();

        ll_student.setClickable(false);
        ll_teacher.setClickable(true);
    }

    /**
     * 监听手机输入框，验证码和密码输入框，当三个输入框都有内容时改变登录的背景
     */
    private void listenEtChanged() {
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        et_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkedText();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /**
     * 判断当手机号输入框,验证码输入框和密码输入框都不为空的时候改变登录的背景
     */
    private void checkedText() {
        phoneText = et_phone.getText().toString().trim();
        coedText = et_code.getText().toString().trim();
        pswText = et_psw.getText().toString().trim();

        if (phoneText != null && phoneText.trim().length() > 0 && coedText != null && coedText.length() > 0 && pswText != null && pswText.trim().length() > 0) {
            ll_regist.setBackgroundResource(R.drawable.bg_login_dark_btn);
            tv_regist.setEnabled(true);
        } else {
            ll_regist.setBackgroundResource(R.drawable.bg_login_light_btn);
            tv_regist.setEnabled(false);
        }
    }

    @OnClick({R.id.tv_regist, R.id.iv_isAgree, R.id.tv_get_number, R.id.tv_regist_agreement, R.id.ll_student, R.id.ll_teacher})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_regist:
                if (et_phone.getText().toString().trim() == null || et_phone.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getActivity(), getString(R.string.write_phone_number), 0);
                    return;
                }
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_number), 0);
                    return;
                }
                if (et_code.getText().toString().trim() == null || et_code.getText().toString().trim().length() != 6) {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_code), 0);
                    return;
                }
                if (!et_code.getText().toString().trim().equals(Constanst.phoneCode)) {
                    ToastUtil.show(getActivity(), getString(R.string.phone_code_error), 0);
                    return;
                }
                if (et_psw.getText().toString().trim() == null || et_psw.getText().toString().trim().length() == 0) {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_psw), 0);
                    return;
                }
                if (!isAgree) {
                    ToastUtil.show(getActivity(), getString(R.string.agree_regist_agreement), 0);
                    return;
                }
                if (!isCodeWork) {
                    ToastUtil.show(getActivity(), getString(R.string.phone_code_time_out), 0);
                    return;
                }
                showLoadingDialog();
                ///请求用户是否注册接口
                isRegist();
                break;
            case R.id.iv_isAgree:
                if (isAgree) {
                    iv_isAgree.setImageResource(R.mipmap.img_no_read);
                    isAgree = false;
                } else {
                    iv_isAgree.setImageResource(R.mipmap.img_yes_read);
                    isAgree = true;
                }
                break;
            case R.id.tv_get_number:
                if (PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
                    isCodeWork = true;
                    MsgCodeUtil.sendMsg(getActivity(), et_phone.getText().toString().trim(), Constanst.registSignature, Constanst.registTemplate);
                    TvUtil.removeUnderLine(tv_get_number);
                    tv_get_number.setText(restSeconds + "s后重试");
                    tv_get_number.setTextColor(getResources().getColor(R.color.light_gray9));
                    ll_get_number.setBackgroundResource(R.drawable.bg_get_number_gray);
                    tv_get_number.setEnabled(false);
                    handler.postDelayed(runnable, 1000);
                } else {
                    ToastUtil.show(getActivity(), getString(R.string.write_right_phone_number), 0);
                }
                break;
            case R.id.tv_regist_agreement:
                startActivity(new Intent(getActivity(), RegistAgreementActivty.class));
                break;
            case R.id.ll_student:
                Log.i("RegistFragment", "    点击了学生         isStudentLeft：" + isStudentLeft);
                ll_student.setClickable(false);

                Picasso.with(getActivity()).load(R.mipmap.img_white_student).into(iv_student);
                tv_student.setTextColor(getResources().getColor(R.color.white));

                float halfWidth = (rl_teacher.getWidth() / 2);
                if (!isStudentLeft) {
                    final ObjectAnimator translationX = new ObjectAnimator().ofFloat(framelayout, "translationX", halfWidth, 0);
                    translationX.setDuration(500);
                    translationX.start();
                    translationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            type = 1;
                            ToastUtil.show(getActivity(), "动画1结束", 0);
                            isStudentLeft = true;
                            ll_teacher.setClickable(true);


                            Picasso.with(getActivity()).load(R.mipmap.img_blue_teacher).into(iv_teacher);
                            tv_teacher.setTextColor(getResources().getColor(R.color.orangeone));

                            translationX.removeAllListeners();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
                break;
            case R.id.ll_teacher:
                Log.i("RegistFragment", "    点击了老师         isStudentLeft：" + isStudentLeft);

                ll_teacher.setClickable(false);

                Picasso.with(getActivity()).load(R.mipmap.img_white_teacher).into(iv_teacher);
                tv_teacher.setTextColor(getResources().getColor(R.color.white));

                float halfWidth1 = (rl_teacher.getWidth() / 2);
                if (isStudentLeft) {
                    final ObjectAnimator translationX = new ObjectAnimator().ofFloat(framelayout, "translationX", 0, halfWidth1);
                    translationX.setDuration(500);
                    translationX.start();
                    translationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            type = 2;
                            ToastUtil.show(getActivity(), "动画2结束", 0);
                            isStudentLeft = false;
                            ll_student.setClickable(true);

                            Picasso.with(getActivity()).load(R.mipmap.img_blue_student).into(iv_student);
                            tv_student.setTextColor(getResources().getColor(R.color.orangeone));

                            translationX.removeAllListeners();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
                break;
        }
    }

    private boolean isStudentLeft = true;//记录学生布局在左边还是右边，true代表初始化时在左边

    //是否注册
    private void isRegist() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "is_register");
        map.put("username", et_phone.getText().toString().trim());
        getJsonUtil().PostJson(getActivity(), map, ll_regist);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);//界面消失的时候删除该任务
    }
}
