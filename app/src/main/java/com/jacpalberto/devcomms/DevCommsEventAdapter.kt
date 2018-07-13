package com.jacpalberto.devcomms

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event.view.*

/**
 * Created by Alberto Carrillo on 7/12/18.
 */
class DevCommsEventAdapter(private var events: List<DevCommsEvent?>, private val isFilteredByTime: Boolean)
    : RecyclerView.Adapter<DevCommsEventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: DevCommsEvent?) = with(itemView) {
            eventTitle.text = event?.title
            eventSpeaker.text = event?.speaker
            eventType.text = event?.type
            eventTime.text = event?.hour
            eventCommunity.text = event?.community
            if (isFilteredByTime) {
                eventRoom.visibility = View.VISIBLE
                eventRoom.text = event?.room
            }
        }
    }
}