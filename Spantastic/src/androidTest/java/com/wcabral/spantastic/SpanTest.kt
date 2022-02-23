package com.wcabral.spantastic

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.style.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class SpanTest {

    //         Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.wcabral.spantastic.test", appContext.packageName)


    @Test
    fun testText_bold() {
        // Given
        val span = spantastic {
            "hello, world" {
                bold()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StyleSpan)
        assertEquals(Typeface.BOLD, (spans[0] as StyleSpan).style)
    }

    @Test
    fun testText_italic() {
        // Given
        val span = spantastic {
            "hello, world" {
                italic()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StyleSpan)
        assertEquals(Typeface.ITALIC, (spans[0] as StyleSpan).style)
    }

    @Test
    fun testText_underline() {
        // Given
        val span = spantastic {
            "hello, world" {
                underline()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, UnderlineSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is UnderlineSpan)
    }

    @Test
    fun testText_url() {
        // Given
        val expectedUrl = "www.google.com"
        val span = spantastic {
            "hello, world" {
                url(expectedUrl)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, URLSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is URLSpan)
        assertEquals(expectedUrl, (spans[0] as URLSpan).url)
    }

    @Test
    fun testText_foreground() {
        // Given
        val expectedColor = Color.RED
        val span = spantastic {
            "hello, world" {
                foreground(expectedColor)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, ForegroundColorSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is ForegroundColorSpan)
        assertEquals(expectedColor, (spans[0] as ForegroundColorSpan).foregroundColor)
    }

    @Test
    fun testText_background() {
        // Given
        val expectedColor = Color.RED
        val span = spantastic {
            "hello, world" {
                background(expectedColor)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, BackgroundColorSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is BackgroundColorSpan)
        assertEquals(expectedColor, (spans[0] as BackgroundColorSpan).backgroundColor)
    }

    @Test
    fun testText_strike() {
        // Given
        val span = spantastic {
            "hello, world" {
                strike()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StrikethroughSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StrikethroughSpan)
    }

    @Test
    fun testText_relativeSize() {
        // Given
        val proportion = 1.5F
        val span = spantastic {
            "hello, world" {
                relativeSize(proportion)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, RelativeSizeSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is RelativeSizeSpan)
        assertEquals(proportion, (spans[0] as RelativeSizeSpan).sizeChange)
    }

    @Test
    fun testText_absoluteSize_isDip_default() {
        // Given
        val size = 18
        val isDip = true
        val span = spantastic {
            "hello, world" {
                absoluteSize(size)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, AbsoluteSizeSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is AbsoluteSizeSpan)
        assertEquals(size, (spans[0] as AbsoluteSizeSpan).size)
        assertEquals(isDip, (spans[0] as AbsoluteSizeSpan).dip)
    }

    @Test
    fun testText_absoluteSize_isDip_false() {
        // Given
        val size = 18
        val isDip = false
        val span = spantastic {
            "hello, world" {
                absoluteSize(size, isDip)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, AbsoluteSizeSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is AbsoluteSizeSpan)
        assertEquals(size, (spans[0] as AbsoluteSizeSpan).size)
        assertEquals(isDip, (spans[0] as AbsoluteSizeSpan).dip)
    }

    @Test
    fun testText_superscript() {
        // Given
        val span = spantastic {
            "hello, world" {
                superscript()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, SuperscriptSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is SuperscriptSpan)
    }

    @Test
    fun testText_subscript() {
        // Given
        val span = spantastic {
            "hello, world" {
                subscript()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, SubscriptSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is SubscriptSpan)
    }

    @Test
    fun testText_style_bold() {
        // Given
        val typeface = Typeface.BOLD
        val span = spantastic {
            "hello, world" {
                style(typeface)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StyleSpan)
        assertEquals(typeface, (spans[0] as StyleSpan).style)
    }

    @Test
    fun testText_style_italic() {
        // Given
        val typeface = Typeface.ITALIC
        val span = spantastic {
            "hello, world" {
                style(typeface)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StyleSpan)
        assertEquals(typeface, (spans[0] as StyleSpan).style)
    }

    @Test
    fun testText_style_boldItalic() {
        // Given
        val typeface = Typeface.BOLD_ITALIC
        val span = spantastic {
            "hello, world" {
                style(typeface)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is StyleSpan)
        assertEquals(typeface, (spans[0] as StyleSpan).style)
    }

    @Test
    fun testText_scaleX() {
        // Given
        val proportion = 1.5F
        val span = spantastic {
            "hello, world" {
                scaleX(proportion)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, ScaleXSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is ScaleXSpan)
        assertEquals(proportion, (spans[0] as ScaleXSpan).scaleX)
    }

    @Test
    fun testText_mask() {
        // Given
        val mask = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
        val span = spantastic {
            "hello, world" {
                mask(mask)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, MaskFilterSpan::class.java)

        // Then
        assertNotNull(spans)
        assertTrue(spans[0] is MaskFilterSpan)
        assertEquals(mask, (spans[0] as MaskFilterSpan).maskFilter)
    }

    @Test
    fun testText_leadingMargin_every() {
        // Given
        val everyDefault = 0
        val every = 2

        val span = spantastic {
            "hello, world" {
                leadingMargin()
                leadingMargin(every)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, LeadingMarginSpan::class.java)

        // Then
        assertNotNull(spans)

        // Default
        assertTrue(spans[0] is LeadingMarginSpan)
        assertEquals(everyDefault, (spans[0] as LeadingMarginSpan).getLeadingMargin(true))

        assertTrue(spans[1] is LeadingMarginSpan)
        assertEquals(every, (spans[1] as LeadingMarginSpan).getLeadingMargin(true))
    }

    @Test
    fun testText_leadingMargin_first_rest() {
        // Given
        val firstDefault = 0
        val restDefault = 0

        val first = 5
        val rest = 5

        val span = spantastic {
            "hello, world" {
                leadingMargin()
                leadingMargin(first, rest)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, LeadingMarginSpan::class.java)

        // Then
        assertNotNull(spans)

        // Default
        assertTrue(spans[0] is LeadingMarginSpan)
        assertEquals(firstDefault, (spans[0] as LeadingMarginSpan).getLeadingMargin(true))
        assertEquals(restDefault, (spans[0] as LeadingMarginSpan).getLeadingMargin(false))

        // Custom
        assertTrue(spans[1] is LeadingMarginSpan)
        assertEquals(first, (spans[1] as LeadingMarginSpan).getLeadingMargin(true))
        assertEquals(rest, (spans[1] as LeadingMarginSpan).getLeadingMargin(false))
    }

    @Test
    fun testText_align() {
        // Given
        val alignNormal = Layout.Alignment.ALIGN_NORMAL
        val alignCenter = Layout.Alignment.ALIGN_CENTER
        val alignOpposite = Layout.Alignment.ALIGN_OPPOSITE
        val span = spantastic {
            "hello, world" {
                align()
                align(alignCenter)
                align(alignOpposite)
            }
        }

        // When
        val spans = span.getSpans(0, span.length, AlignmentSpan::class.java)

        // Then
        assertNotNull(spans)

        // Normal
        assertTrue(spans[0] is AlignmentSpan)
        assertEquals(alignNormal, (spans[0] as AlignmentSpan).alignment)

        // Center
        assertTrue(spans[1] is AlignmentSpan)
        assertEquals(alignCenter, (spans[1] as AlignmentSpan).alignment)

        // Opposite
        assertTrue(spans[2] is AlignmentSpan)
        assertEquals(alignOpposite, (spans[2] as AlignmentSpan).alignment)
    }

    @Test
    fun textCorrectPosition_singleSpan_singleText() {
        // Given
        val span = spantastic {
            "hello, world" {
                italic()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, StyleSpan::class.java)

        // Then
        assertNotNull(spans)
        val start = span.getSpanStart((spans[0] as StyleSpan))
        val end = span.getSpanEnd((spans[0] as StyleSpan))

        assertEquals(0, start)
        assertEquals(12, end)
    }

    @Test
    fun textCorrectPosition_multipleSpans_singleText() {
        // Given
        val span = spantastic {
            "hello, world" {
                bold()
                strike()
                underline()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, Any::class.java)

        // Then
        assertNotNull(spans)
        assertEquals(3, spans.size)

        // Check bold
        assertEquals(0, span.getSpanStart((spans[0] as StyleSpan)))
        assertEquals(12, span.getSpanEnd((spans[0] as StyleSpan)))

        // Check italic
        assertEquals(0, span.getSpanStart((spans[1] as StrikethroughSpan)))
        assertEquals(12, span.getSpanEnd((spans[1] as StrikethroughSpan)))

        // Check underline
        assertEquals(0, span.getSpanStart((spans[2] as UnderlineSpan)))
        assertEquals(12, span.getSpanEnd((spans[2] as UnderlineSpan)))
    }

    @Test
    fun textCorrectPosition_multipleSpans_multipleTexts() {
        // Given
        val span = spantastic {
            "hello, " {
                bold()
            }
            "world " {
                strike()
            }
            "test" {
                underline()
            }
        }

        // When
        val spans = span.getSpans(0, span.length, Any::class.java)

        // Then
        assertNotNull(spans)
        assertEquals(3, spans.size)

        // Check bold
        assertEquals(0, span.getSpanStart((spans[0] as StyleSpan)))
        assertEquals(7, span.getSpanEnd((spans[0] as StyleSpan)))

        // Check italic
        assertEquals(7, span.getSpanStart((spans[1] as StrikethroughSpan)))
        assertEquals(13, span.getSpanEnd((spans[1] as StrikethroughSpan)))

        // Check underline
        assertEquals(13, span.getSpanStart((spans[2] as UnderlineSpan)))
        assertEquals(17, span.getSpanEnd((spans[2] as UnderlineSpan)))
    }


}