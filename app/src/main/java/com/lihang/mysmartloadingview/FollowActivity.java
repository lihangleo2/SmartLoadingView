package com.lihang.mysmartloadingview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lihang.smartloadview.SmartLoadingView;

/**
 * Created by leo
 * on 2019/5/27.
 */
public class FollowActivity extends AppCompatActivity {
    private SmartLoadingView animButton;
    private SmartLoadingView animButtonTwo;

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
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        animButton = findViewById(R.id.animButton);
        animButtonTwo = findViewById(R.id.animButtonTwo);
        animButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!animButton.isAnimRuning()) {

                    if (!animButton.isCanRest()) {
                        animButton.start();
                        //这里是模拟联网情况
                        mhandler.sendEmptyMessageDelayed(11, 1500);
                    } else {
                        animButton.reset();
                    }

                }
            }
        });


        animButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!animButtonTwo.isAnimRuning()) {

                    if (!animButtonTwo.isCanRest()) {
                        animButtonTwo.start();
                        //这里是模拟联网情况
                        mhandler.sendEmptyMessageDelayed(12, 1500);
                    } else {
                        animButtonTwo.reset();
                    }

                }

            }
        });

    }
}
