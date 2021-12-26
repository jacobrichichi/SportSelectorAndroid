package com.jarichichi.sportsdata

class Constants{
    companion object{
        private const val ROOT_URL: String = "http://192.168.1.74:8000/Android/v1/"
        const val URL_GETALLTEAMS: String = ROOT_URL + "getAllTeams.php"
        const val URL_CUSTOMQUERY: String = ROOT_URL + "customQuery.php"

        const val SELECTORS_KEY : String = "SELECTORS"
    }
}