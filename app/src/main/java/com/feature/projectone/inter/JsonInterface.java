package com.feature.projectone.inter;

/**
 * Created by Administrator on 2018/3/28.
 * 传递网络请求数据接口
 */

public interface JsonInterface {
    void JsonResponse(String code, String msg, String url,Object result);
}
