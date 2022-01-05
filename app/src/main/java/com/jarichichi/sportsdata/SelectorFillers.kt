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
            val first_text: TextView = itemView.findViewById<TextView>(R.id.sel_text_one_one) as TextView
            val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)

            first_text.text = "From the "

            //HERE IS WHERE DATABASE NEEDS TO BE QUERIED FOR TEAM NAMES
            RequestRouter.getAllTeams(context, team_drop, current)

        }

        fun selectPosition(itemView: View, context: Context, current: Selector){
            val first_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_one) as TextView
            val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
            val second_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_two) as TextView
            val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
            val third_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_three) as TextView

            first_text.text = "How the position "
            second_text.text = " optionally, from the "
            third_text.text = " when "

            RequestRouter.getAllTeams(context, team_drop, current)
            val positions = arrayOf("QB", "WR","RB", "TE")

            val aAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                positions
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                position_drop.adapter = adapter
            }


        }

        fun selectPlayer(itemView: View, context: Context, current : Selector){

        }

        fun createCustomQuery(context: Context, selectors: List<Selector>)  {
            RequestRouter.sendCustomQuery(context, selectors)
        }

    }

}