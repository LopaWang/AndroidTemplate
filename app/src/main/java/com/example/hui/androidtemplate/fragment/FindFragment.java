package com.example.hui.androidtemplate.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hui.androidtemplate.R;
import com.example.hui.androidtemplate.adapter.NewslListAdapter;
import com.example.hui.androidtemplate.banner.BannerAdapter;
import com.example.hui.androidtemplate.banner.BannerView;
import com.example.hui.androidtemplate.banner.BannerViewPager;
import com.example.hui.androidtemplate.mode.NewsListResult;
import com.hc.framework.engine.util.HttpCallBack;
import com.hc.framework.engine.util.HttpUtils;
import com.hc.framework.ioc.view.ViewById;
import com.hc.framework.mode.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/3.
 */
public class FindFragment extends BaseFragment implements BannerViewPager.BannerItemClickListener {

    private static final String TAG = "MainActivity";

    @ViewById(R.id.list_view)
    private ListView mListView;

    @ViewById(R.id.root_view)
    private ViewGroup mRootView;

    @ViewById(R.id.banner_view)
    private BannerView mBannerView;

    List<String> bannerList;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        HttpUtils httpUtils = new HttpUtils();
        // 数据访问的引擎 用得第三方

        Map<String, Object> params = new HashMap<>();
        params.put("iid", 6152551759L);
        params.put("channel", 360);
        params.put("aid", 7);

        httpUtils.get(params, "http://v.juhe.cn/toutiao/index?type=top", new HttpCallBack<NewsListResult>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(final NewsListResult result) {
                if (isNetRequestOk(result)) {
//                    mListView.setAdapter(new ChannelListAdapter(context,
//                            result.getData().getCategories().getCategory_list()));
                    Log.i(TAG, "onSuccess: result = " + result.reason );
                    mListView.setAdapter(new NewslListAdapter(context,
                            result.getResult().getData()));

                    // 获取到接口返回数据 初始化广告位
//                    initBanner(result.getData().getRotate_banner().getBanners());
                    bannerList = new ArrayList<>();
                    for (int i = 0; i <5; i ++){
                        bannerList.add(result.getResult().getData().get(i).getThumbnail_pic_s());
                    }
                    initBanner(bannerList);
                }
            }
        });
    }

    /**
     * 初始化广告位
     *
     * @param banners
     */
    private void initBanner(final List<String> banners) {

        mBannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView bannerIv = null;
                if (convertView == null) {
                    bannerIv = new ImageView(context);
                    bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    bannerIv = (ImageView) convertView;
                    Log.e(TAG, "界面复用" + convertView);
                }
                // 利用第三方的工具加载图片  Glide
                String imagePath = banners.get(position);
                Glide.with(context).load(imagePath).
                        // 加载默认图片
                                placeholder(R.drawable.banner_default).into(bannerIv);
                return bannerIv;
            }

            @Override
            public int getCount() {
                return banners.size();
            }


        });

        // 开启滚动
        mBannerView.startRoll();

        // 设置条目点击监听
        mBannerView.setOnBannerItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void click(int position) {
        Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
    }
}
