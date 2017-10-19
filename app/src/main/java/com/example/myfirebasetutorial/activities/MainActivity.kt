package com.example.myfirebasetutorial.activities

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.myfirebasetutorial.R
import com.example.myfirebasetutorial.adapters.UsersAdapter
import com.example.myfirebasetutorial.interfaces.ClickListener
import com.example.myfirebasetutorial.utils.FirebaseHelper
import com.example.myfirebasetutorial.models.User
import com.example.myfirebasetutorial.utils.RecyclerTouchListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {

    lateinit var recyclerViewUsers: RecyclerView
    lateinit var mAdapter: UsersAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseHelper: FirebaseHelper

    private lateinit var btnShowAddDialog: Button

    private val TAG_TOUCH = "TOUCH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firebase setting
        databaseReference = FirebaseDatabase.getInstance().reference
        firebaseHelper = FirebaseHelper(this, databaseReference)

        initView()
    }

    // Initialize all of views from MainActivity Layout and config RecyclerView
    private fun initView() {
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = UsersAdapter(firebaseHelper.read())

        recyclerViewUsers = find(R.id.recyclerViewUsers)
        recyclerViewUsers.layoutManager = mLayoutManager
        recyclerViewUsers.itemAnimator = DefaultItemAnimator()
        recyclerViewUsers.adapter = mAdapter
        recyclerViewUsers.addOnItemTouchListener(RecyclerTouchListener(
                applicationContext,
                recyclerViewUsers,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        e(TAG_TOUCH, "$position")
                    }

                    override fun onLongClick(view: View, position: Int) {
                        e(TAG_TOUCH, "$position")
                    }
                }
        ))

        btnShowAddDialog = find(R.id.btnShowAddDialog)
        btnShowAddDialog.onClick {
            inputUserDialog()
        }
    }

    // Display a dialog for input user data and save to Firebase
    private fun inputUserDialog(user: User? = null) {

        // Setup Dialog
        var dialog = Dialog(this)
        dialog.setTitle("User Info")
        dialog.setContentView(R.layout.user_input_dialog)

        // Bind View
        var edtName = dialog.find<EditText>(R.id.edtName)
        var edtEmail = dialog.find<EditText>(R.id.edtEmail)
        var btnAdd = dialog.find<Button>(R.id.btnAdd)

        // If have input. display it!
        if (user!=null) {
            edtName.setText(user.name)
            edtEmail.setText(user.email)
        }

        btnAdd.onClick {
            var newUser = User(name = edtName.text.toString(), email = edtEmail.text.toString())

            if (checkString(edtName.text.toString()) && checkString(edtEmail.text.toString())) {
                if (user==null) {
                    if (firebaseHelper.write(newUser)) {
                        edtName.text.clear()
                        edtEmail.text.clear()
                        dialog.dismiss()
                    }
                } else {
                    if (firebaseHelper.update(newUser)) {
                        edtName.text.clear()
                        edtEmail.text.clear()
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    // Check Empty String
    private fun checkString(str: String): Boolean {
        return str != null && str.isNotEmpty()
    }
}
