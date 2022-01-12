package com.venezuela.venetasa.utils

import android.text.Editable
import android.text.TextWatcher
import com.venezuela.venetasa.ui.custom.CurrencyEditor
import java.text.DecimalFormat


class CurrencyTextWatcher(private val currencyEditor: CurrencyEditor, private val currencyListener: CurrencyListener, pattern: String? = DEFAULT_PATTERN) : TextWatcher {

    private val decimalFormat: DecimalFormat = DecimalFormat(pattern)

    override fun afterTextChanged(s: Editable) {

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        currencyEditor.removeTextChangedListener(this)
        val cleanString: String = s.toString().replace("[$,.]", "")
            .replace(currencyEditor.currencyType.name, "")
        val parsed = cleanString.replace("[^\\d]".toRegex(), "").toDouble()
        val formatted: String = currencyEditor.currencyType.name.uppercase()
            .plus(" ")
            .plus(decimalFormat.format(parsed / 100))
        var nextSelection = currencyEditor.selectionStart
        when {
            nextSelection > formatted.length -> nextSelection = formatted.length
            cleanString.length < formatted.length -> nextSelection+=1
            cleanString.length > formatted.length -> nextSelection-=1
        }
        currencyEditor.setText(formatted)
        currencyEditor.setSelection(nextSelection)
        currencyEditor.addTextChangedListener(this)
        currencyListener.onCurrencyFormatted(parsed / 100, currencyEditor.currencyType)
    }

    interface CurrencyListener{
        fun onCurrencyFormatted(value: Double, currencyType: CurrencyEditor.CurrencyType)
    }

    companion object{
        const val DEFAULT_PATTERN = "#,##0.00"
    }
}