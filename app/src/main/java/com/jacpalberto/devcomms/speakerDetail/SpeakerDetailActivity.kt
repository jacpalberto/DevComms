package com.jacpalberto.devcomms.speakerDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.SpeakerDetail
import com.jacpalberto.devcomms.utils.CircleTransform
import com.jacpalberto.devcomms.utils.applyClickableSpan
import com.jacpalberto.devcomms.utils.startWebIntent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_speaker_detail.*
import android.os.Build
import android.transition.Fade


class SpeakerDetailActivity : AppCompatActivity() {
    companion object {
        private const val DETAIL = "DETAIL"

        fun newIntent(context: Context, event: SpeakerDetail): Intent {
            return Intent(context, SpeakerDetailActivity::class.java).apply {
                putExtra(DETAIL, event)
            }
        }
    }

    private var speakerDetail: SpeakerDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker_detail)
        speakerDetail = intent.getParcelableExtra(SpeakerDetailActivity.DETAIL)
        init()
    }

    private fun init() {
        initToolbar()
        showSpeakerDetail()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(R.id.toolbar, true)
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)

            window.enterTransition = fade
            window.exitTransition = fade
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSpeakerDetail() {
        with(speakerDetail) {
            if (this == null) return
            speakerTitle.text = getString(R.string.complete_name, first_name, last_name)
            showSpeakerPhoto(photo_url)
            showCountry(country)
            showTwitter(twitter)
            showGithub(github)
            showDescription(bio)
        }
    }

    private fun showCountry(country: String?) {
        if (country.isNullOrEmpty()) return
        companyGroup.visibility = View.VISIBLE
        countryTextView.text = country
    }

    private fun showTwitter(twitter: String?) {
        if (twitter.isNullOrEmpty()) return
        companyGroup.visibility = View.VISIBLE
        twitterTextView.applyClickableSpan(applicationContext, twitter) {
            startWebIntent("www.twitter.com/$twitter")
        }
    }

    private fun showGithub(githubUrl: String?) {
        if (githubUrl.isNullOrEmpty()) return
        githubGroup.visibility = View.VISIBLE
        githubTextView.applyClickableSpan(applicationContext, githubUrl) {
            startWebIntent("www.github.com/$githubUrl")
        }
    }

    private fun showDescription(speakerDescription: String?) {
        if (speakerDescription.isNullOrEmpty()) return
        descriptionGroup.visibility = View.VISIBLE
        speakerDescriptionTextView.text = speakerDescription
    }

    private fun showSpeakerPhoto(speakerPhotoUrl: String?) {
        val url = if (speakerPhotoUrl.isNullOrEmpty()) "SpeakerPlaceHolder" else speakerPhotoUrl
        Picasso.get()
                .load(url)
                .transform(CircleTransform())
                .error(R.drawable.placeholder_speaker_big)
                .centerCrop()
                .resize(800, 800)
                .into(speakerImageView)
    }
}
