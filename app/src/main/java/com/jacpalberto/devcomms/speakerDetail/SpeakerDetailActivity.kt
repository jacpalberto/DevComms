package com.jacpalberto.devcomms.speakerDetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.SpeakerDetail
import com.jacpalberto.devcomms.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_speaker_detail.*

//TODO: add clickable links and verify if it is a valid web page
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSpeakerDetail() {
        with(speakerDetail) {
            if (this == null) return
            showSpeakerPhoto(speakerPhotoUrl)
            speakerTitle.text = speaker
            showCompany(company)
            showWebPage(webPageUrl)
            showGithub(githubUrl)
            showDescription(speakerDescription)
        }
    }

    private fun showCompany(company: String?) {
        if (company.isNullOrEmpty()) return
        companyGroup.visibility = View.VISIBLE
        companyNameTextView.text = company
    }

    private fun showWebPage(webPageUrl: String?) {
        if (webPageUrl.isNullOrEmpty()) return
        wepPageGroup.visibility = View.VISIBLE
        webPageTextView.text = webPageUrl
    }

    private fun showGithub(githubUrl: String?) {
        if (githubUrl.isNullOrEmpty()) return
        githubGroup.visibility = View.VISIBLE
        githubTextView.text = githubUrl
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
