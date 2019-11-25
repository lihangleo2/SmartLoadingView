package com.lihang.mysmartloadingview;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.mysmartloadingview.databinding.ActivityMainBinding;
import com.lihang.smartloadview.SmartLoadingView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    ActivityMainBinding binding;
    private int follow4Tag = 0;
    private int follow5Tag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setOnClickListener(this);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smartLoadingView_1_:
                binding.smartLoadingView1.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingView1.onSuccess(MainActivity.this, SecondActivity.class);
                });
                break;

            case R.id.smartLoadingView_2_:
                binding.smartLoadingView2.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingView2.onSuccess(new SmartLoadingView.AnimationFullScreenListener() {
                        @Override
                        public void animationFullScreenFinish() {
                            Toast.makeText(MainActivity.this, "监听动画结束", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                break;

            case R.id.smartLoadingView_3_:
                binding.smartLoadingView3.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingView3.netFaile();
                });
                break;
            case R.id.smartLoadingView_4_:
                if (follow4Tag % 2 == 0) {
                    //这里是模拟关注
                    binding.smartLoadingView4.start();
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                        binding.smartLoadingView4.netFaile("关注成功");
                    });
                } else {
                    //这里是模拟取消关注
                    binding.smartLoadingView4.reset();
                }
                follow4Tag++;

                break;


            case R.id.smartLoadingView_5_:
                //不设置模式，那么默认为正常模式 SmartLoadingView.OKAnimationType.NORMAL
                if (follow5Tag % 2 == 0) {
                    binding.smartLoadingView5.start();
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                        binding.smartLoadingView5.onSuccess(new SmartLoadingView.AnimationOKListener() {
                            @Override
                            public void animationOKFinish() {
                                Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    binding.smartLoadingView5.reset();
                }
                follow5Tag++;
                break;


            case R.id.smartLoadingView_hide:
                binding.smartLoadingViewHide.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingViewHide.onSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    }, SmartLoadingView.OKAnimationType.HIDE);
                });
                break;

            case R.id.smartLoadingView_center:
                binding.smartLoadingViewCenter.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingViewCenter.onSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    }, SmartLoadingView.OKAnimationType.TRANSLATION_CENTER);
                });
                break;

            case R.id.smartLoadingView_6_:
                binding.smartLoadingView6.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    Toast.makeText(MainActivity.this, "关注失败，回到开始", Toast.LENGTH_SHORT).show();
                    binding.smartLoadingView6.backToStart();
                });
                break;

            case R.id.smartLoadingView_7_:
                binding.smartLoadingView7.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingView7.netFaile();
                });
                break;
            case R.id.smartLoadingView_8_:
                binding.smartLoadingView8.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    binding.smartLoadingView8.netFaile();
                });
                break;

            case R.id.smartLoadingView_9_:
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                binding.smartLoadingView9.setSmartClickable(false);
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }


}
