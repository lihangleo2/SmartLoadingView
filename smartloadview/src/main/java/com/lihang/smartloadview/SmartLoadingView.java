package com.lihang.smartloadview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;


/**
 * @Author leo
 * @Address https://github.com/lihangleo2
 * @Date 2024/6/21 重构
 */

public class SmartLoadingView extends AppCompatTextView {

    //view的宽度
    private int width;

    //View的高度
    private int height;

    //圆角半径
    private int circleAngle;

    //从用户获得的圆角
    private int obtainCircleAngle;

    private int textScrollMode = 1;//文字滚动模式，默认为1：来回滚动

    //矩形2边需要缩短的距离
    private int default_all_distance;

    //当前矩形在x轴left的位置
    private int current_left;

    //动画执行时间
    private int duration = 500;

    //圆角矩形画笔
    private Paint paint;


    //对勾（√）画笔
    private Paint okPaint;

    //文字绘制所在矩形
    private Rect textRect = new Rect();

    //根据view的大小设置成矩形
    private RectF rectf = new RectF();

    /**
     * 动画集
     */
    //这是开始的启动
    private AnimatorSet animatorSet = new AnimatorSet();
    //这是网络错误的
    private AnimatorSet animatorNetfail = new AnimatorSet();

    //矩形到正方形过度的动画
    private ValueAnimator animator_rect_to_square;

    //正方形到矩形动画
    private ValueAnimator animator_squareToRect;

    //矩形到圆角矩形过度的动画
    private ValueAnimator animator_rect_to_angle;

    //圆角矩形到矩形的动画
    private ValueAnimator animator_angle_to_rect;


    /**
     * 以下是绘制对勾动画
     */

    //是否开始绘制对勾
    private boolean startDrawOk = false;

    //绘制对勾（√）的动画
    private ValueAnimator animator_draw_ok;

    //对路径处理实现绘制动画效果
    private PathEffect effect;

    //路径--用来获取对勾的路径
    private Path path = new Path();

    //取路径的长度
    private PathMeasure pathMeasure;

    /**
     * 加载loading动画相关
     */
    //是否开始绘画，加载转圈动画
    private boolean isDrawLoading = false;
    //是否处于加载状态，注意，这里和上面是2个概念，只要点击按钮，没有走错误和走正确的都视为在加载状态下
    private boolean isLoading = false;
    private int startAngle = 0;
    private int progAngle = 30;
    private boolean isAdd = true;


    /**
     * 以下是自定义属性
     */

    //按钮背景色
    private int backgroundColor;

    //不可点击的背景颜色
    private int unEnabled_backgroundColor;

    /***************************************** 重构中 *******************************************/

    //加载失败的背景颜色
    private int error_color;

    //按钮文字
    private String normalString = getResources().getString(R.string.normalString);
    private String errorString = getResources().getString(R.string.errorString);
    private String currentString;//当前要绘画的TextStr

    //获取文字绘画区域
    private Rect mRect;
    //当前字体颜色值
    private int textColor;
    //当前字体透明度
    private int textAlpha;
    //文字滚动速度
    private int speed;

    //这是全屏动画
    private CirclBigView circlBigView;

    private boolean isFollow;
    private int clickType = 1;//关注的样式，默认是正常样式


    public SmartLoadingView(Context context) {
        this(context, null);
    }

    public SmartLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlBigView = new CirclBigView(getContext());
        mRect = new Rect();
        init(attrs);
        initPaint();
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SmartLoadingView);
        // 设置title
        if (TextUtils.isEmpty(getText())) {
            currentString = normalString;
        } else {
            normalString = (String) getText();
            currentString = normalString;
        }
        Log.e("当前是否初始化init",isEnabled()+"===---");

        String currentErrorString = typedArray.getString(R.styleable.SmartLoadingView_errorMsg);
        if (!TextUtils.isEmpty(currentErrorString)) {
            errorString = currentErrorString;
        }
        unEnabled_backgroundColor = typedArray.getColor(R.styleable.SmartLoadingView_hl_unEnabled_background, getResources().getColor(R.color.blackbb));
        error_color = typedArray.getColor(R.styleable.SmartLoadingView_background_error, getResources().getColor(R.color.remind_color));
        //赋予背景色默认颜色值
        backgroundColor = getResources().getColor(R.color.guide_anim);
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            backgroundColor = ((ColorDrawable) background).getColor();
        }
        obtainCircleAngle = (int) typedArray.getDimension(R.styleable.SmartLoadingView_cornerRaius, getResources().getDimension(R.dimen.default_corner));
        textScrollMode = typedArray.getInt(R.styleable.SmartLoadingView_textScrollMode, 1);
        speed = typedArray.getInt(R.styleable.SmartLoadingView_speed, 400);
        clickType = typedArray.getInt(R.styleable.SmartLoadingView_click_mode, 1);

        int paddingTop = getPaddingTop() == 0 ? dip2px(7) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? dip2px(7) : getPaddingBottom();
        int paddingLeft = getPaddingLeft() == 0 ? dip2px(15) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? dip2px(15) : getPaddingRight();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        setBackgroundColor(0);
        setMaxLines(1);
        setGravity(Gravity.CENTER);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
//        private BufferType mBufferType = BufferType.NORMAL;
        errorString = (String) text;
        normalString = (String) text;
        currentString = (String) text;
        postInvalidate();
    }


    private int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public boolean isFollow() {
        return isFollow;
    }


    public void setFollow(boolean follow) {
        //不在loading的情况下生效
        if (!isLoading) {

            if (clickType == 4) {
                isFollow = follow;
                if (isFollow) {
                    paint.setColor(error_color);
                    currentString = errorString;
                } else {
                    paint.setColor(backgroundColor);
                    currentString = normalString;
                }
                postInvalidate();
            } else {
                OKAnimationType okAnimationType = OKAnimationType.NORMAL;
                if (clickType == 1) {
                    okAnimationType = OKAnimationType.NORMAL;
                } else if (clickType == 2) {
                    okAnimationType = OKAnimationType.HIDE;
                } else {
                    okAnimationType = OKAnimationType.TRANSLATION_CENTER;
                }
                setFollow(follow, okAnimationType);
            }

        }


    }

    private void setFollow(final boolean follow, final OKAnimationType okAnimationType) {
        if (!isLoading) {
            if (getWidth() == 0 && getHeight() == 0) {

                SmartLoadingView.this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        isFollow = follow;
                        if (isFollow) {
                            current_left = default_all_distance;

                            int nowAlpha = textAlpha / 2 - (current_left * textAlpha / default_all_distance) < 0 ? 0 : textAlpha / 2 - (current_left * textAlpha / default_all_distance);
                            textPaint.setColor(addAlpha(textColor, nowAlpha));
                            if (current_left == default_all_distance) {
                                startDrawOk = true;
                            }

                            effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, 0 * pathMeasure.getLength());
                            okPaint.setPathEffect(effect);
                            postInvalidate();

                            if (okAnimationType == OKAnimationType.HIDE || okAnimationType == OKAnimationType.TRANSLATION_CENTER) {
                                setVisibility(View.INVISIBLE);
                            }

                        } else {
                            reset();
                        }

                    }
                });
            } else {

                isFollow = follow;
                if (isFollow) {
                    current_left = default_all_distance;

                    int nowAlpha = textAlpha / 2 - (current_left * textAlpha / default_all_distance) < 0 ? 0 : textAlpha / 2 - (current_left * textAlpha / default_all_distance);
                    textPaint.setColor(addAlpha(textColor, nowAlpha));
                    if (current_left == default_all_distance) {
                        startDrawOk = true;
                    }

                    effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, 0 * pathMeasure.getLength());
                    okPaint.setPathEffect(effect);
                    postInvalidate();

                    if (okAnimationType == OKAnimationType.HIDE || okAnimationType == OKAnimationType.TRANSLATION_CENTER) {
                        setVisibility(View.INVISIBLE);
                    }
                } else {

                    reset();

                }


            }

        }
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

                int nowAlpha = textAlpha / 2 - (current_left * textAlpha / default_all_distance) < 0 ? 0 : textAlpha / 2 - (current_left * textAlpha / default_all_distance);
                textPaint.setColor(addAlpha(textColor, nowAlpha));
                if (current_left == default_all_distance) {
                    isDrawLoading = true;
                }
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
                //当控件扩展到一半时再显示文字，不然当文案过长时，会先显示文字。超过控件
                if (current_left <= default_all_distance / 2) {
                    int nowAlpha = (default_all_distance / 2 - current_left) * textAlpha / (default_all_distance / 2);
                    textPaint.setColor(addAlpha(textColor, nowAlpha));
                }
                //错误动画全部走完之后，才能被点击
                if (current_left == 0) {
                    isLoading = false;
                    setClickable(true);
                }
                isDrawLoading = false;
                startDrawOk = false;
                postInvalidate();
            }
        });


        animator_angle_to_rect = ValueAnimator.ofInt(height / 2, obtainCircleAngle);
        animator_angle_to_rect.setDuration(duration);
        animator_angle_to_rect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleAngle = (int) animation.getAnimatedValue();
                postInvalidate();
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
                isDrawLoading = false;
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

        if (isEnabled()) {
            paint.setColor(backgroundColor);
        } else {
            paint.setColor(unEnabled_backgroundColor);
        }

        //打勾画笔
        okPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        okPaint.setStrokeWidth(5);
        okPaint.setStyle(Paint.Style.STROKE);
        okPaint.setAntiAlias(true);
        okPaint.setStrokeCap(Paint.Cap.ROUND);


        ColorStateList textColors = getTextColors();
        final int[] drawableState = getDrawableState();
        //获取textView默认颜色值
        textColor = textColors.getColorForState(drawableState, 0);
        okPaint.setColor(textColor);
        textAlpha = Color.alpha(textColor);


        //文字画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(getTextSize());
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (width == 0) {
            width = w;
            height = h;
            if (obtainCircleAngle > (height / 2)) {
                obtainCircleAngle = height / 2;
            }
            circleAngle = obtainCircleAngle;
            default_all_distance = (w - h) / 2;
            initOk();
            initAnimation();
            //如果不是精准模式，我们代码里设置第一次的长宽，成为精准模式
            //这样避免，更改文字内容时，总是会改变控件的长宽
            setWidth(width);
            setHeight(height);
//            setEnabled(isEnabled());
        }
    }

    //给TextView字体设置透明度。
    private int addAlpha(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    //文字画笔
    private Paint textPaint;
    //文字超过一行时，进行的文字滚动动画
    private ValueAnimator animator_text_scroll;//这只是模式之一
    private ValueAnimator animator_marque;
    private int drawTextStart;
    private int drawMarqueTextStart;

    private void drawText(final Canvas canvas) {
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        rectf.left = current_left + getPaddingLeft();
        rectf.top = 0;
        rectf.right = width - current_left - getPaddingRight();
        rectf.bottom = height;
        //画圆角矩形
        canvas.drawRoundRect(rectf, circleAngle, circleAngle, paint);

        //设置混合模式
        textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textRect.left = getPaddingLeft();
        textRect.top = 0;
        textRect.right = width - getPaddingRight();
        textRect.bottom = height;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        final int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //这是测量文字的长度。
        int myTotal = (int) (textPaint.measureText(currentString) + getPaddingRight() + getPaddingLeft());
        if (myTotal > getWidth()) {
            if (textScrollMode == 1) {
                textPaint.setTextAlign(Paint.Align.LEFT);
                if (animator_text_scroll == null && !isLoading) {
                    //此时文字长度已经超过一行，进行文字滚动
                    animator_text_scroll = ValueAnimator.ofInt(textRect.left, (int) (textRect.left - textPaint.measureText(currentString) + (getWidth() - getPaddingLeft() - getPaddingRight())));
                    animator_text_scroll.setDuration(currentString.length() * speed);
                    animator_text_scroll.setRepeatMode(ValueAnimator.REVERSE);
                    animator_text_scroll.setRepeatCount(-1);
                    animator_text_scroll.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            drawTextStart = (int) animation.getAnimatedValue();
                            postInvalidate();
                        }
                    });
                    animator_text_scroll.start();
                }
                canvas.drawText(currentString, drawTextStart, baseline, textPaint);
            } else {
                textPaint.setTextAlign(Paint.Align.LEFT);
                if (animator_text_scroll == null && !isLoading) {
                    //此时文字长度已经超过一行，进行文字滚动
                    animator_text_scroll = ValueAnimator.ofInt(textRect.left, (int) (textRect.left - textPaint.measureText(currentString)));
                    animator_text_scroll.setDuration(currentString.length() * speed);
                    animator_text_scroll.setInterpolator(new LinearInterpolator());
                    animator_text_scroll.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {

                            drawTextStart = (int) animation.getAnimatedValue();
                            postInvalidate();
                            if (drawTextStart == textRect.left) {
                                if (animator_marque != null) {
                                    animator_marque.cancel();
                                    animator_marque = null;
                                }
                            }
                            if (animator_marque == null && !isLoading && drawTextStart <= (int) (textRect.left - textPaint.measureText(currentString) + (getWidth() - getPaddingLeft() - getPaddingRight()) - (getWidth() - getPaddingLeft() - getPaddingRight()) / 3)) {
                                int duration = (int) (((currentString.length() * speed) * (textRect.right - textRect.left)) / textPaint.measureText(currentString));
                                animator_marque = ValueAnimator.ofInt(textRect.right, textRect.left);
                                animator_marque.setDuration(duration);
                                animator_marque.setInterpolator(new LinearInterpolator());
                                animator_marque.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        drawMarqueTextStart = (int) animation.getAnimatedValue();
                                        if (drawMarqueTextStart == textRect.left) {
                                            mHandler.sendEmptyMessageDelayed(14, 1500);
                                        }
                                        postInvalidate();
                                    }
                                });
                                animator_marque.start();
                            }
                        }
                    });
                    animator_text_scroll.start();
                }
                if (animator_marque != null) {
                    canvas.drawText(currentString, drawMarqueTextStart, baseline, textPaint);
                }
                canvas.drawText(currentString, drawTextStart, baseline, textPaint);
            }

        } else {
            cancleScroll();
            textPaint.setTextAlign(Paint.Align.CENTER);
            drawTextStart = textRect.left;
            canvas.drawText(currentString, textRect.centerX(), baseline, textPaint);
        }

        // 还原混合模式
        textPaint.setXfermode(null);
        // 还原画布
        canvas.restoreToCount(sc);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        draw_oval_to_circle(canvas);
        drawText(canvas);

        //绘制加载进度
        if (isDrawLoading) {
            canvas.drawArc(new RectF(width / 2 - height / 2 + height / 4, height / 4, width / 2 + height / 2 - height / 4, height / 2 + height / 2 - height / 4), startAngle, progAngle, false, okPaint);
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
     * 绘制对勾
     */
    private void initOk() {
        //对勾的路径
        path.moveTo(default_all_distance + height / 8 * 3, height / 2);
        path.lineTo(default_all_distance + height / 2, height / 5 * 3);
        path.lineTo(default_all_distance + height / 3 * 2, height / 5 * 2);
        pathMeasure = new PathMeasure(path, true);
    }


    //smartLoadingView 开启动画
    public void start() {
        //没有在loading的情况下才能点击（没有在请求网络的情况下）
        if (!isLoading) {
            cancleScroll();
            startDrawOk = false;
            currentString = normalString;
            this.setClickable(false);
            paint.setColor(backgroundColor);
            isLoading = true;
            animatorSet.start();
        }
    }

    private void cancleScroll() {
        if (animator_text_scroll != null) {
            animator_text_scroll.cancel();
            animator_text_scroll = null;
        }

        if (animator_marque != null) {
            animator_marque.cancel();
            animator_marque = null;
        }
    }


    //加载失败运行(默认加载失败文案)
    public void netFaile() {
        if (isLoading) {
            currentString = errorString;
            paint.setColor(error_color);
            animatorNetfail.start();
        }
    }


    //加载失败运行(文案自定义)
    public void netFaile(String message) {
        if (isLoading) {
            isFollow = true;
            errorString = message;
            currentString = errorString;
            paint.setColor(error_color);
            animatorNetfail.start();
        }
    }

    public void backToStart() {
        if (isLoading) {
            currentString = normalString;
            paint.setColor(backgroundColor);
            animatorNetfail.start();
        }
    }


    //立即重置状态
    public void reset() {
        resetAll();
    }

    //用于，扩散动画，有可能不跳转。停留在当前页，1秒后重置状态
    protected void resetLater() {
        mHandler.sendEmptyMessageDelayed(13, 1000);
    }

    public void resetAll() {
        isFollow = false;
        setClickable(true);
        currentString = normalString;
        textPaint.setColor(textColor);
        circleAngle = obtainCircleAngle;
        paint.setColor(backgroundColor);
        current_left = 0;
        isDrawLoading = false;
        startDrawOk = false;
        isLoading = false;
        invalidate();

        animator_draw_ok.cancel();
        animatorSet.cancel();
        animatorNetfail.cancel();
        if (circlBigView != null) {
            circlBigView.setCircleR(0);
        }
        setVisibility(View.VISIBLE);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (circlBigView == null) {
                return;
            }
            switch (msg.what) {
                case 11:
                    AnimationFullScreenListener animationFullScreenListener = (AnimationFullScreenListener) msg.obj;
                    toBigCircle(animationFullScreenListener);
                    break;
                case 12:
                    Activity activity = (Activity) msg.obj;
                    Class temp = clazz;
                    toBigCircle(activity, temp);
                    clazz = null;
                    break;

                case 13:
                    resetAll();
                    break;

                case 14:
                    if (animator_text_scroll != null) {
                        animator_text_scroll.cancel();
                        animator_text_scroll = null;
                        postInvalidate();
                    }
                    break;
            }
        }
    };


    //开启全屏动画，并且监听动画结束后，实现自己的逻辑
    public void onSuccess(AnimationFullScreenListener animationFullScreenListener) {
        //必须，点击了最开始的动画处于，加载状态，才能获得回调
        if (isLoading) {
            if (!animatorSet.isRunning()) {
                toBigCircle(animationFullScreenListener);
            } else {
                //当点击按钮的时候请求网络，加入动画执行时间大于网络请求时间，
                //那么咱们默认，执行完加载动画后，立即执行加载成功动画
                Message message = new Message();
                message.what = 11;
                message.obj = animationFullScreenListener;
                mHandler.sendMessageDelayed(message, 1000);
            }
        }
    }

    private Class clazz;

    //开启全屏动画，不需要监听动画，只需要传入值，即可实现跳转
    public void onSuccess(Activity activity, Class clazz) {
        //必须，点击了最开始的动画处于，加载状态，才能获得回调
        if (isLoading) {
            this.clazz = clazz;
            if (!animatorSet.isRunning()) {
                toBigCircle(activity, clazz);
            } else {
                //当点击按钮的时候请求网络，加入动画执行时间大于网络请求时间，
                //那么咱们默认，执行完加载动画后，立即执行加载成功动画
                Message message = new Message();
                message.what = 12;
                message.obj = activity;
                mHandler.sendMessageDelayed(message, 1000);
            }
        }
    }


    private AnimationOKListener animationOKListener;

    //开启打勾模式，打勾后，是否隐藏
    public void onSuccess(AnimationOKListener animationOKListenerOkgo) {
        this.animationOKListener = animationOKListenerOkgo;
        OKAnimationType okAnimationType = OKAnimationType.NORMAL;
        if (clickType == 4) {
            return;
        } else if (clickType == 1) {
            okAnimationType = OKAnimationType.NORMAL;
        } else if (clickType == 2) {
            okAnimationType = OKAnimationType.HIDE;
        } else if (clickType == 3) {
            okAnimationType = OKAnimationType.TRANSLATION_CENTER;
        }


        //这个思路是对的，然后就是看clickindex是否一致
        //必须，点击了最开始的动画处于，加载状态，才能获得回调
        if (isLoading) {
            if (okAnimationType != OKAnimationType.TRANSLATION_CENTER) {
//                animator_draw_ok.cancel();
                set_draw_ok_animation();
                animator_draw_ok.start();
                final OKAnimationType finalOkAnimationType = okAnimationType;
                animator_draw_ok.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animationOKListener != null) {
                            animationOKListener.animationOKFinish();
                        }
                        isLoading = false;
                        isFollow = true;
                        setClickable(true);
                        if (finalOkAnimationType == OKAnimationType.HIDE) {
                            //这里是隐藏的操作
                            Animation animations = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_hide);
                            setAnimation(animations);
                            animations.start();
                            setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            } else {
                //如果是要移动到中间的模式的话
                int[] location = new int[2];
                SmartLoadingView.this.getLocationOnScreen(location);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(height, height);
                layoutParams.leftMargin = location[0] + (width / 2 - height / 2);
                layoutParams.topMargin = location[1];

                final OkView okView = new OkView(getContext());
                okView.setLayoutParams(layoutParams);
                okView.setCircleColor(backgroundColor);
                okView.setOkColor(textColor);
                okView.setRadius(height / 2);

                final ViewGroup activityDecorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
                activityDecorView.addView(okView);
                okView.start(duration);
                //初始真正的那个View
                setVisibility(View.INVISIBLE);
//                reset();

                //当前屏幕中心位置
                int window_center_x = UIUtil.getWidth(getContext()) / 2;
                int window_center_y = UIUtil.getHeight(getContext()) / 2;

                //okView当前的中心点
                int okView_center_x = location[0] + width / 2;
                int okView_center_y = location[1] + height / 2;

                ObjectAnimator translationY = ObjectAnimator.ofFloat(okView, "translationY", 0f, window_center_y - okView_center_y)
                        .setDuration(duration);
                ObjectAnimator translationX = ObjectAnimator.ofFloat(okView, "translationX", 0f, window_center_x - okView_center_x)
                        .setDuration(duration);

                ObjectAnimator toViewAnimatorX = ObjectAnimator.ofFloat(okView, "scaleX", 1f, 1.3f).setDuration(duration / 2);
                toViewAnimatorX.setRepeatMode(ValueAnimator.REVERSE);
                toViewAnimatorX.setRepeatCount(1);
                toViewAnimatorX.setInterpolator(new AnticipateInterpolator());
                ObjectAnimator toViewAnimatorY = ObjectAnimator.ofFloat(okView, "scaleY", 1f, 1.3f).setDuration(duration / 2);
                toViewAnimatorY.setRepeatMode(ValueAnimator.REVERSE);
                toViewAnimatorY.setRepeatCount(1);
                toViewAnimatorY.setInterpolator(new AnticipateInterpolator());
                AnimatorSet animatorScale = new AnimatorSet();
                animatorScale.playTogether(toViewAnimatorX, toViewAnimatorY);

                ObjectAnimator toViewAnimatorAlpha = ObjectAnimator.ofFloat(okView, "alpha", 1f, 0f).setDuration(duration);
                //这里就用代码实现把
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(translationY).with(translationX).before(animatorScale).before(toViewAnimatorAlpha);
                animatorSet.start();
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationOKListener.animationOKFinish();
                        isLoading = false;
                        setClickable(true);
                        activityDecorView.removeView(okView);

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
    }

    public enum OKAnimationType {
        NORMAL, HIDE, TRANSLATION_CENTER
    }

    private void toBigCircle(AnimationFullScreenListener animationFullScreenListener) {
        circlBigView.setRadius(this.getMeasuredHeight() / 2);
        circlBigView.setColorBg(backgroundColor);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        circlBigView.setXY(location[0] + this.getMeasuredWidth() / 2, location[1]);
        ViewGroup activityDecorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activityDecorView.removeView(circlBigView);
        activityDecorView.addView(circlBigView, layoutParams);
        circlBigView.startShowAni(animationFullScreenListener, this);
    }


    private void toBigCircle(Activity activity, Class clazz) {
        circlBigView.setRadius(this.getMeasuredHeight() / 2);
        circlBigView.setColorBg(backgroundColor);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        circlBigView.setXY(location[0] + this.getMeasuredWidth() / 2, location[1]);
        ViewGroup activityDecorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activityDecorView.removeView(circlBigView);
        activityDecorView.addView(circlBigView, layoutParams);
        circlBigView.startShowAni(activity, clazz);
    }

    //绘制打勾动画的接口
    public interface AnimationOKListener {
        void animationOKFinish();
    }

    //绘制全屏动画
    public interface AnimationFullScreenListener {
        void animationFullScreenFinish();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleScroll();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (paint != null)
                paint.setColor(backgroundColor);
            postInvalidate();
        } else {
            if (paint != null)
                paint.setColor(unEnabled_backgroundColor);
            postInvalidate();
        }
    }
}
