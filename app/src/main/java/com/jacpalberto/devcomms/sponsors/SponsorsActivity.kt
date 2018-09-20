package com.jacpalberto.devcomms.sponsors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.SponsorsAdapter
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.data.SponsorList
import com.jacpalberto.devcomms.sponsorDetail.SponsorDetailActivity
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

    private val onSponsorsClick = { sponsor: Sponsor, view: View ->
        val transitionIntent = SponsorDetailActivity.newIntent(this, sponsor)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *getSharedElementsPairList(view))
        ActivityCompat.startActivity(this, transitionIntent, options.toBundle())
    }

    private fun getSharedElementsPairList(view: View): Array<android.support.v4.util.Pair<View, String>> {
        val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
        val statusBar = findViewById<View>(android.R.id.statusBarBackground)
        val sponsorImage = view.findViewById<ImageView>(R.id.sponsorImg)

        val pairList = mutableListOf(
                Pair(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                Pair(sponsorImage as View, "sponsorImg")).apply {
            if (navigationBar != null)
                add(Pair(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))
        }
        return pairList.toTypedArray()
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast!!.show()
    }
}
