package com.jacpalberto.devcomms.utils

import android.animation.Animator
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.IdRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.jacpalberto.devcomms.R

/**
 * Created by Alberto Carrillo on 7/13/18.
 */

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.startWebIntent(webPageUrl: String?) {
    try {
        val url: String = if (webPageUrl == null || webPageUrl.isEmpty()) ""
        else if (webPageUrl.contains("http://") || webPageUrl.contains("https://")) {
            webPageUrl
        } else "http://$webPageUrl"
        val webPage = Uri.parse(url)
        startActivity(Intent(Intent.ACTION_VIEW, webPage))
    } catch (e: ActivityNotFoundException) {
        showToast(getString(R.string.url_intent_error))
        e.printStackTrace()
    }
}

fun TextView.applyClickableSpan(context: Context, text: String?, function: () -> Unit) {
    val url = object : ClickableSpan() {
        override fun onClick(widget: View) {
            function()
        }

        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.color = ContextCompat.getColor(context,
                    R.color.detailHyperlinkText)
        }
    }
    val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    val spannableString = SpannableString(text)
    spannableString.setSpan(url, 0, spannableString.length, flag)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun AppCompatActivity.addFragment(@IdRes containerRes: Int, fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.add(containerRes, fragment)
    fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(@IdRes containerRes: Int,
                                      fragment: Fragment,
                                      tag: String = fragment.tag ?: "") {
    val fragmentTransaction = supportFragmentManager.beginTransaction()

    fragmentTransaction.replace(containerRes, fragment, tag)
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

fun createAnimationOnAnimationEnd(animator: (p0: Animator?) -> Unit = {}): Animator.AnimatorListener {
    return object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            animator(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    }
}