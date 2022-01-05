package models

class OneSelector (selectorType: String = "",
                   itemsSelected: Array<MutableList<String>> = arrayOf(mutableListOf("")),
                    isMultiSelect: Array<Int>)
                : Selector(selectorType, itemsSelected, isMultiSelect)