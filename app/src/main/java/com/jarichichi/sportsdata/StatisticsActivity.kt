package com.jarichichi.sportsdata

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.jarichichi.sportsdata.Constants.Companion.SELECTORS_KEY
import org.json.JSONArray
import org.json.JSONObject

class StatisticsActivity : AppCompatActivity() {

    private lateinit var statsLayout: LinearLayout
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        statsLayout = findViewById<LinearLayout>(R.id.stats_layout)

        val response = intent.getStringExtra(SELECTORS_KEY)

        val data = JSONObject(response).getJSONObject("data")

        val type = data.getString("type")

        val playerPassingTuples : JSONArray = data.getJSONArray("playerPassingTuples")
        val playerRushReceiveTuples : JSONArray = data.getJSONArray("playerRushReceiveTuples")


        renderPassing(playerPassingTuples)
        renderRushReceive(playerRushReceiveTuples)

        if(type == "Team") {
            val teamTuples : JSONArray = data.getJSONArray("teamTuples")
            renderTeam(teamTuples)
        }


        Log.d("hu", "hi")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun renderTeam(tuples : JSONArray){
        val attrs = listOf("Week", "DayOfWeek", "Date", "OwnTeamName", "OwnFirstQuarter","OwnSecondQuarter","OwnThirdQuarter",
            "OwnFourthQuarter","OwnOTTotal", "OwnTotalScore", "OwnFirstDowns", "OwnPenalties",
            "OwnPenaltyYards", "OwnThirdDownConversions", "OwnThirdDownAttempts", "OwnFourthDownConversions",
            "OwnFourthDownAttempts", "OwnToP",
            "OppTeamName", "OppFirstQuarter","OppSecondQuarter","OppThirdQuarter",
            "OppFourthQuarter","OppOTTotal", "OppTotalScore", "OppFirstDowns", "OppPenalties",
            "OppPenaltyYards", "OppThirdDownConversions", "OppThirdDownAttempts", "OppFourthDownConversions",
            "OppFourthDownAttempts", "OppToP")

        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "Week" to tuple.getInt("Week").toString(),
                "DayOfWeek" to tuple.getString("DayOfWeek"),
                "Date" to tuple.getString("Date"),
                "OwnTeamName" to tuple.getString("OwnTeamName"),
                "OwnFirstQuarter" to tuple.getInt("OwnFirstQuarter").toString(),
                "OwnSecondQuarter" to tuple.getInt("OwnSecondQuarter").toString(),
                "OwnThirdQuarter" to tuple.getInt("OwnThirdQuarter").toString(),
                "OwnFourthQuarter" to tuple.getInt("OwnFourthQuarter").toString(),
                "OwnOTTotal" to tuple.getInt("OwnOTTotal").toString(),
                "OwnTotalScore" to tuple.getInt("OwnTotalScore").toString(),
                "OwnFirstDowns" to tuple.getInt("OwnFirstDowns").toString(),
                "OwnPenalties" to tuple.getInt("OwnPenalties").toString(),
                "OwnPenaltyYards" to tuple.getInt("OwnFirstQuarter").toString(),
                "OwnThirdDownConversions" to tuple.getInt("OwnThirdQuarter").toString(),
                "OwnThirdDownAttempts" to tuple.getInt("OwnSecondQuarter").toString(),
                "OwnFourthDownConversions" to tuple.getInt("OwnFourthQuarter").toString(),
                "OwnFourthDownAttempts" to tuple.getInt("OwnOTTotal").toString(),
                "OwnToP" to tuple.getInt("OwnToP").toString(),
                "OppTeamName" to tuple.getString("OppTeamName"),
                "OppFirstQuarter" to tuple.getInt("OppFirstQuarter").toString(),
                "OppSecondQuarter" to tuple.getInt("OppSecondQuarter").toString(),
                "OppThirdQuarter" to tuple.getInt("OppThirdQuarter").toString(),
                "OppFourthQuarter" to tuple.getInt("OppFourthQuarter").toString(),
                "OppOTTotal" to tuple.getInt("OppOTTotal").toString(),
                "OppTotalScore" to tuple.getInt("OppTotalScore").toString(),
                "OppFirstDowns" to tuple.getInt("OppFirstDowns").toString(),
                "OppPenalties" to tuple.getInt("OppPenalties").toString(),
                "OppPenaltyYards" to tuple.getInt("OppFirstQuarter").toString(),
                "OppThirdDownConversions" to tuple.getInt("OppThirdQuarter").toString(),
                "OppThirdDownAttempts" to tuple.getInt("OppSecondQuarter").toString(),
                "OppFourthDownConversions" to tuple.getInt("OppFourthQuarter").toString(),
                "OppFourthDownAttempts" to tuple.getInt("OppOTTotal").toString(),
                "OppToP" to tuple.getInt("OppToP").toString(),
            )

            parsedTuples.add(parsedTuple)

            i++
        }

        var tableTitle: TextView = TextView(this)
        tableTitle.text = "Team Aggregate Info"
        tableTitle.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Medium)

        statsLayout.addView(tableTitle)

        var teamTable: TableLayout = TableLayout(this)
        var teamScrollView : HorizontalScrollView = HorizontalScrollView(this)
        var scrollLinear : LinearLayout = LinearLayout(this)


        scrollLinear.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        var scrollViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scrollViewParams.setMargins(20,20,20,20)
        teamScrollView.layoutParams = scrollViewParams

        teamTable.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

        teamTable.setBackgroundColor(Color.BLACK)

        scrollLinear.addView(teamTable)
        teamScrollView.addView(scrollLinear)
        statsLayout.addView(teamScrollView)

        var row : TableRow = TableRow(this)

        var rowLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT)
        rowLayoutParams.setMargins(1, 1, 1, 0)
        row.layoutParams = rowLayoutParams

        for(attr in attrs){
            row.addView(createCell(attr))
        }

        teamTable.addView(row)

        for(tuple in parsedTuples){
            row = TableRow(this)

            for(attr in attrs){
                row.addView(createCell(tuple[attr] as String))
            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)


        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun renderPassing(tuples: JSONArray){
        val attrs = listOf( "Week", "TeamName", "Name", "OpposingTeamName", "Completions", "Attempts", "Yards", "TouchDowns", "Interceptions",
            "Sacks", "SackYards", "PassLong", "PasserRating")

        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "Week" to tuple.getInt("Week").toString(),
                "TeamName" to tuple.getString("TeamName"),
                "Name" to tuple.getString("Name"),
                "OpposingTeamName" to tuple.getString("OpposingTeamName"),
                "Completions" to tuple.getInt("Completions").toString(),
                "Attempts" to tuple.getInt("Attempts").toString(),
                "Yards" to tuple.getInt("Yards").toString(),
                "TouchDowns" to tuple.getInt("TouchDowns").toString(),
                "Interceptions" to tuple.getInt("Interceptions").toString(),
                "Sacks" to tuple.getInt("Sacks").toString(),
                "SackYards" to tuple.getInt("SackYards").toString(),
                "PassLong" to tuple.getInt("PassLong").toString(),
                "PasserRating" to tuple.getDouble("PasserRating").toString()
            )

            parsedTuples.add(parsedTuple)

            i++
        }

        var tableTitle: TextView = TextView(this)
        tableTitle.text = "Player Passing Info"
        tableTitle.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Medium)

        statsLayout.addView(tableTitle)

        var teamTable: TableLayout = TableLayout(this)
        var teamScrollView : HorizontalScrollView = HorizontalScrollView(this)
        var scrollLinear : LinearLayout = LinearLayout(this)


        scrollLinear.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        var scrollViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scrollViewParams.setMargins(20,20,20,20)
        teamScrollView.layoutParams = scrollViewParams

        teamTable.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

        teamTable.setBackgroundColor(Color.BLACK)

        scrollLinear.addView(teamTable)
        teamScrollView.addView(scrollLinear)
        statsLayout.addView(teamScrollView)

        var row : TableRow = TableRow(this)

        var rowLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT)
        rowLayoutParams.setMargins(1, 1, 1, 0)
        row.layoutParams = rowLayoutParams

        for(attr in attrs){
            row.addView(createCell(attr))
        }

        teamTable.addView(row)

        for(tuple in parsedTuples) {
            row = TableRow(this)

            for(attr in attrs){
                row.addView(createCell(tuple[attr] as String))
            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)
            //rowLayoutParams.setMargins(10,10, 10, 10)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun renderRushReceive(tuples: JSONArray){
        val attrs = listOf( "Week", "TeamName", "Name", "OpposingTeamName", "RushAttempts", "RushYards", "RushTouchDowns", "RushLong",
            "Targets", "Receptions", "ReceivingYards", "ReceivingTouchDowns", "Fumbles", "FumblesLost")

        var parsedTuples = mutableListOf<Map<String,String>>()
        var i = 0
        while (i < tuples.length()) {
            val tuple : JSONObject = tuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "Week" to tuple.getInt("Week").toString(),
                "TeamName" to tuple.getString("TeamName"),
                "Name" to tuple.getString("Name"),
                "OpposingTeamName" to tuple.getString("OpposingTeamName"),
                "RushAttempts" to tuple.getInt("RushAttempts").toString(),
                "RushYards" to tuple.getInt("RushYards").toString(),
                "RushTouchDowns" to tuple.getInt("RushTouchDowns").toString(),
                "RushLong" to tuple.getInt("RushLong").toString(),
                "Targets" to tuple.getInt("Targets").toString(),
                "Receptions" to tuple.getInt("Receptions").toString(),
                "ReceivingYards" to tuple.getInt("ReceivingYards").toString(),
                "ReceivingTouchDowns" to tuple.getInt("ReceivingTouchDowns").toString(),
                "Fumbles" to tuple.getInt("Fumbles").toString(),
                "FumblesLost" to tuple.getInt("FumblesLost").toString(),
            )

            parsedTuples.add(parsedTuple)

            i++
        }

        var tableTitle: TextView = TextView(this)
        tableTitle.text = "Player Passing Info"
        tableTitle.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableTitle.setTextAppearance(R.style.TextAppearance_AppCompat_Medium)

        statsLayout.addView(tableTitle)

        var teamTable: TableLayout = TableLayout(this)
        var teamScrollView : HorizontalScrollView = HorizontalScrollView(this)
        var scrollLinear : LinearLayout = LinearLayout(this)


        scrollLinear.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        var scrollViewParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scrollViewParams.setMargins(20,20,20,20)
        teamScrollView.layoutParams = scrollViewParams

        teamTable.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

        teamTable.setBackgroundColor(Color.BLACK)

        scrollLinear.addView(teamTable)
        teamScrollView.addView(scrollLinear)
        statsLayout.addView(teamScrollView)

        var row : TableRow = TableRow(this)

        var rowLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT)
        rowLayoutParams.setMargins(1, 1, 1, 0)
        row.layoutParams = rowLayoutParams

        for(attr in attrs){
            row.addView(createCell(attr))
        }

        teamTable.addView(row)

        for(tuple in parsedTuples) {
            row = TableRow(this)

            for(attr in attrs) {
                row.addView(createCell(tuple[attr] as String))
            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)
            //rowLayoutParams.setMargins(10,10, 10, 10)
        }
    }

    fun createCell(text : String) : TextView{
        val tv : TextView = TextView(this)
        tv.setText(text)
        tv.setBackgroundColor(Color.WHITE)
        tv.setPadding(10,10,10,10)
        var tvLayoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT)
        tvLayoutParams.setMargins(0, 0, 2, 2)
        tv.layoutParams = tvLayoutParams
        return tv
    }


}