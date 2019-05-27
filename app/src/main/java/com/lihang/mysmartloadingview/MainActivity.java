package com.lihang.mysmartloadingview;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lihang.smartloadview.SmartLoadingView;

public class MainActivity extends AppCompatActivity {
    private SmartLoadingView animButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animButton = findViewById(R.id.animButton);



        findViewById(R.id.button_canClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animButton.reset();
            }
        });

        findViewById(R.id.button_cannotClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animButton.cannotClick();
            }
        });

        findViewById(R.id.button_netError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animButton.netFaile();
            }
        });

        findViewById(R.id.button_successOther).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这是打勾动画
                animButton.loginSuccess(new SmartLoadingView.AnimationOKListener() {
                    @Override
                    public void animationOKFinish() {
                        Toast.makeText(MainActivity.this, "这是打勾结束的了", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        findViewById(R.id.button_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //这是扩散动画
                animButton.loginSuccess(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {


                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
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


            }
        });


        animButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animButton.start();
            }
        });


    }
}
