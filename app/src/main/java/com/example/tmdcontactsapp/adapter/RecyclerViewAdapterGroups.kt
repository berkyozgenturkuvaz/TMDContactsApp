package com.example.tmdcontactsapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.model.GroupsModel
import kotlinx.android.synthetic.main.row_layout_groups.view.*

class RecyclerViewAdapterGroups(private var groupsList: ArrayList<GroupsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val colors: Array<String> =
        arrayOf("#FFFFFFFF")



    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(groupsModel: GroupsModel, colors: Array<String>, position: Int) {
            itemView.setOnClickListener {
                lstGroups?.onItemClick(groupsModel)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
            itemView.groupsName.text = groupsModel.name

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_layout_groups, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as RecyclerViewAdapterGroups.GroupsViewHolder).bind(
                groupsList[position],
                colors,
                position
            )

    }

    override fun getItemCount(): Int = groupsList.size

    fun filterList(filteredList : ArrayList<GroupsModel>){
        groupsList = filteredList
        notifyDataSetChanged()
    }

    interface ListenerGroups {
        fun onItemClick(groupsModel: GroupsModel)
    }

    var lstGroups: ListenerGroups? = null
    fun setListenerGroups(listenergroups: ListenerGroups) {
        this.lstGroups = listenergroups
    }

    var type: Int? = 0
    fun setType(t: Int) {
        this.type = t
    }
}