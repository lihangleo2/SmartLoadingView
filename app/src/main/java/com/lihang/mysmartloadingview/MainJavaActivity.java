package com.lihang.mysmartloadingview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lihang.SmartLoadingView;
import com.lihang.mysmartloadingview.databinding.ActivityMainBinding;

/**
 * @Author leo
 * @Address https://github.com/lihangleo2
 * @Date 2024/7/22
 */
public class MainJavaActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ToastUtils.setGravity(Gravity.CENTER,0,0);
        getSupportActionBar().setTitle("SmartLoadingView的使用");
        /**
         * 1.smart_full_screen:全屏
         * 不支持关注模式
         * */

        //finishLoadingWithFullScreen 的使用
        mBinding.smartFullscreenAuto.setOnClickListener(it -> {
            //1.1、开始加载
            mBinding.smartFullscreenAuto.startLoading();

            //1.2、模拟2s后，加载成功并跳转（finishLoadingWithFullScreen会自动跳转页面并关闭当前页面）
            it.postDelayed(() -> {
                mBinding.smartFullscreenAuto.finishLoadingWithFullScreen(MainJavaActivity.this, SecondActivity.class);
                //也可以自己处理动画回调
                //mBinding.smartFullscreenAuto.finishLoading(true, success -> {});
            }, 2000);

        });

        //模拟失败
        mBinding.smartFullscreenFail.setOnClickListener(it -> {
            mBinding.smartFullscreenFail.startLoading();
            it.postDelayed(() -> {
                mBinding.smartFullscreenFail.finishLoading(false);
                ToastUtils.showShort("加载失败");
            }, 2000);
        });

        //模拟失败，并展示失败文案
        mBinding.smartFullscreenFailtxt.setOnClickListener(it -> {
            mBinding.smartFullscreenFailtxt.startLoading();
            it.postDelayed(() -> {
                //mBinding.smartFullscreenFailtxt.setAnimaledText("我是自定义错误文案");
                mBinding.smartFullscreenFailtxt.finishLoading(false);
            }, 2000);
        });


        /**
         * 2.smart_button
         * 支持关注模式
         * */
        //正常情况下的："点击关注" 和 "取消关注"
        mBinding.smartButtonSuccess.setOnClickListener(it->{
            if (!mBinding.smartButtonSuccess.isFinished()){
                mBinding.smartButtonSuccess.startLoading();
                it.postDelayed(()->{
                    //2.1、加载成功--带loading动画的加载成功
                    mBinding.smartButtonSuccess.finishLoading(true,success -> {
                        ToastUtils.showShort("关注成功");
                    });
                },2000);
            }else {
                //2.2、再次点击，取消关注。通过此方法设置不带动画
                mBinding.smartButtonSuccess.setFinished(false);
            }
        });

        //模拟关注失败
        mBinding.smartButtonFail.setOnClickListener(it->{
            mBinding.smartButtonFail.startLoading();
            it.postDelayed(()->{
                mBinding.smartButtonFail.finishLoading(false,success -> {
                    ToastUtils.showShort("关注失败，请稍后再试~");
                });
            },2000);
        });


        //不带动画的 ”关注“ 和 ”取消关注“
        mBinding.smartButtonNoanimal.setOnClickListener(it->{
            if (!mBinding.smartButtonNoanimal.isFinished()){
                mBinding.smartButtonNoanimal.setFinished(true);
            }else {
                mBinding.smartButtonNoanimal.setFinished(false);
            }
        });


        /**
         * 3.smart_tick
         * 支持关注模式
         * */
        //这里的用法和 smart_button 类似
        mBinding.smartTickDemo.setOnClickListener(it->{
            if (!mBinding.smartTickDemo.isFinished()){
                mBinding.smartTickDemo.startLoading();
                it.postDelayed(()->{
                    mBinding.smartTickDemo.finishLoading(true,success -> {
                        ToastUtils.showShort("关注成功");
                    });
                },2000);
            }else {
                mBinding.smartTickDemo.setFinished(false);
            }
        });


        /**
         * 4.smart_tick_hide
         * 支持关注模式
         * */
        //这里的用法和 smart_button 类似
        mBinding.smartTickHideDemo.setOnClickListener(it->{
            if (!mBinding.smartTickHideDemo.isFinished()){
                mBinding.smartTickHideDemo.startLoading();
                it.postDelayed(()->{
                    mBinding.smartTickHideDemo.finishLoading(true,success -> {
                        ToastUtils.showShort("关注成功");
                    });
                },2000);
            }else {
                mBinding.smartTickHideDemo.setFinished(false);
            }
        });


        /**
         * 5.smart_tick_center_hide
         * 支持关注模式
         * */
        //这里的用法和 smart_button 类似
        mBinding.smartTickCenterHideDemo.setOnClickListener(it->{
            if (!mBinding.smartTickCenterHideDemo.isFinished()){
                mBinding.smartTickCenterHideDemo.startLoading();
                it.postDelayed(()->{
                    mBinding.smartTickCenterHideDemo.finishLoading(true,success -> {
                        ToastUtils.showShort("关注成功");
                    });
                },2000);
            }else {
                mBinding.smartTickCenterHideDemo.setFinished(false);
            }
        });

    }
}
