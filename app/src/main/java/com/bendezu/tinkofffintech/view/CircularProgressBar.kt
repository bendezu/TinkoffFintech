package com.bendezu.tinkofffintech.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.bendezu.tinkofffintech.R

class CircularProgressBar @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyleAttr: Int = 0
) : ProgressBar(context, attrs, defStyleAttr) {

    private var thickness: Int
    private val progressBackground = ContextCompat.getColor(context, R.color.colorDivider)
    private val startColor = ContextCompat.getColor(context, R.color.colorSecondAccent)
    private val endColor = ContextCompat.getColor(context, R.color.colorSecondAccentLight)

    private var halfThickness: Float
    private val startAngle = 270f
    private var bounds: RectF? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0).apply {
            try {
                thickness = getDimensionPixelSize(R.styleable.CircularProgressBar_thickness, 28)
                halfThickness = thickness / 2f
                paint.strokeWidth = thickness.toFloat()
            } finally {
                recycle()
            }
        }
        val oval = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setStroke(thickness - 1, progressBackground)
        }
        background = oval
        progressDrawable = null
        max = 100
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val gradient = SweepGradient(
            width / 2f, height / 2f,
            intArrayOf(startColor, endColor),
            floatArrayOf(0f, 1f)
        )
        val matrix = Matrix()
        matrix.preRotate(startAngle, width / 2f, height / 2f)
        gradient.setLocalMatrix(matrix)
        paint.shader = gradient
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (bounds == null) {
            bounds = RectF(background.bounds)
            bounds?.inset(halfThickness, halfThickness)
        }
        canvas.drawArc(bounds, startAngle + halfThickness, progress * 360f / max, false, paint)
    }

}