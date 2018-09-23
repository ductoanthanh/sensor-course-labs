package com.example.mibeo.w3d1_database

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var atUserForm = true
    private var atContactForm = false
    private lateinit var userFragment: UserFragment
    private lateinit var databaseFragment: DatabaseFragment
    private lateinit var contactFragment: ContactFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userFragment = UserFragment()
        databaseFragment = DatabaseFragment()
        contactFragment = ContactFragment()

        supportFragmentManager.beginTransaction().add(R.id.frameLayout_forms, userFragment).commit()

        btn_userDB.setOnClickListener {
            atUserForm = true
            atContactForm = false
            controlCurrentFragment()
        }

        btn_contactDB.setOnClickListener {
            atUserForm = false
            atContactForm = true
            controlCurrentFragment()
        }

        btn_view_database.setOnClickListener {
            atUserForm = false
            atContactForm = false
            controlCurrentFragment()
        }
    }

    private fun controlCurrentFragment() {
        when {
            atUserForm -> {
                btn_userDB.isEnabled = false
                btn_contactDB.isEnabled = true
                btn_view_database.isEnabled = true

                Log.d("fragment", "change to user fragment")
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout_forms, userFragment).commit()
            }
            atContactForm -> {
                btn_userDB.isEnabled = true
                btn_contactDB.isEnabled = false
                btn_view_database.isEnabled = true

                Log.d("fragment", "change to contact fragment")
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout_forms, contactFragment).commit()
            }
            else -> {
                btn_userDB.isEnabled = true
                btn_contactDB.isEnabled = true
                btn_view_database.isEnabled = false

                Log.d("fragment", "change to database fragment")
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout_forms, databaseFragment).commit()

            }
        }
    }
}
