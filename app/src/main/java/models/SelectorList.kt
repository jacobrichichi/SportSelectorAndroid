package models

import android.util.Log

class SelectorList {

    var selectors: MutableList<Selector>

    init {
        selectors = mutableListOf()
    }


    fun addNewSelector(adapter_type: String, isMultiSelect: Array<Int>, num_spinners: Int){
        if(num_spinners == 1) {
            selectors.add(OneSelector(adapter_type, isMultiSelect=isMultiSelect))
        }
        else if(num_spinners == 2) {
            selectors.add(TwoSelector(adapter_type, isMultiSelect=isMultiSelect))
        }
        else{
            selectors.add(ThreeSelector(adapter_type, isMultiSelect=isMultiSelect))
        }
    }

}