package com.example.elogapp.activity.ui.paymenthistory
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.elogapp.R
//
//class PaymentHistoryRecycleAdapter(private val values: List<PaymentHistoryListData>) : RecyclerView.Adapter<PaymentHistoryRecycleAdapter.ViewHolder>()
//
//{
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.fragment_payment_history_list_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
//        holder.idView.text = item.date
//
////            if(item.status){
////                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_green)
////            }else{
////                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_gray)
////            }
//    }
//
//    override fun getItemCount(): Int = values.size
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val idView: TextView = view.findViewById(R.id.item_title)
//
//
//        override fun toString(): String {
//            return super.toString() + " '"
//        }
//    }
//}