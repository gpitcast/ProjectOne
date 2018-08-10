package com.youyi.YWL.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youyi.YWL.R;
import com.youyi.YWL.adapter.YouhuiVolumeAdapter;
import com.youyi.YWL.inter.ReplyIconClickListener;
import com.youyi.YWL.util.ToastUtil;
import com.youyi.YWL.view.BaseDialog;
import com.youyi.YWL.view.BasePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/11.
 * 确认订单界面
 */

public class OrderConfirmActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_avaliable_count)
    TextView tv_avaliable_count;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.iv_state)
    ImageView iv_state;

    private boolean isHasYouhuiVolume = true;
    private YouhuiVolumeAdapter youhuiVolumeAdapter;
    private TextView tv_status;
    private List<Boolean> checkedList = new ArrayList<>();

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_order_confirm);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("确认订单");
        initFalseData();

        if (isHasYouhuiVolume) {
            //显示有优惠券
            tv_avaliable_count.setText("1张可用");
            tv_avaliable_count.setBackgroundResource(R.drawable.bg_blue_choose_student);
            tv_state.setText("未使用");
            iv_state.setImageResource(R.mipmap.icon_gray_arrow_right);
        } else {
            //显示0张优惠券
            tv_avaliable_count.setText("0张可用");
            tv_avaliable_count.setBackgroundResource(R.drawable.bg_dark_gray_half_circle);
            tv_state.setText("添加");
            iv_state.setImageResource(R.mipmap.icon_add_dark_gray);
        }
    }

    private void initFalseData() {
        checkedList.add(false);
        checkedList.add(false);
        checkedList.add(false);
        checkedList.add(false);
        checkedList.add(false);
        checkedList.add(false);
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.ll_goto_payment, R.id.ll_add})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_goto_payment:
                //前往支付
                startActivity(new Intent(this, OrderPaymentActivity.class));
                break;
            case R.id.ll_add:
                //添加优惠券
                if (!isHasYouhuiVolume) {
                    //没有优惠券,点击显示使用优惠码dialog
                    showUseYouHuiCodeDialog();
                } else {
                    //有优惠券,点击显示选择优惠券的popwindow
                    showCheckYouhuiVolumePop();

                }
                break;
        }
    }

    private View popwindowView;
    private BasePopupWindow popupWindow;

    //显示选择优惠券的popwindow
    private void showCheckYouhuiVolumePop() {
        if (popwindowView == null) {
            popwindowView = LayoutInflater.from(this).inflate(R.layout.layout_check_youhui_volume_pop, null);
            //取消按钮
            popwindowView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
            //添加优惠码按钮
            popwindowView.findViewById(R.id.ll_add_youhui_code).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    showUseYouHuiCodeDialog();
                }
            });

            XRecyclerView xRecyclerView = (XRecyclerView) popwindowView.findViewById(R.id.xRecyclerView);
            initRecyclerView(xRecyclerView);
        }

        if (popupWindow == null) {
            popupWindow = new BasePopupWindow(this);
            popupWindow.setContentView(popwindowView);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }


//        View ll_about_money = findViewById(R.id.ll_about_money);
//        popwindowView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int popupWidth = popwindowView.getMeasuredWidth();
//        int popupHeight = popwindowView.getMeasuredHeight();
//        int[] location = new int[2];
//        ll_about_money.getLocationOnScreen(location);

        //设置popwindow的位置
        popupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.BOTTOM, 0, 0);
    }


    //初始化优惠券的recyclerview
    private void initRecyclerView(XRecyclerView xRecyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        xRecyclerView.setLayoutManager(manager);
        youhuiVolumeAdapter = new YouhuiVolumeAdapter(this, checkedList);
        xRecyclerView.setAdapter(youhuiVolumeAdapter);
        View headView1 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        View headView2 = LayoutInflater.from(this).inflate(R.layout.layout_empty_head, null);
        xRecyclerView.addHeaderView(headView1);
        xRecyclerView.addHeaderView(headView2);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
            }
        });

        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_recyclerview_footer, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        tv_status = ((TextView) footerView.findViewById(R.id.tv_status));
        xRecyclerView.setFootView(footerView);

        youhuiVolumeAdapter.setOnCheckedClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                for (int i = 0; i < checkedList.size(); i++) {
                    if (position == i) {
                        checkedList.set(i, true);
                    } else {
                        checkedList.set(i, false);
                    }
                }
                youhuiVolumeAdapter.notifyDataSetChanged();
            }
        });
    }

    //显示使用优惠码的dialog
    private void showUseYouHuiCodeDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Dialog dialog = new Dialog(this, R.style.mDialog);
        View layout = inflater.inflate(R.layout.layout_youhui_code_dialog, null);
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);
        final EditText et_youhui_code = (EditText) layout.findViewById(R.id.et_youhui_code);
        //取消按钮
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        //确认按钮
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_youhui_code.getText().toString().trim();
                if (str == null || str.length() == 0) {
                    ToastUtil.show(OrderConfirmActivity.this, "优惠码不能为空", 0);
                } else {
                    ToastUtil.show(OrderConfirmActivity.this, "点击了确认键,优惠码:" + str, 0);
                    dialog.dismiss();
                }
            }
        });

        dialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.setCancelable(false);//设置不可点击外部消失
        dialog.show();
    }
}
