package com.bendezu.tinkofffintech.courses

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import com.bendezu.tinkofffintech.R


open class CircleImageView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected val imageRectF = RectF()
    private val highlightRectF = RectF()

    private var image: Bitmap? = null
    private var imageShader: Shader? = null
    private val shaderMatrix = Matrix()

    private var initialized = false

    var highlightWidth: Float
        @Dimension get() = highlightPaint.strokeWidth
        set(@Dimension value) {
            highlightPaint.strokeWidth = value
            invalidate()
        }
    var highlightColor: Int
        @ColorInt get() = highlightPaint.color
        set(@ColorInt value) {
            highlightPaint.color = value
            invalidate()
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0).apply {
            try {
                val highlightColor = getColor(R.styleable.CircleImageView_highlightColor, Color.TRANSPARENT)
                val highlightWidth = getDimensionPixelSize(R.styleable.CircleImageView_highlightWidth, 0)
                highlightPaint.color = highlightColor
                highlightPaint.style = Paint.Style.STROKE
                highlightPaint.strokeWidth = highlightWidth.toFloat()

            } finally {
                recycle()
            }
            initialized = true
            setupImage()
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        setupImage()
    }
    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupImage()
    }
    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        setupImage()
    }
    override fun setImageURI(uri: Uri) {
        super.setImageURI(uri)
        setupImage()
    }

    private fun setupImage() {
        if (!initialized) return
        if (drawable is ColorDrawable) {
            imagePaint.color = (drawable as ColorDrawable).color
        } else if (drawable != null) {
            image = getBitmapFromDrawable(drawable)
            imageShader = BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            imagePaint.shader = imageShader
            updateBitmapSize()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }
        val diameter = Math.min(contentWidth, contentHeight)

        imageRectF.set(left, top, left + diameter, top + diameter)
        highlightRectF.set(imageRectF)
        val halfWidth = highlightPaint.strokeWidth / 2f
        highlightRectF.inset(halfWidth, halfWidth)

        updateBitmapSize()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(imageRectF, imagePaint)
        if (highlightPaint.strokeWidth > 0 && highlightPaint.color != Color.TRANSPARENT)
            canvas.drawOval(highlightRectF, highlightPaint)
    }

    private fun updateBitmapSize() {
        val dx: Float
        val dy: Float
        val scale: Float
        val imageWidth = image?.width ?: 0
        val imageHeight = image?.height ?: 0
        if (imageWidth < imageHeight) {
            scale = imageRectF.width() / imageWidth.toFloat()
            dx = imageRectF.left
            dy = imageRectF.top - imageHeight * scale / 2f + imageRectF.width() / 2f
        } else {
            scale = imageRectF.height() / imageHeight.toFloat()
            dx = imageRectF.left - imageWidth * scale / 2f + imageRectF.width() / 2f
            dy = imageRectF.top
        }
        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx, dy)
        imageShader?.setLocalMatrix(shaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}