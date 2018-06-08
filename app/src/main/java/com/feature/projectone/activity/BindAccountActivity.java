package com.feature.projectone.activity;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.view.BaseDialog;
import com.feature.projectone.view.BasePopupWindow;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/12.
 * 第三方账户绑定界面
 */

public class BindAccountActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_bind_account);
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_title.setText("账户绑定");
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.ll_back, R.id.ll_wechat_bind})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_wechat_bind:
                showPop();
                break;
        }
    }

    //显示修改绑定的dialog
    public void showDialog(final String str) {
        BaseDialog.Builder builder = new BaseDialog.Builder(this);
        builder.setTitle("确定解除“" + str + "”绑定?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private View popupWindowView;
    private BasePopupWindow popupWindow;

    //显示接触绑定的popwindow
    public void showPop() {
        if (popupWindowView == null) {
            popupWindowView = LayoutInflater.from(this).inflate(R.layout.layout_bind_pop, null);

            //解除绑定
            popupWindowView.findViewById(R.id.tv_relieve_bind).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        showDialog("微信");
                        popupWindow.dismiss();
                    }
                }
            });

            //取消
            popupWindowView.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
        }
        if (popupWindow == null) {
            popupWindow = new BasePopupWindow(this);
            popupWindow.setContentView(popupWindowView);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }
        popupWindow.showAtLocation(findViewById(R.id.ll_base), Gravity.BOTTOM, 0, 0);
    }
}
