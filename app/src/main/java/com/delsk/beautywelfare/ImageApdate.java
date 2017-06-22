package com.delsk.beautywelfare;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Delsk on 2017/6/15
 */

public class ImageApdate extends BaseQuickAdapter<ImageBean.ResultsBean, BaseViewHolder> {


    public ImageApdate(@Nullable List<ImageBean.ResultsBean> data) {
        super(R.layout.recycler_item_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean.ResultsBean item) {
        ImageView imageView = helper.getView(R.id.item_image);
        imageView.setTag(item.getUrl());
        Glide.with(mContext).load(item.getUrl()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
        imageView.setTransitionName(item.getUrl());
    }
}
