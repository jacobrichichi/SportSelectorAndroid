package com.jarichichi.sportsdata

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jarichichi.sportsdata.Constants.Companion.SELECTORS_KEY
import models.Selector
import models.SelectorList

class MainActivity : AppCompatActivity() {
    private lateinit var selectorsView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var submitButton: Button
    private lateinit var selectorList: SelectorList
    private lateinit var selectorsAdapter: SelectorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectorsView = findViewById(R.id.selectors)
        addButton = findViewById(R.id.adding_button)
        submitButton = findViewById(R.id.submit_button)

        selectorList = SelectorList()

        selectorsAdapter = SelectorsAdapter(this, 0, selectorList.selectors)
        selectorsView.adapter = selectorsAdapter

        selectorsView.setHasFixedSize(true)

        selectorsView.layoutManager = GridLayoutManager(this, 1)

        addButton.setOnClickListener {

            val typeBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val modalView = LayoutInflater.from(this).inflate(R.layout.selector_type_modal, null)


            var type_spinner : Spinner = modalView.findViewById(R.id.choose_type_spinner)
            var options : Array<String>
            if(selectorsAdapter.getItemCount() == 0){
                typeBuilder.setTitle("From where would you like stats retrieved?")
                options = arrayOf("Teams", "Players", "Positions")
            }

            else{
                typeBuilder.setTitle("And what conditions would you like?")
                options = arrayOf("When Against Team", "When Against Player", "Temperature",
                    "Winning Streak", "Losing Streak", "Certain Level of Defense", "This Teammate Is Playing",
                    "This Teammate Isn't Playing")
            }


            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                options
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                type_spinner.adapter = adapter
            }

            typeBuilder.setPositiveButton("OK"){
                dialog, which ->

                    when(type_spinner.getSelectedItem()){
                        "Teams" -> selectorList.addNewSelector("SELECT_TEAM", arrayOf(6),1)
                        "Players" -> selectorList.addNewSelector("SELECT_PLAYER", arrayOf(1, 1, 20), 3)
                        "Positions" -> selectorList.addNewSelector("SELECT_POSITION",arrayOf(2, 4),  2)
                        "When Against Team" -> selectorList.addNewSelector("AGAINST_TEAM", arrayOf(6),  1)
                        "Temperature" -> selectorList.addNewSelector("TEMPERATURE", arrayOf(2),  2)
                        "Winning Streak" -> selectorList.addNewSelector("WINNING_STREAK", arrayOf(1), 1)
                        "Losing Streak" -> selectorList.addNewSelector("LOSING_STREAK", arrayOf(1),  1)
                        "Certain Level of Defense" -> selectorList.addNewSelector("DEFENSIVE_RATING", arrayOf(1),  1)
                        "When Player Is Playing" -> selectorList.addNewSelector("PLAYER_PLAYING", arrayOf(3),  3)
                        "Player Is Not Playing" -> selectorList.addNewSelector("PLAYER_ABSENT", arrayOf(3),  3)
                    }

                    selectorsAdapter.numSelectors += 1
                    selectorsAdapter.notifyDataSetChanged()


            }

            typeBuilder.setView(modalView)

            val alertDialog = typeBuilder.create()
            alertDialog.show()


        }

        submitButton.setOnClickListener {
            var selectors = selectorsAdapter.getSelectors()

            //Logic to begin creating the SQL Query + send it to the backend
            SelectorFillers.createCustomQuery(this, selectors)

        }

    }
}