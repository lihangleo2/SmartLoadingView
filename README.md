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

## 使用（下发有属性说明）
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

# 效果展示（截图分辨率低，请扫描下文二维码体验效果）

## 一、转场动画的使用

|动画结束后自动跳转|自己监听动画实现逻辑|
|:---:|:---:|
|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/loading_1.gif)|![](https://github.com/lihangleo2/SmartLoadingView/blob/master/gif/loading_2.gif)

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
 
 
 
 
 

