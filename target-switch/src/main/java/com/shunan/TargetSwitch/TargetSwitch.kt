package com.shunan.TargetSwitch

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.shunan.target_switch.R

class TargetSwitch(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr), Animator.AnimatorListener {
    private var isAnimating = false
    private val lightBackDrawable: GradientDrawable
    private val darkBackBitmap: Drawable
    private val sunBitmap: Drawable
    private val moonBitmap: Drawable
    private val cloudsBitmap: Drawable
    private var value: Float
    var isNight: Boolean
        private set
    private var duration: Long
    private var listener: TargetSwitchListener? = null
    private var animListener: TargetSwitchAnimListener? = null

    constructor(context: Context) : this(context, null, 0) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    init {
        setWillNotDraw(false)
        value = 0f
        isNight = false
        duration = 600
        setOnClickListener {
            toggle()
        }
        lightBackDrawable = GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(Color.parseColor("#21b5e7"), Color.parseColor("#59ccda"))
        )
        lightBackDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT

        darkBackBitmap = ContextCompat.getDrawable(context, R.drawable.dark_background)!!
        sunBitmap = ContextCompat.getDrawable(context, R.drawable.target_empty)!!
        moonBitmap = ContextCompat.getDrawable(context, R.drawable.target_empty)!!
        cloudsBitmap = ContextCompat.getDrawable(context, R.drawable.dart_arrow)!!
    }

    fun toggle() {
        if (!isAnimating) {
            isAnimating = true
            isNight = !isNight
            if (listener != null) listener!!.onSwitch(isNight)
            startAnimation()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val space = width - height
        darkBackBitmap.setBounds(0, 0, width, height)
        darkBackBitmap.alpha = (value * 255).toInt()
        darkBackBitmap.draw(canvas)
        lightBackDrawable.cornerRadius = height.toFloat() / 2
        lightBackDrawable.setBounds(0, 0, width, height)
        lightBackDrawable.alpha = 255 - (value * 255).toInt()
        lightBackDrawable.draw(canvas)
        moonBitmap.setBounds(space - (value * space).toInt(), 0, width - (value * space).toInt(), height)
        moonBitmap.alpha = (value * 255).toInt()
        //moonBitmap.bitmap
        sunBitmap.setBounds(space - (value * space).toInt(), 0, width - (value * space).toInt(), height)
        sunBitmap.alpha = 255 - (value * 255).toInt()
        moonBitmap.draw(canvas)
        sunBitmap.draw(canvas)
        //val clouds_bitmap_left = (height / 2 - value * (height / 2)).toInt() // (1 - value) * (height / 2)
        if (value <= 0.5) {
            val clouds_bitmap_left = ((1 - value) * space).toInt()
            cloudsBitmap.setBounds(clouds_bitmap_left + height / 2, 0, clouds_bitmap_left + height / 2 + height / 2, height / 2)
        } else {
            val clouds_bitmap_left = (value * width + value * height - height).toInt()
            cloudsBitmap.setBounds(clouds_bitmap_left + height / 2, 0, clouds_bitmap_left + height / 2 + height / 2, height / 2)
        }
        cloudsBitmap.alpha = cloudBitmapAlpha()
        cloudsBitmap.draw(canvas)
    }

    private fun cloudBitmapAlpha(): Int {
        var v = 0
        if (value <= 0.9) {
            v = ((0.9 - value) * 2 * 255).toInt()

            //Log.d("abc123", "A :${a}")
            //v = min(max(a, 0), 255)
        }
        Log.d("abc123", "Value :${value}")
        Log.d("abc123", "Alpha :${v}")
        return v
        //return 255
    }

    private fun startAnimation() {
        val va: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)
        if (value == 1f) va.setFloatValues(1f, 0f)
        va.duration = duration
        va.addListener(this)
        va.interpolator = DecelerateInterpolator()
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
        isNight = is_night
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