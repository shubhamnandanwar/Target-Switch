package com.shunan.TargetSwitch

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.shunan.target_switch.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class TargetSwitch(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr), Animator.AnimatorListener {
    private var isAnimating = false
    private var lightBackDrawable: GradientDrawable
    private var darkBackDrawable: GradientDrawable
    private val dartBoardBitmap: Drawable
    private val dartBitmap: Drawable
    var backgroundPadding: Float = 0f
    private var value: Float
    var isOff: Boolean
        private set
    private var duration: Long
    private var listener: TargetSwitchListener? = null
    private var animListener: TargetSwitchAnimListener? = null

    constructor(context: Context) : this(context, null, 0) {
        style(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        style(context, attrs)

    }

    init {
        setWillNotDraw(false)
        value = 0f
        isOff = false
        duration = 500
        setOnClickListener {
            toggle()
        }

        val backgroundTint = ContextCompat.getColor(context, R.color.default_background)
        val foregroundTint = ContextCompat.getColor(context, R.color.default_foreground)

        lightBackDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(foregroundTint, foregroundTint))
        darkBackDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(backgroundTint, backgroundTint))

        dartBoardBitmap = ContextCompat.getDrawable(context, R.drawable.target_empty)!!
        dartBitmap = ContextCompat.getDrawable(context, R.drawable.dart_arrow)!!
    }

    private fun style(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TargetSwitch)
        val backgroundTint = a.getColor(R.styleable.TargetSwitch_ts_background_tint, ContextCompat.getColor(context, R.color.default_background))
        val foregroundTint = a.getColor(R.styleable.TargetSwitch_ts_foreground_tint, ContextCompat.getColor(context, R.color.default_foreground))
        duration = a.getInteger(R.styleable.TargetSwitch_ts_duration, 500).toLong()

        backgroundPadding = a.getDimension(R.styleable.TargetSwitch_ts_background_padding, 0f)
        lightBackDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(foregroundTint, foregroundTint))
        darkBackDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(backgroundTint, backgroundTint))
        a.recycle()
    }

    fun toggle() {
        if (!isAnimating) {
            isAnimating = true
            isOff = !isOff
            if (listener != null) listener!!.onSwitch(isOff)
            startAnimation()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val space = width - height

        lightBackDrawable.cornerRadius = height.toFloat() / 2
        lightBackDrawable.setBounds(0, backgroundPadding.toInt(), width, height - backgroundPadding.toInt())
        lightBackDrawable.alpha = 255 - (value * 255).toInt()
        lightBackDrawable.draw(canvas)

        darkBackDrawable.cornerRadius = height.toFloat() / 2
        darkBackDrawable.setBounds(0, backgroundPadding.toInt(), width, height - backgroundPadding.toInt())
        darkBackDrawable.alpha = (value * 255).toInt()
        darkBackDrawable.draw(canvas)

        dartBoardBitmap.setBounds(space - (value * space).toInt(), 0, width - (value * space).toInt(), height)
        dartBoardBitmap.draw(canvas)

        if (value <= 0.3) {
            val dartPosition = ((1 - value) * space).toInt()
            dartBitmap.setBounds(dartPosition + height / 2, 0, dartPosition + height / 2 + height / 2, height / 2)
        } else {
            val dartPosition = ((value - 0.3f) * (width - 0.7 * space) / 0.7f + 0.7 * space).toInt()
            dartBitmap.setBounds(dartPosition + height / 2, 0, dartPosition + height / 2 + height / 2, height / 2)
        }
        dartBitmap.alpha = cloudBitmapAlpha()
        dartBitmap.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val displayMetrics = context.resources.displayMetrics
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val defaultWidth = max(suggestedMinimumWidth, (57f * displayMetrics.density).roundToInt())
        val defaultHeight = max(suggestedMinimumHeight, (defaultWidth * 120f / 220f).roundToInt())

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(defaultWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> defaultWidth
            else -> defaultWidth
        }
        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> Math.min(defaultHeight, heightSize)
            MeasureSpec.UNSPECIFIED -> defaultHeight
            else -> defaultHeight
        }
        setMeasuredDimension(width, height)
    }


    private fun cloudBitmapAlpha(): Int {
        var v = 0
        if (value <= 0.9) {
            v = ((0.9 - value) * 2 * 255).toInt()
        }
        return v
    }

    private fun startAnimation() {
        val va: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        if (value == 1f) va.setFloatValues(1f, 0f)
        va.duration = duration
        va.addListener(this)
        va.interpolator = AccelerateDecelerateInterpolator()
        va.addUpdateListener { animation ->
            value = animation.animatedValue.toString().toFloatOrNull() ?: 0f
            if (animListener != null) animListener!!.onAnimValueChanged(value)
            invalidate()
        }
        va.start()
    }

    override fun onAnimationStart(animation: Animator) {
        if (animListener != null) animListener!!.onAnimStart()
    }

    override fun onAnimationEnd(animation: Animator) {
        isAnimating = false
        if (animListener != null) animListener!!.onAnimEnd()
    }

    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}

    fun setIsNight(is_night: Boolean, trigger_listener: Boolean) {
        isOff = is_night
        value = if (is_night) 1f else 0.toFloat()
        invalidate()
        if (listener != null && trigger_listener) listener!!.onSwitch(is_night)
    }

    fun setListener(listener: TargetSwitchListener?) {
        this.listener = listener
    }

    fun setAnimListener(animListener: TargetSwitchAnimListener?) {
        this.animListener = animListener
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }
}