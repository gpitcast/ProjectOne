package com.youyi.ywl.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.youyi.ywl.R;
import com.youyi.ywl.bean.JsonBean;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.PhoneNumberCheckedUtil;
import com.youyi.ywl.util.SoftUtil;
import com.youyi.ywl.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/9/5.
 * 地址编辑界面
 */

public class AddressEditActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_select_city)
    TextView tv_select_city;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_phone_number)
    EditText et_phone_number;
    @BindView(R.id.et_detail_address)
    EditText et_detail_address;

    private static final String ADD_ADDRESS_URL = HttpUtils.Host + "/user/addAddress";//添加收货地址接口
    private static final String EDIT_ADDRESS_URL = HttpUtils.Host + "/user/editAddress";//修改收货地址接口
    private OptionsPickerView addressPickerView;
    private boolean isParseSuccess;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();//省数据
    private ArrayList<String> options1Items_string = new ArrayList<>();//String类型的省数据
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市数据
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//地级数据

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
                    ToastUtil.show(AddressEditActivity.this, "地址数据解析失败", 0);
                    break;
            }
        }
    };
    private HashMap<String, Object> dataMap;
    private String type;

    @Override
    protected void handleIntent(Intent intent) {
        if (intent != null) {
            type = intent.getStringExtra("type");//区分是新建地址还是编辑地址
            dataMap = (HashMap<String, Object>) intent.getExtras().getSerializable("dataMap");
            Log.i("AddressEditActivity", "           type:          " + type);
        }
    }

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case ADD_ADDRESS_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;

                    ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        finish();
                        EventBus.getDefault().post("刷新收货地址列表");
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
            case EDIT_ADDRESS_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        finish();
                        EventBus.getDefault().post("刷新收货地址列表");
                    }
                    ToastUtil.show(this, resultMap.get("msg") + "", 0);
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_address_edit);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_title.setText("地址管理");
        initListener();//添加所有的editText的监听,来动态的改变'确定'按钮的颜色

        if ("0".equals(type)) {
            //编辑地址
            tv_confirm.setBackgroundResource(R.drawable.bg_blue_dark_btn);
            tv_confirm.setEnabled(true);
        } else if ("1".equals(type)) {
            //添加地址
            tv_confirm.setBackgroundResource(R.drawable.bg_blue_light_btn);
            tv_confirm.setEnabled(false);
        }

        if (dataMap != null) {
            et_name.setText(dataMap.get("username") + "");
            et_phone_number.setText(dataMap.get("tel") + "");
            tv_select_city.setText(dataMap.get("city") + "");
            et_detail_address.setText(dataMap.get("address") + "");
        }
    }

    //添加所有的editText的监听,来动态的改变'确定'按钮的颜色
    private void initListener() {
        //名称的editText监听
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //电话号码的editText监听
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //详细地址的editText监听
        et_detail_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //检查eidtText的内容
    private void checkText() {
        String nameTxt = et_name.getText().toString().trim();
        String phoneNumberTxt = et_phone_number.getText().toString().trim();
        String detailAddressTxt = et_detail_address.getText().toString().trim();
        String selectCityTxt = tv_select_city.getText().toString().trim();

        if (nameTxt != null && nameTxt.length() > 0 && phoneNumberTxt != null && phoneNumberTxt.length() > 0
                && detailAddressTxt != null && detailAddressTxt.length() > 0 && selectCityTxt != null && !selectCityTxt.equals("请选择城市")) {
            tv_confirm.setBackgroundResource(R.drawable.bg_blue_dark_btn);
            tv_confirm.setEnabled(true);
        } else {
            tv_confirm.setBackgroundResource(R.drawable.bg_blue_light_btn);
            tv_confirm.setEnabled(false);
        }
    }

    @Override
    public void afterInitView() {
    }


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
            addressPickerView = new OptionsPickerView.Builder(AddressEditActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String tx = options1Items.get(options1).getPickerViewText() +
                            options2Items.get(options1).get(options2) +
                            options3Items.get(options1).get(options2).get(options3);
                    tv_select_city.setText(tx);
                    checkText();//检查输入情况动态改变'确定'按钮的颜色
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

    @OnClick({R.id.ll_back, R.id.tv_select_city, R.id.tv_confirm})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_select_city:
                //选择城市
                SoftUtil.hideSoft(this);
                showAddressPickerView();
                break;
            case R.id.tv_confirm:
                //确定按钮
                if (!PhoneNumberCheckedUtil.checkNumber(et_phone_number.getText().toString().trim())) {
                    ToastUtil.show(this, "请输入正确的手机号码", 0);
                    return;
                }
                if ("0".equals(type)) {
                    //编辑地址
                    showLoadingDialog();
                    PostEditAddressList();//请求修改收货地址接口
                } else if ("1".equals(type)) {
                    //添加地址
                    showLoadingDialog();
                    PostAddAddressList();//请求添加收货地址接口
                }

                break;
        }
    }

    //请求修改收货地址接口
    private void PostEditAddressList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "editAddress");
        map.put("id", dataMap.get("id") + "");
        map.put("username", et_name.getText().toString().trim());
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("city", tv_select_city.getText().toString().trim());
        map.put("address", et_detail_address.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_confirm);
    }

    //请求添加收货地址接口
    private void PostAddAddressList() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user");
        map.put("action", "addAddress");
        map.put("username", et_name.getText().toString().trim());
        map.put("tel", et_phone_number.getText().toString().trim());
        map.put("city", tv_select_city.getText().toString().trim());
        map.put("address", et_detail_address.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_confirm);
    }
}
