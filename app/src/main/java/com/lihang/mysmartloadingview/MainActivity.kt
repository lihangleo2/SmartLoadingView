package com.lihang.mysmartloadingview

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.gyf.barlibrary.ImmersionBar
import com.lihang.SmartLoadingView.LoadingListener
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

        //test button click -- start
        mBinding.smartLoadingView.setOnClickListener {
            mBinding.smartLoadingView.startLoading()
        }

        //test button result -- end
        mBinding.txtButton.setOnClickListener {
            mBinding.smartLoadingView.finishLoading(true,object :LoadingListener{
                override fun loadingFinish(success: Boolean) {
                    Toast.makeText(this@MainActivity,"此结果 == $success",Toast.LENGTH_SHORT).show()
                }
            })
        }

        //check follow status
        mBinding.txtButtonFollow.setOnClickListener {
            Toast.makeText(this@MainActivity,"关注结果 == ${mBinding.smartLoadingView.isFinished}",Toast.LENGTH_SHORT).show()
        }

        //test follow true
        mBinding.txtFollowTrue.setOnClickListener {
            mBinding.smartLoadingView.isFinished = true
        }

        //test follow false
        mBinding.txtFollowFalse.setOnClickListener {
            mBinding.smartLoadingView.isFinished = false
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