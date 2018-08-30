package com.jacpalberto.devcomms.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.PagerAdapter
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.DevCommsEventList
import com.jacpalberto.devcomms.extensions.doOnTabSelected
import kotlinx.android.synthetic.main.fragment_events_by_date.*

class EventsByDateFragment : Fragment() {
    companion object {
        fun newInstance() = EventsByDateFragment()
    }

    private var viewModel: EventsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events_by_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(EventsViewModel::class.java) }
        init()
    }

    private fun init() {
        viewModel?.fetchEvents()?.observe(this, Observer { filter(it) })
    }

    private fun filter(it: DevCommsEventList?) {
        it?.let {
            val eventsMap = it.eventList.groupBy { it?.date }
            setupTabLayout(eventsMap.keys)
            setupViewPager(eventsMap)
        }
    }

    private fun setupViewPager(eventsMap: Map<String?, List<DevCommsEvent?>>) {
        val fragmentList = mutableListOf<Fragment>()
        eventsMap.forEach { _, list ->
            fragmentList.add(EventFragment.newInstance(DevCommsEventList(list)))
        }
        viewPager.adapter = activity?.supportFragmentManager?.let { PagerAdapter(it, fragmentList) }
    }

    private fun setupTabLayout(keys: Set<String?>) {
        if (keys.size >= 2) {
            tabLayout.visibility = View.VISIBLE
            keys.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
            tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.doOnTabSelected {
                viewPager.currentItem = it.position
            }
        }
    }
}
