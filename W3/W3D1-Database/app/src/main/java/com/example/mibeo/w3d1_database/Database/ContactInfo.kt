package com.example.mibeo.w3d1_database.Database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [(ForeignKey(
        entity = User::class,
        parentColumns = ["uId"],
        childColumns = ["userId"]))])
data class ContactInfo (
        val userId: Int,
        val type: String,
        @PrimaryKey
        val value: String)