package com.wcabral.spantastic

import android.text.Layout

fun SpantasticBuilder.h1(text: String) {
    text {
        bold()
        absoluteSize(20)
        align(Layout.Alignment.ALIGN_CENTER)
        divider()
    }
}

fun SpantasticBuilder.title(text: String, shouldBreakLine: Boolean = true) {
    text {
        absoluteSize(10)
        if (shouldBreakLine) breakLine()
    }
}

fun SpantasticBuilder.divider() {
    breakLine()
    breakLine()
}
