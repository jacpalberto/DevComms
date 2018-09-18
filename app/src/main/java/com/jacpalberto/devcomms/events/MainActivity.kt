package com.jacpalberto.devcomms.events

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import com.jacpalberto.devcomms.Agenda.AgendaByDateFragment
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.about.AboutActivity
import com.jacpalberto.devcomms.data.Sponsor
import com.jacpalberto.devcomms.sponsors.SponsorsActivity
import com.jacpalberto.devcomms.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private var viewModel: EventsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        init()
    }

    private fun init() {
        initToolbar()
        //initFragment()
        initNavView()
        test()
    }

    private fun test() {
        val db = FirebaseFirestore.getInstance()
        val event = "jdd-18"
        val eventRef = db.collection("events").document(event)
        eventRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("MainActivity2", it.result.data.toString())
                val x = it.result.toObject(MainEventResponse::class.java)
                Log.d("MainActivity22", x.toString())
            }
        }

        // val query = db.collection("events")
//
        // db.collection("events").document(event).get().addOnSuccessListener {
        //     val a : Query = it.get("ref") as DocumentReference
        //     val response = FirestoreRecyclerOptions.Builder<MainEventResponse>()
        //             .setQuery(a, MainEventResponse::class.java)
        //             .build()
        // }

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
            android.R.id.home -> if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START)
            } else drawerLayout.openDrawer(Gravity.START)
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

    data class MainEventResponse(
            val name: String = "",
            val location: List<Location> = emptyList(),
            val communities: List<DocumentReference> = emptyList(),
            val dates: List<Date> = emptyList(),
            val description: String = "",
            val sponsor: List<SponsorResponse> = emptyList()
    )

    data class SponsorResponse(
            val id: DocumentReference,
            val category: String = ""
    )

    @Parcelize
    data class Location(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("lon")
            val lon: Int = 0,
            @SerializedName("lat")
            val lat: Int = 0
    ) : Parcelable
}
