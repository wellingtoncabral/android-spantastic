/*
 * Copyright (C) 2022 Wellington Cabral da Silva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wcabral.spantastic

import android.graphics.Color
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
