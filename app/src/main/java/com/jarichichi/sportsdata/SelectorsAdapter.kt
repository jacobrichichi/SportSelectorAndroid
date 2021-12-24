package com.jarichichi.sportsdata

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            if(cards.size > 0){

                val current : Selector = cards.get(cards.size-1)


                if(current.selectorType == "SELECT_TEAM") {
                    SelectorFillers.selectTeam(itemView, context)
                }
            }
        }
    }
}
