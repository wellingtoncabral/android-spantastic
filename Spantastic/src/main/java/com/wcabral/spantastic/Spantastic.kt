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

class SpantasticBuilder(
    private val spannableStringBuilder: SpannableStringBuilder
) {

    private val spans = mutableListOf<Span>()

    private fun <T : Span> initSpan(
        span: T,
        init: T.() -> Unit
    ): T {
        spans += span
        span.init()
        return span
    }

    operator fun String.unaryPlus() = text(this)

    operator fun String.invoke(
        init: Span.() -> Unit = {}
    ): Span = initSpan(Text(this), init)

    fun text(
        text: CharSequence,
        init: Span.() -> Unit = {}
    ): Span = initSpan(Text(text), init)

    fun text(
        context: Context,
        @StringRes textResId: Int,
        init: Span.() -> Unit = {}
    ): Span = initSpan(Text(context.getText(textResId)), init)

    fun quote(
        @ColorInt color: Int = Color.BLACK,
        @IntRange stripeWidth: Int = 0,
        @IntRange gapWidth: Int = 0,
        shouldBreakLine: Boolean = true,
        init: Span.() -> Unit = {}
    ): Span {
        if (shouldBreakLine) breakLine()
        return initSpan(Quote(color, stripeWidth, gapWidth), init)
    }

    fun bullet(
        @ColorInt color: Int = Color.BLACK,
        @IntRange gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
        @IntRange bulletRadius: Int = 4,
        shouldBreakLine: Boolean = true,
        init: Span.() -> Unit = {}
    ) : Span {
        if (shouldBreakLine) breakLine()
        return initSpan(Bullet(gapWidth, color, bulletRadius), init)
    }

    fun image(
        context: Context,
        @DrawableRes resourceId: Int,
        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(ImageFromResourceId(context, resourceId, verticalAlignment), init)

    fun image(
        context: Context,
        bitmap: Bitmap,
        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(ImageFromBitmap(context, bitmap, verticalAlignment), init)

    fun image(
        context: Context,
        uri: Uri,
        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(ImageFromUri(context, uri, verticalAlignment), init)

    fun image(
        drawable: Drawable,
        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(ImageFromDrawable(drawable, verticalAlignment), init)

    fun tab(
        @IntRange(from = 0) offset: Int,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(TabStop(offset), init)

    fun breakLine(): Span = initSpan(BreakLine(), {})

    fun setPosition(
        start: Int,
        end: Int,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(Position(start, end), init)

    fun build() : SpannableStringBuilder {
        spans.forEach { span ->
            span.build()

            val start: Int
            val end: Int

            if (span is Position) {
                start = span.start
                end = span.end
            } else {
                start = spannableStringBuilder.length
                spannableStringBuilder.append(span.text)
                end = spannableStringBuilder.length
            }

            span.items.forEach { style ->
                spannableStringBuilder.setSpan(
                    style,
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannableStringBuilder
    }

}

abstract class Span(val text: CharSequence) {

    val items = mutableListOf<Any>()

    fun addSpan(what: Any) {
        items += what
    }

    fun build() {
        items.add(0, initialDecorator())
    }

    abstract fun initialDecorator(): Any
}

internal class Text(text: CharSequence) : Span(text) {
    override fun initialDecorator() = Unit
}

internal class Position(
    val start: Int,
    val end: Int,
) : Span("") {
    override fun initialDecorator() = Unit
}

internal class Quote(
    @ColorInt private val color: Int = Color.BLACK,
    @IntRange private val stripeWidth: Int = 0,
    @IntRange private val gapWidth: Int = 0
) : Span(ONE_SPACE) {

    override fun initialDecorator() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        QuoteSpan(color, stripeWidth, gapWidth)
    } else {
        QuoteSpan(color)
    }

}

internal class Bullet(
    @IntRange private val gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
    @ColorInt private val color: Int = Color.BLACK,
    @IntRange private val bulletRadius: Int = 4
) : Span(ONE_SPACE) {

    override fun initialDecorator() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        BulletSpan(gapWidth, color, bulletRadius)
    } else {
        BulletSpan(gapWidth)
    }

}

private const val ONE_SPACE = " "
private const val BREAK_LINE = "\n"
private const val UNICODE_REPLACEMENT_CHAR = "\uFFFC" // Unicode replacement character

internal class ImageFromResourceId(
    private val context: Context,
    @DrawableRes private val resourceId: Int,
    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) : Span(UNICODE_REPLACEMENT_CHAR) {

    override fun initialDecorator() = ImageSpan(context, resourceId, verticalAlignment)

}

internal class ImageFromBitmap(
    private val context: Context,
    private val bitmap: Bitmap,
    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) : Span(UNICODE_REPLACEMENT_CHAR) {

    override fun initialDecorator() = ImageSpan(context, bitmap, verticalAlignment)

}

internal class ImageFromDrawable(
    private val drawable: Drawable,
    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) : Span(UNICODE_REPLACEMENT_CHAR) {

    override fun initialDecorator() = ImageSpan(drawable, verticalAlignment)

}

internal class ImageFromUri(
    private val context: Context,
    private val uri: Uri,
    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) : Span(UNICODE_REPLACEMENT_CHAR) {

    override fun initialDecorator() = ImageSpan(context, uri, verticalAlignment)

}

internal class TabStop(
    @IntRange(from = 0) private val offset: Int
) : Span("\t") {

    override fun initialDecorator() = TabStopSpan.Standard(offset)

}


internal class BreakLine : Span(BREAK_LINE) {
    override fun initialDecorator() = Unit
}

fun Span.bold() = apply { addSpan(StyleSpan(Typeface.BOLD)) }
fun Span.italic() = apply { addSpan(StyleSpan(Typeface.ITALIC)) }
fun Span.underline() = apply { addSpan(UnderlineSpan()) }
fun Span.url(url: String) = apply { addSpan(URLSpan(url)) }
fun Span.foreground(color: Int) = apply { addSpan(ForegroundColorSpan(color)) }
fun Span.background(color: Int) = apply { addSpan(BackgroundColorSpan(color)) }
fun Span.strike() = apply { addSpan(StrikethroughSpan()) }
fun Span.relativeSize(proportion: Float) = apply { addSpan(RelativeSizeSpan(proportion)) }
fun Span.absoluteSize(size: Int, isDip: Boolean = true) = apply { addSpan(AbsoluteSizeSpan(size, isDip)) }
fun Span.superscript() = apply { addSpan(SuperscriptSpan()) }
fun Span.subscript() = apply { addSpan(SubscriptSpan()) }
fun Span.style(style: Int) = apply { addSpan(StyleSpan(style)) }
fun Span.scaleX(proportion: Float) = apply { addSpan(ScaleXSpan(proportion)) }
fun Span.mask(filter: MaskFilter) = apply { addSpan(MaskFilterSpan(filter)) }
fun Span.leadingMargin(every: Int = 0) = apply { addSpan(LeadingMarginSpan.Standard(every)) }
fun Span.leadingMargin(first: Int = 0, rest: Int = 0) = apply { addSpan(LeadingMarginSpan.Standard(first, rest)) }
fun Span.align(align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL) = apply { addSpan(AlignmentSpan.Standard(align)) }
fun Span.drawableMargin(drawable: Drawable, padding: Int = 0) = apply { addSpan(DrawableMarginSpan(drawable, padding)) }
fun Span.iconMargin(bitmap: Bitmap, padding: Int = 0) = apply { addSpan(IconMarginSpan(bitmap, padding)) }

@RequiresApi(Build.VERSION_CODES.Q)
fun Span.lineHeight(@Px @IntRange(from = 1) heightInPx: Int) = apply { addSpan(LineHeightSpan.Standard(heightInPx)) }

@RequiresApi(Build.VERSION_CODES.P)
fun Span.typeface(typeface: Typeface) = apply { addSpan(TypefaceSpan(typeface)) }
fun Span.typeface(family: String) = apply { addSpan(TypefaceSpan(family)) }

fun Span.sansSerif() = apply { addSpan(TypefaceSpan("sans-serif")) }
fun Span.serif() = apply { addSpan(TypefaceSpan("serif")) }
fun Span.monospace() = apply { addSpan(TypefaceSpan("monospace")) }
fun Span.textAppearance(context: Context, appearance: Int) = apply { addSpan(TextAppearanceSpan(context, appearance)) }

fun Span.clickable(
    isUnderlineText: Boolean = true,
    onClick: (ClickableSpan) -> Unit
) = apply {
    addSpan(
        object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClick(this)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = isUnderlineText
            }
        }
    )
}

fun Editable.asSpannableStringBuilder() = this as SpannableStringBuilder
