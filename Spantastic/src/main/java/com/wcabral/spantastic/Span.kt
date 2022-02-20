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
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.MaskFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.*
import android.view.View
import androidx.annotation.*
import androidx.annotation.IntRange

/**
 * This is a class that creates an abstraction of the native Android Span.
 * Spans are powerful markup objects that you can use to style text at a
 * character or paragraph level. By attaching spans to text objects, you
 * can change text in a variety of ways, including adding color, making
 * the text clickable, scaling the text size, and drawing text in a
 * customized way. Spans can also change TextPaint properties, draw on a
 * Canvas, and even change text layout.
 *
 * @param text that will be styled by spans and decorators
 */
class Span(private val text: CharSequence)  {
    private val decorators = mutableListOf<Decorator>()

    /**
     * Set span for a specific decorator
     */
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

    /**
     * Add decorator to a decorator list and initialize it
     */
    fun addDecorator(
        decorator: Decorator,
        init: Decorator.() -> Unit
    ) {
        decorators += decorator
        decorator.init()
    }

    /**
     * Build the span and yours decorations.
     * Calculates start and end position for each decorator depending on
     * decorator parameters
     *
     * @param spannableStringBuilder to build the spannable string
     */
    internal fun build(spannableStringBuilder: SpannableStringBuilder) {
        val initialStart = spannableStringBuilder.length
        spannableStringBuilder.append(text)

        decorators.forEach { decorator ->

            // Start position
            if (decorator.start == -1) {
                decorator.start = if (decorator.fillOnlyOnePosition) {
                    // Add the extra position and return the penultimate position
                    spannableStringBuilder.append(" ")
                    spannableStringBuilder.length - 1
                } else {
                    initialStart
                }
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

}

/**
 * Span that allows setting the Typeface#BOLD style of the text it's attached to.
 */
fun Span.bold(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = StyleSpan(Typeface.BOLD)), init)
}

/**
 * Span that allows setting the Typeface#ITALIC style of the text it's attached to.
 */
fun Span.italic(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = StyleSpan(Typeface.ITALIC)), init)
}

/**
 * A span that underlines the text it's attached to.
 */
fun Span.underline(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = UnderlineSpan()), init)
}

/**
 * Implementation of the ClickableSpan that allows setting a url string.
 * When selecting and clicking on the text to which the span is attached,
 * the URLSpan will try to open the url, by launching an an Activity with
 * an Intent#ACTION_VIEW intent.
 */
fun Span.url(url: String, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = URLSpan(url)), init)
}

/**
 * Changes the color of the text to which the span is attached.
 */
fun Span.foreground(color: Int, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = ForegroundColorSpan(color)), init)
}

/**
 * Changes the background color of the text to which the span is attached.
 */
fun Span.background(color: Int, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = BackgroundColorSpan(color)), init)
}

/**
 * A span that strikes through the text it's attached to.
 */
fun Span.strike(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = StrikethroughSpan()), init)
}

/**
 * Uniformly scales the size of the text to which it's attached by a certain proportion.
 * @param proportion to create a RelativeSizeSpan based on.
 */
fun Span.relativeSize(proportion: Float, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = RelativeSizeSpan(proportion)), init)
}

/**
 * A span that changes the size of the text it's attached to.
 * @param size Set the text size
 * @param isDip Size physical pixels or size device-independent pixels if dip is true.
 */
fun Span.absoluteSize(size: Int, isDip: Boolean = true, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = AbsoluteSizeSpan(size, isDip)), init)
}

/**
 * The span that moves the position of the text baseline higher.
 */
fun Span.superscript(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = SuperscriptSpan()), init)
}

/**
 * The span that moves the position of the text baseline lower.
 */
fun Span.subscript(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = SubscriptSpan()), init)
}

/**
 * Span that allows setting the style of the text it's attached to.
 * @param style Possible styles are: Typeface#NORMAL, Typeface#BOLD, Typeface#ITALIC
 * and Typeface#BOLD_ITALIC.
 * @see bold
 * @see italic
 */
fun Span.style(style: Int, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = StyleSpan(style)), init)
}

/**
 * Scales horizontally the size of the text to which it's attached by a certain factor.
 * Values > 1.0 will stretch the text wider. Values < 1.0 will stretch the text narrower.
 * @param proportion the horizontal scale factor. Value is 0 or greater
 */
fun Span.scaleX(@FloatRange(from = 0.0) proportion: Float, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = ScaleXSpan(proportion)), init)
}

/**
 * Span that allows setting a MaskFilter to the text it's attached to.
 * @param filter  the filter to be applied to the TextPaint
 */
fun Span.mask(filter: MaskFilter, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = MaskFilterSpan(filter)), init)
}

/**
 * The standard implementation of LeadingMarginSpan, which adjusts the margin but
 * does not do any rendering.
 * Constructor taking an indent for all lines.
 * @param every = the indent of each line
 */
fun Span.leadingMargin(every: Int = 0, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = LeadingMarginSpan.Standard(every)), init)
}

/**
 * The standard implementation of LeadingMarginSpan, which adjusts the margin but
 * does not do any rendering.
 * Constructor taking separate indents for the first and subsequent lines.
 * @param first = the indent for the first line of the paragraph
 * @param rest = the indent for the remaining lines of the paragraph
 */
fun Span.leadingMargin(first: Int = 0, rest: Int = 0, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = LeadingMarginSpan.Standard(first, rest)), init)
}

/**
 * Default implementation of the AlignmentSpan.
 * @param align Possible aligns are: ALIGN_CENTER, ALIGN_NORMAL, ALIGN_OPPOSITE
 */
fun Span.align(align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = AlignmentSpan.Standard(align)), init)
}

/**
 * A span which adds a drawable and a padding to the paragraph it's attached to.
 * @param drawable the drawable to be added
 * @param padding the distance between the drawable and the text
 * For working with Bitmap instead of Drawable:
 * @see iconMargin
 */
fun Span.drawableMargin(drawable: Drawable, padding: Int = 0, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = DrawableMarginSpan(drawable, padding)), init)
}

/**
 * A span which adds a Bitmap and a padding to the paragraph it's attached to.
 * @param bitmap the bitmap to be added
 * @param padding the distance between the drawable and the text
 * For working with Drawable instead of Bitmap:
 * @see drawableMargin
 */
fun Span.iconMargin(bitmap: Bitmap, padding: Int = 0, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = IconMarginSpan(bitmap, padding)), init)
}

/**
 * Span that updates the "sans-serif" typeface of the text it's attached to.
 */
fun Span.sansSerif(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TypefaceSpan("sans-serif")), init)
}

/**
 * Span that updates the "serif" typeface of the text it's attached to.
 */
fun Span.serif(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TypefaceSpan("serif")), init)
}

/**
 * Span that updates the "monospace" typeface of the text it's attached to.
 */
fun Span.monospace(init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TypefaceSpan("monospace")), init)
}

/**
 * Span that updates the typeface family of the text it's attached to.
 */
fun Span.typeface(family: String, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TypefaceSpan(family)), init)
}

/**
 * Span that updates the typeface of the text it's attached to.
 * You can use this method to create a span with a custom font.
 * @param typeface a typeface like that:
 * Typeface myTypeface = Typeface.create(ResourcesCompat.getFont(context, R.font.acme), Typeface.BOLD);
 * @see: https://developer.android.com/reference/android/text/style/TypefaceSpan
 */
@RequiresApi(Build.VERSION_CODES.P)
fun Span.typeface(typeface: Typeface, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TypefaceSpan(typeface)), init)
}

/**
 * Sets the text appearance using the given TextAppearance attributes.
 * By default TextAppearanceSpan only changes the specified attributes in XML.
 * textColorHighlight, textColorHint, textAllCaps and fallbackLineSpacing are
 * not supported by TextAppearanceSpan.
 * @param context the activity/fragment
 * @param appearance the specified TextAppearance resource to determine the text appearance
 */
fun Span.textAppearance(context: Context, appearance: Int, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = TextAppearanceSpan(context, appearance)), init)
}

/**
 * Default implementation of the LineHeightSpan, which changes the line height
 * of the attached paragraph.
 * LineHeightSpan will change the line height of the entire paragraph, even though
 * it covers only part of the paragraph.
 * @param heightInPx This units of this value are pixels. Value is 1 or greater
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun Span.lineHeight(@Px @IntRange(from = 1) heightInPx: Int, init: Decorator.() -> Unit = {}) {
    addDecorator(Decorator(what = LineHeightSpan.Standard(heightInPx)), init)
}

/**
 * If an object of this type is attached to the text of a TextView with a movement
 * method of LinkMovementMethod, the affected spans of text can be selected.
 * If selected and clicked, the onClick(View) method will be called.
 * The text with a ClickableSpan attached will be underlined and the link color will
 * be used as a text color. The default link color is the theme's accent color or
 * android:textColorLink if this attribute is defined in the theme.
 * @param isUnderlineText shows or not the underline text
 * @param onClick callback to click event
 */
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

/**
 * A span which styles paragraphs as bullet points (respecting layout direction).
 * IMPORTANT: BulletSpans must be attached from the first character to the last
 * character of a single paragraph, otherwise the bullet point will not be displayed
 * but the first paragraph encountered will have a leading margin.
 *
 * @param color the bullet point color. By default, the bullet point color is 0 - no
 * color, so it uses the TextView's text color.
 * @param gapWidth the distance, in pixels, between the bullet point and the paragraph.
 * Default value is 2px.
 * @param bulletRadius the radius, in pixels, of the bullet point. Default value is 4px.
 */
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

/**
 * A span which styles paragraphs by adding a vertical stripe at the beginning of
 * the text (respecting layout direction).
 * IMPORTANT: A QuoteSpan must be attached from the first character to the last
 * character of a single paragraph, otherwise the span will not be displayed.
 *
 * @param color the vertical stripe color. By default, the stripe color is 0xff0000ff
 * @param stripeWidth the width, in pixels, of the stripe. Default value is 2px.
 * @param gapWidth the distance, in pixels, between the stripe and the paragraph.
 * Default value is 2px.
 */
fun Span.quote(
    @ColorInt color: Int = Color.BLACK,
    @IntRange stripeWidth: Int = 2,
    @IntRange gapWidth: Int = 2,
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

/**
 * Span that replaces the text it's attached to with a Drawable that can be
 * aligned with the bottom or with the baseline of the surrounding text.
 *
 * @param context used to create a drawable from based on the display metrics
 * of the resources.
 * @param resourceId drawable resource id based on which the drawable is retrieved
 * @param verticalAlignment one of DynamicDrawableSpan#ALIGN_BOTTOM or DynamicDrawableSpan#ALIGN_BASELINE
 */
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

/**
 * Span that replaces the text it's attached to with a Drawable that can be
 * aligned with the bottom or with the baseline of the surrounding text.
 *
 * @param drawable to be rendered.
 * @param verticalAlignment one of DynamicDrawableSpan#ALIGN_BOTTOM or DynamicDrawableSpan#ALIGN_BASELINE
 */
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

/**
 * Span that replaces the text it's attached to with a Drawable that can be
 * aligned with the bottom or with the baseline of the surrounding text.
 *
 * @param context used to create a drawable from based on the display metrics
 * of the resources.
 * @param bitmap to be rendered.
 * @param verticalAlignment one of DynamicDrawableSpan#ALIGN_BOTTOM or DynamicDrawableSpan#ALIGN_BASELINE
 */
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

/**
 * Span that replaces the text it's attached to with a Drawable that can be
 * aligned with the bottom or with the baseline of the surrounding text.
 *
 * @param context used to create a drawable from based on the display metrics
 * of the resources.
 * @param uri used to construct the drawable that will be rendered.
 * @param verticalAlignment one of DynamicDrawableSpan#ALIGN_BOTTOM or DynamicDrawableSpan#ALIGN_BASELINE
 */
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

/**
 * The default implementation of TabStopSpan that allows setting the offset of
 * the tab stop from the leading margin of the first line of text.
 * To work correctly, consider using the combination of "\t" and `Span.tab`
 *
 * @param offset of the tab stop from the leading margin of the line,
 * in pixels Value is 0 or greater
 */
fun Span.tab(
    @IntRange(from = 0) offset: Int,
    init: Decorator.() -> Unit = {}
) {
    addDecorator(Decorator(TabStopSpan.Standard(offset)), init)
}
