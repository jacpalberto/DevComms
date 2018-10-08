package com.jacpalberto.devcomms.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.about.AboutActivity
import com.jacpalberto.devcomms.agenda.AgendaByDateFragment
import com.jacpalberto.devcomms.sponsors.SponsorsActivity
import com.jacpalberto.devcomms.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    //TODO: inject viewModel
    private var viewModel: EventsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        init()
    }

    private fun init() {
        initToolbar()
        initFragment()
        initNavView()
    }

    private fun initFragment() {
        val fragmentTag = viewModel?.fragmentTag ?: EventsByDateFragment.TAG
        when (fragmentTag) {
            EventsByDateFragment.TAG -> replaceFragment(R.id.containerLayout, EventsByDateFragment.newInstance(), fragmentTag)
            AgendaByDateFragment.TAG -> replaceFragment(R.id.containerLayout, AgendaByDateFragment.newInstance(), fragmentTag)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavView() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        toggle.apply {
            isDrawerIndicatorEnabled = false
            setHomeAsUpIndicator(R.drawable.ic_menu)
            syncState()
            setToolbarNavigationClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        }
        navView.itemIconTintList = null
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        when (item.itemId) {
            android.R.id.home -> if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else drawerLayout.openDrawer(GravityCompat.START)
            R.id.scheduleMenuItem -> showScheduleFragment()
            R.id.agendaMenuItem -> showAgendaFragment()
            R.id.sponsorsMenuItem -> {
                startActivity(SponsorsActivity.newIntent(this@MainActivity))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            R.id.aboutMenuItem -> {
                startActivity(AboutActivity.newIntent(this@MainActivity))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return true
    }

    private fun showAgendaFragment() {
        val tag = AgendaByDateFragment.TAG
        viewModel?.fragmentTag = tag
        replaceFragment(R.id.containerLayout, AgendaByDateFragment.newInstance(), tag)
    }

    private fun showScheduleFragment() {
        val tag = EventsByDateFragment.TAG
        viewModel?.fragmentTag = tag
        replaceFragment(R.id.containerLayout, EventsByDateFragment.newInstance(), tag)
    }
}
