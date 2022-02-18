package com.wcabral.spantastic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.style.*
import android.view.View
import androidx.annotation.*
import androidx.annotation.IntRange

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
        return SpanImpl(text).apply {
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
