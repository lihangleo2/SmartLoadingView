package com.lihang.mysmartloadingview

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.lihang.SmartLoadingView.AnimationFullScreenListener
import com.lihang.mysmartloadingview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //沉浸式状态栏
    private var mImmersionBar: ImmersionBar? = null
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.onClickListener = this
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar!!.init()

        mBinding.smartLoadingView.setOnClickListener {
//            mBinding.smartLoadingView.isSelected = true
//            mBinding.smartLoadingView.startLoading()
//            mBinding.smartLoadingView.onSuccess(object :AnimationFullScreenListener{
//                override fun animationFullScreenFinish() {
//                    Log.e("测试当前动画结束","finish")
//                }
//            })
            mBinding.smartLoadingView.isFollow = true
        }
    }

    override fun onClick(v: View) {}
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionBar!!.destroy()
    }
}