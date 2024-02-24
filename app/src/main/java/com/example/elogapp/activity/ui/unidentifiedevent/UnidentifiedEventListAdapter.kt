package com.example.elogapp.activity.ui.unidentifiedevent


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.database.UnidentifiedEvents


class UnidentifiedEventListAdapter(
    private val mContext: Context,
    private val values: List<UnidentifiedEvents>,
    private val eventListener : EventListener,
    private val clickListener: (UnidentifiedEvents) -> Unit
) :
    RecyclerView.Adapter<UnidentifiedEventListAdapter.ViewHolder>() {
    var listener: EventListener? = eventListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_unidentified_data_list_item, parent, false)
        return ViewHolder(view)

    }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]

        try {

            holder.deviceTime.text = "Time: " + item.eventTime
            holder.event.text = "Event"
            holder.odoMeter.text = "ODO Meter: " + item.odometer
            holder.location.text = item.latitude + "," + item.longitude

            holder.checkboxId.isChecked = item.checked

            holder.checkboxId.setOnCheckedChangeListener { _, isChecked ->
                item.checked = isChecked
                listener?.onCheckBoxClickEvent(item)
            }

            holder.geoLocationBtn.setOnClickListener{
                listener?.onLocationButtonClickEvent(item)
            }

            holder.bind(item, clickListener)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val deviceTime: TextView = view.findViewById(R.id.device_time)
        val event: TextView = view.findViewById(R.id.event)
        val odoMeter: TextView = view.findViewById(R.id.odo_meter)
        val location: TextView = view.findViewById(R.id.location_lat_long)
        val geoLocationBtn: LinearLayout = view.findViewById(R.id.location_btn)
        val checkboxId: CheckBox = view.findViewById(R.id.checkbox_id)

        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: UnidentifiedEvents, clickListener: (UnidentifiedEvents) -> Unit) {
            itemView.setOnClickListener {
                clickListener(mData)
            }
        }
    }

    interface EventListener {
        fun onCheckBoxClickEvent(myData: UnidentifiedEvents)
        fun onLocationButtonClickEvent(myData: UnidentifiedEvents)
    }

}

