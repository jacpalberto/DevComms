package com.jacpalberto.devcomms.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.PagerAdapter
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.DevCommsEventList
import com.jacpalberto.devcomms.utils.doOnTabSelected
import kotlinx.android.synthetic.main.fragment_events_by_date.*


class EventsByDateFragment : Fragment() {
    companion object {
        fun newInstance() = EventsByDateFragment()
        val TAG = EventsByDateFragment::class.java.name ?: "EventsByDateFragment"
    }

    private lateinit var snackbar: Snackbar
    private var viewModel: EventsViewModel? = null
    private lateinit var currentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_events_by_date, container, false)
        return currentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.let { ViewModelProviders.of(it).get(EventsViewModel::class.java) }
        snackbar = Snackbar.make(currentView, getString(R.string.connectivity_error), Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY") {
                    viewModel?.fetchEvents()
                }
        init()
    }

    private fun init() {
        showProgress()
        setToolbarTitle()
        viewModel?.fetchEvents()?.observe(this, Observer { handleEventsByDate(it) })
    }

    private fun handleEventsByDate(eventList: DevCommsEventList?) {
        dismissProgress()
        if (eventList?.status == DataState.FAILURE || eventList?.status == DataState.ERROR) {
            if (!snackbar.isShown) snackbar.show()
        } else if (eventList?.status == DataState.SUCCESS) {
            if (snackbar.isShown) snackbar.dismiss()
            eventList.let { events ->
                val eventsMap = events.eventList.groupBy { it.startDateString }
                setupTabLayout(eventsMap.keys)
                setupViewPager(eventsMap)
            }
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        progressBar.visibility = View.GONE
    }

    private fun setupViewPager(eventsMap: Map<String?, List<DevCommsEvent>>) {
        val fragmentList = mutableListOf<Fragment>()
        eventsMap.forEach { _, list ->
            fragmentList.add(EventFragment.newInstance(DevCommsEventList(list)))
        }
        viewPager.adapter = activity?.supportFragmentManager?.let { PagerAdapter(childFragmentManager, fragmentList) }
        dismissProgress()
    }

    private fun setupTabLayout(keys: Set<String?>) {
        if (keys.size >= 2) {
            tabLayout.visibility = View.VISIBLE
            tabLayout.removeAllTabs()
            keys.forEach { tabLayout.addTab(tabLayout.newTab().setText(it)) }
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL
            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            tabLayout.doOnTabSelected {
                viewPager.currentItem = it.position
            }
        }
    }

    private fun setToolbarTitle() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.app_name)
    }
}
