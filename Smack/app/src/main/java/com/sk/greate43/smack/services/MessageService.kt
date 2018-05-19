package com.sk.greate43.smack.services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.sk.greate43.smack.controller.App
import com.sk.greate43.smack.model.Channel
import com.sk.greate43.smack.utilities.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {
    val channel = ArrayList<Channel>()

    fun getChannel(context: Context, complete: (Boolean) -> Unit) {
        val uri = URL_GET_CHANNELS


        val channelsRequest = object : JsonArrayRequest(Method.GET, uri, null, Response.Listener { response ->
            try {

                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val channelName = channel.getString("name")
                    val channelDescription = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(channelName,channelDescription,channelId)
                    this.channel.add(newChannel)
                }


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

        App.prefs.requestQueue.add(channelsRequest)

    }
}