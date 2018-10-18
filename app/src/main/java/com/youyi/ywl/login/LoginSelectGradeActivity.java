package com.youyi.ywl.login;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.youyi.ywl.R;
import com.youyi.ywl.activity.BaseActivity;
import com.youyi.ywl.activity.MainActivity;
import com.youyi.ywl.other.Constanst;
import com.youyi.ywl.util.HttpUtils;
import com.youyi.ywl.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/10/7.
 * 登录 选择学段和年级界面
 */

public class LoginSelectGradeActivity extends BaseActivity {
    @BindView(R.id.tv_select_study_section)
    TextView tv_select_study_section;
    @BindView(R.id.tv_select_grade)
    TextView tv_select_grade;
    @BindView(R.id.tv_start_study)
    TextView tv_start_study;

    private List<String> studyList = new ArrayList<>();
    private List<String> gradeList_xiaoxue = new ArrayList<>();
    private List<String> gradeList_chuzhong = new ArrayList<>();
    private OptionsPickerView studyPickerView;
    private OptionsPickerView xiaoxueGradePicekrView;
    private OptionsPickerView chuzhongGradePickerView;

    private static final String GRADE_URL = HttpUtils.Host + "/user2/learning";

    @Override
    protected void Response(String code, String msg, String url, Object result) {
        switch (url) {
            case GRADE_URL:
                dismissLoadingDialog();
                if (Constanst.success_net_code.equals(code)) {
                    HashMap resultMap = (HashMap) result;
                    String status = resultMap.get("status") + "";
                    if ("0".equals(status)) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.show(this, resultMap.get("msg") + "", 0);
                    }
                } else {
                    ToastUtil.show(this, msg, 0);
                }
                break;
        }
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_login_select_grade);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        tv_start_study.setClickable(false);
        initPickerData();
    }

    private void initPickerData() {
        studyList.add("小学");
        studyList.add("初中");

        gradeList_xiaoxue.add("一年级");
        gradeList_xiaoxue.add("二年级");
        gradeList_xiaoxue.add("三年级");
        gradeList_xiaoxue.add("四年级");
        gradeList_xiaoxue.add("五年级");
        gradeList_xiaoxue.add("六年级");

        gradeList_chuzhong.add("七年级");
        gradeList_chuzhong.add("八年级");
        gradeList_chuzhong.add("九年级");

    }

    @Override
    public void afterInitView() {
    }

    @OnClick({R.id.tv_select_study_section, R.id.tv_select_grade, R.id.tv_start_study})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_study_section:
                //选择学段
                showStudySectionPickerView();
                break;
            case R.id.tv_select_grade:
                //选择年级
                if (studyType == 0) {
                    //显示小学的选择器
                    showXiaoxueGradePickerView();
                } else if (studyType == 1) {
                    //显示初中的选择器
                    showChuzhongGradePickerView();
                }
                break;
            case R.id.tv_start_study:
                PostSetGradeList();
                break;
        }
    }

    /**
     * 设置身份
     */
    private void PostSetGradeList() {
        showLoadingDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("controller", "user2");
        map.put("action", "learning");
        map.put("type", studyType + 1);//学段 1代表小学,2代表初中
        map.put("grade", tv_select_grade.getText().toString().trim());
        getJsonUtil().PostJson(this, map, tv_start_study);
    }

    /**
     * 显示初中年级的选择器
     */
    private void showChuzhongGradePickerView() {
        if (chuzhongGradePickerView == null) {
            chuzhongGradePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    tv_select_grade.setText(gradeList_chuzhong.get(options1));
                    if (!isFirstChange_nianji) {
                        isFirstChange_nianji = true;
                    }

                    if (isFirstChange_nianji && isFirstChange_xueduan) {
                        tv_start_study.setBackgroundColor(getResources().getColor(R.color.orangeone));
                        tv_start_study.setClickable(true);
                    }
                }
            }).setTitleText("分类")
                    .build();
            chuzhongGradePickerView.setPicker(gradeList_chuzhong);
        }
        chuzhongGradePickerView.show();
    }

    /**
     * 显示小学年级的选择器
     */
    private void showXiaoxueGradePickerView() {
        if (xiaoxueGradePicekrView == null) {
            xiaoxueGradePicekrView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    tv_select_grade.setText(gradeList_xiaoxue.get(options1));
                    if (!isFirstChange_nianji) {
                        isFirstChange_nianji = true;
                    }

                    if (isFirstChange_nianji && isFirstChange_xueduan) {
                        tv_start_study.setBackgroundColor(getResources().getColor(R.color.orangeone));
                        tv_start_study.setClickable(true);
                    }
                }
            }).setTitleText("分类")
                    .build();
            xiaoxueGradePicekrView.setPicker(gradeList_xiaoxue);
        }
        xiaoxueGradePicekrView.show();
    }

    private int studyType = -1;//用来记录用户选择的学段 0代表小学,1代表初中
    private boolean isFirstChange_nianji;//是否第一次进去选择grade
    private boolean isFirstChange_xueduan;//是否第一次进去选择学段

    /**
     * 显示学段选择器
     */
    private void showStudySectionPickerView() {
        if (studyPickerView == null) {
            studyPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    tv_select_study_section.setText(studyList.get(options1));
                    if (studyType != options1) {
                        studyType = options1;
                        if (isFirstChange_nianji) {
                            tv_select_grade.setText("一年级");
                        }
                    }

                    if (!isFirstChange_xueduan) {
                        isFirstChange_xueduan = true;
                    }

                    if (isFirstChange_nianji && isFirstChange_xueduan) {
                        tv_start_study.setBackgroundColor(getResources().getColor(R.color.orangeone));
                        tv_start_study.setClickable(true);
                    }
                }
            }).setTitleText("分类")
                    .build();

            studyPickerView.setPicker(studyList);
        }
        studyPickerView.show();
    }
}
