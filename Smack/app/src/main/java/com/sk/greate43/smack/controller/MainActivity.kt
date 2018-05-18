package com.sk.greate43.smack.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sk.greate43.smack.R
import com.sk.greate43.smack.services.AuthService
import com.sk.greate43.smack.services.UserDataService
import com.sk.greate43.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import android.R.string.cancel
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))
        hideKeyboard()
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isUserLoggedIn) {
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email

                val recourseId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(recourseId)
                userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))

                loginButtonNavHeader.text = "Logout"
            }
        }

    }

    fun loginButtonNavClicked(view: View) {
        if (AuthService.isUserLoggedIn) {
            UserDataService.logout()
            loginButtonNavHeader.text = "login"
            userNameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)

        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun addChannelClicked(view: View) {
        if (AuthService.isUserLoggedIn) {
            val builder = AlertDialog.Builder(this)
            // Get the layout inflater
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(dialogView)
                    // Add action buttons
                    .setPositiveButton("Add", { dialog, id ->
                        val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelTextName);
                        val descriptionTextField = dialogView.findViewById<EditText>(R.id.addChannelTextDescription)

                        val channelName = nameTextField.text.toString()
                        val channelDescription = descriptionTextField.text.toString()

                        hideKeyboard()
                    })
                    .setNegativeButton("Cancel", { dialog, id ->
                        hideKeyboard()

                    })
            builder.show()
        }
    }

    fun sendMessageBuutonClicked(view: View) {

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}
