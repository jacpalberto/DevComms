package com.jacpalberto.devcomms.sponsorDetail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.transition.Fade
import android.view.View
import android.widget.TextView
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.shadow)
        extractSponsorDetail()
        sponsorDetail?.let { showSponsor(it) }
        shadowedSponsorDetailLayout.setOnClickListener { onBackPressed() }
        scrollSponsorDetailLayout.setOnClickListener { onBackPressed() }
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
                .resize(500, 500)
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
        val url = object : ClickableSpan() {
            override fun onClick(widget: View) {
                function()
            }

            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = ContextCompat.getColor(this@SponsorDetailActivity,
                        R.color.detailHyperlinkText)
            }
        }
        val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        val spannableString = SpannableString("More info at: " + text)
        spannableString.setSpan(url, 14, spannableString.length, flag)
        sponsorWeb.movementMethod = LinkMovementMethod.getInstance()
        sponsorWeb.setText(spannableString, TextView.BufferType.SPANNABLE)

    }

    private fun setVisibilities(sponsor: Sponsor) {
        sponsorName.visibility = if (sponsor.name.isNullOrEmpty()) View.GONE else View.VISIBLE
        sponsorBrief.visibility = if (sponsor.brief.isNullOrEmpty()) View.GONE else View.VISIBLE
        sponsorWeb.visibility = if (sponsor.web.isNullOrEmpty()) View.GONE else View.VISIBLE
        contactUsButton.visibility = if (sponsor.contact.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun extractSponsorDetail() {
        sponsorDetail = intent.getParcelableExtra(SponsorDetailActivity.SPONSOR)
    }
}