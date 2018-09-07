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
import com.jacpalberto.devcomms.utils.doOnTabSelected
import kotlinx.android.synthetic.main.fragment_events_by_date.*
import android.support.v7.app.AppCompatActivity


/**
 * Created by Alberto Carrillo on 9/7/18.
 */

class AgendaByDateFragment : Fragment() {
    companion object {
        fun newInstance() = AgendaByDateFragment()
        val TAG = AgendaByDateFragment::class.java.name ?: "AgendaByDateFragment"
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
        viewModel?.fetchFavoriteEvents()?.observe(this, Observer { filterEventsByDate(it) })
    }

    private fun filterEventsByDate(eventList: DevCommsEventList?) {
        eventList?.let { events ->
            val eventsMap = events.eventList.groupBy { it.date }
            setupTabLayout(eventsMap.keys)
            setupViewPager(eventsMap)
        }
    }

    private fun setupViewPager(eventsMap: Map<String?, List<DevCommsEvent>>) {
        val fragmentList = mutableListOf<Fragment>()
        eventsMap.forEach { _, list ->
            fragmentList.add(EventFragment.newInstance(DevCommsEventList(list)))
        }
        viewPager.adapter = activity?.supportFragmentManager?.let { PagerAdapter(childFragmentManager, fragmentList) }
    }

    private fun setupTabLayout(keys: Set<String?>) {
        tabLayout.removeAllTabs()
        if (keys.size >= 2) {
            tabLayout.visibility = View.VISIBLE
            updateToolbarTitle(getString(R.string.my_agenda))
            keys.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.doOnTabSelected {
                viewPager.currentItem = it.position
            }
        } else if (keys.size == 1) {
            tabLayout.visibility = View.GONE
            updateToolbarTitle(getString(R.string.my_agenda) + " " + keys.first()?.toLowerCase())
        }
    }

    private fun updateToolbarTitle(title: String) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = title
    }
}
