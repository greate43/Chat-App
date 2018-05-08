package com.sk.greate43.smack.services

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sk.greate43.smack.utilities.URL_REGISTER
import org.json.JSONObject

object AuthService {
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
}
