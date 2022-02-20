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

import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.method.LinkMovementMethod
import android.text.style.DynamicDrawableSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.wcabral.spantastic.*
import com.wcabral.spantastic_sample.databinding.ActivityMainBinding
import com.wcabral.spantastic_sample.R

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val drawable by lazy {
        ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_android_black_24dp,
            null
        )?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }!!
    }

    private val smallBitmap by lazy {
        resources.decodeSampledBitmapFromResource(R.drawable.android, 16, 16)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpantastic()
    }

    private fun initSpantastic() {
//        binding.edtSpan.text = spantastic(binding.edtSpan.text.asSpannableStringBuilder()) {
//            breakLine()
//            h1("Teste")
//            title("Mais coisas")
//
//            "Outro texto" {
//                background(Color.BLACK)
//                foreground(Color.WHITE)
//            }
//        }

//        val span = spantastic {
//
//            h1("Text Style")
//
//            title("Bold:")
//            "Text bold" {
//                bold()
//            }
//
//            divider()
//
//            title("Italic:")
//            "Text italic" {
//                italic()
//            }
//
//            divider()
//
//            title("Underline:")
//            "Text with underline" {
//                underline()
//            }
//
//            divider()
//
//            title("Url:")
//            "Click here to open the url" {
//                url("https:www.google.com.br") {
//                    start = 6
//                    end = 10
//                }
//            }
//
//            divider()
//
//            title("Foreground:")
//            "Text with a foreground color" {
//                foreground(Color.BLUE)
//            }
//
//            divider()
//
//            title("Background:")
//            "Text with a background color" {
//                background(Color.GREEN)
//            }
//
//            divider()
//
//            title("Strikethrough:")
//            "Text with a strikethrough" {
//                strike()
//            }
//
//            divider()
//
//            title("Mask:")
//            "Text with a blur mask and absolute size 18" {
//                mask(BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL))
//                absoluteSize(18)
//            }
//
//            divider()
//
//            title("Clickable:")
//            "Text with a clickable area" {
//                clickable {
//                    Toast.makeText(this@MainActivity, "Text was clicked", Toast.LENGTH_SHORT).show()
//                }
//            }
//
////            "Text with a clickable area" {
////                clickable(
////                    init = {
////                        start = 0
////                        end = 4
////                    }
////                ) {
////                    Toast.makeText(this@MainActivity, "Text was clicked", Toast.LENGTH_SHORT).show()
////                }
////            }
//
//            divider()
//
//            title("Absolute size:")
//            "Text with absolute size of 24" {
//                absoluteSize(24)
//            }
//
//            divider()
//
//            title("Relative size:")
//            "Text increased by 50%" {
//                relativeSize(1.5F)
//            }
//
//            divider()
//
//            title("ScaleX:")
//            "Text with ScaleX" {
//                scaleX(2F)
//            }
//
//            divider()
//
//            title("Text Appearance:")
//            "Text with a custom appearance" {
//                textAppearance(this@MainActivity, R.style.CustomStyle)
//            }
//
//            divider()
//
//            title("Custom font (typeface)")
//            // Custom Font
//            val myTypeface = Typeface.create(
//                ResourcesCompat.getFont(
//                    this@MainActivity,
//                    R.font.aguafina_script
//                ),
//                Typeface.NORMAL
//            )
//            "Text with a custom typeface" {
//                typeface(myTypeface)
//                absoluteSize(22)
//            }
//
//            divider()
//
//            title("Style:")
//            "Bold" {
//                style(Typeface.BOLD)
//            }
//            + " and "
//            "italic" {
//                style(Typeface.ITALIC)
//            }
//
//            divider()
//
//            title("Subscript:")
//            + "☕- C"
//            "8" { subscript() }
//            + "H"
//            "10" { subscript() }
//            + "N"
//            "4" { subscript() }
//            + "O"
//            "2" { subscript() }
//
//            divider()
//
//            title("Superscript:")
//            "1" {
//                "st" { superscript() }
//            }
//
//            divider()
//
//            title("Alignment:")
//            "Text with normal alignment" {
//                align(Layout.Alignment.ALIGN_NORMAL)
//            }
//            newLine()
//            "Text with center alignment" {
//                align(Layout.Alignment.ALIGN_CENTER)
//            }
//            newLine()
//            "Text with opposite alignment" {
//                align(Layout.Alignment.ALIGN_OPPOSITE)
//            }
//
//            divider()
//
//            title("Leading margin:")
//            "Text with leading margin" {
//                leadingMargin(50)
//            }
//
//            divider()
//
//            title("Line height:")
//            "Text with line height\nThis is a multiline paragraph\nThis is a multiline paragraph" {
//                lineHeight(100)
//            }
//
//            divider()
//
//            h1("Groups")
//
//            // BulletSpans must be attached from the first character to
//            // the last character of a single paragraph, otherwise the
//            // bullet point will not be displayed but the first paragraph
//            // encountered will have a leading margin.
//            title("Bullets:", shouldBreakLine = false)
//
//            newLine()
//            "Bullet 1" {
//                bullet(
//                    color = Color.RED,
//                    gapWidth = 5,
//                    bulletRadius = 10
//                )
//            }
//
//            newLine()
//            "Bullet 2" {
//                bullet(
//                    color = Color.BLUE,
//                    gapWidth = 5,
//                    bulletRadius = 10
//                )
//            }
//
//            newLine()
//            "Bullet 3" {
//                bullet(
//                    color = Color.MAGENTA,
//                    gapWidth = 5,
//                    bulletRadius = 10
//                )
//            }
//
//            divider()
//
//            // A QuoteSpan must be attached from the first character to
//            // the last character of a single paragraph, otherwise the
//            // span will not be displayed.
//            // Don't forget to break line (\n) before
//            title("Quote:")
//            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,when an unknown printer took a galley of type and scrambled it to make a type specimen book." {
//                quote(
//                    color = Color.RED,
//                    stripeWidth = 20,
//                    gapWidth = 30
//                )
//            }
//
//            divider()
//
//            title("Tab stop:")
//
//            tabGroup(100) {
//                + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,when an unknown printer took a galley of type and scrambled it to make a type specimen book."
//            }
//
////            tab(100) {
////                + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,when an unknown printer took a galley of type and scrambled it to make a type specimen book."
////            }
//
//            divider()
//
//            h1("Images")
//
//            title("Drawable margin:")
//            "A span which adds a drawable and a padding to the paragraph it's attached to." {
//                drawableMargin(
//                    drawable = drawable,
//                    padding = 5
//                )
//            }
//
//            divider()
//
//            title("Icon margin:")
//            "A span which adds a icon (Bitmap) and a padding to the paragraph it's attached to." {
//                iconMargin(
//                    bitmap = smallBitmap,
//                    padding = 5
//                )
//            }
//
//            divider()
//
//            title("Image:")
//            "Text with image from resource id (bottom) " {
//                image(this@MainActivity, R.drawable.ic_android_black_24dp)
//            }
//
//            newLine()
//
//            "Text with image from resource id (baseline) " {
//                image(this@MainActivity, R.drawable.ic_android_black_24dp, DynamicDrawableSpan.ALIGN_BASELINE)
//            }
//
//            divider()
//
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.android)
//            "Text with image " {
//                image(
//                    context = this@MainActivity,
//                    bitmap = bitmap,
//                    verticalAlignment = DynamicDrawableSpan.ALIGN_CENTER
//                )
//            }
//            + " from Bitmap "
//
//            divider()
//
//            h1("Complex Spans")
//
//            title("Example:")
//            " An" {
//                background(Color.BLACK)
//                foreground(Color.WHITE)
//                absoluteSize(52)
//            }
//            "droid " {
//                background(Color.BLACK)
//                foreground(Color.GREEN)
//                absoluteSize(52)
//            }
//
//            divider()
//
//            title("Example:")
//
//            + "By continuing, you agree to the "
//            "Terms os Service " {
//                url("https://www.google.com")
//                foreground(Color.GREEN)
//            }
//            + "and "
//            "Privacy Policy" {
//                url("https://github.com/wellingtoncabral")
//            }
//            + "."
//
////            divider()
////
////            "By continuing, you agree to the Terms os Service and Privacy Policy." {
////                url("https://www.google.com") {
////                    start = 32
////                    end = 48
////                }
////
////                url("https://github.com/wellingtoncabral") {
////                    start = 53
////                    end = 67
////                }
////            }
//
//            divider()
//
//            title("Example:")
//            "Lorem Ipsum is simply dummy text of the" {
//                lineHeight(70)
//                background(Color.CYAN)
//                foreground(Color.BLACK)
//            }
//            + " printing and typesetting industry. "
//            "Lorem Ipsum has been the industry's standard dummy" {
//                background(Color.MAGENTA)
//                foreground(Color.WHITE)
//            }
//            + " "
//            "text ever since the 1500s when an unknown printer took a galley" {
//                background(Color.GRAY)
//                foreground(Color.WHITE)
//            }
//            + " "
//            "of type and scrambled it to make a type specimen book." {
//                background(Color.YELLOW)
//                foreground(Color.BLACK)
//            }
//
//            divider()
//
//        }

        val span = spantastic {
            h1("Clickable decorations")

            title("Url:")
            "Click here to open the url" {
                url("https:www.google.com.br") {
                    start = 6
                    end = 10
                }
            }

            divider()

            title("Clickable:")
            "Text with a clickable area" {
                clickable {
                    Toast.makeText(this@MainActivity, "Text was clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }



        binding.tvSpannable.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = span
        }
    }
}