package com.delsk.beautywelfare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.yalantis.phoenix.PullToRefreshView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, AppBarLayout.OnOffsetChangedListener {

    private int pageSize = 20;
    private int pageNo = 1;

    private String baseUrl = "http://gank.io/api/data/福利/" + pageSize + "/";

    private AppBarLayout mAppBarLayout;
    private RecyclerView mRecyclerView;
    private List<ImageBean.ResultsBean> mList;
    private ImageApdate imageApdate;
    private PullToRefreshView mToRefreshView;
    private int mCurPosition;
    private int mStartPosition;

    private Bundle getBundle;
    private SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (getBundle != null) {
                if (mStartPosition != mCurPosition && mList != null) {

                    String imgUrl = mList.get(mCurPosition).getUrl();
                    View newSharedElement = mRecyclerView.findViewWithTag(imgUrl);
                    if (newSharedElement != null) {
                        names.clear();
                        names.add(imgUrl);
                        sharedElements.clear();
                        sharedElements.put(imgUrl, newSharedElement);
                    }
                }
                getBundle = null;

            } else {
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (navigationBar != null) {
                    names.add(navigationBar.getTransitionName());
                    sharedElements.put(navigationBar.getTransitionName(), navigationBar);
                }
                if (statusBar != null) {
                    names.add(statusBar.getTransitionName());
                    sharedElements.put(statusBar.getTransitionName(), statusBar);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setExitSharedElementCallback(mCallback);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("BeautyWelfare");
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        setViewAdapter();

        mToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefresh);
        mToRefreshView.setRefreshing(true);
        mToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                requestData();
            }
        });

        requestData();

    }

    private void requestData() {
        OkHttpUtils.get().url(new StringBuilder(baseUrl).append(pageNo).toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    Log.d("mainActivity", response);
                    Gson gson = new Gson();
                    ImageBean bean = gson.fromJson(response, ImageBean.class);
                    if (bean != null) {
                        pageNo++;
                        mList = bean.getResults();
                        imageApdate.setNewData(bean.getResults());
                        mToRefreshView.setRefreshing(false);
                    }
                }

            }
        });

    }

    private void setViewAdapter() {
        imageApdate = new ImageApdate(mList);
        mRecyclerView.setAdapter(imageApdate);
        imageApdate.setOnLoadMoreListener(this, mRecyclerView);
        imageApdate.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, mList.get(position).getUrl());
                Intent mInent = new Intent(MainActivity.this, ImageDetailActivity.class);
                mInent.putParcelableArrayListExtra(Constant.EXTRA_IMAGEBEAN, (ArrayList<? extends Parcelable>) mList);
                mInent.putExtra(Constant.EXTRA_CURRENT_POSITION, position);
                startActivity(mInent, compat.toBundle());
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        OkHttpUtils.get().url(new StringBuilder(baseUrl).append(pageNo).toString()).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                imageApdate.loadMoreFail();
            }

            @Override
            public void onResponse(String response, int id) {
                if (!TextUtils.isEmpty(response)) {
                    Log.d("mainActivity", response);
                    Gson gson = new Gson();
                    ImageBean bean = gson.fromJson(response, ImageBean.class);
                    if (bean != null) {
                        pageNo++;
                        mList.addAll(bean.getResults());
                        imageApdate.addData(bean.getResults());
                        imageApdate.loadMoreComplete();
                    }
                } else {
                    imageApdate.loadMoreEnd();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, FormActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        mToRefreshView.setEnabled(verticalOffset == 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        getBundle = data.getExtras();
        mStartPosition = getBundle.getInt(Constant.EXTRA_START_POSITION);
        mCurPosition = getBundle.getInt(Constant.EXTRA_CURRENT_POSITION);
        if (mStartPosition != mCurPosition) {
            mRecyclerView.scrollToPosition(mCurPosition);
        }
        postponeEnterTransition();
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mRecyclerView.requestLayout();
                startPostponedEnterTransition();
                return true;
            }
        });
    }
}
