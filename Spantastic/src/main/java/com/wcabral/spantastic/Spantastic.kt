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

//    fun tab(
//        @IntRange(from = 0) offset: Int,
//        init: Span.() -> Unit = {}
//    ) {
//        text("\t", init).initDecorator(Decorator(TabStopSpan.Standard(offset))) {}
//    }

    fun build() : SpannableStringBuilder {
        spans.forEach { span ->
            span.build(spannableStringBuilder)
        }
        return spannableStringBuilder
    }

}

open class Decorator(
    val what: Any,
    val fillOnlyOnePosition: Boolean = false,
    var start: Int = -1,
    var end: Int = -1,
    var flags: Int = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
)

interface Span {
    fun addDecorator(decorator: Decorator, init: Decorator.() -> Unit)
    fun build(spannableStringBuilder: SpannableStringBuilder)
}

internal class SpanImpl(private val text: CharSequence) : Span {

    private val items = mutableListOf<Decorator>()

    private fun decorate(
        decorator: Decorator,
        spannableStringBuilder: SpannableStringBuilder
    ) {
        spannableStringBuilder.setSpan(
            decorator.what,
            decorator.start,
            decorator.end,
            decorator.flags
        )
    }

    override fun build(spannableStringBuilder: SpannableStringBuilder) {
        val initialStart = spannableStringBuilder.length
        spannableStringBuilder.append(text)

        items.forEach { decorator ->
            // Start position
            if (decorator.start == -1) {
                val start = if (decorator.fillOnlyOnePosition) {
                    spannableStringBuilder.append(" ")
                    spannableStringBuilder.length - 1
                } else {
                    initialStart
                }
                decorator.start = start
            } else {
                decorator.start = initialStart + decorator.start
            }

            // End position
            if (decorator.end == -1) {
                decorator.end = spannableStringBuilder.length
            } else {
                decorator.end = initialStart + decorator.end
            }

            decorate(decorator, spannableStringBuilder)
        }

    }

    override fun addDecorator(
        decorator: Decorator,
        init: Decorator.() -> Unit
    ) {
        items += decorator
        decorator.init()
    }

}

fun Span.bold(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = StyleSpan(Typeface.BOLD)), init) }
fun Span.italic(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = StyleSpan(Typeface.ITALIC)), init) }
fun Span.underline(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = UnderlineSpan()), init) }
fun Span.url(url: String, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = URLSpan(url)), init) }
fun Span.foreground(color: Int, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = ForegroundColorSpan(color)), init) }
fun Span.background(color: Int, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = BackgroundColorSpan(color)), init) }
fun Span.strike(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = StrikethroughSpan()), init) }
fun Span.relativeSize(proportion: Float, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = RelativeSizeSpan(proportion)), init) }
fun Span.absoluteSize(size: Int, isDip: Boolean = true, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = AbsoluteSizeSpan(size, isDip)), init) }
fun Span.superscript(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = SuperscriptSpan()), init) }
fun Span.subscript(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = SubscriptSpan()), init) }
fun Span.style(style: Int, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = StyleSpan(style)), init) }
fun Span.scaleX(proportion: Float, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = ScaleXSpan(proportion)), init) }
fun Span.mask(filter: MaskFilter, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = MaskFilterSpan(filter)), init) }
fun Span.leadingMargin(every: Int = 0, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = LeadingMarginSpan.Standard(every)), init) }
fun Span.leadingMargin(first: Int = 0, rest: Int = 0, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = LeadingMarginSpan.Standard(first, rest)), init) }
fun Span.align(align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = AlignmentSpan.Standard(align)), init) }
fun Span.drawableMargin(drawable: Drawable, padding: Int = 0, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = DrawableMarginSpan(drawable, padding)), init) }
fun Span.iconMargin(bitmap: Bitmap, padding: Int = 0, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = IconMarginSpan(bitmap, padding)), init) }
fun Span.sansSerif(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TypefaceSpan("sans-serif")), init) }
fun Span.serif(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TypefaceSpan("serif")), init) }
fun Span.monospace(init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TypefaceSpan("monospace")), init) }
fun Span.textAppearance(context: Context, appearance: Int, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TextAppearanceSpan(context, appearance)), init) }

@RequiresApi(Build.VERSION_CODES.Q)
fun Span.lineHeight(@Px @IntRange(from = 1) heightInPx: Int, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = LineHeightSpan.Standard(heightInPx)), init) }

@RequiresApi(Build.VERSION_CODES.P)
fun Span.typeface(typeface: Typeface, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TypefaceSpan(typeface)), init) }
fun Span.typeface(family: String, init: Decorator.() -> Unit = {}) { addDecorator(Decorator(what = TypefaceSpan(family)), init) }

fun Span.clickable(
    isUnderlineText: Boolean = true,
    init: Decorator.() -> Unit = {},
    onClick: (ClickableSpan) -> Unit,
) {
    addDecorator(
        Decorator(
            what = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClick(this)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = isUnderlineText
                }
            }
        ),
        init
    )
}

fun Span.bullet(
    @ColorInt color: Int = Color.BLACK,
    @IntRange gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
    @IntRange bulletRadius: Int = 4,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                BulletSpan(gapWidth, color, bulletRadius)
            } else {
                BulletSpan(gapWidth)
            }
        ),
        init
    )
}

fun Span.quote(
    @ColorInt color: Int = Color.BLACK,
    @IntRange stripeWidth: Int = 0,
    @IntRange gapWidth: Int = 0,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                QuoteSpan(color, stripeWidth, gapWidth)
            } else {
                QuoteSpan(color)
            }
        ),
        init
    )
}

fun Span.image(
    context: Context,
    @DrawableRes resourceId: Int,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = ImageSpan(context, resourceId, verticalAlignment),
            fillOnlyOnePosition = true
        ),
        init
    )
}

fun Span.image(
    drawable: Drawable,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = ImageSpan(drawable, verticalAlignment),
            fillOnlyOnePosition = true
        ),
        init
    )
}

fun Span.image(
    context: Context,
    bitmap: Bitmap,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = ImageSpan(context, bitmap, verticalAlignment),
            fillOnlyOnePosition = true
        ),
        init
    )
}

fun Span.image(
    context: Context,
    uri: Uri,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(
        Decorator(
            what = ImageSpan(context, uri, verticalAlignment),
            fillOnlyOnePosition = true
        ),
        init
    )
}

fun Span.tab(
    @IntRange(from = 0) offset: Int,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(Decorator(TabStopSpan.Standard(offset)), init)
}

fun Editable.asSpannableStringBuilder() = this as SpannableStringBuilder
