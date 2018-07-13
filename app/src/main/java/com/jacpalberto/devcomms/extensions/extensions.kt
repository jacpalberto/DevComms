package com.jacpalberto.devcomms.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
fun AppCompatActivity.addFragment(@IdRes containerRes: Int, fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.add(containerRes, fragment)
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(@IdRes containerRes: Int, fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.replace(containerRes, fragment)
    fragmentTransaction.commit()
}