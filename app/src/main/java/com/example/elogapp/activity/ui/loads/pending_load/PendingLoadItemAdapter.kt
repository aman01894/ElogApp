package com.example.elogapp.activity.ui.loads.pending_load

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.repository.model.ShipperConsigneeList

class PendingLoadItemAdapter(
    private val values: ArrayList<ShipperConsigneeList>
) :
    RecyclerView.Adapter<PendingLoadItemAdapter.ViewHolder>() {

    private var mCallback: PendingLoadItemAdapter.OnShareClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pending_load_accept_reject_list_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]
//        holder.itemNumber.text = (position + 1).toString()
        val tempShipper = item?.shipper

        val mDate = tempShipper?.date
        val ShipperDate = mDate?.subSequence(0,10)
        var ShipperTime = tempShipper?.time

        if(ShipperTime == null){
            ShipperTime = "NA"
        }

        if (item.consignee != null) {

            val item = values[position].consignee

            var pickupStr = item?.shipperConsigneeName + ", " +
                    item?.address + ", " +
                    item?.city + ", " +
                    item?.stateName

            holder.address.text = pickupStr

            holder.pickup.text = "Drop-off: " + item?.pickupNumber?.toString()
            holder.dropOffImg.visibility = View.VISIBLE
            holder.pickUpImg.visibility = View.GONE

            val phoneNo = item?.contactNo?.toString()
            holder.phone.text = phoneNo

            val date = item?.date?.toString()
            holder.dateTime.text = ShipperDate

            holder.time.text = ShipperTime

            val temp = item?.reeferTemp
            holder.tempSet.text = temp

            val referM = item?.referModeId
            when(referM){

                0 -> {
                    holder.referMode.text = "NA"
                }
                1->{
                    holder.referMode.text = "Auto"
                }
                2->{
                    holder.referMode.text = "Continous"
                }
            }


            val comm = item?.comodity?.toString()
            holder.commodity.text = comm

            val cases = item?.caseCount
            holder.cases.text = cases

            val pallets = item?.pallets
            holder.pallets.text = pallets

            val weight = item?.weight
            holder.weight.text = weight

        } else {

            val item = values[position].shipper

            var pickupStr = item?.shipperConsigneeName + ", " +
                    item?.address + ", " +
                    item?.city + ", " +
                    item?.stateName

            holder.address.text = pickupStr

            holder.pickup.text = "Pickup: " + item?.pickupNumber.toString()
            holder.dropOffImg.visibility = View.GONE
            holder.pickUpImg.visibility = View.VISIBLE

            val phoneNo = item?.contactNo?.toString()
            holder.phone.text = phoneNo

            val date = item?.date
            holder.dateTime.text = ShipperDate

            holder.time.text = ShipperTime

            val temp = item?.reeferTemp
            holder.tempSet.text = temp

            val referM = item?.referModeId
            when(referM){

                0 -> {
                    holder.referMode.text = "NA"
                }
                1->{
                    holder.referMode.text = "Auto"
                }
                2->{
                    holder.referMode.text = "Continous"
                }
            }

            val comm = item?.comodity
            holder.commodity.text = comm

            val cases = item?.caseCount
            holder.cases.text = cases

            val pallets = item?.pallets
            holder.pallets.text = pallets

            val weight = item?.weight
            holder.weight.text = weight

        }


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pickUpImg: ImageView = view.findViewById(R.id.pick_up_img)
        val dropOffImg: ImageView = view.findViewById(R.id.drop_off_img)
        val pickup: TextView = view.findViewById(R.id.item_text)
        val address: TextView = view.findViewById(R.id.item_detail)
        val phone: TextView = view.findViewById(R.id.phone)
        val dateTime: TextView = view.findViewById(R.id.datetime)
        val time: TextView = view.findViewById(R.id.time)
        val tempSet: TextView = view.findViewById(R.id.item_temp)
        val referMode: TextView = view.findViewById(R.id.refermode_tv)

        val commodity: TextView = view.findViewById(R.id.commodity_tv)
        val cases: TextView = view.findViewById(R.id.cases_tv)
        val pallets: TextView = view.findViewById(R.id.pallets_tv)
        val weight: TextView = view.findViewById(R.id.weight_tv)
        val navigation: ImageView = view.findViewById(R.id.navigationIcon)

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

    interface OnShareClickedListener {
        fun clickNavigation(mData : ShipperConsigneeList, pos: Int)
    }

    fun setOnClickedListener(mCallback: OnShareClickedListener) {
        this.mCallback = mCallback
    }


}