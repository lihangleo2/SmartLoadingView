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
	        implementation 'com.github.lihangleo2:SmartLoadingView:1.3.2'
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

 # 自定义属性说明
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

