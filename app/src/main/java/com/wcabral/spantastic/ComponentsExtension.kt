package com.wcabral.spantastic

import android.text.Layout
import androidx.annotation.IntRange

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
        if (shouldBreakLine) newLine()
    }
}

fun SpantasticBuilder.divider() {
    newLine()
    newLine()
}

fun SpantasticBuilder.tabGroup(
    @IntRange(from = 0) offset: Int,
    init: () -> Unit = {}
) {
    "\t" {
        tab(offset)
        init()
    }
}
