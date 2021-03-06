package com.lazy.customviews.progressiveview.view


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.lazy.customviews.R

/**
 * Created by John on 2014/10/15.
 */
class ReadProgressBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mAboveWaveColor: Int = 0
    private val mBlowWaveColor: Int = 0
    private var mProgress: Int = 0
    private val mWaveHeight: Int = 0
    private val mWaveMultiple: Int = 0
    private val mWaveHz: Int = 0

    private var mMaskToBottom: Int = 0
    private var mMask: ImageView? = null


    private val DEFAULT_ABOVE_WAVE_COLOR = Color.WHITE
    private val DEFAULT_BLOW_WAVE_COLOR = Color.WHITE
    private val DEFAULT_PROGRESS = 80

    private var mBackGroundDrawable:ClipDrawable? = null

    init {
        orientation = LinearLayout.VERTICAL
        //load styled attributes.
        //        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ReadProgressBar, R.attr.ReadProgressBarStyle, 0);
        //        mAboveWaveColor = attributes.getColor(R.styleable.ReadProgressBar_above_wave_color, DEFAULT_ABOVE_WAVE_COLOR);
        //        mBlowWaveColor = attributes.getColor(R.styleable.ReadProgressBar_blow_wave_color, DEFAULT_BLOW_WAVE_COLOR);
        //        mProgress = attributes.getInt(R.styleable.ReadProgressBar_progress, DEFAULT_PROGRESS);
        //        mWaveHeight = attributes.getInt(R.styleable.ReadProgressBar_wave_height, MIDDLE);
        //        mWaveMultiple = attributes.getInt(R.styleable.ReadProgressBar_wave_length, LARGE);
        //        mWaveHz = attributes.getInt(R.styleable.ReadProgressBar_wave_hz, MIDDLE);
        //        attributes.recycle();
        //
        //        mMask = new Wave(context, null);
        //        mMask.initializeWaveSize(mWaveMultiple, mWaveHeight, mWaveHz);
        //        mMask.setAboveWaveColor(mAboveWaveColor);
        //        mMask.setBlowWaveColor(mBlowWaveColor);
        //        mMask.initializePainters();
        //
        //        mSolid = new Solid(context, null);
        //        mSolid.setAboveWavePaint(mMask.getAboveWavePaint());
        //        mSolid.setBlowWavePaint(mMask.getBlowWavePaint());

        //        addView(mMask);
        //        addView(mSolid);
        mMask = ImageView(context)
//        mMask?.setBackgroundColor(Color.DKGRAY)
//        mMask?.setBackgroundResource( R.mipmap.progress_bg)
        val drawable = ResourcesCompat.getDrawable(resources, R.mipmap.progress_bg,null)
        mBackGroundDrawable = ClipDrawable(drawable,Gravity.TOP,ClipDrawable.VERTICAL)
        mMask?.background = mBackGroundDrawable
//        mMask?.setImageResource(android.R.color.transparent)

        addView(mMask,LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT))
        alpha = 0.8f

        setProgress(mProgress)
    }

    fun setProgress(progress: Int) {
        this.mProgress = if (progress > 1000) 1000 else progress
        computeMaskToBottom()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            computeMaskToBottom()
        }
    }

    private fun computeMaskToBottom() {
        val rate = (1f - mProgress / 1000f)

//        mMask!!.postInvalidate()

//        mMaskToBottom = (height * rate).toInt()
//        val params = mMask!!.layoutParams
//        if (params != null) {
//            (params as LinearLayout.LayoutParams).bottomMargin = mMaskToBottom
//        }
//        mMask!!.layoutParams = params


        //裁剪背景图
        val level =  (10000 * (1f - rate)).toInt()

        Log.e("tag","rate = ${(1f - rate) * 100}%,level = $level")
//        mBackGroundDrawable?.level = level
//        mMask?.background = mBackGroundDrawable

        mMask?.background?.level = level
        mMask?.background?.invalidateSelf()


    }

    public override fun onSaveInstanceState(): Parcelable? {
        // Force our ancestor class to save its state
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.progress = mProgress
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        setProgress(ss.progress)
    }

    private class SavedState : View.BaseSavedState {
        internal var progress: Int = 0

        /**
         * Constructor called from [android.widget.ProgressBar.onSaveInstanceState]
         */
        internal constructor(superState: Parcelable) : super(superState) {}

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(`in`: Parcel) : super(`in`) {
            progress = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(progress)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }


}