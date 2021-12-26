package com.jarichichi.sportsdata

import android.R
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.view.menu.MenuView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import models.Selector
import org.json.JSONObject

class RequestRouter {
    companion object{
        fun getAllTeams(context: Context, team_drop: Spinner, current : Selector){
            var teams : MutableList<String> = mutableListOf()
            var curr : Selector = current
            Log.d("Router", "Router")

            val getRequest = JsonObjectRequest(Request.Method.GET, Constants.URL_GETALLTEAMS, null,
                Response.Listener{ response ->
                    if(!response.getBoolean("error")){
                        val resArray = response.getJSONArray("data")
                        val typ = current.selectorType
                        val itemsSelected = current.itemsSelected

                        for(i in 0 until resArray.length()) {
                            teams.add(resArray.getJSONObject(i).get("TeamName").toString())
                        }

                        val aAdapter = ArrayAdapter(
                            context,
                            R.layout.simple_spinner_item,
                            teams
                        ).also { adapter ->
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            team_drop.adapter = adapter
                        }


                    }
                },
                Response.ErrorListener{ error->
                    Log.d("Router error", error.toString())
                }
            )

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)
        }

        fun sendCustomQuery(context : Context, selectors: List<Selector>){

            val getRequest = object : StringRequest(Request.Method.POST, Constants.URL_CUSTOMQUERY,
                Response.Listener{ response -> Log.d("Success", response.toString())},
                Response.ErrorListener{ error-> }
            ) {
                override fun getParams(): Map<String, String>{

                    var selectorsMap = HashMap<String, String>()
                    var i = 0
                    for(selector in selectors){
                        selectorsMap.put("selectorType" + i.toString(), selector.selectorType)

                        var j = 0
                        for(item in selector.itemsSelected){
                            selectorsMap.put("itemsSelected" + i.toString() + j.toString(), item)
                            j+=1
                        }

                        i += 1

                    }

                    return selectorsMap
                }
            }

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)

        }

    }


}