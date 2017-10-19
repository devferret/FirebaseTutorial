package com.example.myfirebasetutorial.utils

import android.support.v7.widget.RecyclerView
import com.example.myfirebasetutorial.models.User
import com.google.firebase.database.*
import android.util.Log.e
import com.example.myfirebasetutorial.activities.MainActivity
import com.google.firebase.database.DataSnapshot

class FirebaseHelper(var mainActivity: MainActivity, private var databaseReference: DatabaseReference) {

    private var users = ArrayList<User>()
    private val TAG = "CHECK"

    // Save User Data to Firebase
    // Input User object and return boolean
    fun write(user: User): Boolean {
        return if (user == null) {
            false
        } else {
            try {
                var key = databaseReference.ref.child("users").push().key

                user.id = key
                databaseReference.child("users").push().setValue(user)

                true
            } catch (e: DatabaseException) {
                e.printStackTrace()
                false
            }
        }
    }

    // Retrieving Data from Firebase
    // Return List of Users
    fun read(): List<User> {
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                fetchData(dataSnapshot)
                mainActivity.mAdapter.notifyDataSetChanged()
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                fetchData(dataSnapshot)
                mainActivity.mAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

        return users
    }

    // Update User Data to Firebase
    // Input User id,object and return boolean
    fun update(user: User): Boolean {
        return if (user == null) {
            false
        } else {
            try {
                databaseReference.child("users").child(user.id).setValue(user)
                true
            } catch (e: DatabaseException) {
                e.printStackTrace()
                false
            }
        }
    }

    // Match Data to Users Array
    private fun fetchData(dataSnapshot: DataSnapshot) {
        users.clear()
        dataSnapshot.children
                .map { it.getValue(User::class.java) }
                .forEach {
                    e(TAG, "${it.id} ${it.name} ${it.email}")
                    users.add(it)
                }
    }
}