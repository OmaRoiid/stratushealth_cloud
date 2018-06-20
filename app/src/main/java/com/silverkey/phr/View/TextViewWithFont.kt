package com.silverkey.Views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.silverkey.phr.Helpers.PhrApplication
import com.silverkey.phr.R

class TextViewWithFont : TextView {

    private val defaultDimension = 0
    private val TYPE_BOLD = 1
    private val TYPE_ITALIC = 2
    private val FONT_COCON = 1
    private var fontType: Int = 0
    private var fontName: Int = 0

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.font, defStyle, 0)
        fontName = a.getInt(R.styleable.font_name, defaultDimension)
        fontType = a.getInt(R.styleable.font_type, defaultDimension)
        a.recycle()
        val application = context.applicationContext as PhrApplication
        //if (fontName == FONT_COCON) {
        setFontType(application.coconFont!!)
        // }
    }

    private fun setFontType(font: Typeface) {
        if (fontType == TYPE_BOLD) {
            setTypeface(font, Typeface.BOLD)
        } else if (fontType == TYPE_ITALIC) {
            setTypeface(font, Typeface.ITALIC)
        } else {
            typeface = font
        }
    }

}