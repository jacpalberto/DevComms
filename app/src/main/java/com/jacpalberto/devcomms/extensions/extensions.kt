package com.jacpalberto.devcomms.extensions

import android.content.Context
import android.support.annotation.IdRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by Alberto Carrillo on 7/13/18.
 */

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun AppCompatActivity.addFragment(@IdRes containerRes: Int, fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.add(containerRes, fragment)
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(@IdRes containerRes: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.replace(containerRes, fragment)
    if (addToBackStack) fragmentTransaction.addToBackStack(fragment.tag)
    fragmentTransaction.commitNow()
}

inline fun TabLayout.doOnTabSelected(crossinline onTabSelectedListener: (TabLayout.Tab) -> Unit = {}) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            onTabSelectedListener(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    })
}