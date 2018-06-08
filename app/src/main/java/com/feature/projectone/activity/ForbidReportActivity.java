package com.feature.projectone.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.feature.projectone.R;
import com.feature.projectone.adapter.ForbidReportGridViewAdapter;
import com.feature.projectone.inter.ReplyIconClickListener;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.CheckLoginUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.PermissionUtil;
import com.feature.projectone.util.PhoneNumberCheckedUtil;
import com.feature.projectone.util.PictureSelectorUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.BaseDialog;
import com.feature.projectone.view.BasePopupWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/5/18.
 * 违规举报界面
 */

public class ForbidReportActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.tv_report_reason)
    TextView tv_report_reason;
    @BindView(R.id.ll_submit)
    LinearLayout ll_submit;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.et_teacher)
    EditText et_teacher;
    @BindView(R.id.et_class)
    EditText et_class;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.et_phone)
    EditText et_phone;

    private static final String ReportUrl = HttpUtils.Host + "/report/add";
    private ForbidReportGridViewAdapter forbidReportGridViewAdapter;
    private ArrayList<String> mPicList = new ArrayList();//图片路径List
    private ArrayList<File> mPicFileList = new ArrayList<>();//图片文件List
    private List<LocalMedia> mLocalPicList = new ArrayList();//查看图片需要传入的List类型
    private List<String> mReasonList = new ArrayList<>();//举报理由List
    private View popupWindowView;
    private BasePopupWindow popupWindow;
    private final int takePicturePermissionCode = 2000;
    private final int fromCameraPermissionCode = 2001;
    private final int TAKE_PICTURE_CODE = 166;
    private List<String> deniedPermissionList = new ArrayList<>();
    private final int delateCachePermissionCode = 2002;
    private String teacherText;
    private String classtext;
    private String reportReasonText;
    private String contentText;
    private String phoneText;


    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case ReportUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        finish();
                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }

                dismissLoadingDialog();
                break;
        }
    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_forbid_report);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("违规举报");

        mReasonList.add("广告植入");
        mReasonList.add("辱骂言论");
        mReasonList.add("色情主播");
        mReasonList.add("煽动群众");
        mReasonList.add("非法宗教传播");
        initGridView();
    }


    //检查所有的需要输入的内容是否为空
    private boolean checkedText() {
        teacherText = et_teacher.getText().toString().trim();
        classtext = et_class.getText().toString().trim();
        reportReasonText = tv_report_reason.getText().toString().trim();
        contentText = et_content.getText().toString().trim();
        phoneText = et_phone.getText().toString().trim();

        if (TextUtils.isEmpty(teacherText) || teacherText.length() == 0) {
            ToastUtil.show(this, "违规老师不能为空", 0);
            return false;
        }

        if (TextUtils.isEmpty(classtext) || classtext.length() == 0) {
            ToastUtil.show(this, "课程名称不能为空", 0);
            return false;
        }

        if (TextUtils.isEmpty(reportReasonText) || reportReasonText.length() == 0 || "请选择".equals(reportReasonText)) {
            ToastUtil.show(this, "举报原因不能为空", 0);
            return false;
        }

        if (TextUtils.isEmpty(contentText) || contentText.length() == 0) {
            ToastUtil.show(this, "举报说明不能为空", 0);
            return false;
        }

        if (mPicList == null || mPicList.size() == 0) {
            ToastUtil.show(this, "举报图片不能为空", 0);
            return false;
        }

        if (TextUtils.isEmpty(phoneText) || phoneText.length() == 0) {
            ToastUtil.show(this, "联系手机不能为空", 0);
            return false;
        }

        if (!PhoneNumberCheckedUtil.checkNumber(et_phone.getText().toString().trim())) {
            ToastUtil.show(this, getString(R.string.write_right_phone_number), 0);
            return false;
        }

        return true;
    }

    private void initGridView() {
        forbidReportGridViewAdapter = new ForbidReportGridViewAdapter(this, mPicList);
        gridView.setAdapter(forbidReportGridViewAdapter);

        //gridView的条目点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getChildCount() - 1) {
                    //代表是最后一个item
                    //如果“增加按钮形状的”图片的位置是最后一张，且添加了的图片的数量不超过5张，才能点击
                    if (mPicList.size() == Constanst.MAX_SELECT_PIC_NUM) {
                        //添加了最多的三张图片,点击放大查看照片
//                        seePhoto(position);
                        PictureSelector.create(ForbidReportActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, mLocalPicList);
                    } else {
                        //点击添加图片
                        showPicturePopWindow();
                    }
                } else {
                    //代表不是最后一个item,点击放大查看照片
//                    seePhoto(position);
                    PictureSelector.create(ForbidReportActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, mLocalPicList);
                }
            }
        });

        //gridView删除图标点击事件
        forbidReportGridViewAdapter.setOnDeleteClickListener(new ReplyIconClickListener() {
            @Override
            public void OnReplyIconClick(View view, int position) {
                mPicList.remove(position);
                mLocalPicList.remove(position);
                mPicFileList.remove(position);
                forbidReportGridViewAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 查看照片
     *
     * @param position
     */
    private void seePhoto(int position) {
        Intent intent = new Intent(this, PlusImageActivity.class);
        intent.putStringArrayListExtra(Constanst.IMG_LIST, mPicList);
        intent.putExtra(Constanst.POSITION, position);
        startActivityForResult(intent, Constanst.REQUEST_CODE_MAIN);
    }

    /**
     * 进入选择相片界面
     *
     * @param i
     */
    private void selectPic(int i) {
        PictureSelectorUtil.initMultiConfig(this, i);
    }

    /**
     * 显示添加照片的pop
     */
    private void showPicturePopWindow() {

        if (popupWindowView == null) {
            popupWindowView = LayoutInflater.from(this).inflate(R.layout.layout_camera_pop, null);
            //取消键监听
            popupWindowView.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
            });
            //拍照键监听
            popupWindowView.findViewById(R.id.tv_take_picture).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.checkPermission(ForbidReportActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            takePicturePermissionCode)) {
                        //有权限，就调用系统相机拍照
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, TAKE_PICTURE_CODE);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                }
            });
            //相册获取照片键监听
            popupWindowView.findViewById(R.id.tv_from_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PermissionUtil.checkPermission(ForbidReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, fromCameraPermissionCode)) {
                        //有权限，就跳转到相册界面
                        selectPic(Constanst.MAX_SELECT_PIC_NUM - mPicList.size());
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
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

    //权限监听回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case takePicturePermissionCode:
                Log.i("PermissionsResult", "     length    " + grantResults.length);
                if (grantResults.length > 0) {
                    deniedPermissionList.clear();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissions[i]);
                        }
                    }

                    if (deniedPermissionList.isEmpty()) {
                        //已经全部授权
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, TAKE_PICTURE_CODE);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    } else {
                        //没有授权，需要判断是否勾选了‘不再询问’
                        for (String deniedPermission : deniedPermissionList) {
                            boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(this, deniedPermission);
                            if (!flag) {
                                //用户勾选不再询问，拒绝授权
                                BaseDialog.Builder builder = new BaseDialog.Builder(this);
                                builder.setTitle("拍照需要读写SD卡和相机的权限，是否跳转到设置权限界面？");
                                //积极的按钮,点击跳转到权限设置界面
                                builder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PermissionUtil.jumpToPermissionActivity(ForbidReportActivity.this);
                                        dialogInterface.dismiss();
                                    }
                                });
                                //消极的按钮,点击隐藏dialog
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.create().show();
                                return;
                            }
                        }
                        //没有授权，但是没有勾选不再询问，不必理会
                    }
                }
                break;
            case fromCameraPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //请求相册权限成功
                    selectPic(Constanst.MAX_SELECT_PIC_NUM - mPicList.size());
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                } else {
                    //请求权限不成功
                    //当权限申请被拒绝并且shouldShowRequestPermissionRationale() 返回 false 就表示勾选了不再询问
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        //勾选了不在询问，需要弹出dialog提醒用户去权限设置里打开权限
                        BaseDialog.Builder builder = new BaseDialog.Builder(this);
                        builder.setTitle("访问相册需要读取SD卡的权限，是否跳转到设置权限界面？");
                        //积极的按钮,点击跳转到权限设置界面
                        builder.setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PermissionUtil.jumpToPermissionActivity(ForbidReportActivity.this);
                                dialogInterface.dismiss();
                            }
                        });
                        //消极的按钮,点击隐藏dialog
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        //没有勾选不在询问,不需要做任何操作
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PICTURE_CODE:
                if (resultCode != RESULT_OK) {
                    Log.i("onActivityResult", "     取消了添加照片操作    ");
                    return;
                }
                //相机拍照回调
                //拍照后图片的绝对路径
                String videoPath = null;
                if (data != null) {
                    // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
                    Uri uri = data.getData();
                    /*
                     * 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
                     * 拍照后保存到相册的手机
                     */
                    if (uri != null) {
                        Cursor cursor = this.getContentResolver().query(uri, null,
                                null, null, null);
                        if (cursor.moveToFirst()) {
                            videoPath = cursor.getString(cursor.getColumnIndex("_data"));// 获取绝对路径
                        }
                    } else {
                        //小米等 拍照后不保存的手机
                        Bitmap bm = (Bitmap) data.getExtras().get("data");
                        File file = getFile(bm);
                        videoPath = file.getPath();
                    }
                    mPicList.add(videoPath);
                    forbidReportGridViewAdapter.notifyDataSetChanged();
                }

                //压缩图片,并将图片添加到要传入服务器的集合里
                final File file = new File(videoPath);
                Log.i("onActivityResult", "压缩前file.length():" + file.length());
                Luban.with(this).load(file).ignoreBy(100)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                Log.i("onActivityResult", "压缩成功后file.length():" + file.length());
                                mPicFileList.add(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("onActivityResult", "压缩失败的file.length():" + file.length());
                                mPicFileList.add(file);
                            }
                        }).launch();

                //添加预览图片需要的集合
                LocalMedia localMedia1 = new LocalMedia(videoPath, 0L, 0, null);
                mLocalPicList.add(localMedia1);
                break;
            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode != RESULT_OK) {
                    Log.i("onActivityResult", "     取消了添加照片操作    ");
                    return;
                }
                //相册获取照片回调
                List<LocalMedia> result = (List) data.getSerializableExtra("extra_result_media");
                for (LocalMedia localMedia : result) {
                    //被压缩后的图片路径
                    if (localMedia.isCompressed()) {
                        String compressPath = localMedia.getCompressPath();//压缩后的图片路径
                        mPicList.add(compressPath);

                        //压缩图片,并将图片添加到要传入服务器的集合里
                        final File file1 = new File(compressPath);
                        Log.i("onActivityResult", "压缩前file1.length():" + file1.length());
                        Luban.with(this).load(file1).ignoreBy(100)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        Log.i("onActivityResult", "压缩成功后file1.length():" + file.length());
                                        mPicFileList.add(file);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.i("onActivityResult", "压缩失败的file1.length():" + file1.length());
                                        mPicFileList.add(file1);
                                    }
                                }).launch();

                        forbidReportGridViewAdapter.notifyDataSetChanged();
                    }
                }

                //添加预览图片需要的集合
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                mLocalPicList.addAll(selectList);
                break;
        }
    }

    private File getFile(Bitmap bitmap) {
        String pictureDir = "";
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        File file = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/dreamtownImage";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = new File(saveDir, System.currentTimeMillis() + ".jpg");
            file.delete();
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            pictureDir = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.ll_back, R.id.ll_report_reason, R.id.tv_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_report_reason:
                //举报理由
                OptionsPickerView pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tv_report_reason.setText(mReasonList.get(options1));
                    }
                }).build();
                pickerView.setPicker(mReasonList);
                pickerView.show();
                break;
            case R.id.tv_submit:
                //提交按钮
                if (CheckLoginUtil.isLogin(this)) {
                    if (checkedText()) {
                        showLoadingDialog();
                        PostList();
                    }
                } else {
                    ToastUtil.show(this, getResources().getString(R.string.need_login_first), 0);
                }
                break;
        }
    }

    /**
     * 违规举报请求
     */
    private void PostList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "report");
        map.put("action", "add");
        map.put("tname", teacherText);
        map.put("cname", classtext);
        map.put("reason", reportReasonText);
        map.put("content", contentText);
        map.put("tel", phoneText);

        if (mPicList != null && mPicFileList != null) {
            if (mPicList.size() == mPicFileList.size()) {
                getJsonUtil().PostJson(this, map, mPicFileList, tv_submit);
            } else {
                ToastUtil.show(this, "请等待图片压缩完毕", 0);
            }
        } else {
            getJsonUtil().PostJson(this, map, mPicFileList, tv_submit);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (PermissionUtil.checkPermission(ForbidReportActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, delateCachePermissionCode)) {
            //有权限
            //清除缓存,包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
            PictureFileUtils.deleteCacheDirFile(ForbidReportActivity.this);
        }
    }
}
