package com.jacpalberto.devcomms.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jacpalberto.devcomms.R
import kotlinx.android.synthetic.main.content_main.*

class AboutActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        init()
    }

    private fun init() {
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_dialog_map)
    }

    //TODO: Add spans to emails
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
