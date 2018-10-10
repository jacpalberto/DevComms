package com.jacpalberto.devcomms.eventDetail


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.SpeakerDetail
import com.jacpalberto.devcomms.speakerDetail.SpeakerDetailActivity
import com.jacpalberto.devcomms.utils.CircleTransform
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
        if (speakerDetail == null || (speakerDetail?.twitter.isNullOrEmpty()
                        && speakerDetail?.github.isNullOrEmpty()
                        && speakerDetail?.bio.isNullOrEmpty()))
            return

        speakerDetail?.let {
            val transitionIntent = SpeakerDetailActivity.newIntent(this, it)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, *getSharedElementsPairList())
            ActivityCompat.startActivity(this, transitionIntent, options.toBundle())
        }
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
        speakerGroup.visibility = View.VISIBLE
        speakerTitle.text = getString(R.string.complete_name, speaker?.first_name, speaker?.last_name)
        speakerDescriptionTextView.text = speaker?.bio
        showSpeakerPhoto(speaker?.photo_url)
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

    private fun getSharedElementsPairList(): Array<androidx.core.util.Pair<View, String>> {
        val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
        val statusBar = findViewById<View>(android.R.id.statusBarBackground)
        val speakerImage = findViewById<ImageView>(R.id.speakerImageView)
        val speakerTitle = findViewById<TextView>(R.id.speakerTitle)

        val pairList = mutableListOf(
                Pair(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME),
                Pair(toolbar as View, "toolbar"),
                Pair(speakerImage as View, "speakerImage"),
                Pair(speakerTitle as View, "speakerName")).apply {
            if (navigationBar != null)
                add(Pair(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME))
        }
        return pairList.toTypedArray()
    }
}
