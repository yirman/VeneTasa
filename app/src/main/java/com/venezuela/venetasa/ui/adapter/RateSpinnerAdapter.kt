package com.venezuela.venetasa.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ahmadrosid.svgloader.SvgLoader
import com.venezuela.venetasa.R
import com.venezuela.venetasa.databinding.ItemRateBinding
import com.venezuela.venetasa.model.Rate

class RateSpinnerAdapter(context: Context, private val values: List<Rate>) : ArrayAdapter<Rate>(context, 0, values) {

    var svgLoader: SvgLoader? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item: Rate = getItem(position)!!
        return convertView?.let { bindData(item, it) }?: bindData(item, ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    private fun bindData(rate: Rate, view: View): View{
        view.findViewById<ImageView>(R.id.icon).setImageResource(rate.getRateDrawable(context))
        view.findViewById<TextView>(R.id.title).text = rate.name
        view.findViewById<TextView>(R.id.value).text = rate.getFormattedRate(context)

        val triangleColor = ContextCompat.getColor(
            context, when (rate.triangle) {
                "▼" -> R.color.red
                else -> R.color.green
            }
        )

        val triangle = view.findViewById<TextView>(R.id.triangle)
        triangle.text = rate.triangle
        triangle.setTextColor(triangleColor)

        if(!rate.nextUpdateIcon.isEmpty()){
            svgLoader?.load(rate.nextUpdateIcon, view.findViewById(R.id.next_update_icon))
        }

        return view
    }

    private fun bindData(itemRate: Rate, itemRateBinding: ItemRateBinding): View{
        itemRateBinding.icon.setImageResource(itemRate.getRateDrawable(itemRateBinding.root.context))
        itemRateBinding.title.text = itemRate.name
        itemRateBinding.value.text = itemRate.getFormattedRate(itemRateBinding.root.context)

        val triangleColor = ContextCompat.getColor(
            context, when (itemRate.triangle) {
                "▼" -> R.color.red
                else -> R.color.green
            }
        )

        val triangle = itemRateBinding.triangle
        triangle.text = itemRate.triangle
        triangle.setTextColor(triangleColor)

        if(!itemRate.nextUpdateIcon.isEmpty()){
            svgLoader?.load(itemRate.nextUpdateIcon, itemRateBinding.nextUpdateIcon)
        }

        return itemRateBinding.root
    }

}