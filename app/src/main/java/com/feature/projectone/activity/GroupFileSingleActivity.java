package com.feature.projectone.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/25.
 * 群文件 具体文件 子文件
 */

public class GroupFileSingleActivity extends BaseActivity {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_file_type)
    ImageView iv_file_type;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_size)
    TextView tv_size;

    private HashMap<String, Object> dataMap;

    @Override
    protected void Response(String code, String msg, String url, Object result) {

    }

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            dataMap = (HashMap<String, Object>) intent.getSerializableExtra("dataMap");
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_group_file_single);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tvTitle.setText(dataMap.get("title") + "");
        tv_name.setText(dataMap.get("title") + "." + dataMap.get("extension"));
        tv_size.setText(dataMap.get("size") + "");
        //类型图片
        String extension = dataMap.get("extension") + "";
        switch (extension) {
            case "zip":
                Picasso.with(this).load(R.mipmap.img_type_zip).into(iv_file_type);
                break;
            case "ppt":
                Picasso.with(this).load(R.mipmap.img_type_ppt).into(iv_file_type);
                break;
            case "pptx":
                Picasso.with(this).load(R.mipmap.img_type_ppt).into(iv_file_type);
                break;
            case "xls":
                Picasso.with(this).load(R.mipmap.img_type_excel).into(iv_file_type);
                break;
            case "map3":
                Picasso.with(this).load(R.mipmap.img_type_music).into(iv_file_type);
                break;
            case "pdf":
                Picasso.with(this).load(R.mipmap.img_type_pdf).into(iv_file_type);
                break;
            case "png":
                Picasso.with(this).load(R.mipmap.img_type_png).into(iv_file_type);
                break;
            case "jpg":
                Picasso.with(this).load(R.mipmap.img_type_png).into(iv_file_type);
                break;
            case "mp4":
                Picasso.with(this).load(R.mipmap.img_type_video).into(iv_file_type);
                break;
            case "doc":
                Picasso.with(this).load(R.mipmap.img_type_word).into(iv_file_type);
                break;
            case "txt":
                Picasso.with(this).load(R.mipmap.img_type_word).into(iv_file_type);
                break;
            default:
                Picasso.with(this).load(R.mipmap.img_type_zip).into(iv_file_type);
                break;
        }
    }

    @Override
    public void afterInitView() {

    }

    @OnClick({R.id.tvBack})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tvBack:
                finish();
                break;
        }
    }
}
