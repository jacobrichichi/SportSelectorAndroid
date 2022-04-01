package com.jarichichi.sportsdata

import android.content.Intent
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
    private lateinit var backButton: Button

    private var byGamePassing: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
    private var byGameRushReceive: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
    private var bySeasonPassing: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
    private var bySeasonRushReceive: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        statsLayout = findViewById<LinearLayout>(R.id.stats_layout)
        backButton = findViewById<Button>(R.id.stats_back_button)

        backButton.setOnClickListener {
            this.startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val response = intent.getStringExtra(SELECTORS_KEY)

        val data = JSONObject(response).getJSONObject("data")

        val type = data.getString("type")

        // Work around all Passing Tuples
        val playerPassingTuples : JSONArray = data.getJSONArray("playerPassingTuples")
        var parsedPassingTuples : MutableList<Map<String, String>> = mutableListOf()
        // Convert tuples to a workable format
        parsePassingTuples(playerPassingTuples, parsedPassingTuples)
        // {"Cardinals": [{"Name": "Kyler Murray", "Yards": ...}, {"Name" ...}], "Browns" :[{...}, {...}]}
        var seperatedPassingTuples : MutableMap<String, MutableList<Map<String, String>>> = mutableMapOf()
        seperateTuplesByTeam(parsedPassingTuples, seperatedPassingTuples, "TeamName")

        compilePassingByGame(parsedPassingTuples)

        // Work around all rush/receive tuples
        val playerRushReceiveTuples : JSONArray = data.getJSONArray("playerRushReceiveTuples")
        var parsedRushReceiveTuples: MutableList<Map<String, String>> = mutableListOf()
        // Convert tuples to a workable format
        parseRushReceiveTuples(playerRushReceiveTuples, parsedRushReceiveTuples)
        var seperatedRushReceiveTuples : MutableMap<String, MutableList<Map<String, String>>> = mutableMapOf()
        seperateTuplesByTeam(parsedRushReceiveTuples, seperatedRushReceiveTuples, "TeamName")

        compileRushReceiveByGame(parsedRushReceiveTuples)


        // Work around team tuples if need be
        var passingTeamList : MutableList<Map<String, String>>
        var seperatedTeamTuples : MutableMap<String, MutableList<Map<String, String>>>
        if(type == "Team"){
            val teamTuples : JSONArray = data.getJSONArray("teamTuples")
            var parsedTeamTuples : MutableList<Map<String, String>> = mutableListOf()
            parseTeamTuples(teamTuples, parsedTeamTuples)
            seperatedTeamTuples = mutableMapOf()
            seperateTuplesByTeam(parsedTeamTuples, seperatedTeamTuples, "OwnTeamName")

            for((team, rushReceiveTeamList) in seperatedRushReceiveTuples){
                if(seperatedPassingTuples.containsKey(team)){
                    passingTeamList = seperatedPassingTuples[team]!!
                }
                else{
                    passingTeamList = mutableListOf()
                }
                renderTeamTables(passingTeamList, rushReceiveTeamList, seperatedTeamTuples[team])
            }
        }

        else {
            for ((team, rushReceiveTeamList) in seperatedRushReceiveTuples) {
                if(seperatedPassingTuples.containsKey(team)){
                    passingTeamList = seperatedPassingTuples[team]!!
                }
                else{
                    passingTeamList = mutableListOf()
                }

                renderTeamTables(passingTeamList, rushReceiveTeamList)
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun parsePassingTuples(passingTuples: JSONArray, parsedPassingTuples: MutableList<Map<String, String>>){
        var i = 0
        while (i < passingTuples.length()) {
            val tuple : JSONObject = passingTuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "GameID" to tuple.getString("GameID"),
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

            parsedPassingTuples.add(parsedTuple)

            i++
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun parseRushReceiveTuples(rushReceiveTuples: JSONArray, parsedRushReceiveTuples: MutableList<Map<String, String>>){
        var i = 0
        while (i < rushReceiveTuples.length()) {
            val tuple : JSONObject = rushReceiveTuples.get(i) as JSONObject
            val parsedTuple : Map<String, String> = mapOf(
                "GameID" to tuple.getString("GameID"),
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

            parsedRushReceiveTuples.add(parsedTuple)

            i++
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun parseTeamTuples(teamTuples: JSONArray, parsedTeamTuples: MutableList<Map<String, String>>) {
        var i = 0
        while (i < teamTuples.length()) {

            val tuple : JSONObject = teamTuples.get(i) as JSONObject
            val currentGamePassing = byGamePassing[tuple.getString("GameID") + " " + tuple.getString("OwnTeamName")]
            val currentGameRushReceive = byGameRushReceive[tuple.getString("GameID") + " " + tuple.getString("OwnTeamName")]

            val parsedTuple : Map<String, String> = mapOf(
                "Week" to tuple.getInt("Week").toString(),
                "DayOfWeek" to tuple.getString("DayOfWeek"),
                "Date" to tuple.getString("Date"),
                "OwnTeamName" to tuple.getString("OwnTeamName"),
                "OwnPassingCompletions" to currentGamePassing!!["Completions"].toString(),
                "OwnPassingAttempts" to currentGamePassing!!["Attempts"].toString(),
                "OwnPassingYards" to currentGamePassing!!["Yards"].toString(),
                "OwnPassingTouchDowns" to currentGamePassing!!["TouchDowns"].toString(),
                "OwnPassingInterceptions" to currentGamePassing!!["Interceptions"].toString(),
                "OwnPassingSacks" to currentGamePassing!!["Sacks"].toString(),
                "OwnPassingSackYards" to currentGamePassing!!["SackYards"].toString(),
                "OwnRushAttempts" to currentGameRushReceive!!["RushAttempts"].toString(),
                "OwnRushYards" to currentGameRushReceive!!["RushYards"].toString(),
                "OwnRushTouchDowns" to currentGameRushReceive!!["RushTouchDowns"].toString(),
                "OwnReceptions" to currentGameRushReceive!!["Receptions"].toString(),
                "OwnReceivingYards" to currentGameRushReceive!!["ReceivingYards"].toString(),
                "OwnReceivingTouchDowns" to currentGameRushReceive!!["ReceivingTouchDowns"].toString(),
                "OwnFumbles" to currentGameRushReceive!!["Fumbles"].toString(),
                "OwnFumblesLost" to currentGameRushReceive!!["FumblesLost"].toString(),
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

            parsedTeamTuples.add(parsedTuple)

            i++
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun seperateTuplesByTeam(parsedTuples: MutableList<Map<String, String>>,
                             seperatedTuples: MutableMap<String, MutableList<Map<String, String>>>,
                             attrName: String){

        var lastTeam : String = ""
        var currentTeam : String
        var teamPlayerList: MutableList<Map<String, String>>

        for(tuple in parsedTuples){
            currentTeam = tuple[attrName].toString()

            // If this team has not yet been encountered, create a new list for this teams player
            // game logs
            if(currentTeam != lastTeam){
                teamPlayerList = mutableListOf(tuple)
                seperatedTuples.put(currentTeam, teamPlayerList)
                lastTeam = currentTeam
            }

            else{
                teamPlayerList = seperatedTuples.get(lastTeam)!!
                teamPlayerList.add(tuple)

            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun renderTeamTables(passingTuples: MutableList<Map<String, String>>,
                         rushReceiveTuples: MutableList<Map<String, String>>,
                         teamTuples: MutableList<Map<String, String>>? = null){

        renderPassing(passingTuples)
        renderRushReceive(rushReceiveTuples)

        if(teamTuples != null) {
            renderTeam(teamTuples)
        }




    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun compilePassingByGame(tuples: MutableList<Map<String, String>>){
        for(tuple in tuples) {

            if (byGamePassing.containsKey(tuple["GameID"] + " " + tuple["TeamName"])) {
                var currentGamePassing = byGamePassing[tuple["GameID"] + " " + tuple["TeamName"]]

                currentGamePassing?.set(
                    "Completions",
                    currentGamePassing.get("Completions")!! + tuple["Completions"]!!.toInt()
                )
                currentGamePassing?.set(
                    "Attempts",
                    currentGamePassing.get("Completions")!! + tuple["Attempts"]!!.toInt()
                )
                currentGamePassing?.set(
                    "Yards",
                    currentGamePassing.get("Completions")!! + tuple["Yards"]!!.toInt()
                )
                currentGamePassing?.set(
                    "TouchDowns",
                    currentGamePassing.get("Completions")!! + tuple["TouchDowns"]!!.toInt()
                )
                currentGamePassing?.set(
                    "Interceptions",
                    currentGamePassing.get("Completions")!! + tuple["Interceptions"]!!.toInt()
                )
                currentGamePassing?.set(
                    "Sacks",
                    currentGamePassing.get("Completions")!! + tuple["Sacks"]!!.toInt()
                )
                currentGamePassing?.set(
                    "SackYards",
                    currentGamePassing.get("Completions")!! + tuple["SackYards"]!!.toInt()
                )

            } else {
                var currentGamePassing: MutableMap<String, Int> = mutableMapOf(
                    "Completions" to tuple["Completions"]!!.toInt(),
                    "Attempts" to tuple["Attempts"]!!.toInt(),
                    "Yards" to tuple["Yards"]!!.toInt(),
                    "TouchDowns" to tuple["TouchDowns"]!!.toInt(),
                    "Interceptions" to tuple["Interceptions"]!!.toInt(),
                    "Sacks" to tuple["Sacks"]!!.toInt(),
                    "SackYards" to tuple["SackYards"]!!.toInt()
                )

                byGamePassing.put(tuple["GameID"] + " " + tuple["TeamName"], currentGamePassing)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun compileRushReceiveByGame(tuples: MutableList<Map<String, String>>){

        for(tuple in tuples) {

            if (byGameRushReceive.containsKey(tuple["GameID"] + " " + tuple["TeamName"])) {
                var currentGameRushReceive =
                    byGameRushReceive[tuple["GameID"] + " " + tuple["TeamName"]]

                currentGameRushReceive?.set(
                    "RushAttempts",
                    currentGameRushReceive.get("RushAttempts")!! + tuple["RushAttempts"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "RushYards",
                    currentGameRushReceive.get("RushYards")!! + tuple["RushYards"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "RushTouchDowns",
                    currentGameRushReceive.get("RushTouchDowns")!! + tuple["RushTouchDowns"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "Receptions",
                    currentGameRushReceive.get("Receptions")!! + tuple["Receptions"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "ReceivingYards",
                    currentGameRushReceive.get("ReceivingYards")!! + tuple["ReceivingYards"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "ReceivingTouchDowns",
                    currentGameRushReceive.get("ReceivingTouchDowns")!! + tuple["ReceivingTouchDowns"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "Fumbles",
                    currentGameRushReceive.get("Fumbles")!! + tuple["Fumbles"]!!.toInt()
                )
                currentGameRushReceive?.set(
                    "FumblesLost",
                    currentGameRushReceive.get("FumblesLost")!! + tuple["FumblesLost"]!!.toInt()
                )

            } else {
                var currentGameRushReceive = mutableMapOf(
                    "RushAttempts" to tuple["RushAttempts"]!!.toInt(),
                    "RushYards" to tuple["RushYards"]!!.toInt(),
                    "RushTouchDowns" to tuple["RushTouchDowns"]!!.toInt(),
                    "Receptions" to tuple["Receptions"]!!.toInt(),
                    "ReceivingYards" to tuple["ReceivingYards"]!!.toInt(),
                    "ReceivingTouchDowns" to tuple["ReceivingTouchDowns"]!!.toInt(),
                    "Fumbles" to tuple["Fumbles"]!!.toInt(),
                    "FumblesLost" to tuple["FumblesLost"]!!.toInt()
                )

                byGameRushReceive.put(
                    tuple["GameID"] + " " + tuple["TeamName"],
                    currentGameRushReceive
                )
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun renderPassing(tuples: MutableList<Map<String, String>>){
        val attrs = listOf( "Week", "TeamName", "Name", "OpposingTeamName", "Completions", "Attempts", "Yards", "TouchDowns", "Interceptions",
            "Sacks", "SackYards", "PassLong", "PasserRating")

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

        teamTable.setBackgroundColor(Color.WHITE)

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

        for(tuple in tuples) {
            row = TableRow(this)

            for(attr in attrs){
                row.addView(createCell(tuple[attr] as String))

            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.WHITE)
            //rowLayoutParams.setMargins(10,10, 10, 10)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun renderRushReceive(tuples: MutableList<Map<String, String>>){
        val attrs = listOf( "Week", "TeamName", "Name", "OpposingTeamName", "RushAttempts", "RushYards", "RushTouchDowns", "RushLong",
            "Targets", "Receptions", "ReceivingYards", "ReceivingTouchDowns", "Fumbles", "FumblesLost")

        var tableTitle: TextView = TextView(this)
        tableTitle.text = "Player Rushing and Receiving Info"
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

        teamTable.setBackgroundColor(Color.WHITE)

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

        for(tuple in tuples) {
            row = TableRow(this)

            for(attr in attrs) {
                row.addView(createCell(tuple[attr] as String))
            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.WHITE)
            //rowLayoutParams.setMargins(10,10, 10, 10)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun renderTeam(tuples : MutableList<Map<String, String>>){
        val attrs = listOf("Week", "DayOfWeek", "Date", "OwnTeamName", "OwnPassingCompletions", "OwnPassingAttempts",
            "OwnPassingYards", "OwnPassingTouchDowns", "OwnPassingInterceptions", "OwnPassingSacks",
            "OwnPassingSackYards", "OwnRushAttempts", "OwnRushYards", "OwnRushTouchDowns", "OwnReceptions",
            "OwnReceivingYards", "OwnReceivingTouchDowns", "OwnFumbles", "OwnFumblesLost",
            "OwnFirstQuarter","OwnSecondQuarter","OwnThirdQuarter",
            "OwnFourthQuarter","OwnOTTotal", "OwnTotalScore", "OwnFirstDowns", "OwnPenalties",
            "OwnPenaltyYards", "OwnThirdDownConversions", "OwnThirdDownAttempts", "OwnFourthDownConversions",
            "OwnFourthDownAttempts", "OwnToP",
            "OppTeamName", "OppFirstQuarter","OppSecondQuarter","OppThirdQuarter",
            "OppFourthQuarter","OppOTTotal", "OppTotalScore", "OppFirstDowns", "OppPenalties",
            "OppPenaltyYards", "OppThirdDownConversions", "OppThirdDownAttempts", "OppFourthDownConversions",
            "OppFourthDownAttempts", "OppToP")


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

        teamTable.setBackgroundColor(Color.WHITE)

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

        for(tuple in tuples){
            row = TableRow(this)

            for(attr in attrs){
                row.addView(createCell(tuple[attr] as String))
            }

            teamTable.addView(row)
            row.setBackgroundColor(Color.BLACK)


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