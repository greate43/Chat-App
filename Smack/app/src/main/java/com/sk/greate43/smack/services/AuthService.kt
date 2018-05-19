package com.sk.greate43.smack.services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sk.greate43.smack.controller.App
import com.sk.greate43.smack.utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {
//    var isUserLoggedIn = false
//    var authToken = ""
//    var userEmail = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val uri = URL_REGISTER
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        val registerRequest = object : StringRequest(Request.Method.POST, uri, Response.Listener { _ -> complete(true) },
                Response.ErrorListener { error ->
                    complete(false)
                    Log.d("ERROR", "${error.message}")

                }

        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        App.prefs.requestQueue.add(registerRequest)
    }


    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val uri = URL_LOGIN
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, uri, null, Response.Listener { response ->
            try {


                App.prefs.authToken = response.getString("token")

                App.prefs.userEmail  = response.getString("user")

                App.prefs.isLoggedIn  = true

                complete(true)
            } catch (e: JSONException) {
                e.printStackTrace()
                complete(false)

            }


        },
                Response.ErrorListener { error ->
                    App.prefs.isLoggedIn = false
                    Log.d("ERROR", "${error.message}")

                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(loginRequest)

    }


    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val uri = URL_CREATE_USER

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)

        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, uri, null, Response.Listener { response ->
           try {


                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")



                complete(true)
            } catch (e: JSONException) {
                e.printStackTrace()
                complete(false)

            }





        },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "${error.message}")
                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${App.prefs.authToken}"
                return headers
            }
        }

        App.prefs.requestQueue.add(loginRequest)

    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit) {
        val uri = URL_GET_USER


        val findUserRequest = object : JsonObjectRequest(Method.GET, "$uri${App.prefs.userEmail}", null, Response.Listener { response ->
          try {


                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")


              val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGE)
              LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChanged)

                complete(true)
            } catch (e: JSONException) {
                e.printStackTrace()
                complete(false)

            }





        },
                Response.ErrorListener { error ->
                    Log.d("ERROR", "${error.message}")
                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }



            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${App.prefs.authToken}"
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)

    }
}
