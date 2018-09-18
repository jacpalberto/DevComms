package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.SponsorsAdapter
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList
import com.jacpalberto.devcomms.utils.startWebIntent
import kotlinx.android.synthetic.main.activity_sponsors.*

class SponsorsActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, SponsorsActivity::class.java)
    }

    private var viewModel: SponsorsViewModel? = null
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsors)
        viewModel = ViewModelProviders.of(this).get(SponsorsViewModel::class.java)
        init()
    }

    private fun init() {
        sponsorListSwipe.setOnRefreshListener { viewModel?.refreshSponsors() }
        initToolbar()
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        viewModel?.fetchSponsors()?.observe(this, Observer { handleSponsors(it) })
    }

    private fun handleSponsors(it: SponsorList?) {
        if (it?.sponsorList == null)
            showToast(getString(R.string.connectivity_error))
        if (it?.status == DataState.FAILURE || it?.status == DataState.ERROR) {
            showToast(getString(R.string.connectivity_error))
        } else if (it?.status == DataState.SUCCESS) {
            dismissProgress()
            showSponsors(it)
        }
    }

    private fun dismissProgress() {
        if (sponsorListSwipe.isRefreshing)
            sponsorListSwipe.isRefreshing = false
    }

    private fun showSponsors(sponsorList: SponsorList?) {
        sponsorList?.let {
            val adapter = SponsorsAdapter(it.sponsorList, onSponsorsLongClick, onSponsorsClick)
            sponsorsRecyclerView.adapter = adapter
        }
    }

    private fun initRecyclerView() {
        sponsorsRecyclerView.hasFixedSize()
        sponsorsRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val onSponsorsLongClick = { sponsor: Sponsor ->
        val title: String = if (sponsor.name.isNullOrEmpty()) "An awesome sponsor"
        else sponsor.name ?: "An awesome sponsor"
        showToast(title)
        true
    }

    private val onSponsorsClick = { sponsor: Sponsor ->
        startWebIntent(sponsor.contact)
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast!!.show()
    }
}
