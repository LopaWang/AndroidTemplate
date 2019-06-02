package com.hc.framework.engine.dao;

import org.litepal.crud.DataSupport;

/**
 * description：
 * <p/>
 * Created by 曾辉 on 2016/10/17.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class CacheBean extends DataSupport{
    // 缓存的key  url(接口)
    public String cacheKey;

    // 缓存的服务器返回的 jsonStr
    public String cacheJsonStr;
}
