package com.lihang.smartloadview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


/**
 * 登录加载按钮
 * By leo
 * 2019.5.23
 */

public class SmartLoadingView extends View {

    /**
     * view的宽度
     */
    private int width;
    /**
     * view的高度
     */
    private int height;
    /**
     * 圆角半径
     */
    private int circleAngle;
    //从用户获得的圆角
    private int obtainCircleAngle;
    /**
     * 矩形2边需要缩短的距离
     */
    private int default_all_distance;
    /**
     * 当前矩形在x轴left的位置
     */
    private int current_left;

    /**
     * 动画执行时间
     */
    private int duration = 500;

    /**
     * 圆角矩形画笔
     */
    private Paint paint;
    /**
     * 文字画笔
     */
    private Paint textPaint;
    /**
     * 对勾（√）画笔
     */
    private Paint okPaint;

    /**
     * 文字绘制所在矩形
     */
    private Rect textRect = new Rect();

    /**
     * 根据view的大小设置成矩形
     */
    private RectF rectf = new RectF();

    /**
     * 动画集
     */
    private AnimatorSet animatorSet = new AnimatorSet();//这是开始的启动

    private AnimatorSet animatorNetfail = new AnimatorSet();//这是网络错误的


    /**
     * 矩形到正方形过度的动画
     */
    private ValueAnimator animator_rect_to_square;

    /**
     * 正方形到矩形动画
     */
    private ValueAnimator animator_squareToRect;

    /**
     * 矩形到圆角矩形过度的动画
     */
    private ValueAnimator animator_rect_to_angle;


    /**
     * 圆角矩形到矩形的动画
     */
    private ValueAnimator animator_angle_to_rect;


    /**
     * 是否开始绘制对勾
     */
    private boolean startDrawOk = false;

    /**
     * 绘制对勾（√）的动画
     */
    private ValueAnimator animator_draw_ok;

    /**
     * 对路径处理实现绘制动画效果
     */
    private PathEffect effect;


    /**
     * 路径--用来获取对勾的路径
     */
    private Path path = new Path();
    /**
     * 取路径的长度
     */
    private PathMeasure pathMeasure;


    /**
     * 加载动画相关
     */
    private boolean isLoading = false;
    private int startAngle = 0;
    private int progAngle = 30;
    private boolean isAdd = true;


    /*
     * 以下是自定义属性
     * */
    /**
     * 不可点击的背景颜色
     */
    private int cannotclick_color;
    /**
     * 加载失败的背景颜色
     */
    private int error_color;

    /**
     * 正常情况下view的背景颜色
     */
    private int normal_color;

    /**
     * 字体颜色
     */
    private int textColor;


    /**
     * 字体大小
     */
    private int textSize;


    /**
     * 按钮文字字符串
     */
    private String buttonString = "点击联网";
    private String normalString = "点击联网";
    private String errorString = "网络错误";


    /**
     * 是否在加载状态中
     */
    public boolean isLoading() {
        return isLoading;
    }


    /**
     * 用于绘画文字
     */
    private Path textPath = new Path();
    private int textLength;
    private int addLength = 0;//自增长的长度
    private boolean directionFlag = true;


    public SmartLoadingView(Context context) {
        this(context, null);
    }

    public SmartLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec));
    }


    /**
     * 测量宽
     * 这里看是什么模式，如果不是精准模式，那么久是wrap，则默认给个固定大小
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {//这是wrap的模式，给一个固定大小
            result = (int) getContext().getResources().getDimension(R.dimen.dp_150);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量高
     */
    private int measuredHeight(int heightMeasureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = result = (int) getContext().getResources().getDimension(R.dimen.dp_35);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    public void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SmartLoadingView);
        // 设置title
        String title = typedArray
                .getString(R.styleable.SmartLoadingView_textStr);
        normalString = title;
        buttonString = normalString;
        errorString = typedArray.getString(R.styleable.SmartLoadingView_errorStr);

        cannotclick_color = typedArray.getColor(R.styleable.SmartLoadingView_cannotclickBg, getResources().getColor(R.color.blackbb));
        error_color = typedArray.getColor(R.styleable.SmartLoadingView_errorBg, getResources().getColor(R.color.remind_color));
        normal_color = typedArray.getColor(R.styleable.SmartLoadingView_normalBg, getResources().getColor(R.color.guide_anim));
        textColor = typedArray.getColor(R.styleable.SmartLoadingView_textColor, getResources().getColor(R.color.text_color));
        obtainCircleAngle = (int) typedArray.getDimension(R.styleable.SmartLoadingView_cornerRaius, getResources().getDimension(R.dimen.default_corner));
        textSize = (int) typedArray.getDimension(R.styleable.SmartLoadingView_textSize, getResources().getDimension(R.dimen.dp_14));

    }


    /**
     * 初始化所有动画
     */
    private void initAnimation() {
        set_rect_to_circle_animation();
        set_draw_ok_animation();
        animatorSet
                .play(animator_rect_to_square).with(animator_rect_to_angle);

        animatorNetfail.play(animator_squareToRect).with(animator_angle_to_rect);
    }


    /**
     * 设置圆角矩形过度到圆的动画
     * &圆到圆角矩形
     * <p>
     * 矩形到圆角矩形的动画
     * &圆角矩形到矩形的动画
     */
    private void set_rect_to_circle_animation() {
        animator_rect_to_square = ValueAnimator.ofInt(0, default_all_distance);
        animator_rect_to_square.setDuration(duration);
        animator_rect_to_square.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                current_left = (int) animation.getAnimatedValue();
                if (current_left == default_all_distance) {
                    isLoading = true;
                }
                int alpha = 255 - (current_left * 255) / default_all_distance;
                textPaint.setAlpha(alpha);
                invalidate();
            }
        });


        animator_rect_to_angle = ValueAnimator.ofInt(obtainCircleAngle, height / 2);
        animator_rect_to_angle.setDuration(duration);
        animator_rect_to_angle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });


        animator_squareToRect = ValueAnimator.ofInt(default_all_distance, 0);
        animator_squareToRect.setDuration(duration);
        animator_squareToRect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                current_left = (int) animation.getAnimatedValue();
                if (current_left == 0) {
                    isCanClick = true;
                }
                isLoading = false;
                startDrawOk = false;
                int alpha = 255 - (current_left * 255) / default_all_distance;
                textPaint.setAlpha(alpha);
                invalidate();
            }
        });


        animator_angle_to_rect = ValueAnimator.ofInt(height / 2, obtainCircleAngle);
        animator_angle_to_rect.setDuration(duration);
        animator_angle_to_rect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

    }


    /**
     * 绘制对勾的动画
     */
    private void set_draw_ok_animation() {
        animator_draw_ok = ValueAnimator.ofFloat(1, 0);
        animator_draw_ok.setDuration(duration);
        animator_draw_ok.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDrawOk = true;
                isLoading = false;
                float value = (Float) animation.getAnimatedValue();
                effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, value * pathMeasure.getLength());
                okPaint.setPathEffect(effect);
                invalidate();

            }
        });
    }


    private void initPaint() {//初始画笔
        //矩形画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(cannotclick_color);


        //文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);


        //打勾画笔
        okPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        okPaint.setStrokeWidth(5);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setAntiAlias(true);
        okPaint.setStrokeCap(Paint.Cap.ROUND);
        okPaint.setColor(textColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        if (obtainCircleAngle > (height / 2)) {
            obtainCircleAngle = height / 2;
        }


        //如果设置的字体大小大于控件的四分之三高度，那么默认最大字体大小
        if (textSize > (height * 3 / 4)) {
            textSize = height * 3 / 4;
            textPaint.setTextSize(textSize);
        }
        textLength = textSize;
        circleAngle = obtainCircleAngle;
        default_all_distance = (w - h) / 2;

        initOk();
        initAnimation();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制控件
        draw_oval_to_circle(canvas);
        drawText(canvas);

        //绘制加载进度
        if (isLoading) {
            canvas.drawArc(new RectF(width / 2 - height / 2 + UIUtil.dip2px(getContext(), 10), UIUtil.dip2px(getContext(), 10), width / 2 + height / 2 - UIUtil.dip2px(getContext(), 10), height / 2 + height / 2 - UIUtil.dip2px(getContext(), 10)), startAngle, progAngle, false, okPaint);
            startAngle += 6;
            if (progAngle >= 270) {
                progAngle -= 2;
                isAdd = false;
            } else if (progAngle <= 45) {
                progAngle += 6;
                isAdd = true;
            } else {
                if (isAdd) {
                    progAngle += 6;
                } else {
                    progAngle -= 2;
                }
            }
            postInvalidate();
        }

        //绘制打勾
        if (startDrawOk) {
            canvas.drawPath(path, okPaint);
        }

    }


    private void draw_oval_to_circle(Canvas canvas) {
        rectf.left = current_left;
        rectf.top = 0;
        rectf.right = width - current_left;
        rectf.bottom = height;
        //画圆角矩形
        canvas.drawRoundRect(rectf, circleAngle, circleAngle, paint);
    }


    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = width;
        textRect.bottom = height;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        int canScrollLenthRight = (buttonString.length() * textSize) - width - 2 * textSize;
        int canScrollLenthLeft = (buttonString.length() * textSize) - width + (2 * textSize);

        if ((buttonString.length() * textSize) > (width - (2 * textSize))) {
            //如果文字超过控件长度
            Log.e("加载失败走的呢", buttonString + "====111111111111111");
            if (Math.abs(addLength) >= canScrollLenthRight && addLength >= 0) {
                directionFlag = !directionFlag;
            } else if (Math.abs(addLength) >= canScrollLenthLeft && addLength < 0) {
                directionFlag = !directionFlag;
            }


            if (directionFlag) {
                addLength -= 1;
                textLength -= 1;
            } else {
                addLength += 1;
                textLength += 1;
            }

            textPath.reset();
            textPath.moveTo(textLength, baseline);
            textPath.lineTo((buttonString.length() * textSize) + textLength, baseline);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawTextOnPath(buttonString, textPath, 0, 0, textPaint);
            postInvalidate();
        } else {
            Log.e("加载失败走的呢", buttonString + "====222");
            /**
             * 简单的绘制文字，没有考虑文字长度超过控件长度
             * */
            textPaint.setTextAlign(Paint.Align.CENTER);
            //文字不超过控件长度
            canvas.drawText(buttonString, textRect.centerX(), baseline, textPaint);
        }


    }


    /**
     * 绘制对勾
     */
    private void initOk() {
        //对勾的路径
        path.moveTo(default_all_distance + height / 8 * 3, height / 2);
        path.lineTo(default_all_distance + height / 2, height / 5 * 3);
        path.lineTo(default_all_distance + height / 3 * 2, height / 5 * 2);
        pathMeasure = new PathMeasure(path, true);
    }


    private boolean isCanClick = false;//是否可以被点击

    /**
     * 点击启动动画，
     * （这里是点击刚开始登录）
     */
    public void start() {
        if (isCanClick) {
            paint.setColor(normal_color);
            animatorSet.start();
            isCanClick = false;
        }
    }


    //加载失败运行(默认加载失败文案)
    public void netFaile() {
        buttonString = errorString;
        paint.setColor(error_color);
        animatorNetfail.start();
    }

    //加载失败运行(文案自定义)
    public void netFaile(String message) {
        errorString = message;
        buttonString = errorString;
        paint.setColor(error_color);
        animatorNetfail.start();
    }


    //重置所有状态
    public void reset() {
        buttonString = normalString;
        circleAngle = obtainCircleAngle;
        paint.setColor(normal_color);
        current_left = 0;
        isLoading = false;
        isCanClick = true;
        startDrawOk = false;
        int alpha = 255;
        textPaint.setAlpha(alpha);
        invalidate();
    }


    //按钮不能被点击
    public void cannotClick() {
        isCanClick = false;
        paint.setColor(cannotclick_color);
        invalidate();
    }


    /**
     * mode:1
     * 扩散全屏动画
     */
    public void loginSuccess(final Animator.AnimatorListener endListener) {
        //按钮到圆的加载动画已经结束，才启动；否则，网络请求较快严重影响效果
        if (!animatorSet.isRunning()) {
            toBigCircle(endListener);
        } else {
            //当点击按钮的时候请求网络，加入动画执行时间大于网络请求时间，
            //那么咱们默认，执行完加载动画后，立即执行加载成功动画
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    toBigCircle(endListener);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }
    }

    /**
     * mode:2
     * 绘制打勾动画
     */
    public void loginSuccess(final AnimationOKListener animationOKListener) {
        animator_draw_ok.start();
        animator_draw_ok.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationOKListener.animationOKFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    public void toBigCircle(Animator.AnimatorListener endListener) {
        CirclBigView circlBigView = new CirclBigView(getContext());
        circlBigView.setRadius(this.getMeasuredHeight() / 2);
        circlBigView.setColorBg(normal_color);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        circlBigView.setXY(location[0] + this.getMeasuredWidth() / 2, location[1]);

        ViewGroup activityDecorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activityDecorView.addView(circlBigView, layoutParams);
        circlBigView.startShowAni(endListener);
    }


    /**
     * 绘制打勾动画的接口回调
     */
    public interface AnimationOKListener {

        /**
         * 动画完成回调
         */
        void animationOKFinish();
    }


}
