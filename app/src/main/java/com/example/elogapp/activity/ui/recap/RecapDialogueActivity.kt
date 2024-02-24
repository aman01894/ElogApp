package com.example.elogapp.activity.ui.recap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.elogapp.R


var data : ArrayList<RecapModel> = ArrayList();
private lateinit var rAdapter: RecapDialogueActivity.RecapRecyclerViewAdapter
lateinit var rView: RecyclerView


class RecapDialogueActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recapdialog)

        rView = findViewById(R.id.recycle_recap)

        var ext_but = findViewById<Button>(R.id.exit);
        rAdapter = RecapRecyclerViewAdapter(data);
        rView.adapter = rAdapter
        rAdapter.notifyDataSetChanged()

        data.clear()
        data.add(RecapModel("26 Jan 2021","10"))
        data.add(RecapModel("26 Jan 2021", "9"))
        data.add(RecapModel("27 Jan 2021", "8"))
        data.add(RecapModel("28 Jan 2021", "7"))

        ext_but.setOnClickListener {
            finish()
        }


    }

    class RecapRecyclerViewAdapter(
        private val values: List<RecapModel>
    ) : RecyclerView.Adapter<RecapRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recapdialoglistitem, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.date
            holder.isSynced.text = item.time

//            if(item.status){
//                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_green)
//            }else{
//                holder.isSynced.setBackgroundResource(R.drawable.ic_check_ok_gray)
//            }
        }

        override fun getItemCount(): Int = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.date)
            val isSynced: TextView = view.findViewById(R.id.time)

            override fun toString(): String {
                return super.toString() + " '"
            }
        }
    }
}