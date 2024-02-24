package com.example.elogapp.activity.ui.shippingdocs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.database.ShippingDocs
import com.example.elogapp.repository.responses.shippingdocs.ShippingDocsData


class ShippingDocsAdapter(var values: List<ShippingDocs>?) :
    RecyclerView.Adapter<ShippingDocsAdapter.ViewHolder>()

{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_shipping_list_item, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)
        holder.docNo.text = "Doc. No.: " + item?.documentNumber
        holder.date.text = item?.saveDate

            if(item?.isSync == true){
                holder.delete.visibility = View.GONE
            }else{
                holder.delete.visibility = View.INVISIBLE
            }
    }

    fun updateDocList(list: List<ShippingDocs>?){
        values = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = values!!.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val docNo: TextView = view.findViewById(R.id.doc_number)
        val date: TextView = view.findViewById(R.id.shipping_date)
        val delete: ImageView = view.findViewById(R.id.deleteBtn)

        override fun toString(): String {
            return super.toString() + " '"
        }
    }
}
