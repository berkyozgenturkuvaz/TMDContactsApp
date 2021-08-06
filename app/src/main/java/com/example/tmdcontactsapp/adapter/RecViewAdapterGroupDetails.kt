package com.example.tmdcontactsapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.model.GroupDetailsModel
import kotlinx.android.synthetic.main.row_layout_groups.view.*

class RecViewAdapterGroupDetails(private var groupDetailsList: ArrayList<GroupDetailsModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val colors: Array<String> =
        arrayOf("#FFFFFFFF")


    inner class GroupDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(groupDetailsModel: GroupDetailsModel, colors: Array<String>, position: Int) {
            itemView.setOnClickListener {
                lstGroupDetails?.onItemClick(groupDetailsModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
            itemView.groupsName.text = groupDetailsModel.name + " " + groupDetailsModel.surname

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupDetailsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout_groups, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecViewAdapterGroupDetails.GroupDetailsViewHolder).bind(
            groupDetailsList[position],
            colors,
            position
        )
    }

    override fun getItemCount(): Int = groupDetailsList.size

    fun filterList(filteredList : ArrayList<GroupDetailsModel>){
        groupDetailsList = filteredList
        notifyDataSetChanged()
    }

    interface ListenerGroupDetails {
        fun onItemClick(groupDetailsModel: GroupDetailsModel)
    }

    var lstGroupDetails: ListenerGroupDetails? = null
    fun setListenerGroupDetails(listenergroupdetails: ListenerGroupDetails) {
        this.lstGroupDetails = listenergroupdetails
    }

    var type: Int? = 0
    fun setType(t: Int) {
        this.type = t
    }
}