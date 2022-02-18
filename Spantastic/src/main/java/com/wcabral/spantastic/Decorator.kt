package com.wcabral.spantastic

import android.text.Spannable

open class Decorator(
    val what: Any,
    val fillOnlyOnePosition: Boolean = false,
    var start: Int = -1,
    var end: Int = -1,
    var flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
)
