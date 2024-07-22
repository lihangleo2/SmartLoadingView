package com.lihang.mysmartloadingview

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.lihang.SmartLoadingView
import com.lihang.SmartLoadingView.LoadingListener
import com.lihang.mysmartloadingview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.title = "SmartLoadingView的使用"
        ToastUtils.setGravity(Gravity.CENTER,0,0)
        //
        mBinding.run {
            /**
             * 1.smart_full_screen:全屏
             * 不支持关注模式
             * */

            //finishLoadingWithFullScreen 的使用
            smartFullscreenAuto.setOnClickListener {
                //1.1、开始加载
                smartFullscreenAuto.startLoading()

                //1.2、模拟2s后，加载成功并跳转（finishLoadingWithFullScreen会自动跳转页面并关闭当前页面）
                it.postDelayed({
                    smartFullscreenAuto.finishLoadingWithFullScreen(this@MainActivity, SecondActivity::class.java)
                    //也可以自己处理动画回调
                    //smartFullscreenAuto.finishLoading(true) {}
                }, 2000)
            }

            //模拟失败
            smartFullscreenFail.setOnClickListener {
                smartFullscreenFail.startLoading()
                it.postDelayed({
                    smartFullscreenFail.finishLoading(false) {
                        ToastUtils.showShort("加载失败")
                    }
                }, 2000)
            }


            //模拟失败，并展示失败文案
            smartFullscreenFailtxt.setOnClickListener {
                smartFullscreenFailtxt.startLoading()
                it.postDelayed({
                    //smartFullscreenFailtxt.setAnimaledText("我是自定义错误文案")
                    smartFullscreenFailtxt.finishLoading(false)
                }, 2000)
            }


            /**
             * 2.smart_button
             * 支持关注模式
             * */
            //正常情况下的："点击关注" 和 "取消关注"
            smartButtonSuccess.setOnClickListener {
                if (!smartButtonSuccess.isFinished) {
                    smartButtonSuccess.startLoading()
                    it.postDelayed({
                        //2.1、加载成功--带loading动画的加载成功
                        smartButtonSuccess.finishLoading(true) {
                            ToastUtils.showShort("关注成功")
                        }
                    }, 2000)
                } else {
                    //2.2、再次点击，取消关注。通过此方法设置不带动画
                    smartButtonSuccess.isFinished = false
                }
            }

            //模拟关注失败
            smartButtonFail.setOnClickListener {
                smartButtonFail.startLoading()
                it.postDelayed({
                    smartButtonFail.finishLoading(false) {
                        ToastUtils.showShort("关注失败，请稍后再试~")
                    }
                }, 2000)
            }


            /**
             * 3.smart_tick
             * 支持关注模式
             * */
            //这里的用法和 smart_button 类似
            smartTickDemo.setOnClickListener {
                if (!smartTickDemo.isFinished) {
                    smartTickDemo.startLoading()
                    it.postDelayed({
                        //2.1、加载成功--带loading动画的加载成功
                        smartTickDemo.finishLoading(true) {
                            ToastUtils.showShort("关注成功")
                        }
                    }, 2000)
                } else {
                    //2.2、再次点击，取消关注。通过此方法设置不带动画
                    smartTickDemo.isFinished = false
                }
            }


            /**
             * 4.smart_tick_hide
             * 支持关注模式
             * */
            //这里的用法和 smart_button 类似
            smartTickHideDemo.setOnClickListener {
                if (!smartTickHideDemo.isFinished) {
                    smartTickHideDemo.startLoading()
                    it.postDelayed({
                        //2.1、加载成功--带loading动画的加载成功
                        smartTickHideDemo.finishLoading(true) {
                            ToastUtils.showShort("关注成功")
                        }
                    }, 2000)
                } else {
                    //2.2、再次点击，取消关注。通过此方法设置不带动画
                    smartTickHideDemo.isFinished = false
                }
            }


            /**
             * 5.smart_tick_center_hide
             * 支持关注模式
             * */
            //这里的用法和 smart_button 类似
            smartTickCenterHideDemo.setOnClickListener {
                if (!smartTickCenterHideDemo.isFinished) {
                    smartTickCenterHideDemo.startLoading()
                    it.postDelayed({
                        //2.1、加载成功--带loading动画的加载成功
                        smartTickCenterHideDemo.finishLoading(true) {
                            ToastUtils.showShort("关注成功")
                        }
                    }, 2000)
                } else {
                    //2.2、再次点击，取消关注。通过此方法设置不带动画
                    smartTickCenterHideDemo.isFinished = false
                }
            }

        }
    }
}