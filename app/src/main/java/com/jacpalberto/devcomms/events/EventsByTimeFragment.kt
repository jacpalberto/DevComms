package com.jacpalberto.devcomms.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.DevCommsEventAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import kotlinx.android.synthetic.main.fragment_events_by_time.*

//TODO: update this fragment as filtered by room. to show several tabs if the events has several days.
class EventsByTimeFragment : Fragment() {
    companion object {
        fun newInstance() = EventsByTimeFragment()
    }

    private var viewModel: EventsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events_by_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(EventsViewModel::class.java) }
        init()
    }

    private fun init() {
        initRecycler()
        initObservers()
    }

    private fun initObservers() {
        viewModel?.fetchEvents()?.observe(this, Observer { showEvents(it) })
    }

    private fun showEvents(it: List<DevCommsEvent?>?) {
        it?.let { events -> eventsRecycler.adapter = DevCommsEventAdapter(events) }
    }

    private fun initRecycler() {
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        eventsRecycler.layoutManager = if (isPortraitScreen) LinearLayoutManager(activity) else GridLayoutManager(activity, 2)
    }
}
