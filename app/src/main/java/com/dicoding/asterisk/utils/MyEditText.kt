package com.dicoding.asterisk.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.asterisk.R

class MyEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs), View.OnTouchListener {
    private var isError = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input = s.toString()
                when(inputType) {
                    EMAIL -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                            error = context.getString(R.string.error_email)
                            isError = true
                        } else {
                            isError = false
                        }
                    }
                    PASSWORD -> {
                        isError = if (input.length < 8) {
                            setError(context.getString(R.string.error_password), null)
                            true
                        } else {
                            false
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Nothing
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

    companion object {
        const val EMAIL = 0x00000021
        const val PASSWORD = 0x00000081
    }
}