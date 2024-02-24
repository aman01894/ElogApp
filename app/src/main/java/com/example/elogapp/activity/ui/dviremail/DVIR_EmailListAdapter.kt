package com.example.elogapp.activity.ui.dviremail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R


class DVIR_EmailListAdapter(private val values: List<DvirEmailData>) : RecyclerView.Adapter<DVIR_EmailListAdapter.ViewHolder>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_dvir_email_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.date

//            if(item.status){
//                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_green)
//            }else{
//                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_gray)
//            }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.report_date)
        val idReviewtxt: TextView = view.findViewById(R.id.review_text)
        val isSynced: ImageView = view.findViewById(R.id.ok_checked)

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}
