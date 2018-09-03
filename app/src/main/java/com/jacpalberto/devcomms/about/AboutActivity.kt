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
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white)
        }
    }

    //TODO 1: Add spans to emails
    //TODO 2: Add styles to about
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
