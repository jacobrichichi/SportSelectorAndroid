package models

import android.util.Log

class SelectorList {

    var selectors: MutableList<Selector>

    init {
        selectors = mutableListOf()
    }


    fun addNewSelector(adapter_type: String, num_spinners: Int){
        if(num_spinners == 1) {
            selectors.add(OneSelector(adapter_type, listOf("")))
        }
        else if(num_spinners == 2) {
            selectors.add(TwoSelector(adapter_type, listOf("")))
        }
        else{
            selectors.add(ThreeSelector(adapter_type, listOf("")))
        }
    }

}