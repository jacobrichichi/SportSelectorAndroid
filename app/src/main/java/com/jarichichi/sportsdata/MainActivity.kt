package com.jarichichi.sportsdata

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import models.Selector
import models.SelectorList

class MainActivity : AppCompatActivity() {
    private lateinit var selectorsView: RecyclerView
    private lateinit var spinner : Spinner
    private lateinit var addButton: Button
    private lateinit var selectorList: SelectorList
    private lateinit var selectorsAdapter: SelectorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectorsView = findViewById(R.id.selectors)
        addButton = findViewById(R.id.adding_button)

        selectorList = SelectorList()

        selectorsAdapter = SelectorsAdapter(this, 1, selectorList.selectors)
        selectorsView.adapter = selectorsAdapter

        selectorsView.setHasFixedSize(true)

        selectorsView.layoutManager = GridLayoutManager(this, 1)

        addButton.setOnClickListener {
            var types : List<String> = listOf("TEAM_SELECTION", "PLAYER_SELECTION")

            /*if(cards.size == 1) {
                types.add("TEAM_SELECTION")
                types.add("PLAYER_SELECTION")
            }*/

            val typeBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
            val modalView = LayoutInflater.from(this).inflate(R.layout.selector_type_modal, null)
            typeBuilder.setTitle("Select your Selector")

            var type_spinner : Spinner = modalView.findViewById(R.id.choose_type_spinner)

            ArrayAdapter.createFromResource(
                this,
                R.array.typeList,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                type_spinner.adapter = adapter
            }

            typeBuilder.setPositiveButton("OK"){
                dialog, which ->
                    if(type_spinner.getSelectedItem() == "Teams"){
                        selectorList.addFirstSelector("Teams")
                        selectorsAdapter.numSelectors += 1
                        selectorsAdapter.notifyDataSetChanged()

                    }

                    else{
                        selectorList.addFirstSelector("Player")
                        selectorsAdapter.numSelectors += 1
                        selectorsAdapter.notifyDataSetChanged()
                    }
            }

            typeBuilder.setView(modalView)

            val alertDialog = typeBuilder.create()
            alertDialog.show()


        }

    }
}