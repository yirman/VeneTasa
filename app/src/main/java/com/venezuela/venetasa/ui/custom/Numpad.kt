package com.venezuela.venetasa.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.venezuela.venetasa.R


class Numpad(context: Context, attrs: AttributeSet): GridLayout(context, attrs) {

    var selectedEditText: CurrencyEditor? = null
    var selectedEditTextRes: Int

    private val keys = arrayOf("7", "8", "9", "4", "5", "6", "1", "2", "3", "C", "0", "<")

    init {
        keys.forEach { element ->
            addView(MaterialButton(context).apply {
                val outValue = TypedValue()
                context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    outValue,
                    true
                )
                cornerRadius = 600
//                setBackgroundResource(outValue.resourceId)

                setBackgroundColor(ContextCompat.getColor(context, R.color.main_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                text = element
                textSize = 22F
                setOnClickListener {
                    selectedEditText?.let {
                        when(text){
                            "C" -> {
                                it.setText("0.00")
                                it.setSelection(it.text!!.length)
                            }
                            "<" -> {
                                if(it.selectionStart > 0) {
                                    val char = it.text.toString()[it.selectionStart - 1]
                                    if(char != ',' && char != '.')
                                        it.text!!.delete(it.selectionStart - 1, it.selectionStart)
                                    else
                                        it.text!!.delete(it.selectionStart - 2, it.selectionStart)
                                }
//                                it.setText(it.text.toString().dropLast(1))
                            }
                            else -> {
                                it.text!!.insert(it.selectionStart, text)
//                                it.setText(it.text.toString().plus(text))
                            }
                        }
                    }
                }
                val param = LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                )
                param.width = ViewGroup.LayoutParams.WRAP_CONTENT
                param.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams = param
            })
        }
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Numpad,
            0, 0).apply {

            try {
                selectedEditTextRes = getResourceId(R.styleable.Numpad_upperCurrency, -1)
            } finally {
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        selectedEditText = (parent as View).findViewById<CurrencyEditor>(selectedEditTextRes)
    }

}