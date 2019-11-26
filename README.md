[![](https://jitpack.io/v/lihangleo2/SmartLoadingView.svg)](https://jitpack.io/#lihangleo2/SmartLoadingView)

## SmartLoadingView一个自带dialog的联网请求按钮
* 支持在任何布局上使用
* 支持转场动画（可用于登录）
* 支持正常网络请求且带有多种酷炫效果

<br>

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
	        implementation 'com.github.lihangleo2:SmartLoadingView:2.0.1'
	}
   ```

<br>

## 使用（下方有属性说明）
```xml
<com.lihang.smartloadview.SmartLoadingView
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
如果你们的需求是，失败文案显示在别的地方，或者只是弹一个toast,按钮箱回到初始状态，只需要这样
```java
smartLoadingView.backToStart();
```

<br>

## 三、正常的联网请求（目前作者用于关注）

|3.1、正常的联网，正常出结果|3.2、正常联网，打勾出结果|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_1.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_2.gif)
|3.2、打勾出结果，打勾消失|3.4、打勾出结果，提醒用户|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_3.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/follow_4.gif)


## 扫描二维体验效果(下载密码是：123456)
![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/SmartLoadingView_.png)

<br>

 # 自定义属性
 #### 按钮文案
 - android:text="自带dialog按钮"  使用了textView的text文字属性
  
 #### 按钮文案颜色值
 - android:textColor="#fff" 使用了textView的textColor颜色值属性
  
 #### 按钮文案字体大小
 - android:textSize="15dp" 使用了textView的字体大小
 
 #### 正常情况下的背景颜色值
 - app:background_normal="#fff" 按钮正常的背景颜色值

 #### 联网失败文案
 - app:errorMsg="联网失败文案" 联网失败展示的文案，比如登录时，账号密码错误
 
 #### 联网失败下的背景颜色值
 - app:background_error="#ED7676" 联网失败时展示的背景颜色值，一般为殷红色

 #### 不可点击状态下的背景颜色值
 - app:background_cannotClick="#bbbbbb" 不可点击状态下的背景颜色值
 
 #### 圆角属性
 - app:cornerRaius="30dp" 背景的圆角属性
 
 #### 文字滚动模式（文字超过一行时，文字自动滚动）
 - app:textScrollMode="marquee" 比如联网失败后，失败文案太长了。文字自动滚动，这里有2种方式。1、normal来回滚动。 2、marquee跑马灯效果
 
 #### 文字滚动速度
 - app:speed="400" 文字的滚动速度。可以理解为一个文字滚动出屏幕外需要的时间
 
 #### 按钮的点击状态
 - app:smart_clickable="true" 不设置时，默认可以点击，为true。代码里也能通过 smartLoadingView9.setSmartClickable(false) 进行设置
 
 #### 这里稍微说下长宽
 长宽都是用系统的layout_width和layout_height，包括设置padding。如果不设置，是有默认间距的
 
 <br>
 
 ## 关于作者。
Android工作多年了，一直向往大厂。在前进的道路上是孤独的。如果你在学习的路上也感觉孤独，请和我一起。让我们在学习道路上少些孤独
* [关于我的经历](https://mp.weixin.qq.com/s?__biz=MzAwMDA3MzU2Mg==&mid=2247483667&idx=1&sn=1a575ea2c636980e5f4c579d3a73d8ab&chksm=9aefcb26ad98423041c61ad7cbad77f0534495d11fc0a302b9fdd3a3e6b84605cad61d192959&mpshare=1&scene=23&srcid=&sharer_sharetime=1572505105563&sharer_shareid=97effcbe7f9d69e6067a40da3e48344a#rd)
 * QQ群： 209010674 <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=5e29576e7d2ebf08fa37d8953a0fea3b5eafdff2c488c1f5c152223c228f1d11"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="android交流群" title="android交流群"></a>（点击图标，可以直接加入）
 
 
 
 

