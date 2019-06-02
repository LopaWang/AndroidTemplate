package com.example.hui.androidtemplate;

import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.hui.androidtemplate.fragment.FindFragment;
import com.example.hui.androidtemplate.fragment.HomeFragment;
import com.example.hui.androidtemplate.fragment.MessageFragment;
import com.example.hui.androidtemplate.fragment.NewFragment;
import com.hc.framework.ioc.view.OnClick;
import com.hc.framework.mode.BaseActivity;
import com.hc.framework.navigationbar.DefaultNavigationBar;

public class MainActivity extends BaseActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private NewFragment mNewFragment;
    private MessageFragment mMessageFragment;

    private FragmentManagerHelper mFragmentHelper;

    @Override
    protected void initTitle() {
        initSystemBar(R.color.title_bar_bg_day);

        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this,
                getRootView()).setTitle("首页").create();
    }

    private ViewGroup getRootView() {
        return (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    protected void initData() {
        mFragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentHelper.add(mHomeFragment);
        /*// 加载第一个Fragment  兼容11以下的版本
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事物
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 把第一个Fragment HomeFragment加载进来
        mHomeFragment = new HomeFragment();
        // 第一个参数是Fragment的容器id，需要添加的Fragment
        fragmentTransaction.add(R.id.main_tab_fl, mHomeFragment);
        // 一定要commit
        fragmentTransaction.commit();*/
    }


    @Override
    protected void initView() {
        RadioButton homeRb = (RadioButton) findViewById(R.id.home_rb);
        homeRb.setChecked(true);
    }

    @Override
    protected void setRootView() {
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.home_rb)
    private void homeRbClick() {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragmentHelper.switchFragment(mHomeFragment);
    }

    @OnClick(R.id.find_rb)
    private void findRbClick() {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        mFragmentHelper.switchFragment(mFindFragment);
    }

    @OnClick(R.id.new_rb)
    private void newRbClick() {
        if (mNewFragment == null) {
            mNewFragment = new NewFragment();
        }
        mFragmentHelper.switchFragment(mNewFragment);
    }

    @OnClick(R.id.message_rb)
    private void messageRbClick() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        mFragmentHelper.switchFragment(mMessageFragment);
    }
}
