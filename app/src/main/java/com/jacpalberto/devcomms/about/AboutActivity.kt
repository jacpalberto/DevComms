package com.jacpalberto.devcomms.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.utils.startWebIntent
import kotlinx.android.synthetic.main.activity_about.*

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
        setHyperLinkSpannables()
    }

    private fun setHyperLinkSpannables() {
        aboutAppContent.addHyperlinkSpannables(applicationContext)
        aboutEventContent.addHyperlinkSpannables(applicationContext)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back_white)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

private fun TextView.addHyperlinkSpannables(context: Context) {
    val splitAppText = this.text.split(" ")
    val ssb = SpannableStringBuilder()
    splitAppText.forEach {
        if (!it.isEmpty()) {
            if (it.startsWith("º")) {
                ssb.append("\n")
                ssb.append(createHyperlinkSpannable(it, context))
            } else ssb.append("$it ")
        }
    }
    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(ssb, TextView.BufferType.SPANNABLE)
}

fun createHyperlinkSpannable(text: String, context: Context): SpannableString {
    val urlString = text.replace("º", "")
    val url = object : ClickableSpan() {
        override fun onClick(widget: View) {
            context.startWebIntent(urlString)
        }

        override fun updateDrawState(textPaint: TextPaint) {
            textPaint.color = ContextCompat.getColor(context, R.color.detailHyperlinkText)
        }
    }
    val flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    val spannableString = SpannableString(urlString)
    return spannableString.apply { setSpan(url, 0, spannableString.length, flag) }
}