package com.hc.framework.engine.util;

import android.os.Handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hui on 2016/10/10
 */
public class HttpUtils implements HttpEngine{

    private HttpEngine mHttpEngine;

    public static Handler handler = new Handler();

    public HttpUtils(){
        // 这里用来切换引擎
        mHttpEngine = new OkHttpEngine<>();
    }

    @Override
    public void post(Map<String, Object> params, String url, HttpCallBack httpCallBack) {
        mHttpEngine.post(params,url,httpCallBack);
    }

    @Override
    public void get(Map<String, Object> params, String url, HttpCallBack httpCallBack) {
        if(params == null){
            params = new HashMap<>();
        }
        params.put("key","c429552ff2d965929082148f13344f06");
//        params.put("longitude","113.000366");
//        params.put("manifest_version_code","570");
//        params.put("update_version_code","5701");
//        params.put("latitude","28.171377");
//        params.put("device_type","Redmi+Note+3");
//        params.put("device_platform","android");
        mHttpEngine.get(params, url, httpCallBack);
    }

    @Override
    public void post(Map<String, Object> params, String url, HttpCallBack httpCallBack, boolean isCache) {
        mHttpEngine.post(params, url, httpCallBack, isCache);
    }

    @Override
    public void get(Map<String, Object> params, String url, HttpCallBack httpCallBack, boolean isCache) {
        mHttpEngine.get(params, url, httpCallBack, isCache);
    }


    /**
     * 获取类上的泛型class
     */
    public static Class getTypeClass(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) types[0];
    }

    /**
     * 组装URL 请求参数
     */
    public static String appendParams(String url, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if(url.contains("?")){
            sb.append(url + "&");
        }else {
            sb.append(url + "?");
        }

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
