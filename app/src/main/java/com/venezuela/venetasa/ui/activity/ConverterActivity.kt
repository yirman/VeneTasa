package com.venezuela.venetasa.ui.activity


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ahmadrosid.svgloader.SvgLoader
import com.venezuela.venetasa.R
import com.venezuela.venetasa.databinding.ActivityConverterBinding
import com.venezuela.venetasa.model.Rate
import com.venezuela.venetasa.ui.MainViewModel
import com.venezuela.venetasa.ui.adapter.RateAdapter
import com.venezuela.venetasa.ui.adapter.RateSpinnerAdapter
import com.venezuela.venetasa.ui.custom.CurrencyEditor
import com.venezuela.venetasa.utils.CurrencyTextWatcher
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConverterActivity : AppCompatActivity(), RateAdapter.OnRateClickListener, CurrencyTextWatcher.CurrencyListener {

    private lateinit var binding: ActivityConverterBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var selectedRate: Rate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.converter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        mainViewModel.queryRates().observe(this, {
            setupSpinner(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSpinner(rates: List<Rate>){
        val rateSpinnerAdapter = RateSpinnerAdapter(this, rates)
        rateSpinnerAdapter.svgLoader = SvgLoader.pluck().with(this)
        binding.rateSpinner.adapter = rateSpinnerAdapter
        val id = intent.extras?.getInt(EXTRA_RATE_ID, -1)
        binding.rateSpinner.setSelection((rates.indexOfFirst { it.id == id }))
        binding.rateSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedRate = rates[position]
                binding.numpad.selectedEditText?.apply{
                    setText(text.toString())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })
    }

    private fun initViews(){
        //binding.editText.keyListener = null
        binding.dollars.showSoftInputOnFocus = false
        binding.dollars.addTextChangedListener(CurrencyTextWatcher(binding.dollars, this@ConverterActivity))
        binding.bolivares.showSoftInputOnFocus = false
        binding.bolivares.addTextChangedListener(CurrencyTextWatcher(binding.bolivares, this@ConverterActivity))
        binding.dollars.setOnTouchListener { v, event ->
            binding.numpad.selectedEditText = binding.dollars
            v?.onTouchEvent(event) ?: true
        }
        binding.bolivares.setOnTouchListener { v, event ->
            binding.numpad.selectedEditText = binding.bolivares
            v?.onTouchEvent(event) ?: true
        }
    }

    override fun onClickRate(id: Int) {
        Log.e("", id.toString())
    }

    override fun onLongClickRate(id: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        const val EXTRA_RATE_ID = "EXTRA_RATE_ID"
    }

    override fun onCurrencyFormatted(value: Double, currencyType: CurrencyEditor.CurrencyType) {
        when{
            currencyType == CurrencyEditor.CurrencyType.usd ->
                if(binding.numpad.selectedEditText?.equals(binding.dollars) == true)
                    binding.bolivares.setText(CurrencyEditor.CurrencyType.bss.name.uppercase()
                        .plus(" ")
                        .plus(String.format("%.2f", selectedRate.rate * value))
                    )
            currencyType == CurrencyEditor.CurrencyType.bss ->
                if(binding.numpad.selectedEditText?.equals(binding.bolivares) == true)
                    binding.dollars.setText(CurrencyEditor.CurrencyType.usd.name.uppercase()
                        .plus(" ")
                        .plus(String.format("%.2f", value / selectedRate.rate))
                    )
        }
    }
}