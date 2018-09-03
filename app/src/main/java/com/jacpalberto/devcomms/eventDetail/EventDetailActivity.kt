package com.jacpalberto.devcomms.eventDetail


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        devCommsEvent = intent.getParcelableExtra(EVENT)
        viewModel = ViewModelProviders.of(this).get(EventDetailViewModel::class.java)
        init()
    }

    private fun init() {
        initListeners()
        initToolbar()
        devCommsEvent?.let { viewModel?.setEvent(it) }
        viewModel?.getEvent()?.observe(this, Observer { showEventDetail(it) })
    }

    private fun initListeners() {
        speakerImageView.setOnClickListener { startSpeakerDetail() }
        speakerTitle.setOnClickListener { startSpeakerDetail() }
        speakerDescription.setOnClickListener { startSpeakerDetail() }
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

    }

    private fun showEventDetail(event: DevCommsEvent?) {
        event?.let {
            eventTitle.text = it.title
            eventRoomDate.text = getString(R.string.event_room_plus_date, it.room, it.date)
            if (!it.description.isNullOrEmpty()) {
                descriptionGroup.visibility = View.VISIBLE
                eventDescription.text = event.description
            }
            if (!it.speaker.isNullOrEmpty()) {
                speakerGroup.visibility = View.VISIBLE
                speakerTitle.text = event.speaker
            }
        }
    }
}
