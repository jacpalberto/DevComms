package com.jacpalberto.devcomms.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.DevCommsEventAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.EventListWrapper
import com.jacpalberto.devcomms.eventDetail.EventDetailActivity
import kotlinx.android.synthetic.main.fragment_event.*

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventFragment : Fragment() {
    companion object {
        const val EVENT_LIST = "EVENT_LIST"
        fun newInstance(eventListWrapper: EventListWrapper) = EventFragment().apply {
            arguments = Bundle().apply { putParcelable(EVENT_LIST, eventListWrapper) }
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
            viewModel?.toggleFavorite(event.key, !isFavorite)
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
        val devCommsListEvent = arguments?.getParcelable<EventListWrapper>(EVENT_LIST)
        showEvents(devCommsListEvent?.eventList)
        eventsListSwipe.setOnRefreshListener {
            viewModel?.fetchEvents()
            if (eventsListSwipe.isRefreshing) eventsListSwipe.isRefreshing = false
        }
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
