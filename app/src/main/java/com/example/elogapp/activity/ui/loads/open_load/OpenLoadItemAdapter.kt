package com.example.elogapp.activity.ui.loads.open_load

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.repository.model.ShipperConsigneeList
import com.example.elogapp.util.AppConstants


class OpenLoadItemAdapter(
    private val values: ArrayList<ShipperConsigneeList>, private val mContext: Context) :
    RecyclerView.Adapter<OpenLoadItemAdapter.ViewHolder>() {

    private var mCallback: OnShareClickedListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_open_load_accept_reject_list_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]

        val tempShipper = item?.shipper

        val mDate = tempShipper?.date
        val ShipperDate = mDate?.subSequence(0,10)
        var ShipperTime = tempShipper?.time

        if(ShipperTime == null){
            ShipperTime = "NA"
        }

        if(item.consignee != null){

            val item =  values[position].consignee

            var pickupStr = item?.shipperConsigneeName + ", " +
                    item?.address + ", " +
                    item?.city + ", " +
                    item?.stateName

            holder.address.text = pickupStr
            holder.pickup.text = "Delivery: " + item?.pickupNumber.toString()

            holder.dropOffImg.visibility = View.VISIBLE
            holder.pickUpImg.visibility = View.GONE

            val phoneNo = item?.contactNo.toString()
            holder.phone.text = phoneNo

            val date = item?.date
            holder.dateTime.text = ShipperDate

            holder.time.text = ShipperTime

            val temp = item?.reeferTemp
            holder.tempSet.text = temp

            val referM = item?.referModeId
            holder.referMode.text = "NA"
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

            holder.checkOut.text = "Delivered"

            resetAll(holder)

            when (item?.status) {

                AppConstants.ENROUTE -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }

                    holder.enroute.isEnabled = false
                }

                AppConstants.CHECK_IN -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }

                    holder.enroute.isEnabled = false
                    holder.checkIn.isEnabled = false

                }
                AppConstants.DOOR -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.door.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }

                    holder.enroute.isEnabled = false
                    holder . door . isEnabled = false
                    holder.checkIn.isEnabled = false

                }
                AppConstants.CHECK_OUT -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.door.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkOut.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }



                    holder.enroute.isEnabled = false
                    holder . door . isEnabled = false
                    holder.checkIn.isEnabled = false
                    holder.checkOut.isEnabled = false
                }
            }



        }else {

            val item =  values[position].shipper

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
            holder.referMode.text = "NA"
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

            holder.checkOut.text = "Loaded"

            resetAll(holder)
            when (item?.status) {

                AppConstants.ENROUTE -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }
                    holder.enroute.isEnabled = false
                }

                AppConstants.CHECK_IN -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }
                    holder.enroute.isEnabled = false
                    holder.checkIn.isEnabled = false
                }
                AppConstants.DOOR -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.door.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }
                    holder.enroute.isEnabled = false
                    holder.door.isEnabled = false
                    holder.checkIn.isEnabled = false
                }
                AppConstants.CHECK_OUT -> {
                    holder.enroute.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkIn.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.door.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_gray
                        )
                    }
                    holder.checkOut.background = mContext?.let { it1 ->
                        ContextCompat.getDrawable(
                            it1, R.drawable.rectangle_btn_bg_green
                        )
                    }

                    holder.enroute.isEnabled = false
                    holder . door . isEnabled = false
                    holder.checkIn.isEnabled = false
                    holder.checkOut.isEnabled = false
                }
            }

        }



        holder.checkIn.setOnClickListener {
//            Log.d("=>", "Click Check In")
            mCallback?.shareClicked("CheckIn", item, item.pos)
        }

        holder.door.setOnClickListener {
//            Log.d("=>", "Click Door")
            mCallback?.shareClicked("Door", item, item.pos)
        }

        holder.checkOut.setOnClickListener {
//            Log.d("=>", "Click Check Out")
            mCallback?.shareClicked("CheckOut", item, item.pos)
        }
        holder.enroute.setOnClickListener {
//            Log.d("=>", "Click Check Out")
            mCallback?.shareClicked("Enroute", item, item.pos)
        }


        holder.navigation.setOnClickListener {
            mCallback?.clickNavigation(item, item.pos)
        }

    }

    fun resetAll(holder: ViewHolder){
        holder.enroute.background = mContext?.let { it1 ->
            ContextCompat.getDrawable(
                it1, R.drawable.rectangle_bg
            )
        }
        holder.checkOut.background = mContext?.let { it1 ->
            ContextCompat.getDrawable(
                it1, R.drawable.rectangle_bg
            )
        }
        holder.door.background = mContext?.let { it1 ->
            ContextCompat.getDrawable(
                it1, R.drawable.rectangle_bg
            )
        }
        holder.checkIn.background = mContext?.let { it1 ->
            ContextCompat.getDrawable(
                it1, R.drawable.rectangle_bg
            )
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

        val enroute: TextView = view.findViewById(R.id.enroute)
        val checkIn: TextView = view.findViewById(R.id.check_in)
        val door: TextView = view.findViewById(R.id.click_door)
        val checkOut: TextView = view.findViewById(R.id.check_out)
        val navigation: ImageView = view.findViewById(R.id.navigationIcon)

        override fun toString(): String {
            return super.toString() + " '"
        }
    }

    interface OnShareClickedListener {
        fun shareClicked(clickType: String?, mData : ShipperConsigneeList, pos: Int)
        fun clickNavigation(mData : ShipperConsigneeList, pos: Int)
    }

    fun setOnShareClickedListener(mCallback: OnShareClickedListener) {
        this.mCallback = mCallback
    }

}