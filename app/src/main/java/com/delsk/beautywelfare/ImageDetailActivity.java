package com.delsk.beautywelfare;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ViewTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageDetailActivity extends AppCompatActivity {

    private ArrayList<ImageBean.ResultsBean> mList;
    private ViewPageImageApdater mAdapter;
    private ViewPager mViewPager;
    private int mStartPosition;
    private int mCurPosition;
    private boolean isReturn;
    private SharedElementCallback mCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (isReturn) {
                ImageView imageView = (ImageView) mAdapter.getCurrentView().findViewById(R.id.image_detail);
                Rect rect = new Rect();
                if (imageView.getLocalVisibleRect(rect)) {
                    if (mStartPosition != mCurPosition) {
                        names.clear();
                        names.add(imageView.getTransitionName());
                        sharedElements.clear();
                        sharedElements.put(imageView.getTransitionName(), imageView);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        postponeEnterTransition();
        setEnterSharedElementCallback(mCallback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.image_viewpager);

        mList = getIntent().getParcelableArrayListExtra(Constant.EXTRA_IMAGEBEAN);
        mStartPosition = getIntent().getIntExtra(Constant.EXTRA_CURRENT_POSITION, 0);
        if (savedInstanceState == null) {
            mCurPosition = mStartPosition;
        } else {
            mCurPosition = savedInstanceState.getInt(Constant.EXTRA_CURRENT_POSITION);
        }

        if (mList != null) {
            mAdapter = new ViewPageImageApdater(ImageDetailActivity.this, mList, mStartPosition);
            mViewPager.setAdapter(mAdapter);
        }

        mViewPager.setCurrentItem(mStartPosition);
        setTitleBarAction(mStartPosition);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPosition = position;
                if (mViewPager != null) {
                    setTitleBarAction(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constant.EXTRA_CURRENT_POSITION, mCurPosition);
    }

    @Override
    public void finishAfterTransition() {

        isReturn = true;
        Intent intent = new Intent();
        intent.putExtra(Constant.EXTRA_START_POSITION, mStartPosition);
        intent.putExtra(Constant.EXTRA_CURRENT_POSITION, mCurPosition);
        setResult(RESULT_OK, intent);

        super.finishAfterTransition();
    }

    private void setTitleBarAction(int position) {
        if (mViewPager != null && mList.size() > 1) {
            int totalNum = mViewPager.getAdapter().getCount();

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(new StringBuilder(position + 1 + "").append("/").append(totalNum).toString());
            }

        }

    }
}
