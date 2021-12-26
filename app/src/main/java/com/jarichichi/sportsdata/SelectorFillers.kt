package com.jarichichi.sportsdata

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import models.Selector

class SelectorFillers {
    companion object {
        fun selectTeam(itemView: View, context: Context, current : Selector) {
            val first_text: TextView =
                itemView.findViewById<TextView>(R.id.sel_text_one_one) as TextView
            val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)

            first_text.text = "From the "

            //HERE IS WHERE DATABASE NEEDS TO BE QUERIED FOR TEAM NAMES
            RequestRouter.getAllTeams(context, team_drop, current)

        }

        fun createCustomQuery(context: Context, selectors: List<Selector>) {
            var query = "SELECT * FROM "

           /* val initial = selectors.get(0)

            if(initial.selectorType == "SELECT_TEAM"){
                query += "Team "
                query += "WHERE TeamName = " + initial.itemsSelected[0]
            }
            else if(initial.selectorType == "SELECT_PLAYER"){
                query += "Player "
                query += "WHERE PlayerName = " + initial.itemsSelected[2]
            }
            else if(initial.selectorType == "SELECT_POSITION"){
                query += "Player "
                query += "WHERE Position = " + initial.itemsSelected[0]
            }*/

            RequestRouter.sendCustomQuery(context, selectors)

           // RequestRouter.sendCustomQuery(context, query)


        }

    }

}