package com.jarichichi.sportsdata

class Constants{
    companion object{
        private const val ROOT_URL: String = "http://172.19.16.1:8000/Android/v1/"
        const val URL_GETALLTEAMS: String = ROOT_URL + "getAllTeams.php"
        const val URL_GETCERTAINPLAYERS: String = ROOT_URL + "getCertainPlayers.php"
        const val URL_GETPLAYERSMULTIPLETEAMS: String = ROOT_URL + "getPlayersMultipleTeams.php"
        const val URL_CUSTOMQUERY: String = ROOT_URL + "customQuery.php"

        const val SELECTORS_KEY : String = "SELECTORS"
    }
}