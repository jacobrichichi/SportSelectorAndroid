package com.jarichichi.sportsdata

class Constants{
    companion object{
        private const val ROOT_URL: String = "http://10.0.1.66:8000/Android/v1/"
        const val URL_GETALLTEAMS: String = ROOT_URL + "getAllTeams.php"
        const val SELECTORS_KEY : String = "SELECTORS"
    }
}