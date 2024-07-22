# SmartLoadingView - 一个自带dialog的联网请求按钮
[![](https://jitpack.io/v/lihangleo2/SmartLoadingView.svg)](https://jitpack.io/#lihangleo2/SmartLoadingView)
[![](https://img.shields.io/badge/license-MIT-green)](https://github.com/lihangleo2/SmartLoadingView/blob/master/LICENSE)


## 特点功能
任何view被包裹后即可享受阴影，以及系统shape,selector功能（清空项目drawable文件夹）。具体介绍如下：
```
    1. 支持在任何布局上使用
    2. smart_full_screen模式：全屏扩散跳转动画，让你的按钮和跳转页面更有活力
    3. smart_tick模式：仿抖音，关注按钮及动画
    4. smart_tick_hide模式：打勾动画及隐藏
    5. smart_tick_center_hide模式：打勾隐藏，及移动到屏幕中间提醒模式
    6. smart_button模式：正常关注和取消关注
```


## SmartLoadingView动态
* [ShadowLayout成长史](https://github.com/lihangleo2/SmartLoadingView/wiki)  


## Demo
为录制流畅，截图分辨率比较模糊。可在下方扫描二维码下载apk，查看真机效果。

![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/SmartLoadingView_.png)
<br/>

## 效果展示
为录制流畅，截图分辨率模糊。可下载apk查看真机效果
* ### smart_full_screen模式：全屏扩散动画（不支持关注）
|全屏扩散页面跳转|加载失败|失败文案在按钮上|
|:---:|:---:|:---:|
|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/fullScreen.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/fullScreen_fail_toast.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/fullScreen_fail_show.gif" alt="Sample"  width="100%">
<br/>

* ### smart_button模式：正常按钮动画（支持关注）
|关注并加载成功|关注失败|不带动画关注|
|:---:|:---:|:---:|
|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartButton.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartButton_fail.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartButton_noAnimal.gif" alt="Sample"  width="100%">
<br/>

* ### smart_tick模式：仿抖音打勾关注（支持关注）；smart_tick_hide模式：打勾隐藏（支持关注）；smart_tick_center_hide模式：打勾隐藏，并移至中间提醒（支持关注）。
|smart_tick|smart_tick_hide|smart_tick_center_hide|
|:---:|:---:|:---:|
|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartTick.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartTick_hide.gif" alt="Sample"  width="100%">|<img src="https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/smartTick_hide_center.gif" alt="Sample"  width="100%">
<br/>

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
	        implementation 'com.github.lihangleo2:SmartLoadingView:2.0.2'
	}
   ```

<br>

## 使用（下方有属性说明）

```xml
<com.lihang.SmartLoadingView
        android:id="@+id/smartLoadingView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="自带dialog按钮"
        android:textColor="#fff"
        android:textSize="15dp"
        app:background_normal="#fff"
        app:errorMsg="联网失败文案"
        app:background_error="#ED7676"
        app:background_cannotClick="#bbbbbb"
        app:cornerRaius="30dp"
        app:textScrollMode="marquee"
        app:smart_clickable="true"
        app:speed="400"
        />
```

<br>

# 效果展示（截图分辨率低，请扫描下文二维码体验效果）

## 一、开启动画和转场动画的使用

|1.1、动画结束后自动跳转|1.2、自己监听动画实现逻辑|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/loading_1.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/loading_2.gif)

<br>

### 1.1、动画结束后自动跳转
这里点击事件和普通的控件点击事件一致。设置setOnClickListener()即可。
```java
smartLoadingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smartLoadingView.start();
                Observable.timer(2000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(along -> {
                    smartLoadingView.onSuccess(MainActivity.this, SecondActivity.class);
                });
            }
        });
```

<br>

点击按钮，联网开始时，启动动画
```java
smartLoadingView.start();
```

<br>

这里我用了RxJava延迟了2s 模拟联网成功。你也可以用handler延迟实现这个功能,这里用了下lambda表达式，可以忽略，只要看下面代码。
```java
//这样既可实现，从一个页面转场动画跳转到另外一个页面（注意这样跳转，第一个页面会被finish掉）。
smartLoadingView.onSuccess(MainActivity.this, SecondActivity.class);
```

<br>

### 1.2、自己监听动画实现逻辑
前面点击事件和启动动画和 1.1 都一样，不同的时，联网成功的时候，增加动画结束的监听（这里逻辑都是自己处理，不会关闭和跳转任何页面）
```java
smartLoadingView.onSuccess(new SmartLoadingView.AnimationFullScreenListener() {
                        @Override
                        public void animationFullScreenFinish() {
                            Toast.makeText(MainActivity.this, "监听动画结束", Toast.LENGTH_SHORT).show();
                        }
                    });
```

<br>

## 二、联网请求失败的样式

|2.1、请求失败，文案显示在控件上|2.2、请求失败，回到初始状态|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/error_1.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/error_2.gif)

<br>

### 2.1、请求失败，文案显示在控件上
这里点击和启动动画都和上面一致。如果你xml里设置了失败文案的话，联网失败时，只需要调用
```java
smartLoadingView.netFaile();
```
当然，如果你再联网前并不知道失败文案也可以这样，带入当前失败的文案
```java
smartLoadingView.netFaile(msg);
```

<br>

### 2.2、请求失败，回到初始状态
如果你们的需求是，失败文案显示在别的地方，或者只是弹一个toast,按钮箱回到初始状态，只需要这样（这里的意思是，控件还在转圈等待，网络回调后，动画平滑回到初始状态）
```java
smartLoadingView.backToStart();
```

<br>

## 三、正常的联网请求（目前作者用于关注）

|3.1、正常的联网，正常出结果|3.2、正常联网，打勾出结果|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_1.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_2.gif)
|3.3、打勾出结果，打勾消失|3.4、打勾出结果，提醒用户|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_3.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_4.gif)

<br> 

```java
//2.0.2后把关注方式做成了属性，切记，如果你想用哪种方式，务必要在xml上写上
 <attr name="click_mode">
            <!-- 正常的样式 -->
            <enum name="normal" value="1" />
            <!-- 隐藏的样式 -->
            <enum name="hide" value="2" />
            <!-- 平移到中间的样式-->
            <enum name="translate_center" value="3" />
            <!-- 走失败模式的样式-->
            <enum name="like_faile" value="4" />

        </attr>
```


### 3.1、正常的联网，正常出结果 app:click_mode="like_faile"
这里点击事件和启动动画都和之前一样。正常出结果，只需要结合失败的方法去使用，失败文案，失败背景设置成关注成功的样式，调用只需要这样
```java
smartLoadingView.netFaile("关注成功");
```
再次点击后，回到初始状态。注意这里不能使用backToStart（）。因为backToStart（）是出结果时使用，即使你使用也不起效果，这里已经出了结果“关注成功”。所以此时，再次点击，需要如下
```java
smartLoadingView.reset();
```

<br>

### 3.2、正常联网，打勾出结果 app:click_mode="normal"
前面都是一样的，只是出结果时，实现AnimationOKListener接口
```java
smartLoadingView.onSuccess(new SmartLoadingView.AnimationOKListener() {
                            @Override
                            public void animationOKFinish() {
                                Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                            }
                        });
```

<br>

### 3.3、打勾出结果，打勾消失 app:click_mode="hide"
如果想实现抖音那样，打勾后，打勾消失，只需要实现，添加一个模式就好了,OKAnimationType.HIDE。（当然上面就是默认的OKAnimationType.NORMAL）
```java
smartLoadingView.onSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    });
```

<br>

### 3.4、打勾出结果，提醒用户 app:click_mode="translate_center"
这个就有点类似提醒效果，不管你的控件在屏幕上的任何位置，最终都会运行到屏幕中间，提醒用户，成功了。也只需添加一个模式OKAnimationType.TRANSLATION_CENTER
```java
smartLoadingView.onSuccess(new SmartLoadingView.AnimationOKListener() {
                        @Override
                        public void animationOKFinish() {
                            Toast.makeText(MainActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                        }
                    });
```
<br>

### 3.5、上个版本很多朋友说，没有默认选中的状态。在xml里写上你的click_mode后，只需一句代码
```java
binding.smartLoadingViewNormal.setFollow(true);
```
<br>

## 四、文字超出一行，文字自动滚动
设计这个的初衷是因为，可能按钮的错误文案太长了，按钮放不下时使用的

|4.1、文字来回滚动|4.2、仿跑马灯滚动|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/scroll.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/scroll.gif)

<br>

### 4.1、文字来回滚动
只需要在xml里加上 app:textScrollMode="normal"。或者可以不加，因为默认滚动就是这种方式

<br>

### 4.2、仿跑马灯滚动
xml里只需要加上 app:textScrollMode="marquee"

<br>

## 五、设置不可点击状态

|5.1、设置不可点击状态||
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/cannot_click.gif)|![]()

<br>

### 5.1、设置不可点击状态
在xml里可以通过app:smart_clickable="false"进行设置。当然也能通过代码来设置
```java
smartLoadingView.setSmartClickable(false);
```

<br>

## 六、这里作者还提供了2个小demo。登录demo和关注demo

|6.1、登录demo|6.2、关注demo|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/demo_login.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/demo_follow.gif)

可以下载demo自行查看


## 扫描二维体验效果(下载密码是：123456)
![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/SmartLoadingView_.png)

<br>

 # 自定义属性
 #### 1、按钮文案
 - android:text="自带dialog按钮"  使用了textView的text文字属性
  
 #### 2、按钮文案颜色值
 - android:textColor="#fff" 使用了textView的textColor颜色值属性
  
 #### 3、按钮文案字体大小
 - android:textSize="15dp" 使用了textView的字体大小
 
 #### 4、正常情况下的背景颜色值
 - app:background_normal="#fff" 按钮正常的背景颜色值

 #### 5、联网失败文案
 - app:errorMsg="联网失败文案" 联网失败展示的文案，比如登录时，账号密码错误
 
 #### 6、联网失败下的背景颜色值
 - app:background_error="#ED7676" 联网失败时展示的背景颜色值，一般为殷红色

 #### 7、不可点击状态下的背景颜色值
 - app:background_cannotClick="#bbbbbb" 不可点击状态下的背景颜色值
 
 #### 8、圆角属性
 - app:cornerRaius="30dp" 背景的圆角属性
 
 #### 9、文字滚动模式（文字超过一行时，文字自动滚动）
 - app:textScrollMode="marquee" 比如联网失败后，失败文案太长了。文字自动滚动，这里有2种方式。1、normal来回滚动。 2、marquee跑马灯效果
 
 #### 10、文字滚动速度
 - app:speed="400" 文字的滚动速度。可以理解为一个文字滚动出屏幕外需要的时间
 
 #### 11、按钮的点击状态
 - app:smart_clickable="true" 不设置时，默认可以点击，为true。代码里也能通过 smartLoadingView9.setSmartClickable(false) 进行设置
 
 #### 12、这里稍微说下长宽
 长宽都是用系统的layout_width和layout_height，包括设置padding。如果不设置，是有默认间距的
 
 #### 13、click_mode关注方式
 - 这里有四种模式，like_faile、normal、hide、translate_center；不管用哪种模式，xml一定要加上这个属性。like_faile：像联网失败的方式展示关注成功；normal：正常的关注打勾动画；hide：正常打勾关注后，按钮消失。translate_center：打勾关注后，移动到屏幕中心提醒。
	
 <br>
 
 ## 关于作者。
Android工作多年了，一直向往大厂。在前进的道路上是孤独的。如果你在学习的路上也感觉孤独，请和我一起。让我们在学习道路上少些孤独
* [关于我的经历](https://mp.weixin.qq.com/s?__biz=MzAwMDA3MzU2Mg==&mid=2247483667&idx=1&sn=1a575ea2c636980e5f4c579d3a73d8ab&chksm=9aefcb26ad98423041c61ad7cbad77f0534495d11fc0a302b9fdd3a3e6b84605cad61d192959&mpshare=1&scene=23&srcid=&sharer_sharetime=1572505105563&sharer_shareid=97effcbe7f9d69e6067a40da3e48344a#rd)
 * QQ群： 209010674 <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=5e29576e7d2ebf08fa37d8953a0fea3b5eafdff2c488c1f5c152223c228f1d11"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="android交流群" title="android交流群"></a>（点击图标，可以直接加入）
 
<br>

## LICENSE

```
MIT License

Copyright (c) 2019 leo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
``` 
 
 

