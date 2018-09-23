package com.example.mibeo.w3d1_database.Database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers() : LiveData<List<User>>

    @Query("SELECT * FROM user INNER JOIN contactinfo ON user.uId = contactinfo.userId WHERE user.uId = :userId")
    fun getUserWithContacts(userId: Int) : User

    @Query("SELECT * FROM user INNER JOIN contactinfo ON user.uId = contactinfo.userId")
    fun getAllUsersWithContacts() : LiveData<List<User>>
}

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: ContactInfo)

    @Delete
    fun delete(contact: ContactInfo)

    @Update
    fun update(contact: ContactInfo)

    @Query("SELECT * FROM contactinfo")
    fun getAllContactInfo() : LiveData<List<ContactInfo>>
}