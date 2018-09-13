package com.jacpalberto.devcomms.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_event.view.*

/**
 * Created by Alberto Carrillo on 9/7/18.
 */
class AgendaAdapter(private var events: MutableList<DevCommsEvent?>,
                    private var onEventClick: (DevCommsEvent) -> Unit,
                    private var onFavoriteClick: (Int, DevCommsEvent) -> Unit,
                    private var onEmptyAgenda: () -> Unit)
    : RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(itemView, onEventClick, onFavoriteClick)
    }

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    fun removeEventFromFavorites(position: Int) {
        events.removeAt(position)
        notifyItemRemoved(position)
        if (events.isEmpty()) onEmptyAgenda()
    }

    class ViewHolder(itemView: View, var func: (DevCommsEvent) -> Unit,
                     var onFavoriteClick: (Int, DevCommsEvent) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(event: DevCommsEvent?) = with(itemView) {
            eventTitle.text = event?.title
            eventSpeaker.text = event?.speaker
            eventType.text = event?.type
            eventTime.text = event?.hour
            eventCommunity.text = event?.community
            eventRoom.visibility = View.VISIBLE
            eventRoom.text = event?.room
            event?.let { devCommsEvent ->
                if (devCommsEvent.isFavorite == true) favoriteImageView.setImageResource(R.drawable.ic_favorite)
                else favoriteImageView.setImageResource(R.drawable.ic_not_favorite)
                eventCardView.setOnClickListener { func(devCommsEvent) }
                favoriteImageView.setOnClickListener { onFavoriteClick(layoutPosition, devCommsEvent) }
                showSpeakerImage(event, event.speakerPhotoUrl)
            }
        }

        private fun View.showSpeakerImage(event: DevCommsEvent?, speakerPhotoUrl: String?) {
            if (event?.speakerPhotoUrl != null)
                Picasso.get()
                        .load(if (!speakerPhotoUrl.isNullOrEmpty()) speakerPhotoUrl else "placeholder")
                        .transform(CircleTransform())
                        .placeholder(R.drawable.java_dev_day)
                        .resize(300, 300)
                        .centerCrop()
                        .into(this.speakerImageView)
        }
    }
}