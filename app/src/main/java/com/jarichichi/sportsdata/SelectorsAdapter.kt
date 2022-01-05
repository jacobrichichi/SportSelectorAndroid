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
            else -> view = LayoutInflater.from(context).inflate(R.layout.two_selector, parent, false)
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


                if(current.selectorType == "SELECT_TEAM" && current.itemsSelected[0].get(0) == "") {
                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_one_one)
                    team_drop.onItemSelectedListener = setItemSelected(current, 0)
                    SelectorFillers.selectTeam(itemView, context, current)
                }

                else if(current.selectorType == "SELECT_POSITION" && current.itemsSelected[0].get(0) == "") {
                    val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_one)
                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_two_two)
                    position_drop.onItemSelectedListener = setItemSelected(current, 0)
                    team_drop.onItemSelectedListener = setItemSelected(current, 1)

                    SelectorFillers.selectPosition(itemView, context, current)
                }

                else if(current.selectorType == "SELECT_PLAYER" && current.itemsSelected[0].get(0) == "") {
                    val team_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_one)
                    val position_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_two)
                    val player_drop = itemView.findViewById<Spinner>(R.id.sel_drop_three_three)
                    team_drop.onItemSelectedListener = setItemSelected(current, 0)
                    position_drop.onItemSelectedListener = setItemSelected(current, 1)
                    player_drop.onItemSelectedListener = setItemSelected(current, 2)

                    SelectorFillers.selectTeam(itemView, context, current)
                }
            }
        }

        fun setItemSelected(current : Selector, idx: Int) : AdapterView.OnItemSelectedListener {
            return object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                            position: Int, id: Long) {

                    if(current.itemsSelected[idx].size < current.isMultiSelect[idx]) {
                        val item = parent?.getItemAtPosition(position).toString()

                        if(current.itemsSelected[idx].contains(item)){
                            current.itemsSelected[idx].remove(item)
                        }

                        else {
                            current.itemsSelected[idx].add(item)
                        }
                    }

                }

            }
        }
    }
}
