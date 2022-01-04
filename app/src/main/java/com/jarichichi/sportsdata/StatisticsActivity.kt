package com.jarichichi.sportsdata

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jarichichi.sportsdata.Constants.Companion.SELECTORS_KEY
import org.json.JSONArray
import org.json.JSONObject

class StatisticsActivity : AppCompatActivity() {

    private lateinit var statsLayout: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        statsLayout = findViewById<ConstraintLayout>(R.id.stats_layout)

        val response = intent.getStringExtra(SELECTORS_KEY)

        val data = JSONObject(response).getJSONObject("data")

        val type = data.getString("type")
        val tuples : JSONArray = data.getJSONArray("tuples")

        if(type == "Team") {
            renderTeam(tuples)
        }


        Log.d("hu", "hi")
    }

    fun renderTeam(tuples : JSONArray){

        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "TeamName" to tuple.getString("TeamName"),
                "Points" to tuple.getInt("Points").toString(),
                "Yards" to tuple.getInt("Yards").toString(),
                "TOs" to tuple.getInt("TOs").toString(),
                "PassYards" to tuple.getInt("PassYards").toString(),
                "PassTDs" to tuple.getInt("PassTDs").toString(),
                "RushYards" to tuple.getInt("RushYards").toString(),
                "RushTDs" to tuple.getInt("RushTDs").toString()
            )

            parsedTuples.add(parsedTuple)

            i++
        }

        var teamTable: TableLayout = TableLayout(this)
        var teamScrollView : HorizontalScrollView = HorizontalScrollView(this)
        var scrollLinear : LinearLayout = LinearLayout(this)


        scrollLinear.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        teamScrollView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        teamTable.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

        teamTable.setBackgroundColor(Color.BLACK)

        scrollLinear.addView(teamTable)
        teamScrollView.addView(scrollLinear)
        statsLayout.addView(teamScrollView)

        var row : TableRow = TableRow(this)

        var rowLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT)
        rowLayoutParams.setMargins(1, 1, 1, 0)
        row.layoutParams = rowLayoutParams

        row.addView(createCell("TeamName"))
        row.addView(createCell("Points"))
        row.addView(createCell("Yards"))
        row.addView(createCell("TOs"))
        row.addView(createCell("PassYards"))
        row.addView(createCell("PassTDs"))
        row.addView(createCell("RushYards"))
        row.addView(createCell("RushTDs"))

        teamTable.addView(row)

        for(tuple in parsedTuples){
            row = TableRow(this)

            row.addView(createCell(tuple["TeamName"] as String))
            row.addView(createCell(tuple["Points"] as String))
            row.addView(createCell(tuple["Yards"] as String))
            row.addView(createCell(tuple["TOs"] as String))
            row.addView(createCell(tuple["PassYards"] as String))
            row.addView(createCell(tuple["PassTDs"] as String))
            row.addView(createCell(tuple["RushYards"] as String))
            row.addView(createCell(tuple["RushTDs"] as String))

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)
            //rowLayoutParams.setMargins(10,10, 10, 10)


        }


    }

    fun createCell(text : String) : TextView{
        val tv : TextView = TextView(this)
        tv.setText(text)
        tv.setBackgroundColor(Color.WHITE)
        var tvLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
        tvLayoutParams.setMargins(0, 0, 2, 2)
        tv.layoutParams = tvLayoutParams
        return tv
    }


}