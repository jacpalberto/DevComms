package com.jacpalberto.devcomms.sponsors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.SponsorsAdapter
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.sponsorDetail.SponsorDetailActivity
import kotlinx.android.synthetic.main.activity_sponsors.*

class SponsorsActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, SponsorsActivity::class.java)
    }

    //TODO: inject viewModel
    private var viewModel: SponsorsViewModel? = null

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsors)
        viewModel = ViewModelProviders.of(this).get(SponsorsViewModel::class.java)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.onDestroy()
    }

    private fun init() {
        sponsorListSwipe.setOnRefreshListener { viewModel?.refreshSponsors() }
        initToolbar()
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        showProgress()
        viewModel?.fetchSponsors()?.observe(this, Observer { handleSponsors(it) })
    }

    private fun showProgress() {
        if (!sponsorListSwipe.isRefreshing) progressBar.visibility = View.VISIBLE
    }

    private var currentSponsorList: List<Sponsor>? = null

    private fun handleSponsors(response: DataResponse<List<Sponsor>>?) {
        if (response == null) return
        dismissProgress()
        if (response.isStatusFailedOrError()) {
            showToast(getString(R.string.connectivity_error))
            val adapter = SponsorsAdapter(emptyList(), onSponsorsLongClick, onSponsorsClick)
            sponsorsRecyclerView.adapter = adapter
        } else if (response.status == DataState.SUCCESS) {
            if (currentSponsorList == null || response.data != currentSponsorList) {
                currentSponsorList = response.data
                showSponsors(response.data)
            }
        }
    }

    private fun dismissProgress() {
        progressBar.visibility = View.GONE
        if (sponsorListSwipe.isRefreshing)
            sponsorListSwipe.isRefreshing = false
    }

    private fun showSponsors(sponsorList: List<Sponsor>) {
        sponsorList.let {
            val adapter = SponsorsAdapter(it, onSponsorsLongClick, onSponsorsClick)
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

    private fun getSharedElementsPairList(view: View): Array<androidx.core.util.Pair<View, String>> {
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
