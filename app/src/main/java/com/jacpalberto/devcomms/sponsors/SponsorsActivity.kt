package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList
import com.jacpalberto.devcomms.extensions.showToast
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
            showSponsors(it)
        }
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
            setHomeAsUpIndicator(android.R.drawable.ic_dialog_map)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private val onSponsorsLongClick = { sponsor: Sponsor ->
        toast?.cancel()
        toast = Toast.makeText(this, sponsor.title, Toast.LENGTH_SHORT)
        toast!!.show()
        true
    }
    private val onSponsorsClick = { sponsor: Sponsor ->
        try {
            val webPage = Uri.parse("http://" + sponsor.webPageUrl)
            startActivity(Intent(Intent.ACTION_VIEW, webPage))
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.url_intent_error))
            e.printStackTrace()
        }
    }
}
