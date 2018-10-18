package com.youyi.ywl.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyi.ywl.R;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.GlideUtil;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;
import com.youyi.ywl.util.WebViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/30.
 * 积分商城商品详情界面
 */

public class CreditGoodsDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_goods)
    ImageView iv_goods;
    @BindView(R.id.tv_goods_name)
    TextView tv_goods_name;
    @BindView(R.id.tv_goods_credit)
    TextView tv_goods_credit;
    @BindView(R.id.tv_goods_price)
    TextView tv_goods_price;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_exchange)
    TextView tv_exchange;
    @BindView(R.id.et_address)
    EditText et_address;

    private static final String DATA_URL = HttpUtils.Host + "/scoreMall/detail";//商品详情接口
    private static final String EXCHANGE_URL = HttpUtils.Host + "/scoreMall/exchange";//商品兑换接口
    private String name;
    private String id;
    private String exchange;//是否兑换
    private String creditCount;//用来保存需要的积分

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case DATA_URL:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap<String, Object> resultMap = (HashMap<String, Object>) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //0代表数据正常
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) resultMap.get("data");
                        GlideUtil.loadNetImageView(this,dataMap.get("img") + "",iv_goods);//商品图片
                        tv_goods_name.setText(dataMap.get("title") + "");//商品名称
                        tv_goods_credit.setText(dataMap.get("score") + "分");//商品兑换所需要的积分
                        tv_goods_price.setText("¥ " + dataMap.get("price"));//商品原价格
                        tv_time.setText(dataMap.get("start_time") + "至" + dataMap.get("end_time"));//有效时间
                        webView.loadUrl(dataMap.get("desc") + "");//商品描述
                        creditCount = dataMap.get("score")+"";//保存积分
                        //商品兑换状态
                        exchange = dataMap.get("exchange") + "";
                        if ("0".equals(exchange)) {
                            //0代表未兑换,显示一键兑换按钮
                            tv_exchange.setText("一键兑换");
                            tv_exchange.setBackgroundResource(R.drawable.bg_blue_choose_student);
                        } else {
                            //1或其他代表已兑换,显示已兑换按钮
                            tv_exchange.setText("已兑换");
                            tv_exchange.setBackgroundResource(R.drawable.bg_dark_gray_half_circle2);
                        }
                    } else {
                        //1或其它代表数据不正常
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case EXCHANGE_URL:
                dismissLoadingDialog();

                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //0表示状态没问题,继续判断resultMap里的code
                        String resultCode = resultMap.get("code") + "";
                        if ("000".equals(resultCode)) {
                            //000 ==> 兑换成功
                            exchange = "1";//本地修改是否兑换状态码,表示已兑换
                            tv_exchange.setText("已兑换");
                            tv_exchange.setBackgroundResource(R.drawable.bg_dark_gray_half_circle2);
                            ToastUtil.show(this, resultMap.get("msg") + "", 0);
                            //兑换成功,通知列表界面跟新数据
                            EventBus.getDefault().post("商品兑换成功"+creditCount);
                        } else if ("001".equals(resultCode)) {
                            //001 ==> 库存不足
                            showLackOfStockDialog();
                        } else if ("002".equals(resultCode)) {
                            //002 ==> 积分不足
                            showCreditNotEnoughDialog();
                        } else if ("003".equals(resultCode)) {
                            //003 ==> 您已兑换过
                            ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        }
                    } else {
                        //1或其他代表兑换失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            name = intent.getStringExtra("name");
            id = intent.getStringExtra("id");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_credit_goods_details);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText(name);
        WebViewUtil.setWebViewSettings(webView);
        PostList();
    }

    //请求数据
    private void PostList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "scoreMall");
        map.put("action", "detail");
        map.put("id", id + "");
        getJsonUtil().PostJson(this, map);
    }

    //兑换接口
    private void PostExchangeList() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("controller", "scoreMall");
        map.put("action", "exchange");
        map.put("id", id + "");
        map.put("address", et_address.getText().toString().trim());
        getJsonUtil().PostJson(this, map);
    }

    @Override
    public void afterInitView() {
    }

    //显示积分不够弹窗
    public void showCreditNotEnoughDialog() {
        final Dialog dialog = new Dialog(this, R.style.mDialog);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.credit_not_enough_dialog, null);
        layout.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        TextView tv_instroction = (TextView) layout.findViewById(R.id.tv_instroction);//说明
        tv_instroction.setText("积分不够,无法兑换此商品!");
        dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    //显示库存不足弹窗
    public void showLackOfStockDialog() {
        final Dialog dialog = new Dialog(this, R.style.mDialog);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.credit_not_enough_dialog, null);
        layout.findViewById(R.id.iv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        TextView tv_instroction = (TextView) layout.findViewById(R.id.tv_instroction);//说明
        tv_instroction.setText("库存不足,无法兑换此商品!");
        dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    //显示询问确认兑换弹框
    public void showConfirmExchangeDialog() {
        final Dialog dialog = new Dialog(this, R.style.mDialog);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.confirm_exchange_goods_dialog, null);
        layout.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        layout.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                PostExchangeList();
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    @OnClick({R.id.ll_back, R.id.tv_exchange})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_exchange:
                //兑换按钮
                if ("0".equals(exchange)) {
                    //0代表没有兑换过,不为0的情况下不用管

                    if (et_address.getText() == null || et_address.getText().toString().trim().length() == 0) {
                        ToastUtil.show(this, "配送地址不能为空", 0);
                        return;
                    }
                    showConfirmExchangeDialog();//显示确认兑换dialog
                }
                break;
        }
    }

}
