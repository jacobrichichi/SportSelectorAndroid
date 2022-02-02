package models

import android.view.View

open class Selector (var selectorType: String = "",
                     var itemsSelected: Array<MutableList<String>>,
                     var isMultiSelect: Array<Int>,
                     var itemView: View? = null
)