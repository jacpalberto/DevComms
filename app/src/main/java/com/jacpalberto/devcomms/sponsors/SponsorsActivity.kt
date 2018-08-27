package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.Sponsor
import kotlinx.android.synthetic.main.activity_sponsors.*

class SponsorsActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, SponsorsActivity::class.java)
    }

    private var viewModel: SponsorsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsors)
        viewModel = ViewModelProviders.of(this).get(SponsorsViewModel::class.java)
        init()
    }

    private fun init() {
        initToolbar()
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        viewModel?.fetchSponsors()?.observe(this, Observer { handleSponsors(it) })
    }

    private fun handleSponsors(it: List<Sponsor?>?) {
        Log.d(this::class.java.name, it?.toString() + "hola")
    }

    private fun initRecyclerView() {
        sponsorsRecyclerView.hasFixedSize()
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        sponsorsRecyclerView.layoutManager = GridLayoutManager(this, if (isPortraitScreen) 2 else 4)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_dialog_map)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
