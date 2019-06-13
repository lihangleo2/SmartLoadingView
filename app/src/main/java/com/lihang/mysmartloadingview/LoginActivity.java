package com.lihang.mysmartloadingview;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.smartloadview.SmartLoadingView;

/**
 * Created by leo
 * on 2019/5/27.
 */
public class LoginActivity extends AppCompatActivity   implements View.OnClickListener {
    private EditText edit_phone;
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    private SmartLoadingView animButton;
    private RelativeLayout relative_normal;
    private RelativeLayout relative_error;
    private boolean  isNormalNet = true;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    animButton.loginSuccess(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startActivity(new Intent(LoginActivity.this, SecondActivity.class));
                        finish();
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
                    animButton.netFaile("登录失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        animButton = findViewById(R.id.animButton);
        edit_phone = findViewById(R.id.edit_phone);
        relative_normal = findViewById(R.id.relative_normal);
        relative_normal.setOnClickListener(this);
        relative_error = findViewById(R.id.relative_error);
        relative_error.setOnClickListener(this);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        animButton.setLoginClickListener(new SmartLoadingView.LoginClickListener() {
            @Override
            public void click() {
                //按钮点击后去进行联网操作
                //这里模拟联网操作
                if (isNormalNet){
                    mhandler.sendEmptyMessageDelayed(11, 2000);
                }else {
                    mhandler.sendEmptyMessageDelayed(12, 2000);
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击edittxt外，关闭软键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardUtils.isShouldHideInput(v, ev)) {
                KeyBoardUtils.closeKeybord(edit_phone, LoginActivity.this);
                edit_phone.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relative_normal:
                isNormalNet = true;
                relative_normal.setSelected(true);
                relative_error.setSelected(false);
                break;
            case R.id.relative_error:
                isNormalNet = false;
                relative_normal.setSelected(false);
                relative_error.setSelected(true);
                break;
        }
    }
}
