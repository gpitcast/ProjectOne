package com.feature.projectone.util;

import android.content.Context;
import android.widget.Toast;

import com.feature.projectone.inter.JsonInterface;

/**
 * Created by Administrator on 2018/3/28.
 */

public class JsonUtils {
    JsonInterface jsonInterface;

    public void setJsonInterfaceListener(JsonInterface jsonInterface) {
        this.jsonInterface = jsonInterface;
    }

    public void getJsonUtils(Context context) {
        Toast.makeText(context, "测试GIT", Toast.LENGTH_SHORT).show();
    }
}
