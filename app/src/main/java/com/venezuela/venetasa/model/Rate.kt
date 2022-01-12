package com.venezuela.venetasa.model

import android.content.Context
import android.content.res.Resources
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.venezuela.venetasa.R
import java.text.DecimalFormat
import java.text.Normalizer
import java.text.NumberFormat

@Entity(tableName = "rates")
data class Rate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val rate: Double,
    val triangle: String,
    val nextUpdateIcon: String
){

    companion object{
        val bsFormat: NumberFormat = DecimalFormat("0.00")
    }


    fun getFormattedRate(context: Context): String = context.getString(R.string.rate_value, bsFormat.format(rate))

    fun getRateDrawable(context: Context): Int {
        val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
        var temp = Normalizer.normalize(name, Normalizer.Form.NFD)
            .replace(" ", "_")
            .lowercase()
        temp = REGEX_UNACCENT.replace(temp, "")
        val resources: Resources = context.resources
        return resources.getIdentifier(temp, "drawable", context.packageName)
    }
}