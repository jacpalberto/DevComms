package com.jacpalberto.devcomms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jacpalberto.devcomms.extensions.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private var viewModel: DevCommsViewModel? = null
    private var isFilteredByTime: Boolean = true
    private var isDataNull: Boolean = true
    private val eventsByTimeFragment = EventsByTimeFragment
    private val eventsByRoomFragment = EventsByRoomFragment
    private var item: MenuItem? = null
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(DevCommsViewModel::class.java)
        init()
    }

    private fun init() {
        initObservers()
        initToolbar()
        initFragment()
    }

    private fun initFragment() {
        val filteredByTime = viewModel?.getFilteredByTime()
        if (filteredByTime != null && filteredByTime)
            replaceFragment(R.id.containerLayout, eventsByTimeFragment.newInstance())
        else
            replaceFragment(R.id.containerLayout, eventsByRoomFragment.newInstance())
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initObservers() {
        val filteredByTime = viewModel?.getFilteredByTime()
        if (filteredByTime != null) isFilteredByTime = filteredByTime

        viewModel?.getEvents()?.observe(this, Observer { isDataNull = (it == null) })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        this.menu = menu
        item = menu.findItem(R.id.action_filter_toggle)
        updateMenuItem(isFilteredByTime)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter_toggle) {
            if (!isDataNull) toggle()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (isFilteredByTime) showListFilteredByRoom()
        else showListFilteredByTime()
    }

    private fun showListFilteredByTime() {
        isFilteredByTime = true
        updateMenuItem(isFilteredByTime)
        viewModel?.setFilteredByTime(true)
        replaceFragment(R.id.containerLayout, eventsByTimeFragment.newInstance())
    }

    private fun showListFilteredByRoom() {
        isFilteredByTime = false
        updateMenuItem(isFilteredByTime)
        viewModel?.setFilteredByTime(false)
        replaceFragment(R.id.containerLayout, eventsByRoomFragment.newInstance())
    }

    private fun updateMenuItem(filteredByTime: Boolean) {
        val title = if (filteredByTime) R.string.filtered_by_time else R.string.filtered_by_room
        val icon = if (filteredByTime) android.R.drawable.ic_btn_speak_now else android.R.drawable.ic_delete
        item?.setIcon(icon)
        item?.title = getString(title)
    }
}
