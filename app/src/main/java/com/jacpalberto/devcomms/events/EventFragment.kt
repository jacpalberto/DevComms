package com.jacpalberto.devcomms.events

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.DevCommsEventAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.DevCommsListEvent
import kotlinx.android.synthetic.main.fragment_event.*

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class EventFragment : Fragment() {
    private var viewModel: EventsViewModel? = null

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
        val devCommsListEvent = arguments?.getParcelable<DevCommsListEvent>(EVENT_LIST)
        showEvents(devCommsListEvent?.eventList)
    }

    private fun showEvents(it: List<DevCommsEvent?>?) {
        Log.d("EventFragment", it.toString())
        it?.let { events -> eventsRecycler.adapter = DevCommsEventAdapter(events, true) }
    }

    private fun initRecycler() {
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        eventsRecycler.layoutManager = if (isPortraitScreen) LinearLayoutManager(activity) else GridLayoutManager(activity, 2)
    }

    companion object {
        const val EVENT_LIST = "EVENT_LIST"
        fun newInstance(eventList: DevCommsListEvent) = EventFragment().apply {
            arguments = Bundle().apply { putParcelable(EVENT_LIST, eventList) }
        }
    }
}
