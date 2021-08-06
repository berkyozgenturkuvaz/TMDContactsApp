package com.example.tmdcontactsapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.model.ContactsModel
import kotlinx.android.synthetic.main.row_layout.view.*
import kotlinx.android.synthetic.main.row_layout_groups.view.*


class RecyclerViewAdapter(private var contactList: ArrayList<ContactsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var contactsFilterList: ArrayList<ContactsModel>? = null

    init {
        contactsFilterList = contactList
    }

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    private val colors: Array<String> =
        arrayOf("#FFFFFFFF")

     inner class ContacsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            fun bind(contactsModel: ContactsModel, colors: Array<String>, position: Int) {
                itemView.setOnClickListener {
                    lst?.onItemClick(contactsModel)
                }
                itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
                itemView.contactsName.text = contactsModel.name + " " + contactsModel.surname
            }

    }

    inner class GroupsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bind(contactsModel: ContactsModel, colors: Array<String>, position: Int) {
                itemView.setOnClickListener {
                    lst?.onItemClick(contactsModel)
                }
                itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
                itemView.groupsName.text = contactsModel.email

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(type == VIEW_TYPE_ONE){
            ContacsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
        }else{
            GroupsViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.row_layout_groups, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(type == VIEW_TYPE_ONE){
            (holder as ContacsViewHolder).bind(contactList[position], colors, position)
        }else{
            (holder as GroupsViewHolder).bind(contactList[position], colors, position)
        }


    }

    override fun getItemCount(): Int = contactList.size

    fun filterList(filteredList : ArrayList<ContactsModel>){
        contactList = filteredList
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int = type!!



    interface Listener {
        fun onItemClick(contactsModel: ContactsModel)
    }

    var lst: Listener? = null
    fun setListener(listener: Listener) {
        this.lst = listener
    }

    var type : Int? = 0
    fun setType(t : Int){
        this.type = t
    }

//    fun deleteItem(i : Int){
//        contactList.removeAt()
//        notifyDataSetChanged()
//    }






    /* inner class RowHolder(view: View) : RecyclerView.ViewHolder(view) {

         fun bind(contactsModel: ContactsModel, colors: Array<String>, position: Int) {
             itemView.setOnClickListener {
                 lst?.onItemClick(contactsModel)
             }
             itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
             itemView.contactsName.text = contactsModel.name + " " + contactsModel.surname

         }
     }

     inner class RowHolder2(view: View) : RecyclerView.ViewHolder(view) {

         fun bind(contactsModel: ContactsModel, colors: Array<String>, position: Int) {
             itemView.setOnClickListener {
                 lst?.onItemClick(contactsModel)
             }
             itemView.setBackgroundColor(Color.parseColor(colors[position % 1]))
             itemView.groupsName.text = contactsModel.email

         }
     }


     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
         return RowHolder(view)
     }

     override fun getItemViewType(position: Int): Int {
         return super.getItemViewType(position)
     }

     override fun onBindViewHolder(holder: RowHolder, position: Int) {
         holder.bind(contactList[position], colors, position)
     }

     override fun getItemCount(): Int {
         return contactList.count()
     }

 */


}