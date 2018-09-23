package com.example.mibeo.w3d1_database

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mibeo.w3d1_database.Database.ContactInfo
import com.example.mibeo.w3d1_database.Database.UserDatabase
import kotlinx.android.synthetic.main.fragment_contact.*
import org.jetbrains.anko.doAsync

class ContactFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = UserDatabase.getDatabase(context!!)

        btn_done_contact.setOnClickListener {
            if(!isFormFilled()) {
                Toast.makeText(context, "Please fill all the empty fields!", Toast.LENGTH_SHORT).show()
            } else {
                val userId = Integer.parseInt(editText_user.text.toString())
                val contactValue = editText_value.text.toString()
                val contactType = editText_type.text.toString()
                doAsync {
                    when {
                        radioButton_add_contact.isChecked -> {
                            val creatingContact = ContactInfo(userId, contactType, contactValue)
                            db.contactDao().insert(creatingContact)
                            Log.d("abc", "Contact added to DB")
                        }
                        radioButton_delete_contact.isChecked -> {
                            val deletingContact = ContactInfo(userId, contactType, contactValue)
                            db.contactDao().delete(deletingContact)
                            Log.d("abc", "Contact deleted from DB")
                        }
                        radioButton_update_contact.isChecked -> {
                            val updatingContact = ContactInfo(userId, contactType, contactValue)
                            db.contactDao().update(updatingContact)
                            Log.d("abc", "Contact updated")
                        }
                    }
                }
                editText_type.text.clear()
                editText_value.text.clear()
                editText_user.text.clear()
            }
        }
    }

    private fun isFormFilled(): Boolean {
        return (editText_user.text.isNotBlank() &&
                editText_type.text.isNotBlank() &&
                editText_value.text.isNotBlank())
    }
}