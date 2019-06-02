package com.hc.framework.engine.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hc.framework.engine.dao.CacheService;
import com.hc.framework.log.LogUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hui on 2016/10/10.
 */
public class OkHttpEngine<T> implements HttpEngine {
    private static final String TAG = "OkHttpEngine";

    @Override
    public void post(Map<String, Object> params, String url, final HttpCallBack httpCallBack) {
        post(params,url,httpCallBack,false);
    }

    @Override
    public void get(Map<String, Object> params, String url, final HttpCallBack httpCallBack) {
        get(params, url, httpCallBack, false);
    }

    @Override
    public void post(Map<String, Object> params, final String url, final HttpCallBack httpCallBack, final boolean isCache) {
        // 获取泛型的类型
        final Class<T> typeClass = HttpUtils.getTypeClass(httpCallBack);
        // 变成可操作的对象
        final Gson gson = new Gson();

        // 判断一下是否要读取缓存
        if(isCache){
            // 需要读取缓存   查询数据库    判断数据库有没有缓存信息   有则那么直接  httpCallBack.onSuccess
            /*List<CacheBean> caches = DataSupport.where("cacheKey=?", url).find(CacheBean.class);
            if(caches != null && caches.size()>=1){
                CacheBean cacheBean = caches.get(0);
                String jsonStr = cacheBean.cacheJsonStr;
                // 利用Gson转换类型
                final T resultObj = gson.fromJson(jsonStr, typeClass);
                httpCallBack.onSuccess(resultObj);

                LogUtils.e(TAG,"读取缓存成功");
            }*/

            String cacheJsonStr = CacheService.queryCache(url);
            if(!TextUtils.isEmpty(cacheJsonStr)){
                final T resultObj = gson.fromJson(cacheJsonStr, typeClass);
                httpCallBack.onSuccess(resultObj);
            }
        }

        // 方便给内部类用
        final String cacheUrl = HttpUtils.appendParams(url,params);
        LogUtils.e("请求接口： " + cacheUrl);

        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody.Builder pramsBuilder= new FormBody.Builder();

        // 添加的参数肯定是从  params 里面解析  遍历Map集合
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,Object> entry = (Map.Entry) iterator.next();
            /*Object key = entry.getKey();
            Object val = entry.getValue();*/
            pramsBuilder.add(entry.getKey(),entry.getValue()+"");
        }

        RequestBody formBody =  pramsBuilder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = new String(response.body().bytes());

                LogUtils.e("OkHttpEngine", result);

                // 利用Gson转换类型
                final T resultObj = gson.fromJson(result, typeClass);

                HttpUtils.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 把它弄到主线程中
                        httpCallBack.onSuccess(resultObj);
                    }
                });

                // 判断是不是需要缓存数据
                if(isCache){
                    // 如果需要  那么把返回的数据写到本地数据库中
                    /*CacheBean cacheBean = new CacheBean();
                    cacheBean.cacheKey = url;
                    cacheBean.cacheJsonStr = result;
                    cacheBean.save();
                    LogUtils.e(TAG, "保存缓存成功");*/
                    CacheService.addCache(cacheUrl,result);
                }
            }
        });
    }

    @Override
    public void get(Map<String, Object> params, String url, final HttpCallBack httpCallBack, final boolean isCache) {
        // 获取泛型的类型
        final Class<T> typeClass = HttpUtils.getTypeClass(httpCallBack);
        // 变成可操作的对象
        final Gson gson = new Gson();

        // 判断一下是否要读取缓存
        if (isCache) {
            String cacheJsonStr = CacheService.queryCache(url);
            if (!TextUtils.isEmpty(cacheJsonStr)) {
                final T resultObj = gson.fromJson(cacheJsonStr, typeClass);
                httpCallBack.onSuccess(resultObj);
            }
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        // 拼接参数   http://v2.ffu365.com/index.php?m=Api&c=Team&a=teamList&appid=1
        // 添加的参数肯定是从  params 里面解析  遍历Map集合
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();


        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
            /*Object key = entry.getKey();
            Object val = entry.getValue();*/
            url += "&" + entry.getKey() + "=" + entry.getValue();
        }

        // 方便给内部类用
        final String cacheUrl = HttpUtils.appendParams(url, params);

        if (cacheUrl != null) {

            LogUtils.e("OkHttpEngine 请求接口： " + cacheUrl);

            Request.Builder requestBuilder = new Request.Builder().url(url);
            //可以省略，默认是GET请求
            requestBuilder.method("GET", null);

            Request request = requestBuilder.build();
            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    httpCallBack.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = new String(response.body().bytes());

                    Log.e("OkHttpEngine", result);

                    // 获取泛型的类型  封装  过度封装  该分开的分开
                    // 利用Gson转换类型
                    try{
                        final T resultObj = gson.fromJson(result, typeClass);
                        HttpUtils.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 把它弄到主线程中
                                httpCallBack.onSuccess(resultObj);
                            }
                        });
                        // 判断是不是需要缓存数据
                        if (isCache) {
                            // 如果需要  那么把返回的数据写到本地数据库中
                            CacheService.addCache(cacheUrl, result);
                        }
                    }catch (Exception e){
                        Log.i(TAG, "onResponse: e =" + e);
                        httpCallBack.onError(e);
                    }



                }
            });
        }
    }
}
