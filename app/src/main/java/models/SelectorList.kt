package models

import android.util.Log

class SelectorList {

    var selectors: MutableList<Selector>

    init {
        selectors = mutableListOf()
    }


    fun addNewSelector(adapter_type: String){
        Log.d("TAG", "AddNewSelector")
        selectors.add(Selector("Teams", listOf("")))
    }

    fun addFirstSelector(adapter_type: String){
        Log.d("TAG", "AddNewSelector")
        selectors.add(Selector("Teams", listOf("")))
    }



}