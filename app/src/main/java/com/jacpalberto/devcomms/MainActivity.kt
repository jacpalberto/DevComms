package com.jacpalberto.devcomms

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private var viewModel: DevCommsViewModel? = null
    private var isFilteredByTime: Boolean = true
    private var item: MenuItem? = null
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(DevCommsViewModel::class.java)
        init()
    }

    private fun init() {
        initToolbar()
        initFragment()
        initObservers()
    }

    private fun initFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = EventsByTimeFragment
        fragmentTransaction.add(R.id.containerLayout, fragment.newInstance())
        fragmentTransaction.commit()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initObservers() {
        viewModel?.getEvents()?.observe(this,
                Observer {
                    if (it != null && !it.isEmpty()) item?.isVisible = true
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        this.menu = menu
        item = menu.findItem(R.id.action_filter_toggle)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter_toggle) {
            toggle()
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
    }

    private fun showListFilteredByRoom() {
        isFilteredByTime = false
        updateMenuItem(isFilteredByTime)
    }

    private fun updateMenuItem(filteredByTime: Boolean) {
        val title = if (filteredByTime) R.string.filtered_by_time else R.string.filtered_by_room
        val icon = if (filteredByTime) android.R.drawable.ic_btn_speak_now else android.R.drawable.ic_delete
        item?.setIcon(icon)
        item?.title = getString(title)
    }
}
