package com.jacpalberto.devcomms.agenda

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.AgendaAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.eventDetail.EventDetailActivity
import com.jacpalberto.devcomms.events.EventsViewModel
import kotlinx.android.synthetic.main.fragment_event.*
import org.koin.android.ext.android.inject

/**
 * Created by Alberto Carrillo on 9/7/18.
 */
class AgendaFragment : Fragment() {
    companion object {
        const val EVENT_LIST = "EVENT_LIST"
        fun newInstance(eventList: ArrayList<DevCommsEvent>) = AgendaFragment().apply {
            arguments = Bundle().apply { putParcelableArrayList(EVENT_LIST, eventList) }
        }
    }

    private val viewModel: EventsViewModel by inject()
    private lateinit var adapter: AgendaAdapter
    private val onEmptyAgenda = {
        viewModel.updateAgenda()
    }

    private val onEventClick = { event: DevCommsEvent ->
        startActivity(EventDetailActivity.newIntent(activity as Context, event))
    }

    private val onFavoriteClick = { position: Int, event: DevCommsEvent ->
        val isFavorite = event.isFavorite ?: false
        viewModel.toggleFavorite(event.key, !isFavorite)
        adapter.removeEventFromFavorites(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initRecycler()
        disableSwipeLayout()
        val devCommsListEvent: ArrayList<DevCommsEvent>? = arguments?.getParcelableArrayList(EVENT_LIST)
        showEvents(devCommsListEvent)
    }

    private fun disableSwipeLayout() {
        eventsListSwipe.isRefreshing = false
        eventsListSwipe.isEnabled = false
    }

    private fun showEvents(it: List<DevCommsEvent>?) {
        it?.let { events ->
            adapter = AgendaAdapter(events.toMutableList(), onEventClick, onFavoriteClick, onEmptyAgenda)
            eventsRecycler.adapter = adapter
        }
    }

    private fun initRecycler() {
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        eventsRecycler.layoutManager = GridLayoutManager(activity, if (isPortraitScreen) 1 else 2)
        (eventsRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}
