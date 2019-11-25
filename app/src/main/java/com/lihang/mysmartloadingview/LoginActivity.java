package com.lihang.mysmartloadingview;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.lihang.mysmartloadingview.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by leo
 * on 2019/5/27.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    //是否联网成功
    private boolean isNormalNet = true;
    //沉浸式状态栏
    protected ImmersionBar mImmersionBar;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setOnclickListener(this);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        binding.relativeNormal.setSelected(true);
        binding.editPhone.addTextChangedListener(this);
        binding.editPassWord.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_normal:
                switchSelect();
                break;
            case R.id.relative_error:
                switchSelect();
                break;
            case R.id.smartLoadingView_login:
                if (isNormalNet) {
                    binding.smartLoadingViewLogin.start();
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                        binding.smartLoadingViewLogin.onSuccess(LoginActivity.this, SecondActivity.class);
                    });
                } else {
                    binding.smartLoadingViewLogin.start();
                    Observable.timer(2000, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                        binding.smartLoadingViewLogin.netFaile();
                    });
                }
                break;
        }
    }

    public void switchSelect() {
        binding.relativeNormal.setSelected(!binding.relativeNormal.isSelected());
        binding.relativeError.setSelected(!binding.relativeError.isSelected());
        isNormalNet = binding.relativeNormal.isSelected();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击edittxt外，关闭软键盘
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardUtils.isShouldHideInput(v, ev)) {
                KeyBoardUtils.closeKeybord(binding.editPhone, LoginActivity.this);
                binding.editPhone.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(binding.editPhone.getText().toString().trim()) && !TextUtils.isEmpty(binding.editPassWord.getText().toString().trim())) {
            binding.smartLoadingViewLogin.setSmartClickable(true);
        } else {
            binding.smartLoadingViewLogin.setSmartClickable(false);
        }
    }
}
