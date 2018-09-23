package com.example.mibeo.w3d1_database.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(User::class), (ContactInfo::class)], version = 2)

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun contactDao(): ContactDao

    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): UserDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "user.db").build()
            }
            return instance!!
        }
    }

}