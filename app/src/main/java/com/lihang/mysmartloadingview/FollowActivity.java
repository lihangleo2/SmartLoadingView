package com.lihang.mysmartloadingview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.smartloadview.SmartLoadingView;

/**
 * Created by leo
 * on 2019/5/27.
 */
public class FollowActivity extends AppCompatActivity {
    private SmartLoadingView animButton;
    private SmartLoadingView animButtonTwo;
    private SmartLoadingView animButtonThree;
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    //只有在动画还在runingd才能走(防止重复点击按钮操作)
                    animButton.loginSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(FollowActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case 12:
                    animButtonTwo.netFaile("关注成功");
                    break;

                case 13:
                    animButtonThree.netFaile("关注成功");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        animButton = findViewById(R.id.animButton);
        animButtonTwo = findViewById(R.id.animButtonTwo);
        animButtonThree = findViewById(R.id.animButtonThree);

        animButton.setFollowClickListener(new SmartLoadingView.FollowClickListener() {
            @Override
            public void followClick() {
                //按钮点击后去进行联网操作
                //这里是模拟联网情况
                mhandler.sendEmptyMessageDelayed(11, 2000);
            }
        });

        animButtonTwo.setFollowClickListener(new SmartLoadingView.FollowClickListener() {
            @Override
            public void followClick() {
                //按钮点击后去进行联网操作
                //这里是模拟联网情况
                mhandler.sendEmptyMessageDelayed(12, 2000);
            }
        });


        animButtonThree.setFollowClickListener(new SmartLoadingView.FollowClickListener() {
            @Override
            public void followClick() {
                //按钮点击后去进行联网操作
                //这里是模拟联网情况
                mhandler.sendEmptyMessageDelayed(13, 2000);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
