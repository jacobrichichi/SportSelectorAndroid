package models

class ThreeSelector (selectorType: String = "",
                     itemsSelected: Array<MutableList<String>> = arrayOf(mutableListOf(""),mutableListOf(""),mutableListOf("")),
                     isMultiSelect: Array<Int>)
    : Selector(selectorType, itemsSelected, isMultiSelect)