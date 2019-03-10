package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.CircleImageView

class InitialsCircleView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0
) : CircleImageView(context, attrs, defStyleAttr) {

    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var initials: String = ""
        set(value) {
            field = value
            invalidate()
        }
    var initialsColor: Int
        @ColorInt get() = initialsPaint.color
        set(@ColorInt value) {
            initialsPaint.color = value
            invalidate()
        }

    private var textY: Float = 0f

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.InitialsCircleView, 0, 0).apply {
            try {
                initialsColor = getColor(R.styleable.InitialsCircleView_initialsColor, Color.WHITE)
                initials = getString(R.styleable.InitialsCircleView_initials).orEmpty()
                initialsPaint.color = initialsColor
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initialsPaint.textSize = imageRectF.height() / 2f
        textY = (height / 2) - ((initialsPaint.descent() + initialsPaint.ascent()) / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val textWidth = initialsPaint.measureText(initials)
        val textX = (width - textWidth ) / 2f
        if (initials.isNotEmpty())
            canvas.drawText(initials, textX, textY, initialsPaint)
    }

}