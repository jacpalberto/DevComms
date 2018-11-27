package com.jacpalberto.devcomms.eventDetail


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.SpeakerDetail
import com.jacpalberto.devcomms.utils.BorderedCircleTransform
import com.jacpalberto.devcomms.utils.startWebIntent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.koin.android.ext.android.inject

class EventDetailActivity : AppCompatActivity() {
    companion object {
        private const val EVENT = "EVENT"

        fun newIntent(context: Context, event: DevCommsEvent): Intent {
            return Intent(context, EventDetailActivity::class.java).apply {
                putExtra(EVENT, event)
            }
        }
    }

    private val viewModel: EventDetailViewModel by inject()
    private var devCommsEvent: DevCommsEvent? = null
    private var speakerDetail: SpeakerDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        devCommsEvent = intent.getParcelableExtra(EVENT)
        init()
    }

    private fun init() {
        initSpeakerDetail()
        initListeners()
        initToolbar()
        devCommsEvent?.let { viewModel.setEvent(it) }
        viewModel.getEvent()?.observe(this, Observer { showEventDetail(it) })
    }

    private fun initSpeakerDetail() {
        if (devCommsEvent == null) return
        speakerDetail = devCommsEvent?.speakerDetail
    }

    private fun initListeners() {

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

    private fun showEventDetail(event: DevCommsEvent?) {
        event?.let {
            eventTitle.text = it.title
            eventRoomDate.text = getString(R.string.event_room_plus_date,
                    it.room, it.startDateString + " " + it.startTimeString)
            showEventDescription(event)
            showSpeaker(event)
        }
    }

    private fun showSpeaker(event: DevCommsEvent) {
        val speaker = event.speakerDetail
        if (speaker?.first_name.isNullOrEmpty())
            return
        applySpeakerVisibility(speaker)
        showSpeakerPhoto(speaker?.photo_url)
        speakerTitle.text = getString(R.string.complete_name, speaker?.first_name, speaker?.last_name)
        speakerCountry.text = "${speaker?.country}"
        speakerDescriptionTextView.text = speaker?.bio
        speakerGithub.setOnClickListener { startWebIntent("www.github.com/${speaker?.github}") }
        speakerTwitter.setOnClickListener { startWebIntent("www.github.com/${speaker?.twitter}") }
    }

    private fun applySpeakerVisibility(speaker: SpeakerDetail?) {
        speakerGroup.visibility = View.VISIBLE
        if (!speaker?.country.isNullOrBlank()) speakerCountry.visibility = View.VISIBLE else View.GONE
        if (!speaker?.twitter.isNullOrEmpty()) speakerTwitter.visibility = View.VISIBLE else View.GONE
        if (!speaker?.github.isNullOrEmpty()) speakerGithub.visibility = View.VISIBLE else View.GONE
    }

    private fun showSpeakerPhoto(speakerPhotoUrl: String?) {
        val url = if (speakerPhotoUrl.isNullOrEmpty()) "SpeakerPlaceHolder" else speakerPhotoUrl
        Picasso.get()
                .load(url)
                .transform(BorderedCircleTransform())
                .error(R.drawable.placeholder_speaker_big)
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
}
