package com.hc.framework.engine.util;

import java.util.Map;

/**
 * Created by hui on 2016/10/10.
 * 自定义的引擎规范
 */
public interface HttpEngine {
    // post提交  参数  url  自定义回调
    public void post(Map<String, Object> params, String url, HttpCallBack httpCallBack);

    // get提交
    public void get(Map<String, Object> params, String url, HttpCallBack httpCallBack);


    // post提交  参数  url  自定义回调  isCache  是否读取缓存
    public void post(Map<String, Object> params, String url, HttpCallBack httpCallBack, boolean isCache);

    // get提交  isCache 是否读取缓存
    public void get(Map<String, Object> params, String url, HttpCallBack httpCallBack, boolean isCache);
}
