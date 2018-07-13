package com.jacpalberto.devcomms

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class PagerAdapter(fm: FragmentManager, private val mFragmentList: List<Fragment>)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = mFragmentList[position]

    override fun getCount() = mFragmentList.size
}