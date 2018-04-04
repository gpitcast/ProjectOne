package com.feature.projectone.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.feature.projectone.R;
import com.feature.projectone.adapter.PeiTaoSourceAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/2.
 * 资源下载目录下的  配套资源  fragment
 */

public class PeiTaoSourceFragment extends BaseFragment {
    @BindView(R.id.xRecyclerView)
    XRecyclerView xRecyclerView;

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
        return R.layout.fragment_peitao_source;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        initXRecyclerView();
    }

    private void initXRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(manager);
        PeiTaoSourceAdapter peiTaoSourceAdapter = new PeiTaoSourceAdapter(getActivity());
        xRecyclerView.setAdapter(peiTaoSourceAdapter);
        xRecyclerView.setPullRefreshEnabled(false);
        xRecyclerView.setLoadingMoreEnabled(true);
    }
}
