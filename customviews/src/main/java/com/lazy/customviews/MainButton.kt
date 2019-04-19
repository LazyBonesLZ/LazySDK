package com.lazy.customviews

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainButton : View {
    private var mBackGroundColor = Color.parseColor("#ffffff")
    private var normalColor: Int = 0
    private var pressedColor: Int = 0
    private var radius: Float = 0.toFloat()
    private var text: String = ""
    private var mBtnTextSize = 10.toFloat()
    private var mTextColor = Color.BLACK
    private var textAligment: Int = LEFT_BOTTOM
    private var icon: Drawable? = null
    private var iconRate = 4f
    private var iconTextSpace = 5f

    private var padding = 20f
    private var bTouchCost = false

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context!!, attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context!!, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainButton)
        mBackGroundColor = typedArray.getColor(R.styleable.MainButton_mainBtnBgColor, Color.WHITE)
        normalColor = mBackGroundColor
        pressedColor = getPressedColor(normalColor, 0.5f)

        text = typedArray.getString(R.styleable.MainButton_mainBtnText)
        mBtnTextSize = typedArray.getDimension(R.styleable.MainButton_mainBtnTextSize, 10f)
        textAligment = typedArray.getInt(R.styleable.MainButton_mainBtnTextAlign, LEFT_BOTTOM)
        mTextColor = typedArray.getColor(R.styleable.MainButton_mainBtnTextColor, Color.BLACK)

        icon = typedArray.getDrawable(R.styleable.MainButton_mainBtnIcon)
        iconRate = typedArray.getFloat(R.styleable.MainButton_mainBtnIconRate, 4.0f)
        iconTextSpace = typedArray.getDimension(R.styleable.MainButton_mainBtnTextSpaceIcon, 2f)

        radius = typedArray.getDimension(R.styleable.MainButton_mainBtnRadius, 0f)
        padding = typedArray.getDimension(R.styleable.MainButton_mainBtnTextPadding, 10f)
        typedArray.recycle()

        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                //判断点击操作
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        mBackGroundColor = pressedColor

                    MotionEvent.ACTION_UP ->
                        mBackGroundColor = normalColor
                }
                //刷新
                invalidate()
                return bTouchCost //解决ACTION_UP不响应的问题：继承自VIEW的话
//        return super.onTouchEvent(event)//To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun getPressedColor(normalColor: Int, darkRadio: Float): Int {

        val hsv = FloatArray(3)
        Color.colorToHSV(normalColor, hsv)
        hsv[1] += darkRadio
        return Color.HSVToColor(hsv)
    }

    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        canvas?.save()
        drawBackground(canvas!!)
        drawDrawable(canvas)
        drawText(canvas)
        canvas.restore()
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val paint = Paint()

        paint.color = mBackGroundColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        canvas.drawRoundRect(rect, radius, radius, paint)
    }

    private fun drawText(canvas: Canvas) {
        if (TextUtils.isEmpty(text))
            return

        val paint = TextPaint()
        val font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        val textBounds = Rect()

        paint.color = mTextColor
        paint.textSize = mBtnTextSize
        paint.textAlign = Paint.Align.CENTER
        paint.isAntiAlias = true
        paint.typeface = font
        paint.getTextBounds(text, 0, text.length, textBounds)

        var x = 0f;
        var y = 0f;
        when (textAligment) {
            LEFT_BOTTOM -> {
                x = padding + textBounds.width() / 2f
                y = height - padding - textBounds.height() / 2.0f
            }
            BOTTOM -> {
                x = width / 2.0f /*- textBounds.width() / 2.0f*/
                y = height - padding - textBounds.height() / 2.0f
            }

            RIGHT_BOTTOM -> {
                x = width - textBounds.width() / 2.0f - padding
                y = height - padding - textBounds.height() / 2.0f
            }

            CENTER -> {
                if (icon != null) {
                    val bounds = getIconBounds(icon!!)
                    x = bounds.centerX().toFloat() /*+ textBounds.width()/2.0f*/
                    y = bounds.bottom + textBounds.height() / 2.0f + iconTextSpace;

                } else {
                    x = width / 2.0f
                    y = height / 2.0f + textBounds.height() / 2.0f
                }
            }
        }
//        val layout = StaticLayout(text, paint, (width - 2 * padding).toInt(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true)
//        canvas.translate(x,y)
//        layout.draw(canvas)

        canvas.drawText(text, x, y, paint)
    }

    fun getTitle(): String {
        return text
    }

    private fun getIconBounds(icon: Drawable): Rect {
        val left: Int
        val top: Int
        var iconWidth = icon.intrinsicWidth
        var iconHeight = icon.intrinsicHeight

        var mRadius = if (width >= height) height / iconRate else width / iconRate

        if (iconWidth > iconHeight) {
            iconWidth = (mRadius * Math.sqrt(2.0)).toInt()
            iconHeight = iconWidth * icon.intrinsicHeight / icon.intrinsicWidth
        } else {
            iconHeight = (mRadius * Math.sqrt(2.0)).toInt()
            iconWidth = iconHeight * icon.intrinsicWidth / icon.intrinsicHeight
        }

        left = (width - iconWidth) / 2
        top = (height - iconHeight) / 2

        return Rect(left, top, left + iconWidth, top + iconHeight)
    }

    private fun drawDrawable(canvas: Canvas) {
        if (icon == null)
            return
        val bounds = getIconBounds(icon!!)
        icon?.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
        icon?.draw(canvas)

    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        //判断点击操作
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN ->
//                mBackGroundColor = pressedColor
//
//            MotionEvent.ACTION_UP ->
//                mBackGroundColor = normalColor
//        }
//        //刷新
//        invalidate()
//        return true //解决ACTION_UP不响应的问题：继承自VIEW的话
////        return super.onTouchEvent(event)
//    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        bTouchCost = false
    }

    fun setTitle(title:String){
        text = title
        invalidate()
    }
    fun setColor(color:Int){
        mBackGroundColor = color
        normalColor = mBackGroundColor
        pressedColor =getPressedColor(mBackGroundColor,0.5f)
        invalidate()
    }

    companion object {
        private const val LEFT_BOTTOM = 0 //左下角对齐
        private const val BOTTOM = 1//底部居中对齐
        private const val RIGHT_BOTTOM = 2//右下角对齐
        private const val CENTER = 3//居中对齐

    }


}