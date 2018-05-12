package com.sk.greate43.smack.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sk.greate43.smack.utilities.URL_LOGIN
import com.sk.greate43.smack.utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
    var isUserLoggedIn = false
    var authToken = ""
    var userEmail = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val uri = URL_REGISTER
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()
        val registerRequest = object : StringRequest(Request.Method.POST, uri, Response.Listener { _ -> complete(true) },
                Response.ErrorListener { error ->
                    complete(false)
                }

        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }


    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val uri = URL_LOGIN
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object : JsonObjectRequest(Method.POST, uri, null, Response.Listener { response ->
          try {


            authToken = response.getString("token")

            userEmail = response.getString("user")

            isUserLoggedIn = true

            complete(true)
          }catch (e :JSONException){
              e.printStackTrace()
              complete(false)

          }


        },
                Response.ErrorListener { error ->
                    isUserLoggedIn = false
                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)

    }
}
