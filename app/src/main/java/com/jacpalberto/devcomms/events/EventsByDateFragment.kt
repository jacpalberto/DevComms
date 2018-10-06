package com.jacpalberto.devcomms.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.adapters.PagerAdapter
import com.jacpalberto.devcomms.data.DataResponse
import com.jacpalberto.devcomms.data.DataState
import com.jacpalberto.devcomms.data.DevCommsEvent
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
    private var currentEventList: List<DevCommsEvent>? = null

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

    private fun handleEventsByDate(eventList: DataResponse<List<DevCommsEvent>>?) {
        dismissProgress()
        if (eventList == null && eventList?.isStatusFailedOrError() != false) {
            showNetworkErrorSnackbar()
        } else if (eventList.status == DataState.SUCCESS) {
            dismissNetworkErrorSnackbar()
            if (currentEventList == null) {
                currentEventList = eventList.data
                filterEventsByDate(eventList)
            } else if (eventList.data != currentEventList) {
                currentEventList = eventList.data
                filterEventsByDate(eventList)
            }
        }
    }

    private fun dismissNetworkErrorSnackbar() {
        if (snackbar.isShown) snackbar.dismiss()
    }

    private fun showNetworkErrorSnackbar() {
        if (!snackbar.isShown) snackbar.show()
    }

    private fun filterEventsByDate(eventList: DataResponse<List<DevCommsEvent>>) {
        eventList.let { events ->
            val eventsMap = events.data.groupBy { it.startDateString }
            setupTabLayout(eventsMap.keys)
            setupViewPager(eventsMap)
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        progressBar.visibility = View.GONE
    }

    private fun setupViewPager(eventsMap: Map<String?, List<DevCommsEvent>>) {
        dismissProgress()
        val fragmentList = mutableListOf<Fragment>()
        eventsMap.forEach {
            fragmentList.add(EventFragment.newInstance(it.value.toCollection(ArrayList())))
        }
        viewPager.adapter = activity?.supportFragmentManager?.let {
            PagerAdapter(childFragmentManager, fragmentList)
        }
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
