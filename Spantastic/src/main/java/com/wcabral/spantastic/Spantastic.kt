package com.wcabral.spantastic

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.*
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

//    fun image(
//        context: Context,
//        @DrawableRes resourceId: Int,
//        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
//    ) : Span = initSpan(ImageFromResourceId(context, resourceId, verticalAlignment), {})

//    fun image(
//        context: Context,
//        bitmap: Bitmap,
//        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
//        init: Span.() -> Unit = {}
//    ) : Span = initSpan(ImageFromBitmap(context, bitmap, verticalAlignment), init)
//
//    fun image(
//        context: Context,
//        uri: Uri,
//        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
//        init: Span.() -> Unit = {}
//    ) : Span = initSpan(ImageFromUri(context, uri, verticalAlignment), init)
//
//    fun image(
//        drawable: Drawable,
//        verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
//        init: Span.() -> Unit = {}
//    ) : Span = initSpan(ImageFromDrawable(drawable, verticalAlignment), init)
//
//    fun tab(
//        @IntRange(from = 0) offset: Int,
//        init: Span.() -> Unit = {}
//    ) : Span = initSpan(TabStop(offset), init)

    fun breakLine(): Span = initSpan(BreakLine(), {})

    fun setPosition(
        start: Int,
        end: Int,
        init: Span.() -> Unit = {}
    ) : Span = initSpan(Position(start, end), init)

    fun build() : SpannableStringBuilder {
        spans.forEach { span ->
            span.build(spannableStringBuilder)
        }
        return spannableStringBuilder
    }

}

interface Decorator {
    fun onMeasuringPositions(length: Int)
}

open class SpanDecorator(
    val what: Any,
    val text: CharSequence = "",
    val start: Int = -1,
    val end: Int = -1,
    val flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
) : Decorator {

    var measuringStart: Int = start
    private set

    var measuringEnd: Int = end
    private set

    override fun onMeasuringPositions(length: Int) {
        if (start != -1 && end != -1) {
            measuringStart = start
            measuringEnd = end
        } else {
            measuringStart = length
            measuringEnd = measuringStart + text.length
        }
    }

}

abstract class Span {

    private val items = mutableListOf<SpanDecorator>()

    fun addDecorator(what: SpanDecorator) {
        items += what
    }

    private fun decorate(
        decorator: SpanDecorator,
        spannableStringBuilder: SpannableStringBuilder
    ) {
        spannableStringBuilder.apply {
            append(decorator.text)
            setSpan(
                decorator.what,
                decorator.measuringStart,
                decorator.measuringEnd,
                decorator.flags
            )
        }
    }

    private fun decorateItems(
        spannableStringBuilder: SpannableStringBuilder
    ) {
        items.forEach { decorator ->
            decorate(decorator, spannableStringBuilder)
        }
    }

    fun build(spannableStringBuilder: SpannableStringBuilder) {
        val decorator = initialDecorator()
        decorator.onMeasuringPositions(spannableStringBuilder.length)
        decorate(
            decorator = decorator,
            spannableStringBuilder = spannableStringBuilder
        )
        decorateItems(spannableStringBuilder)
    }

    abstract fun initialDecorator(): SpanDecorator
}

internal class Text(private val text: CharSequence) : Span() {
    override fun initialDecorator() = SpanDecorator(
        what = Unit,
        text = text
    )
}

internal class Position(
    private val start: Int,
    private val end: Int,
) : Span() {
    override fun initialDecorator() = SpanDecorator(
        what = Unit,
        start = start,
        end = end
    )
}

internal class Quote(
    @ColorInt private val color: Int = Color.BLACK,
    @IntRange private val stripeWidth: Int = 0,
    @IntRange private val gapWidth: Int = 0
) : Span() {

    override fun initialDecorator() = SpanDecorator(
        what = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            QuoteSpan(color, stripeWidth, gapWidth)
        } else {
            QuoteSpan(color)
        },
        text = " "
    )

}

internal class Bullet(
    @IntRange private val gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
    @ColorInt private val color: Int = Color.BLACK,
    @IntRange private val bulletRadius: Int = 4
) : Span() {

    override fun initialDecorator() = SpanDecorator(
        what = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BulletSpan(gapWidth, color, bulletRadius)
        } else {
            BulletSpan(gapWidth)
        },
        text = " "
    )

}

private const val ONE_SPACE = " "
private const val BREAK_LINE = "\n"
private const val UNICODE_REPLACEMENT_CHAR = "\uFFFC" // Unicode replacement character

internal class ImageFromResourceId(
    private val context: Context,
    @DrawableRes private val resourceId: Int,
    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) : Span() {

    override fun initialDecorator() = SpanDecorator(
        what = ImageSpan(context, resourceId, verticalAlignment),
        text = UNICODE_REPLACEMENT_CHAR
    )

}

//internal class ImageFromBitmap(
//    private val context: Context,
//    private val bitmap: Bitmap,
//    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
//) : Span(UNICODE_REPLACEMENT_CHAR) {
//
//    override fun initialDecorator() = ImageSpan(context, bitmap, verticalAlignment)
//
//}
//
//internal class ImageFromDrawable(
//    private val drawable: Drawable,
//    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
//) : Span(UNICODE_REPLACEMENT_CHAR) {
//
//    override fun initialDecorator() = ImageSpan(drawable, verticalAlignment)
//
//}
//
//internal class ImageFromUri(
//    private val context: Context,
//    private val uri: Uri,
//    private val verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
//) : Span(UNICODE_REPLACEMENT_CHAR) {
//
//    override fun initialDecorator() = ImageSpan(context, uri, verticalAlignment)
//
//}
//
//internal class TabStop(
//    @IntRange(from = 0) private val offset: Int
//) : Span("\t") {
//
//    override fun initialDecorator() = TabStopSpan.Standard(offset)
//
//}

internal class BreakLine : Span() {

    override fun initialDecorator() = SpanDecorator(
        what = Unit,
        text = BREAK_LINE
    )

}

fun Span.bold() = apply { addDecorator(SpanDecorator(StyleSpan(Typeface.BOLD))) }
fun Span.italic() = apply { addDecorator(SpanDecorator(StyleSpan(Typeface.ITALIC))) }
fun Span.underline() = apply { addDecorator(SpanDecorator(UnderlineSpan())) }
//fun Span.url(url: String) = apply { addDecorator(URLSpan(url)) }
//fun Span.foreground(color: Int) = apply { addDecorator(ForegroundColorSpan(color)) }
//fun Span.background(color: Int) = apply { addDecorator(BackgroundColorSpan(color)) }
//fun Span.strike() = apply { addDecorator(StrikethroughSpan()) }
//fun Span.relativeSize(proportion: Float) = apply { addDecorator(RelativeSizeSpan(proportion)) }
//fun Span.absoluteSize(size: Int, isDip: Boolean = true) = apply { addDecorator(AbsoluteSizeSpan(size, isDip)) }
//fun Span.superscript() = apply { addDecorator(SuperscriptSpan()) }
//fun Span.subscript() = apply { addDecorator(SubscriptSpan()) }
//fun Span.style(style: Int) = apply { addDecorator(StyleSpan(style)) }
//fun Span.scaleX(proportion: Float) = apply { addDecorator(ScaleXSpan(proportion)) }
//fun Span.mask(filter: MaskFilter) = apply { addDecorator(MaskFilterSpan(filter)) }
//fun Span.leadingMargin(every: Int = 0) = apply { addDecorator(LeadingMarginSpan.Standard(every)) }
//fun Span.leadingMargin(first: Int = 0, rest: Int = 0) = apply { addDecorator(LeadingMarginSpan.Standard(first, rest)) }
//fun Span.align(align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL) = apply { addDecorator(AlignmentSpan.Standard(align)) }
//fun Span.drawableMargin(drawable: Drawable, padding: Int = 0) = apply { addDecorator(DrawableMarginSpan(drawable, padding)) }
//fun Span.iconMargin(bitmap: Bitmap, padding: Int = 0) = apply { addDecorator(IconMarginSpan(bitmap, padding)) }
//
//@RequiresApi(Build.VERSION_CODES.Q)
//fun Span.lineHeight(@Px @IntRange(from = 1) heightInPx: Int) = apply { addDecorator(LineHeightSpan.Standard(heightInPx)) }
//
//@RequiresApi(Build.VERSION_CODES.P)
//fun Span.typeface(typeface: Typeface) = apply { addDecorator(TypefaceSpan(typeface)) }
//fun Span.typeface(family: String) = apply { addDecorator(TypefaceSpan(family)) }
//
//fun Span.sansSerif() = apply { addDecorator(TypefaceSpan("sans-serif")) }
//fun Span.serif() = apply { addDecorator(TypefaceSpan("serif")) }
//fun Span.monospace() = apply { addDecorator(TypefaceSpan("monospace")) }
//fun Span.textAppearance(context: Context, appearance: Int) = apply { addDecorator(TextAppearanceSpan(context, appearance)) }
//
//fun Span.clickable(
//    isUnderlineText: Boolean = true,
//    onClick: (ClickableSpan) -> Unit
//) = apply {
//    addDecorator(
//        object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                onClick(this)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = isUnderlineText
//            }
//        }
//    )
//}

fun Span.image(
    context: Context,
    @DrawableRes resourceId: Int,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
) = apply {
    addDecorator(
        SpanDecorator(
            ImageSpan(context, resourceId, verticalAlignment),
            text = " ",
        )
    )
}

fun Editable.asSpannableStringBuilder() = this as SpannableStringBuilder
