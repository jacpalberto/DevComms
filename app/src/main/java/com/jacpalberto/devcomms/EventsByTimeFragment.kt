package com.jacpalberto.devcomms

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
import kotlinx.android.synthetic.main.fragment_events_by_time.*

class EventsByTimeFragment : Fragment() {
    private var viewModel: DevCommsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events_by_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(DevCommsViewModel::class.java) }
        init()
    }

    private fun init() {
        initRecycler()
        initObservers()
    }

    private fun initObservers() {
        viewModel?.getEvents()?.observe(this, Observer { showEvents(it) })
    }

    private fun showEvents(it: List<DevCommsEvent?>?) {
        it?.let { events -> eventsRecycler.adapter = DevCommsEventAdapter(events, true) }
    }

    private fun initRecycler() {
        val isPortraitScreen = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        eventsRecycler.layoutManager = if (isPortraitScreen) LinearLayoutManager(activity) else GridLayoutManager(activity, 2)
    }

    companion object {
        fun newInstance() = EventsByTimeFragment()
    }
}
