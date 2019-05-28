package com.lihang.mysmartloadingview;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.smartloadview.SmartLoadingView;

//startActivity(new Intent(MainActivity.this, SecondActivity.class));
//        finish();
//        overridePendingTransition(R.anim.scale_test_home, R.anim.scale_test2);
public class MainActivity extends AppCompatActivity {
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        findViewById(R.id.text_style_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FollowActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }
}
