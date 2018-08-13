package com.jacpalberto.devcomms.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.extensions.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
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
    }

    private fun initFragment() {
        replaceFragment(R.id.containerLayout, EventsByTimeFragment.newInstance())
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }
}
