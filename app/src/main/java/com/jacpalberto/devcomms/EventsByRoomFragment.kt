package com.jacpalberto.devcomms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events_by_room.*

class EventsByRoomFragment : Fragment() {
    private var viewModel: DevCommsViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events_by_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(DevCommsViewModel::class.java) }
        init()
    }

    private fun init() {
        viewModel?.getEvents()?.observe(this, Observer { filter(it) })
    }

    private fun filter(it: List<DevCommsEvent?>?) {
        it?.let {
            val eventsMap = it.groupBy { it?.room }
            setupTabLayout(eventsMap.keys)
            setupViewPager(eventsMap)
        }
    }

    private fun setupViewPager(eventsMap: Map<String?, List<DevCommsEvent?>>) {
        val fragmentList = mutableListOf<Fragment>()
        eventsMap.forEach { _, list ->
            fragmentList.add(EventFragment.newInstance(DevCommsListEvent(list)))
        }
        viewPager.adapter = activity?.supportFragmentManager?.let { PagerAdapter(it, fragmentList) }
    }

    private fun setupTabLayout(keys: Set<String?>) {
        keys.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
        tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    companion object {
        fun newInstance() = EventsByRoomFragment()
    }
}
