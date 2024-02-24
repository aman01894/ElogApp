package com.example.elogapp.activity.ui.document

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.elogapp.R
import com.example.elogapp.activity.ui.dalily_log.DailyLogAdapter
import com.example.elogapp.activity.ui.dalily_log.DailyLogData
import com.example.elogapp.repository.responses.load.DocGalleryInfo
import com.example.elogapp.util.AppUtils

class DocGalleryAdapter(
    private val mContext: Context,
    private var values: List<DocGalleryInfo>,
    private val clickListener: (DocGalleryInfo) -> Unit,
) : RecyclerView.Adapter<DocGalleryAdapter.ViewHolder>() {

    private var mCallback: OnShareClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_document_list_gallery_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        //holder.idView.text = item.date

        AppUtils.logger(item?.imgPath!!)

        Glide.with(mContext)
            .load(item.imgPath).centerCrop()
            .apply(RequestOptions.placeholderOf(R.drawable.ic_add_24))
            .into(holder.imgThumb)


        holder.imgDelBtn.setOnClickListener {
            mCallback?.deleteBtnClicked(item.id!!, item.imgPath!!)
        }

        holder.txtTitle.text=position.toString()
        holder.bind(item, clickListener)
    }

    override fun getItemCount(): Int = values.size

    fun updateReceiptsList(docList: List<DocGalleryInfo>) {
        values = docList
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumb: ImageView = view.findViewById(R.id.imgThumb)
        val imgDelBtn: ImageView = view.findViewById(R.id.delBtn)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)


        override fun toString(): String {
            return super.toString() + " '"
        }

        fun bind(mData: DocGalleryInfo, clickListener: (DocGalleryInfo) -> Unit) {
            itemView.setOnClickListener { clickListener(mData) }
        }
    }

    interface OnShareClickedListener {
        fun deleteBtnClicked(itemId: String, path: String)
    }


    fun deleteBtnListener(mCallback: OnShareClickedListener) {
        this.mCallback = mCallback
    }


}