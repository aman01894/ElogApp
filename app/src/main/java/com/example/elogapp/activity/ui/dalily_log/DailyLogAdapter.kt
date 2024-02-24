package com.example.elogapp.activity.ui.dalily_log

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.util.AppConstants
import com.example.elogapp.util.AppUtils
import com.example.elogapp.util.PreferenceHelper


class DailyLogAdapter(private val mContext : Context,
                         private val values: List<DailyLogData>,
                         private val clickListener: (DailyLogData) -> Unit) : RecyclerView.Adapter<DailyLogAdapter.ViewHolder>() {


    private var mCallback: OnShareClickedListener? = null
    val timeZone = PreferenceHelper().getString(mContext,AppConstants.TIME_ZONE_KEY)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_daily_log_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val date = values[position].date


        if (timeZone != null) {
            holder.dateTv.text = AppUtils.timeZoneConverter(date ,timeZone)
        }

        //holder.dateTv.text = date

        try {

            if(item.isCertified == 1){
                holder.certified.background = mContext?.let { it1 ->
                    ContextCompat.getDrawable(
                        it1, R.drawable.ic_ok_icon
                    )
                }
            }else{

                holder.certified.background = mContext?.let { it1 ->
                    ContextCompat.getDrawable(
                        it1, R.drawable.ic_cancel_24
                    )
                }

                holder.certified.setOnClickListener {
                    mCallback?.shareClicked("Certificate", item, position)
                }
                //holder.bind(item, clickListener)
            }

            holder.dateTv.setOnClickListener {
                mCallback?.shareClicked("date", item, position)
            }

   }catch (e : Exception){
            e.printStackTrace()
        }
    }




    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.report_date)
        val certified : ImageView = view.findViewById(R.id.ok_checked)


        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: DailyLogData, clickListener: (DailyLogData) -> Unit) {
            itemView.setOnClickListener {
                clickListener(mData)
            }
        }

        fun bindImageView(mData: DailyLogData, clickListener: (DailyLogData) -> Unit) {
            certified.setOnClickListener {
                clickListener(mData)
            }
        }


    }

    interface OnShareClickedListener {
        fun shareClicked(clickType: String?, mData : DailyLogData, pos: Int)
        fun clickNavigation(mData : DailyLogData, pos: Int)
    }


    fun setOnShareClickedListener(mCallback: OnShareClickedListener) {
        this.mCallback = mCallback
    }





}