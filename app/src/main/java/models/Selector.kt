package models

data class Selector (
    var selectorType: String = "",
    var itemsSelected: List<String> = listOf()
)