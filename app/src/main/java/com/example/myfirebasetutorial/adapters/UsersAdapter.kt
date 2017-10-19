package com.example.myfirebasetutorial.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myfirebasetutorial.R
import com.example.myfirebasetutorial.models.User
import org.jetbrains.anko.find

class UsersAdapter(private var usersList: List<User>): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtName = itemView.find<TextView>(R.id.txtName)
        var txtEmail = itemView.find<TextView>(R.id.txtEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.user_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var user = usersList[position]
        holder?.txtName?.text = user.name
        holder?.txtEmail?.text = user.email
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

}