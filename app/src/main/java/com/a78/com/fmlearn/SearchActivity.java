package com.a78.com.fmlearn;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a78.com.fmlearn.adapters.RecommendationListAdapter;
import com.a78.com.fmlearn.adapters.SearchRecomedAdapter;
import com.a78.com.fmlearn.base.BaseActivity;
import com.a78.com.fmlearn.interfaces.ISearchCallBack;
import com.a78.com.fmlearn.presenters.RecommendationContentPresenter;
import com.a78.com.fmlearn.presenters.SearchPresenter;
import com.a78.com.fmlearn.views.FlowTextLayout;
import com.a78.com.fmlearn.views.UiLoad;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchCallBack {

    private static final String TAG = "SearchActivity";

    private ImageView backButton;
    private TextView searchButton;
    private EditText searchEdit;
    private FrameLayout containFrameLayout;
    private SearchPresenter searchPresenter;
    private UiLoad contentLoad;
    private RecyclerView searchResultRecycler;
    private RecommendationListAdapter listAdapter;
    private FlowTextLayout flowTextLayout;
    private InputMethodManager inputManager;
    private ImageView deleteButton;
    private RecyclerView searchRecomendRecycler;
    private SearchRecomedAdapter searchRecomedAdapter;
    private TwinklingRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initEvent();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.unregisterViewCallback(this);
    }

    private void initView() {
        backButton = findViewById(R.id.search_back_image);
        searchButton = findViewById(R.id.search_beign_text);
        searchEdit = findViewById(R.id.search_content_edit);
        deleteButton = findViewById(R.id.search_delete_image);
        deleteButton.setVisibility(View.GONE);
        searchEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchEdit.requestFocus();
                inputManager.showSoftInput(searchEdit,InputMethodManager.SHOW_IMPLICIT);
            }
        },500);
        containFrameLayout = findViewById(R.id.search_contain_fragment);

        inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    private void initEvent() {

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEdit.setText("");
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = searchEdit.getText().toString();
                if (!TextUtils.isEmpty(keyword)){
                    searchPresenter.doSearch(keyword);
                    contentLoad.updateUi(UiLoad.UiLoadType.LOADING );
                }else {
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    searchPresenter.getHotWord();
                    deleteButton.setVisibility(View.GONE);
                }else {
                    deleteButton.setVisibility(View.VISIBLE);
                    if (searchPresenter != null) {
                        searchPresenter.getRecommendWord(charSequence.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (contentLoad == null) {
            contentLoad = new UiLoad(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView();
                }
            };
        }
        if (contentLoad.getParent() instanceof ViewGroup){
            ((ViewGroup) contentLoad.getParent()).removeView(contentLoad);
        }

        containFrameLayout.addView(contentLoad);

    }

    private View createSuccessView() {
        View view = LayoutInflater.from(this).inflate(R.layout.search_result_layout, null);
        flowTextLayout = view.findViewById(R.id.search_flowtext);
        flowTextLayout.setClickListener(new FlowTextLayout.ItemClickListener() {
            @Override
            public void onItemClick(String text) {
//                Toast.makeText(SearchActivity.this, text, Toast.LENGTH_SHORT).show();
                switchToSearch(text);
            }
        });

        searchResultRecycler = view.findViewById(R.id.search_result_recycler);
        LinearLayoutManager resultLayoutManager = new LinearLayoutManager(this);
        searchResultRecycler.setLayoutManager(resultLayoutManager);
        listAdapter = new RecommendationListAdapter();
        searchResultRecycler.setAdapter(listAdapter);
        listAdapter.setonRecommendationRecycleClickListener(new RecommendationListAdapter.OnRecommendationRecycleClickListener() {
            @Override
            public void itemClick(int position, Album date) {
                RecommendationContentPresenter.getInstance().setTagAlbun(date);
                Intent intent = new Intent(SearchActivity.this, RecommendationContentActivity.class);
                startActivity(intent);
            }
        });


        searchRecomendRecycler = view.findViewById(R.id.search_recomend_recycler);
        LinearLayoutManager recomendLinearLayoutManager = new LinearLayoutManager(this);
        searchRecomendRecycler.setLayoutManager(recomendLinearLayoutManager);
        searchRecomedAdapter = new SearchRecomedAdapter();
        searchRecomendRecycler.setAdapter(searchRecomedAdapter);
        searchRecomedAdapter.setItemClickLisenter(new SearchRecomedAdapter.ItemClick() {
            @Override
            public void onItemClick(String keyword) {
                switchToSearch(keyword);
            }
        });

        refreshLayout = view.findViewById(R.id.search_result_refresh_layout);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (searchPresenter != null) {
                    searchPresenter.loadMore();
                }
            }
        });
        return view;
    }

    private void switchToSearch(String text) {
        searchEdit.setText(text);
        searchEdit.setSelection(text.length());
        if (searchPresenter != null) {
            searchPresenter.doSearch(text);
        }
        if (contentLoad != null) {
            contentLoad.updateUi(UiLoad.UiLoadType.LOADING);
        }
    }

    private void initPresenter() {
        searchPresenter = SearchPresenter.getInstance();
        searchPresenter.registerViewCallBack(this);
        searchPresenter.getHotWord();
        hidenAllSuccessView();
        flowTextLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchResultLoaded(List<Album> result) {
        hidenAllSuccessView();
        refreshLayout.setVisibility(View.VISIBLE);

//        inputManager.hideSoftInputFromWindow(containFrameLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        inputManager.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        if (result != null) {
            if (result.size() == 0) {
                if (contentLoad != null) {
                    contentLoad.updateUi(UiLoad.UiLoadType.EMPTY);
                }
            }else {
                if (listAdapter != null) {
                    listAdapter.setAlbumList(result);
                }
                if (contentLoad != null) {
                    contentLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
                }
            }
        }
    }

    @Override
    public void onHotWordLoaded(List<HotWord> hotWordList) {
        hidenAllSuccessView();
        flowTextLayout.setVisibility(View.VISIBLE);
        List<String> stringList = new ArrayList<>();
        if (hotWordList != null){
            contentLoad.updateUi(UiLoad.UiLoadType.SUCCESS);
            stringList.clear();
            for (HotWord word : hotWordList) {
                stringList.add(word.getSearchword());
            }
            flowTextLayout.setTextContents(stringList);
        }

    }

    @Override
    public void onLoadMoreResult(List<Album> result, boolean isOkay) {
        refreshLayout.finishLoadmore();
        if (isOkay){
            listAdapter.setAlbumList(result);
        }
    }

    @Override
    public void onRecommendWordLoaded(List<QueryResult> keyWordList) {
        if (keyWordList != null){
            searchRecomedAdapter.upDate(keyWordList);
            hidenAllSuccessView();
            searchRecomendRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        if (contentLoad != null) {
            contentLoad.updateUi(UiLoad.UiLoadType.ERROR);
        }
    }

    private void hidenAllSuccessView() {
        flowTextLayout.setVisibility(View.GONE);
//        searchResultRecycler.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        searchRecomendRecycler.setVisibility(View.GONE);
    }
}
