package com.jacpalberto.devcomms.eventDetail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.SpeakerDetail
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.speakerDetail.SpeakerDetailActivity
import com.jacpalberto.devcomms.utils.CircleTransform
import com.jacpalberto.devcomms.utils.showToast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetailActivity : AppCompatActivity() {
    companion object {
        private const val EVENT = "EVENT"

        fun newIntent(context: Context, event: DevCommsEvent): Intent {
            return Intent(context, EventDetailActivity::class.java).apply {
                putExtra(EVENT, event)
            }
        }
    }

    private var devCommsEvent: DevCommsEvent? = null
    private var viewModel: EventDetailViewModel? = null
    private var speakerDetail: SpeakerDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        devCommsEvent = intent.getParcelableExtra(EVENT)
        viewModel = ViewModelProviders.of(this).get(EventDetailViewModel::class.java)
        init()
    }

    private fun init() {
        initSpeakerDetail()
        initListeners()
        initToolbar()
        devCommsEvent?.let { viewModel?.setEvent(it) }
        viewModel?.getEvent()?.observe(this, Observer { showEventDetail(it) })
    }

    private fun initSpeakerDetail() {
        with(devCommsEvent) {
            if (this == null) return
            speakerDetail = SpeakerDetail(speaker, speakerDescription, speakerPhotoUrl, company, githubUrl, webPageUrl)
        }
    }

    private fun initListeners() {
        speakerImageView.setOnClickListener { startSpeakerDetail() }
        speakerTitle.setOnClickListener { startSpeakerDetail() }
        speakerDescriptionTextView.setOnClickListener { startSpeakerDetail() }
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

    private fun startSpeakerDetail() {
        if (speakerDetail == null || (speakerDetail?.company.isNullOrEmpty()
                        && speakerDetail?.githubUrl.isNullOrEmpty()
                        && speakerDetail?.speakerDescription.isNullOrEmpty()
                        && speakerDetail?.webPageUrl.isNullOrEmpty()))
            return
        startActivity(speakerDetail?.let { SpeakerDetailActivity.newIntent(this, it) })
    }

    private fun showEventDetail(event: DevCommsEvent?) {
        event?.let {
            eventTitle.text = it.title
            eventRoomDate.text = getString(R.string.event_room_plus_date, it.room, it.date)
            showEventDescription(event)
            showSpeaker(event)
        }
    }

    private fun showSpeaker(event: DevCommsEvent) {
        if (event.speaker.isNullOrEmpty())
            return
        speakerGroup.visibility = View.VISIBLE
        speakerTitle.text = event.speaker
        speakerDescriptionTextView.text = event.speakerDescription
        showSpeakerPhoto(event.speakerPhotoUrl)
    }

    private fun showSpeakerPhoto(speakerPhotoUrl: String?) {
        val url = if (speakerPhotoUrl.isNullOrEmpty()) "SpeakerPlaceHolder" else speakerPhotoUrl
        Picasso.get()
                .load(url)
                .transform(CircleTransform())
                .error(R.drawable.placeholder_speaker)
                .resize(300, 300)
                .centerCrop()
                .into(speakerImageView)
    }

    private fun showEventDescription(event: DevCommsEvent) {
        if (event.description.isNullOrEmpty())
            return
        descriptionGroup.visibility = View.VISIBLE
        eventDescription.text = event.description
    }

    private val onSponsorsClick = { detail: SpeakerDetail ->
        try {
            val webPageUrl = detail.webPageUrl
            val url: String = if (webPageUrl == null || webPageUrl.isEmpty()) ""
            else if (webPageUrl.contains("http://") || webPageUrl.contains("https://")) {
                webPageUrl
            } else "http://$webPageUrl"
            val webPage = Uri.parse(url)
            startActivity(Intent(Intent.ACTION_VIEW, webPage))
        } catch (e: ActivityNotFoundException) {
            showToast(getString(R.string.url_intent_error))
            e.printStackTrace()
        }
    }
}
