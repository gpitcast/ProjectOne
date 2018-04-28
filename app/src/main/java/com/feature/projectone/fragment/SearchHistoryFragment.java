package com.feature.projectone.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.adapter.SearchHistoryAdapter;
import com.feature.projectone.inter.HistoryItemListener;
import com.feature.projectone.util.SearchHistoryDao;
import com.feature.projectone.util.ToastUtil;
import com.feature.projectone.util.TvUtil;
import com.feature.projectone.view.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 * 搜索历史fragment
 */

public class SearchHistoryFragment extends BaseFragment {
    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.tv_clean)
    TextView tv_clean;

    private List<String> mDataList = new ArrayList<>();//历史搜索记录list
    private SearchHistoryAdapter searchHistoryAdapter;
    private HistoryItemListener historyItemListener;

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
        return R.layout.fragment_search_history;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        TvUtil.addUnderLine(tv_clean);

        initView();
    }

    private void initView() {
        searchHistoryAdapter = new SearchHistoryAdapter(getActivity(), mDataList);
        gridView.setAdapter(searchHistoryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ToastUtil.show(getActivity(), "点击第" + position + "个条目", 0);
                String str = mDataList.get(position);
                if (str != null && str.length() > 0) {
                    historyItemListener.OnHistoryItemClick(str);
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 不在最前端显示,相当于调用了onPause();
        } else {
            // 在最前端显示 相当于调用了onResume() 在每次显示历史搜索记录的时候，查询存储在本地的历史搜索记录
            selectData();
            searchHistoryAdapter.notifyDataSetChanged();
        }
    }

    //查询数据库的数据
    private void selectData() {
        SearchHistoryDao historyDao = new SearchHistoryDao(getActivity());
        List<String> list = historyDao.select();
        mDataList.clear();
        if (list != null) {
            mDataList.addAll(list);
        }
    }

    @OnClick({R.id.ll_clean_history})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_clean_history:
                if (mDataList == null || mDataList.size() == 0) {
                    ToastUtil.show(getActivity(), "搜索记录为空", 0);
                    return;
                }
                BaseDialog.Builder builder = new BaseDialog.Builder(getActivity());
                builder.setTitle("确定清空历史搜索记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SearchHistoryDao historyDao = new SearchHistoryDao(getActivity());
                        historyDao.deleteAll();
                        selectData();
                        searchHistoryAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        historyItemListener = (HistoryItemListener) context;
        super.onAttach(context);
    }
}
