package com.example.playlist_maker_dev.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlist_maker_dev.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageBitmap: Bitmap?
    private val imageBitmapPlay: Bitmap?
    private val imageBitmapPause: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlayerButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imageBitmapPlay = getDrawable(R.styleable.PlayerButtonView_imagePlay)?.toBitmap()
                imageBitmapPause = getDrawable(R.styleable.PlayerButtonView_imagePause)?.toBitmap()
                imageBitmap = imageBitmapPlay
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let {
            canvas.drawBitmap(it, null, imageRect, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }

            MotionEvent.ACTION_UP -> {
                isPlaying = !isPlaying
                imageBitmap =
                    if (isPlaying) imageBitmapPause else imageBitmapPlay
                invalidate()
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun redraw(isPlaying: Boolean) {
        imageBitmap =
            if (isPlaying) imageBitmapPause else imageBitmapPlay
        invalidate()
    }
}