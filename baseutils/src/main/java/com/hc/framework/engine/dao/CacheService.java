package com.hc.framework.engine.dao;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * description：
 * <p/>
 * Created by 曾辉 on 2016/10/17.
 * QQ：240336124
 * Email: 240336124@qq.com
 * Version：1.0
 */
public class CacheService {
    public static String queryCache(String url) {
        List<CacheBean> caches = DataSupport.where("cacheKey=?", url).find(CacheBean.class);
        if (caches != null && caches.size() >= 1) {
            CacheBean cacheBean = caches.get(0);
            String jsonStr = cacheBean.cacheJsonStr;
            return jsonStr;
        }
        return "";
    }

    public static void addCache(String key,String jsonStr) {
        // 把原来的清空 删除
        DataSupport.deleteAll(CacheBean.class, "cacheKey = ?" , key);

        // 直接添加
        CacheBean cacheBean = new CacheBean();
        cacheBean.cacheKey = key;
        cacheBean.cacheJsonStr = jsonStr;
        cacheBean.save();
    }
}
