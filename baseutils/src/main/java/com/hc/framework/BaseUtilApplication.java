package com.hc.framework;

import android.app.Application;

import com.hc.framework.log.LogUtils;

import org.litepal.LitePalApplication;
import org.xutils.x;

/**
 * Created by hui on 2016/10/10.
 */
public class BaseUtilApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        // isDebuge 代表当前是调试模式  true再调试的时候配置的 false上线需要配置的
        LogUtils.initParam(true);

        // 这是初始化数据库
        LitePalApplication.initialize(this);
    }
}
