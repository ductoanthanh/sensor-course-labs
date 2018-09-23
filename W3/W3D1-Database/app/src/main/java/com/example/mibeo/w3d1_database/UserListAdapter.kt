package com.example.mibeo.w3d1_database

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mibeo.w3d1_database.Database.User

class UserListAdapter(private val dataset: List<User>): RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    class UserViewHolder(val itemView: View,
                         val idTextView: TextView = itemView.findViewById(R.id.tv_id),
                         val fNameTextView: TextView = itemView.findViewById(R.id.tv_fName),
                         val lNameTextView: TextView = itemView.findViewById(R.id.tv_lName),
                         val contactTypeTextView: TextView = itemView.findViewById(R.id.tv_contactType),
                         val contactValueTextView: TextView = itemView.findViewById(R.id.tv_contactValue)) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item_view, parent, false)
        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (dataset != null) {
            val thisUser = dataset[position]

            holder.idTextView.text = thisUser.uId.toString()
            holder.fNameTextView.text = thisUser.fName
            holder.lNameTextView.text = thisUser.lName
            holder.contactTypeTextView.text = ""
            holder.contactValueTextView.text = ""

            if(thisUser.contact != null) {
                holder.contactTypeTextView.text = thisUser.contact.type
                holder.contactValueTextView.text = thisUser.contact.value
            }
        }
    }

}