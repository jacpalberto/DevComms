package com.jacpalberto.devcomms.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Alberto Carrillo on 7/13/18.
 */
class PagerAdapter(fm: FragmentManager, private val mFragmentList: List<Fragment>)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int) = mFragmentList[position]

    override fun getCount() = mFragmentList.size
}