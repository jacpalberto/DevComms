package com.jacpalberto.devcomms.sponsorDetail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.utils.startWebIntent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sponsor_detail.*

class SponsorDetailActivity : AppCompatActivity() {
    companion object {
        private const val SPONSOR = "SPONSOR"

        fun newIntent(context: Context, sponsor: Sponsor): Intent {
            return Intent(context, SponsorDetailActivity::class.java).apply {
                putExtra(SPONSOR, sponsor)
            }
        }
    }

    private var sponsorDetail: Sponsor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsor_detail)
        init()
    }

    override fun onBackPressed() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        super.onBackPressed()
    }

    private fun init() {
        extractSponsorDetail()
        sponsorDetail?.let { showSponsor(it) }
        shadowedSponsorDetailLayout.setOnClickListener { onBackPressed() }
        scrollSponsorDetailLayout.setOnClickListener { onBackPressed() }
        setupTransitionConfig()
    }

    private fun setupTransitionConfig() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.shadow)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(R.id.toolbar, true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)

            window.enterTransition = fade
            window.exitTransition = fade
        }
    }

    private fun showSponsor(sponsor: Sponsor) {
        Picasso.get()
                .load(if (sponsor.logo_url.isNullOrEmpty()) "placeholder" else sponsor.logo_url)
                .error(R.drawable.logo_community)
                .resize(800, 800)
                .centerInside()
                .into(sponsorLogo)

        setVisibilities(sponsor)
        sponsorName.text = sponsor.name
        sponsorBrief.text = sponsor.brief
        showSponsorWeb(sponsor.web) { startWebIntent(sponsor.web) }
        contactUsButton.setOnClickListener { startWebIntent(sponsor.contact) }
    }

    private fun showSponsorWeb(text: String?, function: () -> Unit) {
        if (text.isNullOrEmpty()) return
        webButton.setOnClickListener { function() }
    }

    private fun setVisibilities(sponsor: Sponsor) {
        sponsorName.visibility = if (sponsor.name.isNullOrEmpty()) View.GONE else View.VISIBLE
        sponsorBrief.visibility = if (sponsor.brief.isNullOrEmpty()) View.GONE else View.VISIBLE
        webButton.visibility = if (sponsor.web.isNullOrEmpty()) View.GONE else View.VISIBLE
        contactUsButton.visibility = if (sponsor.contact.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun extractSponsorDetail() {
        sponsorDetail = intent.getParcelableExtra(SponsorDetailActivity.SPONSOR)
    }
}
