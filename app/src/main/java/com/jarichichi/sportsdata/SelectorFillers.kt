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
            third_text.text = " plays "

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
            val first_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_three_one) as TextView
            val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_one)
            val second_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_three_two) as TextView
            val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_two)
            val third_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_three_three) as TextView
            val player_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_three)
            val fourth_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_three_four) as TextView

            first_text.text = "From the "
            second_text.text = " playing "
            third_text.text = ", I'd like to know how the player(s) "
            fourth_text.text = " performed"

            RequestRouter.getAllTeams(context, team_drop, current)
            RequestRouter.getPlayers(context, player_drop, current, current.itemsSelected[0][0])

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

        fun fillWhenWithPlayerTeamsSelected(itemView: View, context: Context, current: Selector){
            val first_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_one) as TextView
            val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
            val second_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_two) as TextView
            val player_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
            val third_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_three) as TextView

            first_text.text = "Playing "
            second_text.text = " I'd like to know the performance when "
            third_text.text = " is also playing"

           // RequestRouter.getPlayersMultipleTeams(context, player_drop, current, teams)
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

        fun fillWhenAgainstTeam(itemView: View, context: Context, current : Selector){
            val first_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_one_one) as TextView
            val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)

            first_text.text = "When playing against the "

            RequestRouter.getAllTeams(context, team_drop, current)




        }

        fun fillWhenTemperature(itemView: View, context: Context, current : Selector) {
            val first_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_one) as TextView
            val ab_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
            val second_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_two) as TextView
            val temps_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
            val third_text : TextView = itemView.findViewById<TextView>(R.id.sel_text_two_three) as TextView

            first_text.text = "When it is "
            third_text.text = " degrees fahrenheit"

            val ab = arrayOf("above", "below")
            val aAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                ab
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                ab_drop.adapter = adapter
            }

            val temps : Array<Int> = 0.rangeTo(100).toList().toTypedArray()

            val tAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                temps
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                temps_drop.adapter = adapter
            }

        }

        fun createCustomQuery(context: Context, selectors: List<Selector>)  {
            RequestRouter.sendCustomQuery(context, selectors)
        }

        fun fillSelectPlayer(context: Context, player_drop: Spinner, current: Selector, team: String){
            RequestRouter.getPlayers(context, player_drop, current, team)

        }

        fun fillMultipleTeams(context: Context, player_drop: Spinner?, current: Selector, teams: List<String>){
            RequestRouter.getPlayersMultipleTeams(context, player_drop, current, teams)
        }

    }

}