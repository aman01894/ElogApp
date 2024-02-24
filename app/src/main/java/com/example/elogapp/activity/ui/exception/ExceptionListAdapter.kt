package com.example.elogapp.activity.ui.exception


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R
import com.example.elogapp.repository.model.Exceptions


class ExceptionListAdapter(
    private val mContext: Context,
    private val values: List<Exceptions>,
    private val clickListener: (Exceptions) -> Unit,
) :
    RecyclerView.Adapter<ExceptionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_exception_list_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]

        try {

            holder.exception.text = item.exceptionDetail
            holder.exceptionId.isChecked = item.checked

            holder.exceptionId.setOnCheckedChangeListener { _, isChecked ->
                item.checked = isChecked

            }

            //holder.bind(item, clickListener)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val exception: TextView = view.findViewById(R.id.exception_detail)
        val exceptionId: CheckBox = view.findViewById(R.id.exception_id)

        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: Exceptions, clickListener: (Exceptions) -> Unit) {
            itemView.setOnClickListener {
                clickListener(mData)
            }
        }
    }


}