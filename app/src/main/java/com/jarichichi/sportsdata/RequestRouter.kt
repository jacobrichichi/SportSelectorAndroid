package com.jarichichi.sportsdata

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RequestRouter {
    companion object{
        fun getAllTeams(context: Context){
            var teams : String
            Log.d("Router", "Router")

            val getRequest = JsonObjectRequest(Request.Method.GET, Routing_Constants.URL_GETALLTEAMS, null,
                Response.Listener{ response ->
                    Log.d("Success", response.toString())
                },
                Response.ErrorListener{ error->
                    Log.d("Router error", error.toString())
                }
            )

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)
        }

    }


}