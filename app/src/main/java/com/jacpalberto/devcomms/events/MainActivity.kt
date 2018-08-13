package com.jacpalberto.devcomms.events

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.about.AboutActivity
import com.jacpalberto.devcomms.extensions.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initToolbar()
        initFragment()
        initNavView()
    }

    private fun initFragment() {
        replaceFragment(R.id.containerLayout, EventsByTimeFragment.newInstance())
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initNavView() {
        val toggle = ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)

        toggle.apply {
            isDrawerIndicatorEnabled = false
            setHomeAsUpIndicator(android.R.drawable.ic_secure)
            syncState()
            setToolbarNavigationClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        }
        navView.itemIconTintList = null
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()
        when (item.itemId) {
            android.R.id.home -> if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START)
            } else drawerLayout.openDrawer(Gravity.START)
            R.id.scheduleMenuItem -> {
            }
            R.id.agendaMenuItem -> {
            }
            R.id.sponsorsMenuItem -> {
            }
            R.id.aboutMenuItem -> {
                startActivity(AboutActivity.newIntent(this@MainActivity))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return true
    }
}
