package com.example.mibeo.w3d1_database

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mibeo.w3d1_database.Models.UserModel
import kotlinx.android.synthetic.main.fragment_database.*

class DatabaseFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_database, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userModelProvider = ViewModelProviders.of(activity!!).get(UserModel::class.java)
        userModelProvider.getAllUsersWithContacts().observe(this, Observer {
            rv_user_database.layoutManager = LinearLayoutManager(super.getContext())
            rv_user_database.adapter = UserListAdapter(it!!)
        })
    }
}