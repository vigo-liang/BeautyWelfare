package com.delsk.beautywelfare;

import android.app.Application;

import com.bumptech.glide.request.target.ViewTarget;

/**
 * Created by Delsk on 2017/6/22
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.indexTag);
    }
}
