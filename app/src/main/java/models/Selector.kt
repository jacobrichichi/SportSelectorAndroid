package models

open class Selector (var selectorType: String = "",
                     var itemsSelected: Array<MutableList<String>>,
                     var isMultiSelect: Array<Int>
)