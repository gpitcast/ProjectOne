package com.feature.projectone.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.feature.projectone.R;
import com.feature.projectone.bean.JsonBean;
import com.feature.projectone.other.Constanst;
import com.feature.projectone.util.FileUtil;
import com.feature.projectone.util.HttpUtils;
import com.feature.projectone.util.PermissionUtil;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.view.BaseDialog;
import com.feature.projectone.view.BasePopupWindow;
import com.feature.projectone.view.YuanJiaoImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/5/9.
 * 修改个人资料界面
 */

public class UpdatePersonalDataActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_head)
    YuanJiaoImageView iv_head;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.et_nickName)
    EditText et_nickName;
    @BindView(R.id.et_realName)
    EditText et_realName;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    @BindView(R.id.tv_save_update)
    TextView tv_save_update;


    private View popupWindowView;
    private BasePopupWindow popupWindow;
    private final int takePicturePermissionCode = 1000;
    private final int fromCameraPermissionCode = 1001;
    private final int TAKE_PICTURE_CODE = 666;
    private final int FROM_CAMERA_CODE = 888;
    private TimePickerView timePickerView;
    private OptionsPickerView sexPickerView;
    private OptionsPickerView typePickerView;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();//省数据
    private ArrayList<String> options1Items_string = new ArrayList<>();//String类型的省数据
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市数据
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//地级数据
    private boolean isParseSuccess;
    private OptionsPickerView addressPickerView;
    private int roleId;//记录选择的类型id
    private ArrayList<File> mPicFileList = new ArrayList<>();//图片文件List
    private static final String updateUrl = HttpUtils.Host + "/user/update_user";//修改用户信息接口
    private static final String userInfoUrl = HttpUtils.Host + "/user/user_info";//用户信息接口

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case updateUrl:
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //修改成功
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                        PostUserInfoList();//在修改成功后,再次请求用户信息接口
                    } else {
                        dismissLoadingDialog();
                        //修改失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    dismissLoadingDialog();
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case userInfoUrl:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        //请求成功
                        HashMap<String, Object> dataList = (HashMap<String, Object>) resultMap.get("data");
                        //昵称
                        if (dataList.get("nickname") != null) {
                            Constanst.userNickName = dataList.get("nickname") + "";
                        } else {
                            Constanst.userNickName = "";
                        }
                        //真实姓名
                        if (dataList.get("realname") != null) {
                            Constanst.userRealName = dataList.get("realname") + "";
                        } else {
                            Constanst.userRealName = "";
                        }
                        //手机号码
                        if (dataList.get("username") != null) {
                            Constanst.userPhoneNum = dataList.get("username") + "";
                        } else {
                            Constanst.userPhoneNum = "";
                        }
                        //头像url
                        if (dataList.get("img") != null) {
                            Constanst.userHeadImg = dataList.get("img") + "";
                        } else {
                            Constanst.userHeadImg = "";
                        }
                        //性别
                        if (dataList.get("sex") != null) {
                            Constanst.userSex = dataList.get("sex") + "";
                        } else {
                            Constanst.userSex = "";
                        }
                        //生日
                        if (dataList.get("birthday") != null) {
                            Constanst.userBirthday = dataList.get("birthday") + "";
                        } else {
                            Constanst.userBirthday = "";
                        }
                        //邮寄地址
                        if (dataList.get("address") != null) {
                            Constanst.userAddress = dataList.get("address") + "";
                        } else {
                            Constanst.userAddress = "";
                        }
                        //身份
                        if (dataList.get("role_id") != null) {
                            Constanst.user_role_id = dataList.get("role_id") + "";
                        } else {
                            Constanst.user_role_id = "";
                        }
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(Constanst.userPhoneNum, Constanst.userNickName, Uri.parse(Constanst.userHeadImg)));
                    } else {
                        //请求失败
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                    EventBus.getDefault().post("调用initMyView()");
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                finish();
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_update_personal_data);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("个人资料修改");
        Picasso.with(this).load(Constanst.userHeadImg).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(iv_head);
        et_nickName.setText(Constanst.userNickName);
        if (!TextUtils.isEmpty(et_nickName.getText().toString().trim())) {
            et_nickName.setSelection(et_nickName.getText().toString().trim().length());
        }
        et_realName.setText(Constanst.userRealName);
        tv_sex.setText(Constanst.userSex);
        if (!TextUtils.isEmpty(Constanst.userPhoneNum) && Constanst.userPhoneNum.length() == 11) {
            tv_phone_num.setText(Constanst.userPhoneNum.substring(0, 3) + "****" + Constanst.userPhoneNum.substring(7, 11));
        }
        tv_birthday.setText(Constanst.userBirthday);
        tv_address.setText(Constanst.userAddress);
        if (!TextUtils.isEmpty(Constanst.user_role_id)) {
            if ("0".equals(Constanst.user_role_id)) {
                tv_type.setText("学生");
                roleId = 0;
            } else if ("1".equals(Constanst.user_role_id)) {
                tv_type.setText("老师");
                roleId = 1;
            } else {
                tv_type.setText("不明");
                roleId = -1;
            }
        }

    }

    @Override
    public void afterInitView() {
    }

    //显示修改身份的dialog
    public void showTypeDialog(final String str) {
        BaseDialog.Builder builder = new BaseDialog.Builder(this);
        builder.setTitle("确定修改身份为“" + str + "”?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_type.setText(str);
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

    //显示选择拍照还是相册的ppwindow
    public void showPop() {
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
                    if (PermissionUtil.checkPermission(UpdatePersonalDataActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            takePicturePermissionCode)) {
                        //有权限，就调用系统相机拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    if (PermissionUtil.checkPermission(UpdatePersonalDataActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, fromCameraPermissionCode)) {
                        //有权限，就跳转到相册界面
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, FROM_CAMERA_CODE);
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

    private List<String> deniedPermissionList = new ArrayList<>();

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
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                        PermissionUtil.jumpToPermissionActivity(UpdatePersonalDataActivity.this);
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
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, FROM_CAMERA_CODE);
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
                                PermissionUtil.jumpToPermissionActivity(UpdatePersonalDataActivity.this);
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

    //拍照或者照相activity回调


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        mPicFileList.clear();//清空之前的照片

        if (resultCode != RESULT_OK) {
            Log.i("onActivityResult", "     取消了照片操作    ");
            return;
        }
        switch (requestCode) {
            case TAKE_PICTURE_CODE:
                //相机拍照回调
                Bitmap photo = (Bitmap) data.getParcelableExtra("data");
                iv_head.setImageBitmap(photo);

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

                break;
            case FROM_CAMERA_CODE:
                //相册获取照片回调
                Uri imageUri = data.getData();
//                iv_head.setImageURI(imageUri);
                String realPath = FileUtil.getRealPathFromUri(this, imageUri);
                final File file1 = new File(realPath);
                Log.i("onActivityResult", "压缩前file.length():" + file1.length());
                Luban.with(this).load(file1).ignoreBy(100)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {
                                Log.i("onActivityResult", "压缩成功后file.length():" + file.length());
                                Picasso.with(UpdatePersonalDataActivity.this).load(file).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(iv_head);
                                mPicFileList.add(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i("onActivityResult", "压缩失败的file.length():" + file1.length());
                                Picasso.with(UpdatePersonalDataActivity.this).load(file1).placeholder(R.mipmap.img_loading).error(R.mipmap.img_load_error).into(iv_head);
                                mPicFileList.add(file1);
                            }
                        }).launch();

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

    @OnClick({R.id.ll_back, R.id.ll_change_identity, R.id.ll_head_picture, R.id.ll_sex, R.id.ll_time, R.id.ll_address, R.id.tv_save_update})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_identity:
                //切换身份
                showTypePickerView();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_head_picture:
                //头像
                SoftUtil.hideSoft(this);
                showPop();
                break;
            case R.id.ll_sex:
                //性别
                showSexPickerView();
                break;
            case R.id.ll_time:
                //出生年月日
                showTimePickerView();
                break;
            case R.id.ll_address:
                //邮寄地址
                showAddressPickerView();
                break;
            case R.id.tv_save_update:
                //保存修改
                PostUpdateList();
                showLoadingDialog();
                break;
        }
    }

    //保存修改接口
    private void PostUpdateList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "update_user");
        map.put("nickname", et_nickName.getText().toString().trim());
        map.put("realname", et_realName.getText().toString().trim());
        map.put("sex", tv_sex.getText().toString().trim());
        map.put("birthday", tv_birthday.getText().toString().trim());
        map.put("address", tv_address.getText().toString().trim());
        String tvType = tv_type.getText().toString().trim();
        if (tvType != null) {
            if ("学生".equals(tvType)) {
                roleId = 0;
            }

            if ("老师".equals(tvType)) {
                roleId = 1;
            }
        }
        map.put("role_id", roleId);
        getJsonUtil().PostJson(this, map, mPicFileList, tv_save_update);
    }

    //用户信息接口
    public void PostUserInfoList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "user_info");
        getJsonUtil().PostJson(this, map);
    }

    private Handler mHandler = new Handler() {

        private Thread thread;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    //开始解析,在子线程中解析数据
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initJsonData();
                        }
                    });
                    thread.start();
                    break;
                case MSG_LOAD_SUCCESS:
                    //解析成功,显示地址选择器
                    isParseSuccess = true;
                    showAddressPickerView2();
                    break;
                case MSG_LOAD_FAILED:
                    //解析失败
                    ToastUtil.show(UpdatePersonalDataActivity.this, "地址数据解析失败", 0);
                    break;
            }
        }
    };

    //显示地址选择选择器
    private void showAddressPickerView() {
        //首先解析出assets目录下的json文件数据
        if (!isParseSuccess) {
            mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        } else {
            showAddressPickerView2();
        }
    }

    //显示地址选择选择器2
    private void showAddressPickerView2() {
        if (addressPickerView == null) {
            addressPickerView = new OptionsPickerView.Builder(UpdatePersonalDataActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String tx = options1Items.get(options1).getPickerViewText() +
                            options2Items.get(options1).get(options2) +
                            options3Items.get(options1).get(options2).get(options3);
                    tv_address.setText(tx);
                }
            }).build();
        }

        addressPickerView.setPicker(options1Items_string, options2Items, options3Items);
        addressPickerView.show();
    }

    private void initJsonData() {
        //从assets的json文件读出json数据
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getAssets();//获取assets的管理者
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open("province.json")));
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        String jsonData = sb.toString();

        ArrayList<JsonBean> detail = new ArrayList<>();
        ArrayList<String> ProvinceList = new ArrayList<>();

        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
                ProvinceList.add(entity.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = detail;
        options1Items_string.addAll(ProvinceList);

        for (int i = 0; i < detail.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < detail.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = detail.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市所有地区列表

                //如果无数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (detail.get(i).getCityList().get(c).getArea() == null
                        || detail.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(detail.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    //显示出生日期选择器
    private void showTimePickerView() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //设置起始选择数据
        startDate.set(1990, 0, 1);
        endDate.set(2020, 11, 31);

        // 默认全部显示
        if (timePickerView == null) {
            timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    tv_birthday.setText(getTime(date));
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCenterLabel(false)
                    .build();
        }

        timePickerView.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = sdf.format(date);
        return str;
    }

    private List<String> sexList = new ArrayList<>();//性别选择器数据集合

    //显示性别选择器
    private void showSexPickerView() {
        sexList.clear();
        sexList.add("男");
        sexList.add("女");

        if (sexPickerView == null) {
            sexPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String tx = sexList.get(options1);
                    tv_sex.setText(tx);
                }
            }).build();
        }

        sexPickerView.setPicker(sexList);
        sexPickerView.show();
    }

    private List<String> typeList = new ArrayList<>();//身份选择器数据集合

    //显示性别选择器
    private void showTypePickerView() {
        typeList.clear();
        typeList.add("学生");
        typeList.add("老师");

        if (typePickerView == null) {

            typePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String tx = typeList.get(options1);
                    showTypeDialog(tx);
                }
            }).build();
        }

        typePickerView.setPicker(typeList);
        typePickerView.show();
    }

}
