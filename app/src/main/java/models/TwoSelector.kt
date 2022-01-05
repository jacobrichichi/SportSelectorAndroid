package models

class TwoSelector (selectorType: String = "",
                   itemsSelected: Array<MutableList<String>> = arrayOf(mutableListOf(""),mutableListOf("")),
                   isMultiSelect: Array<Int>)
        : Selector(selectorType, itemsSelected, isMultiSelect)