package com.feature.projectone.adapter;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.BaseFragment;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.HttpUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/4/3.
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

    private boolean isAgree;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

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
    }

    @OnClick({R.id.ll_regist, R.id.iv_isAgree, R.id.tv_get_number})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_regist:

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

                break;
        }
    }
}
