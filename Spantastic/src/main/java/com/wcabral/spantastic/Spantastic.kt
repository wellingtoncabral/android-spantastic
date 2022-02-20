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

import android.content.Context
import android.text.*
import androidx.annotation.*

/**
 *  Gives a `SpantasticBuilder` context to attach spans.
 *  How to use:
 *  ```
 *  // Making text
 *  val span = spantastic {
 *      +"This is an example using unary plus operator"
 *
 *      "This is an example using a string invoke" {
 *          ...
 *      }
 *
 *      text("This is an example using text function") {
 *          ...
 *      }
 *  }
 *  binding.tvSpannable.text = span
 *
 *  // Adding decorators
 *  val span = spantastic {
 *      "Text with bold, foreground and background" {
 *          bold()
 *          foreground(Color.WHITE)
 *          background(Color.BLACK)
 *      }
 *  }
 *
 *  // Adding nested texts
 *  val span = spantastic {
 *      "Text with bold " {
 *          bold()
 *          foreground(Color.WHITE)
 *          background(Color.BLACK)
 *      }
 *      "foreground " {
 *          foreground(Color.WHITE)
 *      }
 *      +"and "
 *      "background" {
 *          background(Color.BLACK)
 *      }
 *  }
 *
 *  ```
 */
fun spantastic(
    spannableStringBuilder: SpannableStringBuilder = SpannableStringBuilder(),
    builder: SpantasticBuilder.() -> Unit
) : SpannableStringBuilder {
    return SpantasticBuilder(spannableStringBuilder).apply(builder).build()
}

class SpantasticBuilder(private val spannableStringBuilder: SpannableStringBuilder) {

    private val spans = mutableListOf<Span>()

    private fun initSpan(
        text: CharSequence,
        init: Span.() -> Unit
    ): Span {
        return Span(text).apply {
            spans += this
            init()
        }
    }

    operator fun String.unaryPlus() = text(this)

    operator fun String.invoke(
        init: Span.() -> Unit = {}
    ): Span = initSpan(this, init)

    fun text(
        text: CharSequence,
        init: Span.() -> Unit = {}
    ): Span = initSpan(text, init)

    fun text(
        context: Context,
        @StringRes textResId: Int,
        init: Span.() -> Unit = {}
    ): Span = initSpan(context.getText(textResId), init)

    fun newLine() { +"\n" }

    fun build() : SpannableStringBuilder {
        spans.forEach { span ->
            span.build(spannableStringBuilder)
        }
        return spannableStringBuilder
    }

}

fun Editable.asSpannableStringBuilder() = this as SpannableStringBuilder
