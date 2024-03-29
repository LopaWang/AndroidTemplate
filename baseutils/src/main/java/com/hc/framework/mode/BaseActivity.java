package com.hc.framework.mode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.hui.baseutils.R;
import com.hc.framework.engine.mode.BaseResult;
import com.hc.framework.ioc.util.ViewUtils;
import com.hc.framework.ioc.view.ShowLoadView;
import com.hc.framework.utils.ScreenUtils;
import com.hc.framework.view.LoadView;

import java.lang.reflect.Method;

/**
 * Created by hui on 2016/10/9.
 */
public abstract class BaseActivity extends AppCompatActivity{
    /** 正在加载数据的页面 */
    private View mLoadingView;
    /** WindowManager下的LayoutParams实例 */
    private WindowManager.LayoutParams mlodingParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1.设置布局
        setRootView();

        // 注解的逻辑代码，功能实现代码  公用的东西   改变状态栏颜色  捕捉异常
        ViewUtils.inject(this);

        initTitle();

        // 公用的加载动画   判断你 initData上面有没有加ShowLoadView的注解
        try {
            Method method = this.getClass().getDeclaredMethod("initData");
            ShowLoadView showLoadView = method.getAnnotation(ShowLoadView.class);
            // 代表你有加载数据动画的注解
            if(showLoadView != null){
                addLoadView();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 2.初始化界面
        initView();

        // 3.初始化数据
        initData();
    }

    /**
     * TODO initialization system status bar , but build version need greater 19
     */
    protected void initSystemBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(this, true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);

        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(color);
    }

    /**
     * TODO 设置系统顶部栏和程序主题颜色统一
     * @param activity 当前活动Activity实例
     * @param on
     * void
     */
    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 利用代码的方式加载动画布局
     */
    protected void addLoadView(){
        // 新建加载动画对象
        final WindowManager mWM = getWindowManager();
        mlodingParams = new WindowManager.LayoutParams();
        final int statusBarHeight = ScreenUtils.getStatusHeight(this);
        // 获取头部的高度
        final int titleBarHeight = (int) getResources().getDimension(
                R.dimen.title_height);
        // height = 屏幕的高度 - 状态栏的高度 - 头部的高度
        mlodingParams.height =  ScreenUtils.getScreenHeight(this)
                - statusBarHeight - titleBarHeight;
        mlodingParams.width = WindowManager.LayoutParams.MATCH_PARENT;


        //  控制能够响应其他布局
        mlodingParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 效果为背景透明
        mlodingParams.format = PixelFormat.RGBA_8888;
        // 在底部显示
        mlodingParams.gravity = Gravity.BOTTOM;
        // 床架加载动画布局
        mLoadingView = new LoadView(this);
        mWM.addView(mLoadingView, mlodingParams);
    }

    /**
     * TODO 如果有缓存或是数据获取成功了，就把正在加载数据的界面干掉
     */
    protected final void dismissLoadView() {
        if (mLoadingView == null)
            return;
        // mLoadingView.setVisibility(View.GONE);
        final WindowManager mWM = getWindowManager();
        // 移除LoadView
        mWM.removeView(mLoadingView);
        this.mLoadingView = null;
        this.mlodingParams = null;
    }

    protected abstract void initTitle();

    // 3.初始化数据
    protected abstract void initData();

    // 2.初始化界面
    protected abstract void initView();

    // 1.设置布局
    protected abstract void setRootView();


    /**
     * 启动一个Activity
     */
    protected void startActivity(Class<? extends Activity> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
        // 启动动画
    }

    /**
     * 获取屏幕的宽高  Utils
     * 常用的启动Activity
     * 为了内存的管理 不要把不常用的方法写到这里面 abstract  --> 对象
     */

    /**
     * 判断后台返回的数据是否成功
     */
    protected  boolean isNetRequestOk(BaseResult result){
        if(result.reason.equals("成功的返回")){
            // 成功
            dismissLoadView();
        }

        return result.reason.equals("成功的返回");
    }
}
