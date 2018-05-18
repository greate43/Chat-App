package com.sk.greate43.smack.controller

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.sk.greate43.smack.R
import com.sk.greate43.smack.services.AuthService
import com.sk.greate43.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5 ,0.5 ,0.5]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createUserSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createUserAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createUserAvatarImageView.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255;
        val savedG = g.toDouble() / 255;
        val savedB = b.toDouble() / 255;

        avatarColor = "[$savedR,$savedG,$savedB,1]"

    }

    fun createUserClicked(view: View) {
        enableSpinner(true)
        val userName = createUserNameText.text.toString()
        val email = createUserEmailText.text.toString()
        val password = createUserPassword.text.toString()


        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {
                                    val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChanged)

                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure email , password and email are filled in ", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }


    }

     private fun errorToast() {
        Toast.makeText(this, "Something went wrong , please try again .", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    private fun enableSpinner(enable: Boolean) {
        if (enable) {
            createUserSpinner.visibility = View.VISIBLE

        } else {
            createUserSpinner.visibility = View.INVISIBLE

        }

        createUserAvatarImageView.isEnabled = !enable
        createUserBackgroundColorButton.isEnabled = !enable
        createUserButton.isEnabled = !enable
    }


}

