package com.lihang.mysmartloadingview;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.lihang.smartloadview.SmartLoadingView;


public class MainActivity extends AppCompatActivity implements SmartLoadingView.LoginClickListener {
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    private SmartLoadingView text_style_one;
    private SmartLoadingView text_style_two;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    text_style_one.loginSuccess(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.scale_test_home, R.anim.scale_test2);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    break;

                case 12:

                    text_style_two.loginSuccess(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startActivity(new Intent(MainActivity.this, FollowActivity.class));
                            overridePendingTransition(R.anim.scale_test_home, R.anim.scale_test2);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_style_one = findViewById(R.id.text_style_one);
        text_style_one.setLoginClickListener(this);
        text_style_two = findViewById(R.id.text_style_two);
        text_style_two.setLoginClickListener(new SmartLoadingView.LoginClickListener() {
            @Override
            public void click() {
                mHandler.sendEmptyMessage(12);
            }
        });
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //注意这里不需要关闭上一步的activity需要做如此操作
        text_style_one.reset();
        text_style_two.reset();
    }

    @Override
    public void click() {
        mHandler.sendEmptyMessage(11);
    }
}
