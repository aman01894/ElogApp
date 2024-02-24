package com.example.elogapp.activity.ui.dalily_log

import TableData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.PreferenceHelper

class DailyLogChartAdapter(private val mContext : Context,
                           private var values: List<TableData>,
                           private val clickListener: (TableData) -> Unit

) :
    RecyclerView.Adapter<DailyLogChartAdapter.ViewHolder>() {

    private var mCallback: OnViewItemClickedListener? = null
    val timeZone = PreferenceHelper().getString(mContext, AppConstants.TIME_ZONE_KEY)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_daily_log_chart_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val data = values[position]


        timeZone?.let {
            holder.eventTime.text = AppUtils.dateToTimeWithTimeZone(data.eventTime ,timeZone)
        }


        holder.eventName.text = data.eventName
        holder.odometerValue.text = data.odoMeter?.toString()
        holder.engineHoursValue.text = "-"
        holder.sourceValue.text = "Driver"
        holder.location.text = data.location

        holder.bind(item, clickListener)

    }




    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val eventName : TextView = view.findViewById(R.id.event_name)
        val eventTime : TextView = view.findViewById(R.id.eventTime)
        val odometerValue: TextView = view.findViewById(R.id.odometerValue)
        val engineHoursValue : TextView = view.findViewById(R.id.engineHoursValue)
        val sourceValue : TextView = view.findViewById(R.id.sourceValue)
        val location : TextView = view.findViewById(R.id.location)



        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: TableData, clickListener: (TableData) -> Unit) {
            itemView.setOnClickListener { clickListener(mData) }
        }
    }

    interface OnViewItemClickedListener {
        fun shareClicked(mData : DailyLogData, pos: Int)
    }
    fun setOnViewItemClickedListener(mCallback: OnViewItemClickedListener) {
        this.mCallback = mCallback
    }
}