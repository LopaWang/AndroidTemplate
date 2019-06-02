package com.hc.framework.ioc.util;

import android.app.Activity;
import android.view.View;

/**
 * description：注解findViewById的辅助类
 * <p/>
 * Created by 曾辉 on 2016/10/17.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class ViewFinder {
    private View mView;
    private Activity mActivity;
    public ViewFinder(View view){
        this.mView = view;
        this.mActivity = null;
    }

    public ViewFinder(Activity activity){
        this.mActivity = activity;
        this.mView = null;
    }


    public View findViewById(int viewId) {
        if(mView != null){
            return mView.findViewById(viewId);
        }
        return mActivity.findViewById(viewId);
    }
}
