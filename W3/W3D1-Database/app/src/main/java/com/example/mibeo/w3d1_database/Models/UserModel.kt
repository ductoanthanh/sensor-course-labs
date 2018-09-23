package com.example.mibeo.w3d1_database.Models

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.example.mibeo.w3d1_database.Database.User
import com.example.mibeo.w3d1_database.Database.UserDatabase

class UserModel(mApplication: Application): AndroidViewModel(mApplication) {
    private val userDatabase = UserDatabase.getDatabase(getApplication())
    private var users: LiveData<List<User>> = userDatabase.userDao().getAllUsersWithContacts()

    fun getAllUsersWithContacts() = users

}