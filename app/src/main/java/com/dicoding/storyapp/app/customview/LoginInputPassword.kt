package com.dicoding.storyapp.app.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.storyapp.R

class LoginInputPassword: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Enter your Password"

        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                error = if (input.length < 8) {
                    context.getString(R.string.password_error_message)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}