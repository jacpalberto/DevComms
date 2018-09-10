package com.jacpalberto.devcomms.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.DevCommsEventAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.DevCommsEventList
import com.jacpalberto.devcomms.eventDetail.EventDetailActivity
import kotlinx.android.synthetic.main.fragment_event.*
import android.support.v7.widget.SimpleItemAnimator

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventFragment : Fragment() {
    companion object {
        const val EVENT_LIST = "EVENT_LIST"
        fun newInstance(eventList: DevCommsEventList) = EventFragment().apply {
            arguments = Bundle().apply { putParcelable(EVENT_LIST, eventList) }
        }
    }

    private var viewModel: EventsViewModel? = null
    private lateinit var adapter: DevCommsEventAdapter

    private val onEventClick = { event: DevCommsEvent ->
        startActivity(EventDetailActivity.newIntent(activity as Context, event))
    }

    private val onFavoriteClick = { position: Int, event: DevCommsEvent ->
        if (viewModel != null) {
            val isFavorite = event.isFavorite ?: false
            viewModel?.toggleFavorite(event.key ?: 0, !isFavorite)
            adapter.updateFavoriteStatus(position, !isFavorite)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(EventsViewModel::class.java) }
        init()
    }

    private fun init() {
        initRecycler()
        val devCommsListEvent = arguments?.getParcelable<DevCommsEventList>(EVENT_LIST)
        showEvents(devCommsListEvent?.eventList)
    }

    private fun showEvents(it: List<DevCommsEvent>?) {
        it?.let { events ->
            adapter = DevCommsEventAdapter(events, onEventClick, onFavoriteClick)
            eventsRecycler.adapter = adapter
        }
    }

    private fun initRecycler() {
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        eventsRecycler.layoutManager = GridLayoutManager(activity, if (isPortraitScreen) 1 else 2)
        (eventsRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}
