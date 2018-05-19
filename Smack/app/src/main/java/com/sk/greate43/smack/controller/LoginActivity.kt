package com.sk.greate43.smack.controller

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sk.greate43.smack.R
import com.sk.greate43.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginButtonClicked(view: View) {
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        if (email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginUser( email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { userFoundSuccessfully ->
                        if (userFoundSuccessfully) {
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
            loginSpinner.visibility = View.VISIBLE

        } else {
            loginSpinner.visibility = View.INVISIBLE

        }

        loginButton.isEnabled = !enable
//        loginEmailText.isEnabled = !enable
//        loginPasswordText.isEnabled = !enable
        loginCreateNewUser.isEnabled = !enable

    }

    fun loginCreateUserButtonClicked(view: View) {
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }




}
