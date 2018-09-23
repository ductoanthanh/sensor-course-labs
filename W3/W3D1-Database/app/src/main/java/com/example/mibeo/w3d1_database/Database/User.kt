package com.example.mibeo.w3d1_database.Database

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User (
        @PrimaryKey (autoGenerate = true)
        val uId: Int,
        val fName: String,
        val lName: String,
        @Embedded
        val contact: ContactInfo?
) {

    override fun toString(): String {
        return "$fName $lName"
    }
}