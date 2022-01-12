package com.venezuela.venetasa.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ahmadrosid.svgloader.SvgLoader
import com.venezuela.venetasa.R
import com.venezuela.venetasa.databinding.ItemRateBinding
import com.venezuela.venetasa.model.Rate


class RateAdapter(private var rates: List<Rate>, private var onRateClickListener: OnRateClickListener): RecyclerView.Adapter<RateAdapter.RateViewHolder>() {

    var svgLoader: SvgLoader? = null

    fun setRates(rates: List<Rate>){
        this.rates = rates
        notifyDataSetChanged()
    }

    inner class RateViewHolder(private val itemRateBinding: ItemRateBinding): RecyclerView.ViewHolder(itemRateBinding.root){


        fun bind(rate: Rate){

            itemRateBinding.icon.setImageResource(rate.getRateDrawable(itemRateBinding.root.context))
            itemRateBinding.title.text = rate.name
            itemRateBinding.value.text = rate.getFormattedRate(itemRateBinding.root.context)

            val triangleColor = ContextCompat.getColor(
                itemRateBinding.root.context, when (rate.triangle) {
                    "▼" -> R.color.red
                    else -> R.color.green
                }
            )
            itemRateBinding.triangle.text = rate.triangle
            itemRateBinding.triangle.setTextColor(triangleColor)

            if(!rate.nextUpdateIcon.isEmpty()){
                svgLoader?.load(rate.nextUpdateIcon, itemRateBinding.nextUpdateIcon)
            }

            itemRateBinding.root.setOnClickListener {
                onRateClickListener.let {
                    onRateClickListener.onClickRate(rate.id)
                }
            }

            itemRateBinding.root.setOnLongClickListener {
                onRateClickListener.let {
                    onRateClickListener.onLongClickRate(rate.id)
                }
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val binding = ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) = holder.bind(rates[position])

    override fun getItemCount(): Int = rates.size

    interface OnRateClickListener{
        fun onClickRate(id: Int)
        fun onLongClickRate(id: Int)
    }
}