package com.jarichichi.sportsdata

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import models.Selector
import org.json.JSONArray
import org.json.JSONObject

class RequestRouter {
    companion object{
        fun getAllTeams(context: Context, team_drop: Spinner, current : Selector){
            var teams : MutableList<String> = mutableListOf()
            Log.d("Router", "Router")

            val getRequest = JsonObjectRequest(Request.Method.GET, Constants.URL_GETALLTEAMS, null,
                Response.Listener{ response ->
                    if(!response.getBoolean("error")){
                        val resArray = response.getJSONArray("data")
                        for(i in 0 until resArray.length()) {
                            teams.add(resArray.getJSONObject(i).get("TeamName").toString())
                        }

                        object : ArrayAdapter<String>(
                            context,
                            R.layout.simple_spinner_item,
                            teams
                        ){
                            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) : View {
                                var view = super.getView(position, convertView, parent) as TextView
                                if(current.itemsSelected[0].contains(view.text)){
                                    view.setTextColor(Color.BLUE)
                                }
                                else{
                                    view.setTextColor(Color.BLACK)
                                }

                                return view
                            }
                        }.also { adapter ->
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

        fun getPlayersMultipleTeams(context: Context, player_drop: Spinner?, current: Selector, teams: List<String>){
            val getRequest = object : StringRequest(Request.Method.POST, Constants.URL_GETPLAYERSMULTIPLETEAMS,
                Response.Listener{ response ->
                    var names = mutableListOf<String>()
                    val data = JSONObject(response).getJSONArray("data")
                    for(i in 0 until data.length()) {
                        names.add(data.getJSONObject(i).get("Name").toString())
                    }

                    val aAdapter = object : ArrayAdapter<String>(
                        context,
                        R.layout.simple_spinner_item,
                        names
                    )
                    {
                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) : View {
                            var view = super.getView(position, convertView, parent) as TextView
                            if(current.itemsSelected[1].contains(view.text)){
                                view.setTextColor(Color.BLUE)
                            }
                            else{
                                view.setTextColor(Color.BLACK)
                            }
                            return view
                        }
                    }.also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        player_drop?.adapter = adapter
                    }

                },
                Response.ErrorListener{ error-> Log.d("fail", error.toString())}

            ) {
                override fun getParams(): Map<String, String>{

                    var params = HashMap<String, String>()

                    params.put("NumTeams", teams.size.toString())
                    var i = 0
                    for(team in teams){
                        params.put("Team$i", team)
                        i++
                    }
                    params.put("NumPositions", current.itemsSelected[0].size.toString())

                    i = 0
                    for(position in current.itemsSelected[0]){
                        params.put("Position$i", position)
                        i++
                    }
                    return params
                }
            }

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)

        }

        fun getPlayers(context: Context, player_drop: Spinner, current: Selector, team: String){
            val getRequest = object : StringRequest(Request.Method.POST, Constants.URL_GETCERTAINPLAYERS,
                Response.Listener{ response ->
                    var names = mutableListOf<String>()
                    val data = JSONObject(response).getJSONArray("data")
                    for(i in 0 until data.length()) {
                        names.add(data.getJSONObject(i).get("Name").toString())
                    }

                    val aAdapter = object : ArrayAdapter<String>(
                        context,
                        R.layout.simple_spinner_item,
                        names
                    )
                    {
                        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) : View {
                            var view = super.getView(position, convertView, parent) as TextView
                            if(current.itemsSelected[2].contains(view.text)){
                                view.setTextColor(Color.BLUE)
                            }
                            else{
                                view.setTextColor(Color.BLACK)
                            }
                            return view
                        }
                    }.also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        player_drop.adapter = adapter
                    }

                    },
                Response.ErrorListener{ error-> Log.d("fail", error.toString())}
            ) {
                override fun getParams(): Map<String, String>{

                    var params = HashMap<String, String>()
                    params.put("Team", current.itemsSelected[0][0])
                    params.put("Position", current.itemsSelected[1][0])
                    return params
                }
            }

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)
        }

        fun sendCustomQuery(context : Context, selectors: List<Selector>){

            val getRequest = object : StringRequest(Request.Method.POST, Constants.URL_CUSTOMQUERY,
                        Response.Listener{ response ->
                            val intent = Intent(context, StatisticsActivity::class.java).apply {
                                putExtra(Constants.SELECTORS_KEY, response)
                            }

                            context.startActivity(intent) },
                        Response.ErrorListener{ error-> Log.d("fail", error.toString())}
                ) {
                override fun getParams(): Map<String, String>{

                    var selectorsMap = HashMap<String, String>()
                    var i = 0
                    var selectorType: String = ""

                    selectorsMap.put("numSelectors", selectors.size.toString())
                    for(selector in selectors){
                        selectorType = selector.selectorType

                        if(selectorType == "TEAM_WITH_PLAYER_PLAYING"){
                            selectorType = "PLAYER_PLAYING"
                        }

                        selectorsMap.put("selectorType" + i.toString(), selectorType)

                        var j = 0
                        for(itemList in selector.itemsSelected){
                            //NEED TO PROPERLY IMPLEMENT WAY TO PASS LIST OF ITEMS SELECTED TO BACKEND
                            var k = 0
                            for(item in itemList){
                                selectorsMap.put("itemsSelected" + i.toString() + j.toString() + k.toString(), item)
                                k +=1
                            }

                            selectorsMap.put("numItems" + i.toString() + j.toString(), k.toString())

                            j+=1
                        }

                        //Number of items corresponding to the ith selector
                        selectorsMap.put("numItemLists" + i.toString(), j.toString())

                        i += 1

                    }

                    return selectorsMap
                }
            }

            RequestHandlerSingleton.getInstance(context).addToRequestQueue(getRequest)
        }


    }




}