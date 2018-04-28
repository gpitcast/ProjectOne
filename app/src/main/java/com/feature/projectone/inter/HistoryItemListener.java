package com.feature.projectone.inter;

/**
 * Created by Administrator on 2018/4/21.
 * 用来传递搜索fragment的历史搜索被点击像fragment发送消息
 */

public interface HistoryItemListener {
    void OnHistoryItemClick(String str);
}
