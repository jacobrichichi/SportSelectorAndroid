package com.jarichichi.sportsdata

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import models.Selector


class SelectorsAdapter(
    private val context: Context,
    var numSelectors: Int,
    private var cards: MutableList<Selector>
)
    : RecyclerView.Adapter<SelectorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.selector, parent, false)

        /*val spinner = view.findViewById<Spinner>(R.id.drop_down)

        ArrayAdapter.createFromResource(
            context!!,
            R.array.team_names,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }*/

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = numSelectors


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val dropdown = itemView.findViewById<Spinner>(R.id.selector_spinner)

        fun bind(position: Int) {
            if(cards.size > 0) {

                //Adding the first selector
                if(cards.size == 1){
                    val current: Selector = cards.get(cards.size - 1)
                    Log.d("TAG", position.toString())
                    ArrayAdapter.createFromResource(
                        context,
                        R.array.teamNames,
                        android.R.layout.simple_spinner_item
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        dropdown.adapter = adapter
                    }
                }
                val current: Selector = cards.get(cards.size - 1)
                Log.d("TAG", position.toString())
                ArrayAdapter.createFromResource(
                    context,
                    R.array.teamNames,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dropdown.adapter = adapter
                }
            }
        }
    }
}
