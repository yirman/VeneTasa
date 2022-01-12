package com.venezuela.venetasa.ui.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText
import com.venezuela.venetasa.R

class CurrencyEditor(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs),
    android.view.ActionMode.Callback {

    var currencyType: CurrencyType

    init{

        setTextIsSelectable(false)
        customSelectionActionModeCallback = this
        customInsertionActionModeCallback = this
        isLongClickable = false

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurrencyEditor,
            0, 0).apply {

            try {
                currencyType = CurrencyType.fromId(getInt(R.styleable.CurrencyEditor_currency_type, CurrencyType.usd.id))
            } finally {
                recycle()
            }

            setText(currencyType.name.uppercase()
                .plus(" ")
                .plus(text.toString()))
        }
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        currencyType?.let {
            if(selStart < currencyType.name.plus(" ").length)
                setSelection(currencyType.name.plus(" ").length)
        }
    }

    override fun getSelectionStart(): Int {
        for (element in Thread.currentThread().stackTrace) {
            Log.d("CurrencyEditor", element.methodName)
            if (element.methodName == "canPaste") {
                return -1
            }
        }
        return super.getSelectionStart()
    }

    override fun isSuggestionsEnabled(): Boolean {
        return false
    }

    enum class CurrencyType(var id: Int) {
        bss(1), usd(0);

        companion object {
            fun fromId(id: Int): CurrencyType {
                for (f in values()) {
                    if (f.id == id) return f
                }
                throw IllegalArgumentException()
            }
        }
    }

    override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean { return false }

    override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean { return false }

    override fun onActionItemClicked(mode: android.view.ActionMode?, item: MenuItem?): Boolean { return false }

    override fun onDestroyActionMode(mode: android.view.ActionMode?) { }

}