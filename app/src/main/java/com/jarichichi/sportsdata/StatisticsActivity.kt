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

    private lateinit var statsLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        statsLayout = findViewById<LinearLayout>(R.id.stats_layout)

        val response = intent.getStringExtra(SELECTORS_KEY)

        val data = JSONObject(response).getJSONObject("data")

        val type = data.getString("type")

        val playerPassingTuples : JSONArray = data.getJSONArray("playerPassingTuples")
        val playerRushReceiveTuples : JSONArray = data.getJSONArray("playerRushReceiveTuples")

        if(type == "Team") {
            val teamTuples : JSONArray = data.getJSONArray("teamTuples")
            renderTeam(teamTuples)
        }

        renderPassing(playerPassingTuples)
        renderRushReceive(playerRushReceiveTuples)


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

    fun renderPassing(tuples: JSONArray){
        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "Year" to tuple.getInt("Year").toString(),
                "Team" to tuple.getString("Team"),
                "Name" to tuple.getString("Name"),
                "Age" to tuple.getInt("Age").toString(),
                "Position" to tuple.getString("Position"),
                "Games" to tuple.getInt("Games").toString(),
                "GamesStarted" to tuple.getInt("GamesStarted").toString(),
                "QBRecord" to tuple.getString("QBRecord"),
                "Completions" to tuple.getInt("Completions").toString(),
                "PassAttempts" to tuple.getInt("PassAttempts").toString(),
                "PassingYards" to tuple.getInt("PassingYards").toString(),
                "PassingTDs" to tuple.getInt("PassingTDs").toString(),
                "PassingInterceptions" to tuple.getInt("PassingInterceptions").toString(),
                "LongestPass" to tuple.getInt("LongestPass").toString(),
                "PasserRating" to tuple.getDouble("PasserRating").toString(),
                "QBR" to tuple.getDouble("QBR").toString(),
                "Comebacks" to tuple.getInt("Comebacks").toString(),
                "GWD" to tuple.getInt("GWD").toString()
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

        row.addView(createCell("Year"))
        row.addView(createCell("Team"))
        row.addView(createCell("Name"))
        row.addView(createCell("Age"))
        row.addView(createCell("Position"))
        row.addView(createCell("Games"))
        row.addView(createCell("GamesStarted"))
        row.addView(createCell("QBRecord"))
        row.addView(createCell("Completions"))
        row.addView(createCell("PassAttempts"))
        row.addView(createCell("PassingYards"))
        row.addView(createCell("PassingTDs"))
        row.addView(createCell("PassingInterceptions"))
        row.addView(createCell("LongestPass"))
        row.addView(createCell("PasserRating"))
        row.addView(createCell("QBR"))
        row.addView(createCell("Comebacks"))
        row.addView(createCell("GWD"))

        teamTable.addView(row)

        for(tuple in parsedTuples) {
            row = TableRow(this)

            row.addView(createCell(tuple["Year"] as String))
            row.addView(createCell(tuple["Team"] as String))
            row.addView(createCell(tuple["Name"] as String))
            row.addView(createCell(tuple["Age"] as String))
            row.addView(createCell(tuple["Position"] as String))
            row.addView(createCell(tuple["Games"] as String))
            row.addView(createCell(tuple["GamesStarted"] as String))
            row.addView(createCell(tuple["QBRecord"] as String))
            row.addView(createCell(tuple["Completions"] as String))
            row.addView(createCell(tuple["PassAttempts"] as String))
            row.addView(createCell(tuple["PassingYards"] as String))
            row.addView(createCell(tuple["PassingTDs"] as String))
            row.addView(createCell(tuple["PassingInterceptions"] as String))
            row.addView(createCell(tuple["LongestPass"] as String))
            row.addView(createCell(tuple["PasserRating"] as String))
            row.addView(createCell(tuple["QBR"] as String))
            row.addView(createCell(tuple["Comebacks"] as String))
            row.addView(createCell(tuple["GWD"] as String))

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)
            //rowLayoutParams.setMargins(10,10, 10, 10)
        }
    }

    fun renderRushReceive(tuples: JSONArray){
        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "Year" to tuple.getInt("Year").toString(),
                "Team" to tuple.getString("Team"),
                "Name" to tuple.getString("Name"),
                "Age" to tuple.getInt("Age").toString(),
                "Position" to tuple.getString("Position"),
                "Games" to tuple.getInt("Games").toString(),
                "GamesStarted" to tuple.getInt("GamesStarted").toString(),
                "RushAttempts" to tuple.getInt("RushAttempts").toString(),
                "RushYards" to tuple.getInt("RushYards").toString(),
                "RushTDs" to tuple.getInt("RushTDs").toString(),
                "RushLong" to tuple.getInt("RushLong").toString(),
                "Targets" to tuple.getInt("Targets").toString(),
                "Receptions" to tuple.getInt("Receptions").toString(),
                "ReceivingYards" to tuple.getInt("ReceivingYards").toString(),
                "ReceivingTDs" to tuple.getInt("ReceivingTDs").toString(),
                "ReceivingLong" to tuple.getInt("ReceivingLong").toString(),
                "Fumbles" to tuple.getInt("Fumbles").toString()
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

        row.addView(createCell("Year"))
        row.addView(createCell("Team"))
        row.addView(createCell("Name"))
        row.addView(createCell("Age"))
        row.addView(createCell("Position"))
        row.addView(createCell("Games"))
        row.addView(createCell("GamesStarted"))
        row.addView(createCell("RushAttempts"))
        row.addView(createCell("RushYards"))
        row.addView(createCell("RushTDs"))
        row.addView(createCell("RushLong"))
        row.addView(createCell("Targets"))
        row.addView(createCell("Receptions"))
        row.addView(createCell("ReceivingYards"))
        row.addView(createCell("ReceivingTDs"))
        row.addView(createCell("ReceivingLong"))
        row.addView(createCell("Fumbles"))

        teamTable.addView(row)

        for(tuple in parsedTuples) {
            row = TableRow(this)

            row.addView(createCell(tuple["Year"] as String))
            row.addView(createCell(tuple["Team"] as String))
            row.addView(createCell(tuple["Name"] as String))
            row.addView(createCell(tuple["Age"] as String))
            row.addView(createCell(tuple["Position"] as String))
            row.addView(createCell(tuple["Games"] as String))
            row.addView(createCell(tuple["GamesStarted"] as String))
            row.addView(createCell(tuple["RushAttempts"] as String))
            row.addView(createCell(tuple["RushYards"] as String))
            row.addView(createCell(tuple["RushTDs"] as String))
            row.addView(createCell(tuple["RushLong"] as String))
            row.addView(createCell(tuple["Targets"] as String))
            row.addView(createCell(tuple["Receptions"] as String))
            row.addView(createCell(tuple["ReceivingYards"] as String))
            row.addView(createCell(tuple["ReceivingTDs"] as String))
            row.addView(createCell(tuple["ReceivingLong"] as String))
            row.addView(createCell(tuple["Fumbles"] as String))

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