package com.jacpalberto.devcomms.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jacpalberto.devcomms.R
import com.jacpalberto.devcomms.data.DevCommsEvent
import com.jacpalberto.devcomms.data.SpeakerDetail
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
            val speaker = event?.speakerDetail
            eventTitle.text = event?.title
            showSpeaker(speaker)
            eventType.text = event?.type
            eventTime.text = context.getString(R.string.time_format, event?.startTimeString, event?.endTimeString)
            eventRoom.visibility = View.VISIBLE
            eventRoom.text = event?.room

            event?.let { devCommsEvent ->
                if (devCommsEvent.isFavorite == true) favoriteImageView.setImageResource(R.drawable.ic_favorite)
                else favoriteImageView.setImageResource(R.drawable.ic_not_favorite)
                eventCardView.setOnClickListener { func(devCommsEvent) }
                favoriteImageView.setOnClickListener { onFavoriteClick(layoutPosition, devCommsEvent) }
                showSpeakerImage(event.speakerDetail?.photo_url)
            }
        }

        private fun View.showSpeaker(speaker: SpeakerDetail?) {
            if (speaker?.first_name.isNullOrEmpty() && speaker?.last_name.isNullOrEmpty())
                eventSpeaker.text = ""
            else eventSpeaker.text = context.getString(R.string.complete_name, speaker?.first_name, speaker?.last_name)
        }

        private fun View.showSpeakerImage(speakerPhotoUrl: String?) {
            if (speakerPhotoUrl != null)
                Picasso.get()
                        .load(if (!speakerPhotoUrl.isNullOrEmpty()) speakerPhotoUrl else "placeholder")
                        .transform(CircleTransform())
                        .error(R.drawable.java_dev_day)
                        .resize(300, 300)
                        .centerCrop()
                        .into(this.speakerImageView)
        }
    }
}