package com.delsk.beautywelfare;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Delsk on 2017/6/19
 */

public class ViewPageImageApdater extends PagerAdapter {

    private Activity mContext;
    private List<ImageBean.ResultsBean> mImageUrlList;
    private int mStartPosition;
    private View currentView;


    public ViewPageImageApdater(Activity mContext, List<ImageBean.ResultsBean> mImageUrlList, int mStartPosition) {
        this.mContext = mContext;
        this.mImageUrlList = mImageUrlList;
        this.mStartPosition = mStartPosition;
    }

    @Override
    public int getCount() {
        return mImageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentView = (View) object;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.image_deltail_item_layout, null);

        String imageUrl = mImageUrlList.get(position).getUrl();
        final ImageView imageView = (ImageView) viewLayout.findViewById(R.id.image_detail);
        Glide.with(container.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
        imageView.setTransitionName(imageUrl);
        if (position == mStartPosition) {
            imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mContext.startPostponedEnterTransition();
                    return true;
                }
            });
        }
        container.addView(viewLayout, 0);

        return viewLayout;
    }

    public View getCurrentView() {
        return currentView;
    }
}
