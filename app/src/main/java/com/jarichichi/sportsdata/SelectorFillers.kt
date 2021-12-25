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
    }

}