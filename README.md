## SmartLoadingView多效果按钮，先展示效果
[我的博客,关于smartLoadingView的讲解](https://blog.csdn.net/leol_2/article/details/92564600)  

|登录效果展示|关注效果展示|非圆角按钮|
|:---:|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/login_normal.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/otherfollow.gif)

## 添加依赖

 - 项目build.gradle添加如下
   ```java
   allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
   ```
 - app build.gradle添加如下
    ```java
   dependencies {
	        implementation 'com.github.lihangleo2:SmartLoadingView:1.3.2'
	}
   ```
   
## 使用
```xml
      <com.lihang.smartloadview.SmartLoadingView
            android:id="@+id/smartLoadingView"
            android:layout_width="120dp"
            android:layout_height="30dp"
            android:layout_gravity="center_horizontal"
            android:layout_marginTop="20dp"
            app:cornerRaius="30dp"
            app:normalBg="#08f2c7"
            app:textColor="#fff"
            app:textSize="14dp"
            app:textStr="登录"
            app:cannotclickBg="#bcb6b6"
            app:errorBg="#f57676"
            app:errorStr="错误信息"
            app:scrollSpeed="500"
            />
```
 ## 1.1 登录效果页
```java
  //通过设置监听LoginClickListener即启动 联网动画
   smartLoadingView.setLoginClickListener(new SmartLoadingView.LoginClickListener() {
            @Override
            public void click() {
                //按钮点击后去进行联网操作
                //这里模拟联网操作
                    mhandler.sendEmptyMessageDelayed(11, 2000);          
            }
        });
```

 ## 1.2 联网出结果 
```java

        private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                //联网成功：通过设置监听AnimatorListener即是启动 小圆扩散全屏动画。在此动画全部完成后拿到回调onAnimtionEnd
                    smartLoadingView.loginSuccess(new Animator.AnimatorListener() {
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
                //联网失败，可能是超时，也有可能是其他原因
                //如果你在xml设置了errorMsg那么这里也可以直接调用animButton.netFaile();
                //如果要改变文案则用以下方法，同时我还处理了，文案内容超过设置控件长度时，文案来回滚动
                    smartLoadingView.netFaile("登录失败");
                    break;
            }
        }
    };
```

 ## 2.1 关注效果--打勾
```java
//直接设置监听 FollowClickListener,即开启联网操作
smartLoadingView.setFollowClickListener(new SmartLoadingView.FollowClickListener() {
            @Override
            public void followClick() {
                //按钮点击后去进行联网操作
                //这里是模拟联网情况
                mhandler.sendEmptyMessageDelayed(11, 2000);
            }
        });
```


 ## 2.2 关注出结果 
```java

private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    //直接设置监听 AnimationOkListener,即刻开启关注成功的打勾效果
                    smartLoadingView.loginSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(FollowActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;

                case 12:
                    //直接通过错误信息的方式也可实现
                    smartLoadingView.netFaile("关注成功");
                    break;
            }
        }
    };
```

 # Api说明
 ## ① 圆角属性
  - app:cornerRaius="30dp"  按钮圆角大小
  
 ## ② 正常背景颜色值
  - app:normalBg="#08f2c7"  按钮正常背景颜色值
  
 ## ③ 按钮字体
 - app:textColor="#fff"  字体颜色
 - app:textSize="14dp"   字体大小
 - app:textStr="登录"    字体文案

 ## ④ 按钮不可点击状态下的背景颜色值（设置不可点击：smartLoadingView.cannotClick();）
 - app:cannotclickBg="#bcb6b6"    按钮不可点击状态背景颜色值
 
 ## ⑤ 按钮联网错误状态
 - app:errorBg="#f57676"    错误背景颜色值
 - app:errorStr="错误信息"   错误文案

 ## ⑥ 按钮文案过程的文字滚动速度
 - app:scrollSpeed="500"    文字滚动速度,每个文字滚动屏幕外所需的时间
 
 ## ⑦  想恢复所有初始值通过：
 - smartLoadingView.reset();    
 

