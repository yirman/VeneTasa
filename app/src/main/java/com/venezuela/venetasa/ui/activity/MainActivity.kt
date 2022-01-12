package com.venezuela.venetasa.ui.activity

import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ahmadrosid.svgloader.SvgLoader
import com.google.android.material.snackbar.Snackbar
import com.venezuela.venetasa.databinding.ActivityMainBinding
import com.venezuela.venetasa.model.Rate
import com.venezuela.venetasa.ui.MainViewModel
import com.venezuela.venetasa.ui.adapter.RateAdapter
import com.venezuela.venetasa.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RateAdapter.OnRateClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RateAdapter
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var rates: List<Rate>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mainViewModel.queryRates().observe(this, {
            rates = it
            setupRecyclerView(rates)
        })
        mainViewModel.getNetworkCallStatus().observe(this, {
            handleNetworkStatus(it)
        })
        mainViewModel.fetchRates()
    }

    private fun handleNetworkStatus(status: Resource.Status){
        when (status) {
            Resource.Status.SUCCESS -> {
                binding.swipeRefresh.isRefreshing = false
            }
            Resource.Status.ERROR -> {
                binding.swipeRefresh.isRefreshing = false
            }

            Resource.Status.LOADING -> {
                binding.swipeRefresh.isRefreshing = true
            }
        }
    }

    private fun setupRecyclerView(rates: List<Rate>) {
        adapter = RateAdapter(rates, this)
        adapter.svgLoader = SvgLoader.pluck().with(this)
//        binding.rates.layoutManager = LinearLayoutManager(this)
        binding.rates.layoutManager = GridLayoutManager(this,2)
        binding.rates.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.fetchRates()
        }
    }

    private fun showSnackBar(msg: String){
        val snack = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snack.show()
    }

    override fun onClickRate(id: Int) {
        val intent = Intent(this, ConverterActivity::class.java)
        intent.putExtra(ConverterActivity.EXTRA_RATE_ID, id)
        startActivity(intent)
    }

    override fun onLongClickRate(id: Int) {
        val rate = rates[rates.indexOfFirst { it.id == id }]
        val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(rate.toString(), "Cotización de " + rate.name + " en " + rate.getFormattedRate(this) + ".")
        clipboard.setPrimaryClip(clip)
        showSnackBar("Cotización de " + rate.name + " copiado al portapapeles.")
    }
}