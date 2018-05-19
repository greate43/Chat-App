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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.sk.greate43.smack.R
import com.sk.greate43.smack.model.Channel
import com.sk.greate43.smack.services.AuthService
import com.sk.greate43.smack.services.MessageService
import com.sk.greate43.smack.services.UserDataService
import com.sk.greate43.smack.utilities.BROADCAST_USER_DATA_CHANGE
import com.sk.greate43.smack.utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*


class MainActivity : AppCompatActivity() {
    val socket = IO.socket(SOCKET_URL);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        socket.connect()
        socket.on("channelCreated", onNewChannel)


        
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))




    }

    override fun onPause() {

        super.onPause()


    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
        super.onDestroy()
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

                        socket.emit("newChannel", channelName, channelDescription)

                    })
                    .setNegativeButton("Cancel", { dialog, id ->

                    })
            builder.show()
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread({
            val channelName = args[0] as String
            val channelDescription = args[1] as String
            val channelIds = args[2] as String

            val channel = Channel(channelName,channelDescription,channelIds)
            MessageService.channel.add(channel)
        })
    }

    fun sendMessageBurtonClicked(view: View) {
        hideKeyboard()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}
