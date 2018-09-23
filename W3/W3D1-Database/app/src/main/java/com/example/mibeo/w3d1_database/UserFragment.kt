package com.example.mibeo.w3d1_database

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mibeo.w3d1_database.Database.User
import com.example.mibeo.w3d1_database.Database.UserDatabase
import kotlinx.android.synthetic.main.fragment_user.*
import org.jetbrains.anko.doAsync

class UserFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = UserDatabase.getDatabase(context!!)

        btn_done.setOnClickListener {
            if(!isFormFilled()) {
                Toast.makeText(context, "Please fill all the empty fields!", Toast.LENGTH_SHORT).show()
            } else {
                val userId = Integer.parseInt(editText_uId.text.toString())
                val userLName = editText_lName.text.toString()
                val userFName = editText_fName.text.toString()
                doAsync {
                    when {
                        radioButton_add.isChecked -> {
                            val creatingUser = User(userId, userFName, userLName, null)
                            db.userDao().insert(creatingUser)
                            Log.d("abc", "Added to DB")
                        }
                        radioButton_delete.isChecked -> {
                            val deletingUser = User(userId, userFName, userLName, null)
                            db.userDao().delete(deletingUser)
                            Log.d("abc", "Deleted from DB")
                        }
                        radioButton_update.isChecked -> {
                            val updatingUser = User(userId, userFName, userLName, null)
                            db.userDao().update(updatingUser)
                            Log.d("abc", "User updated")
                        }
                    }
                }
                editText_fName.text.clear()
                editText_lName.text.clear()
                editText_uId.text.clear()
            }
        }
    }

    private fun isFormFilled(): Boolean {
        return (editText_uId.text.isNotBlank() &&
                editText_fName.text.isNotBlank() &&
                editText_lName.text.isNotBlank())
    }
}