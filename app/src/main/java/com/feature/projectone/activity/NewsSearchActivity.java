package com.feature.projectone.activity;

import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feature.projectone.R;
import com.feature.projectone.fragment.SearchHistoryFragment;
import com.feature.projectone.fragment.SearchResultFragment;
import com.feature.projectone.inter.HistoryItemListener;
import com.feature.projectone.inter.MyPassViewListener;
import com.feature.projectone.util.EtDrawableLeftUtil;
import com.feature.projectone.util.SearchHistoryDao;
import com.feature.projectone.util.SoftUtil;
import com.feature.projectone.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 * 新闻搜索界面
 */

public class NewsSearchActivity extends BaseActivity implements MyPassViewListener, HistoryItemListener {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;

    private SearchResultFragment searchResultFragment;
    private SearchHistoryFragment searchHistoryFragment;
    private boolean isSearchResultFragmentShowing;
    private boolean isSearchHistoryFragmentShowing;

    @Override
    protected void Response(String code, String msg, String url, Object result) {
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_news_search);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    public void initView() {
        initViews();
    }

    private void initViews() {

        showSearchHistoryFragment();//首次进入页面显示搜索历史fragment
        EtDrawableLeftUtil.setEtImgSize(et_search);

        //添加软键盘的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (et_search.getText().toString().trim() != null && et_search.getText().toString().trim().length() > 0) {
                        SoftUtil.hideSoft(NewsSearchActivity.this);//隐藏键盘
                        SearchHistoryDao historyDao = new SearchHistoryDao(NewsSearchActivity.this);
                        historyDao.add(et_search.getText().toString().trim());
                        showSearchResultFragment();//显示搜索结果的fragment
                    } else {
                        ToastUtil.show(NewsSearchActivity.this, "搜索关键字不能为空", 0);
                    }
                    return true;
                }
                return false;
            }
        });

        //添加EidtText的文本内容变化的监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString().trim();
                if (str == null || str.length() == 0 && isSearchResultFragmentShowing) {
                    showSearchHistoryFragment();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (searchResultFragment != null) {
            transaction.hide(searchResultFragment);
        }
        if (searchHistoryFragment != null) {
            transaction.hide(searchHistoryFragment);
        }
    }

    //切换搜索结果的fragment
    private void showSearchResultFragment() {
        isSearchResultFragmentShowing = true;
        isSearchHistoryFragmentShowing = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (searchResultFragment == null) {
            searchResultFragment = new SearchResultFragment();
        }
        if (searchHistoryFragment == null) {
            searchHistoryFragment = new SearchHistoryFragment();
        }
        hideFragment(transaction);
        transaction.show(searchResultFragment);
        transaction.commit();
    }

    //切换搜索结果的fragment
    private void showSearchHistoryFragment() {
        isSearchResultFragmentShowing = false;
        isSearchHistoryFragmentShowing = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (searchResultFragment == null) {
            searchResultFragment = new SearchResultFragment();
            transaction.add(R.id.framelayout, searchResultFragment);
        }
        if (searchHistoryFragment == null) {
            searchHistoryFragment = new SearchHistoryFragment();
            transaction.add(R.id.framelayout, searchHistoryFragment);
        }
        hideFragment(transaction);
        transaction.show(searchHistoryFragment);
        transaction.commit();
    }

    @Override
    public void afterInitView() {
    }

    @Override
    public EditText getEtSearch() {
        return et_search;
    }

    @Override
    public void OnHistoryItemClick(String str) {
        et_search.setText(str);
        et_search.setSelection(et_search.getText().length());//让光标移动到文本框末尾
        SoftUtil.hideSoft(NewsSearchActivity.this);//隐藏键盘
        SearchHistoryDao historyDao = new SearchHistoryDao(NewsSearchActivity.this);
        historyDao.add(et_search.getText().toString().trim());
        showSearchResultFragment();//显示搜索结果的fragment
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (isSearchHistoryFragmentShowing) {
                    //当历史搜索fragment显示的时候，直接退出界面
                    isSearchHistoryFragmentShowing = false;
                    finish();
                }
                if (isSearchResultFragmentShowing) {
                    //当搜索结果fragment显示的时候，清空EditText，并显示历史搜索
                    isSearchResultFragmentShowing = false;
                    et_search.setText("");
                    showSearchHistoryFragment();
                }
                break;
        }
    }
}
