package com.sample.libchat.ui.views.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.transform


class BitmapRoundRectDrawable : Drawable() {

    companion object {
        const val DEFAULT_CORNER_RADIUS = 10.0f
        const val DEFAULT_SHADOW_RADIUS = 15.0f
        const val DEFAULT_STROKE_SIZE = 1.0f
    }

    private var mPath = Path()
    private var mBitmapShader: BitmapShader? = null

    private val mPaintBackground = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val mPaintStroke = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    var scaleType = ImageView.ScaleType.CENTER_CROP

    var isShadowVisible = false
    var shadowSize = DEFAULT_SHADOW_RADIUS
    var shadowColor = Color.LTGRAY

    var isStrokeVisible = true
    var strokeSize = DEFAULT_STROKE_SIZE
    var strokeColor = Color.LTGRAY

    var bitmap: Bitmap? = null
        set(value) {
            field = value
            mBitmapShader = if (value != null) {
                BitmapShader(value, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            } else {
                null
            }
        }

    var color: Int = Color.WHITE
        set(value) {
            bitmap = null
            field = value
        }

    var cornerRadii = floatArrayOf(
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS,
            DEFAULT_CORNER_RADIUS, DEFAULT_CORNER_RADIUS
    )

    override fun draw(canvas: Canvas) {
        val rect = RectF().apply {
            left = shadowSize
            top = shadowSize
            right = bounds.width() - shadowSize
            bottom = bounds.height() - shadowSize
        }

        drawColorBackground(canvas, rect)
        drawBitmapBackground(canvas, rect)
    }

    override fun setAlpha(alpha: Int) {
        mPaintBackground.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaintBackground.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    private fun drawColorBackground(canvas: Canvas, rect: RectF) {
        mPaintBackground.shader = null
        mPaintBackground.color = color

        if (isShadowVisible) {
            mPaintBackground.setShadowLayer(shadowSize, 0.0f, 0.0f, shadowColor)
        }

        mPath.reset()
        mPath.addRoundRect(rect, cornerRadii, Path.Direction.CW)

        if (isStrokeVisible) {
            mPaintStroke.strokeWidth = strokeSize
            mPaintStroke.color = strokeColor
            canvas.drawPath(mPath, mPaintStroke)
        }

        canvas.drawPath(mPath, mPaintBackground)
    }

    private fun drawBitmapBackground(canvas: Canvas, rect: RectF) {
        val bmp = bitmap
        val shader = mBitmapShader
        if (bmp != null && shader != null) {
            val ratioX =  canvas.width / bmp.width.toFloat()
            val ratioY =  canvas.height / bmp.height.toFloat()
            val ratioMax = ratioX.coerceAtLeast(ratioY)
            val offsetX = (bmp.width * ratioMax - canvas.width) / 2
            val offsetY = (bmp.height * ratioMax - canvas.height) / 2

            mPaintBackground.shader = shader.apply {
                val values = FloatArray(9)
                val matrix = Matrix()
                getLocalMatrix(matrix)
                matrix.getValues(values)

                when(scaleType) {
                    ImageView.ScaleType.CENTER_CROP -> {
                        if (values[Matrix.MSCALE_X] != ratioMax && values[Matrix.MSCALE_Y] != ratioMax) {
                            transform {
                                postScale(ratioMax, ratioMax)
                                postTranslate(-offsetX, -offsetY)
                            }
                        }
                    }

                    else -> {
                        if (values[Matrix.MSCALE_X] != ratioX && values[Matrix.MSCALE_Y] != ratioY) {
                            transform { postScale(ratioX, ratioY) }
                        }
                    }
                }
            }

            mPaintBackground.clearShadowLayer()
            mPath.addRoundRect(rect, cornerRadii, Path.Direction.CW)
            canvas.drawPath(mPath, mPaintBackground)
        }
    }

    fun setCorners(leftBottom: Float, leftTop: Float, rightTop: Float, rightBottom: Float) {
        cornerRadii = floatArrayOf(
                leftBottom, leftBottom,
                leftTop, leftTop,
                rightTop, rightTop,
                rightBottom, rightBottom
        )
    }
}