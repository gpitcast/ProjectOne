package com.feature.projectone.adapter;

import android.content.Context;

import com.feature.projectone.other.NewsFourDelagate;
import com.feature.projectone.other.NewsOneDelagate;
import com.feature.projectone.other.NewsThreeDelagate;
import com.feature.projectone.other.NewsTwoDelagate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class NewsAdapter extends MultiItemTypeAdapter<String> {
    public NewsAdapter(Context context, List<String> datas) {
        super(context, datas);
        addItemViewDelegate(new NewsOneDelagate(context, (ArrayList<String>) datas));
        addItemViewDelegate(new NewsTwoDelagate());
        addItemViewDelegate(new NewsThreeDelagate(context));
        addItemViewDelegate(new NewsFourDelagate());
    }
}
