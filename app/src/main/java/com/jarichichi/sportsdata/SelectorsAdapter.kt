package com.jarichichi.sportsdata

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import models.Selector





class SelectorsAdapter(
    private val context: Context,
    var numSelectors: Int,
    private var cards: MutableList<Selector>
)
    : RecyclerView.Adapter<SelectorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view: View

        Log.d("onCreate", cards.get(cards.size-1).selectorType)

        when (cards.get(cards.size-1).selectorType){
            "SELECT_TEAM" -> view = LayoutInflater.from(context).inflate(R.layout.one_selector, parent, false)
            "SELECT_PLAYER" -> view = LayoutInflater.from(context).inflate(R.layout.three_selector, parent, false)
            "SELECT_POSITION" -> view = LayoutInflater.from(context).inflate(R.layout.two_selector, parent, false)
            "AGAINST_TEAM" -> view = LayoutInflater.from(context).inflate(R.layout.one_selector, parent, false)
            "TEMPERATURE" -> view = LayoutInflater.from(context).inflate(R.layout.two_selector, parent, false)
            else -> view = LayoutInflater.from(context).inflate(R.layout.two_selector, parent, false)  // PLAYER PLAYING
        }


        return ViewHolder(view)
    }

    /*override fun getItemViewType(position: Int): Int {
        return position % 3
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = numSelectors

    fun getSelectors() = cards


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if(cards.size > 0){

                val current : Selector = cards.get(position)

                // Certain Selectors are dependent upon the changing of other selectors, requiring
                // the view itself to be attached to each selector
                current.itemView = itemView

                if(current.selectorType == "SELECT_TEAM"
                    && current.itemsSelected[0].get(0) == ""
                    && current.itemsSelected[0].size < 2) {

                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)
                    team_drop.onItemSelectedListener = setItemSelected(current, 0)
                    SelectorFillers.selectTeam(itemView, context, current)
                }

                else if(current.selectorType == "SELECT_POSITION"
                    && current.itemsSelected[0].get(0) == ""
                    && current.itemsSelected[0].size < 2) {

                    val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
                    position_drop.onItemSelectedListener = setItemSelected(current, 0)
                    team_drop.onItemSelectedListener = setItemSelected(current, 1)

                    SelectorFillers.selectPosition(itemView, context, current)
                }

                else if(current.selectorType == "SELECT_PLAYER"
                    && current.itemsSelected[0].get(0) == ""
                    && current.itemsSelected[0].size < 2) {

                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_one)
                    val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_two)
                    val player_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_three)
                    team_drop.onItemSelectedListener = setItemSelected(current, 0, player_drop)
                    position_drop.onItemSelectedListener = setItemSelected(current, 1, player_drop)
                    player_drop.onItemSelectedListener = setItemSelected(current, 2)

                    SelectorFillers.selectPlayer(itemView, context, current)
                }

                else if(current.selectorType == "PLAYER_PLAYING"){
                    val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
                    val player_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
                    position_drop.onItemSelectedListener = setItemSelected(current, 0, player_drop)
                    player_drop.onItemSelectedListener = setItemSelected(current, 1)

                    SelectorFillers.fillWhenWithPlayerTeamsSelected(itemView, context, current)
                }

                else if(current.selectorType == "AGAINST_TEAM"){
                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)
                    team_drop.onItemSelectedListener = setItemSelected(current, 0)

                    SelectorFillers.fillWhenAgainstTeam(itemView, context, current)
                }

                else if(current.selectorType == "TEMPERATURE"){
                    val above_below_drop =itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
                    val temps_drop =itemView.findViewById<Spinner>(R.id.sel_drop_two_two)

                    above_below_drop.onItemSelectedListener = setItemSelected(current, 0)
                    temps_drop.onItemSelectedListener = setItemSelected(current, 1)

                    SelectorFillers.fillWhenTemperature(itemView, context, current)
                }

            }
        }

        fun setItemSelected(current : Selector, idx: Int, emptyDrop: Spinner? = null) : AdapterView.OnItemSelectedListener {
            return object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                            position: Int, id: Long) {

                    val item = parent?.getItemAtPosition(position).toString()

                    // If only one item can be selected, select it
                    if(current.isMultiSelect[idx] == 1){

                        // INITIAL DEFAULT POSITION BEING SELECTED IS SPECIAL CASE
                        if(current.itemsSelected[idx].size == 1){
                            current.itemsSelected[idx].add(item)
                        }
                        else{
                            current.itemsSelected[idx][1] = item
                        }

                        if(current.selectorType == "PLAYER_PLAYING" && idx == 0){

                            // SELECTION OF NEW POSITION MEANS PLAYERS NEED TO BE DESELECTED
                            current.itemsSelected[1] = mutableListOf("")

                            // KEEP UPDATED WHEN POSITION IS ADDED / REMOVED
                            // If a position was selected, the player drop selections need to be updated
                            SelectorFillers.fillMultipleTeams(context,
                                current.itemView?.findViewById<Spinner>(R.id.sel_drop_two_two),
                                current, cards.get(0).itemsSelected[0])

                        }


                        if ((current.selectorType == "SELECT_PLAYER")
                            && (current.itemsSelected[0][0] != "" && current.itemsSelected[1][0] != "")
                            && (emptyDrop != null)
                        ) {
                            SelectorFillers.fillSelectPlayer(context, emptyDrop, current, current.itemsSelected[0][0])

                            /*for(selector in cards){
                                if(selector.selectorType == "PLAYER_PLAYING" && selector.itemsSelected[0][0] != ""){
                                    SelectorFillers.fillSelectPlayer(context, emptyDrop, selector, current.itemsSelected[0][0])
                                }
                            }*/
                        }
                    }


                    //Multi selection logic
                    else if(current.isMultiSelect[idx] > 1){
                        if (current.itemsSelected[idx].contains(item)) {
                            current.itemsSelected[idx].remove(item)
                        }
                        else if (current.itemsSelected[idx].size < current.isMultiSelect[idx]) {
                            current.itemsSelected[idx].add(item)
                        }

                        // THESE FUNCTION TO KEEP PLAYER DROP UPDATED FOR HOW TEAM PLAYS WITH OTHER
                        // PLAYER PLAYING

                        // KEEP UPDATED WHEN TEAM IS ADDED/ REMOVED
                        if(current.selectorType == "SELECT_TEAM"){
                            for(selector in cards){
                                if(selector.selectorType == "PLAYER_PLAYING"){
                                    SelectorFillers.fillMultipleTeams(context,
                                        selector.itemView?.findViewById<Spinner>(R.id.sel_drop_two_two),
                                        selector, cards.get(0).itemsSelected[0])
                                }
                            }
                        }
                    }

                    // INITIALLY CERTAIN DROPDOWNS NEED NOTHING SELECTED, THIS ALLOWS THIS FEATURE
                    else if(current.selectorType == "SELECT_POSITION" && idx == 1){
                            current.isMultiSelect = arrayOf(2, 4)
                        }


                }

            }
        }
    }
}
