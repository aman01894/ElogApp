package com.example.elogapp.activity.ui.loads.pending_load

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.repository.responses.load.Consignees
import com.example.elogapp.repository.responses.load.Data
import com.example.elogapp.repository.responses.load.Shippers


class PendingLoadsListAdapter(private val mContext : Context,
    private val values: List<Data>,
    private val clickListener: (Data) -> Unit,
    ) :
    RecyclerView.Adapter<PendingLoadsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pending_list_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val dis = values[position].totalMiles


        holder.distance.text = dis + " miles"

        try {

            val loadNameStr = values[position].loadNo
            holder.loadName.text = loadNameStr

            if (item.consignees?.size!! > 1 || item.shippers?.size!! > 1) {
                holder.hasMultiple.visibility = View.VISIBLE
            } else {
                holder.hasMultiple.visibility = View.INVISIBLE
            }

            for(consignee in values[position].consignees){
                addConsignee(holder.lay_pickup, consignee)

                val dateStr = "Date: " + consignee.date.replace("T", " ")
                holder.dateTime.text = dateStr
            }

            for(shipper in values[position].shippers){
                addShipper(holder.lay_drop, shipper)
                val dateStr = "Date: " + shipper.date.replace("T", " ")
                holder.dateTime.text = dateStr
            }

            holder.bind(item, clickListener)

        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    private fun addShipper(view: View, shipper: Shippers) {
        val inflater: LayoutInflater = mContext.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewGroup : ViewGroup = view.findViewById (R.id.lay_drop)
        //viewGroup!!.removeAllViews()

        val view : ViewGroup = inflater.inflate(R.layout.address, viewGroup, false) as ViewGroup
        val tvTitle : TextView = view.findViewById (R.id.textLocation)
        val imagePickUp : ImageView = view.findViewById (R.id.pick_up_img)
        val mobNo : TextView = view.findViewById (R.id.textMobile)
        val mobImg : ImageView = view.findViewById (R.id.mob_img)

        var pickupStr = shipper.shipperConsigneeName + ", " +
                shipper.address + ", " +
                shipper.city + ", " +
                shipper.stateName

            tvTitle.text = pickupStr
            imagePickUp.visibility = View.VISIBLE


        val stringMob = shipper.contactNo
        if(stringMob != null && stringMob != ""){
            val temp = SpannableString(stringMob)
            temp.setSpan(UnderlineSpan(), 0,temp.length, 0)
            mobNo.text = temp

            mobImg.visibility = View.VISIBLE
            mobNo.visibility = View.VISIBLE
        }else{
            mobNo.text = "NA"
            mobNo.visibility = View.GONE
            mobImg.visibility = View.GONE
        }


        viewGroup.addView(view)

    }

    private fun addConsignee(view: View, consignee: Consignees) {
        val inflater: LayoutInflater = mContext.
        getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewGroup : ViewGroup = view.findViewById (R.id.lay_pickup)
        //viewGroup!!.removeAllViews()

        val view : ViewGroup = inflater.
        inflate(R.layout.address, viewGroup, false) as ViewGroup
        val tvTitle : TextView = view.findViewById (R.id.textLocation)
        val imageDrop : ImageView = view.findViewById (R.id.drop_loc_img)
        val mobNo : TextView = view.findViewById (R.id.textMobile)
        val mobImg : ImageView = view.findViewById (R.id.mob_img)

        var pickupStr = consignee.shipperConsigneeName + ", " +
                consignee.address + ", " +
                consignee.city + ", " +
                consignee.stateName

        tvTitle.text = pickupStr
        imageDrop.visibility = View.VISIBLE


        val stringMob = consignee?.contactNo

        if(stringMob != null && stringMob != ""){
            val temp = SpannableString(stringMob)
            temp.setSpan(UnderlineSpan(), 0,temp.length, 0)
            mobNo.text = temp
            mobImg.visibility = View.VISIBLE
            mobNo.visibility = View.VISIBLE
        }else{
            mobNo.text = "NA"
            mobNo.visibility = View.GONE
            mobImg.visibility = View.GONE
        }

        viewGroup.addView(view)

    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val loadName: TextView = view.findViewById(R.id.textViewLoadName)
        val distance: TextView = view.findViewById(R.id.textViewDistance)
        val hasMultiple: TextView = view.findViewById(R.id.item_has_multiple)
        val lay_pickup : LinearLayout = view.findViewById(R.id.lay_pickup)
        val lay_drop : LinearLayout = view.findViewById(R.id.lay_drop)
        val dateTime : TextView = view.findViewById(R.id.date_time)


        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: Data, clickListener: (Data) -> Unit) {
            itemView.setOnClickListener { clickListener(mData) }
        }
    }


}